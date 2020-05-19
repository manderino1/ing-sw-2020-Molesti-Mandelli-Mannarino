package it.polimi.ingsw.PSP18.server.controller;

import com.google.gson.Gson;
import it.polimi.ingsw.PSP18.networking.messages.toclient.*;
import it.polimi.ingsw.PSP18.networking.messages.toserver.WorkerReceiver;
import it.polimi.ingsw.PSP18.server.backup.MatchBackup;
import it.polimi.ingsw.PSP18.networking.SocketThread;
import it.polimi.ingsw.PSP18.server.backup.PlayerManagerBackup;
import it.polimi.ingsw.PSP18.server.model.Color;
import it.polimi.ingsw.PSP18.server.model.GameMap;
import it.polimi.ingsw.PSP18.server.model.MatchStatus;
import it.polimi.ingsw.PSP18.server.model.PlayerData;
import it.polimi.ingsw.PSP18.server.view.MapObserver;
import it.polimi.ingsw.PSP18.server.view.PlayerDataObserver;

import java.io.*;
import java.util.*;

/***
 * class that deals with the information of the current match
 */
public class Match {
    private ArrayList<PlayerManager> playerManagers;
    private TurnManager turnManager;
    private ArrayList<SocketThread> sockets;
    private HashMap<PlayerManager, SocketThread> playerSocketMap;
    private HashMap<SocketThread, PlayerManager> socketPlayerMap;
    private PlayerManager currentPlayer;
    private MatchStatus matchStatus;
    private GameMap gameMap;
    private ArrayList<String> divinitySelection = new ArrayList<>();
    private Integer divinitySelectionIndex = 0;
    private Integer workerPlacementIndex = 0;
    private ArrayList<String> divinities;
    private int playerN;
    private String fileName;
    private BackupManager backupManager;

    /***
     * Match constructor, initializes the arrayLists and the game map
     */
    public Match(){
        backupManager= new BackupManager(this);
        playerManagers = new ArrayList<>();
        sockets = new ArrayList<>();
        playerSocketMap = new HashMap<>();
        socketPlayerMap = new HashMap<>();
        gameMap = new GameMap();
        matchStatus = MatchStatus.WAITING_FOR_PLAYERS;
        String[] divArray = {"Apollo", "Artemis", "Athena", "Atlas", "Demeter", "Hephaestus", "Minotaur", "Pan", "Prometheus"};
        divinities = new ArrayList<>(Arrays.asList(divArray));
    }

    public Match(int playerN){
        this();
        this.playerN = playerN;
    }

    /***
     * Returns the game map
     * @return the game map, unique for all players
     */
    public GameMap getGameMap() {
        return gameMap;
    }

    /***
     * Return the list of the player managers into the match
     * @return the player managers list
     */
    public ArrayList<PlayerManager> getPlayerManagers() {
        return playerManagers;
    }

    /***
     * Return the list of the sockets paired to the connected clients into the match
     * @return the list of sockets
     */
    public ArrayList<SocketThread> getSockets() {
        return sockets;
    }

    /***
     * Return the state of the match, there are some pre-defined states as enum in MatchStatus class
     * @return the match state
     */
    public MatchStatus getMatchStatus() {
        return matchStatus;
    }

    /***
     * Set the state of the match in pre-defined states as described in MatchStatus class
     * @param matchStatus the new state of the match
     */
    public void setMatchStatus(MatchStatus matchStatus) {
        this.matchStatus = matchStatus;
    }

    /***
     * Add a player to the players list
     * @param player the playermanager player reference
     * @param socket the socket reference
     */
    public void addPlayer(PlayerManager player, SocketThread socket){
        for(PlayerManager playerPresent : playerManagers) {
            if (player.getPlayerData().getPlayerID().equals(playerPresent.getPlayerData().getPlayerID())) {
                socket.sendMessage(new WaitingNick());
                return;
            }
        }

        // Subscribe the current player to all the existing players
        for(PlayerManager exPlayer : playerManagers) {
            exPlayer.getPlayerData().attach(new PlayerDataObserver(socket));
        }

        playerManagers.add(player);
        playerSocketMap.put(player, socket);
        socketPlayerMap.put(socket, player);

        // Subscribe all the existing players to the new player
        for(PlayerManager exPlayer : playerManagers) {
            player.getPlayerData().attach(new PlayerDataObserver(playerSocketMap.get(exPlayer)));
        }

        socket.sendMessage(new MatchReady());
    }

    /***
     * Add a socket to the sockets list and register the observers
     * related to the socket and client connection
     * @param socket the socket reference
     */
    public void addSocket(SocketThread socket){
        if(sockets.size() <= 2 && matchStatus == MatchStatus.WAITING_FOR_PLAYERS) {
            sockets.add(socket);
            socket.sendMessage(new WaitingNick());
        }
    }

    /***
     * Get the current playing player
     * @return che current player playing the turn
     */
    public PlayerManager getCurrentPlayer() {
        return currentPlayer;
    }

    /***
     * Set the new current player at the turn change
     * @param currentPlayer the new currently playing player
     */
    public void setCurrentPlayer(PlayerManager currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    /***
     * Get the turn manager reference
     * @return the turn manager reference
     */
    public TurnManager getTurnManager() {
        return turnManager;
    }
    public void setTurnManager(TurnManager turnManager) {
        this.turnManager= turnManager;
    }

    /***
     * Wait for all the players to be ready and then start the divinity selection phase
     * @param socket the reference to the socket
     */
    public void readyManagement(SocketThread socket) {
        socketPlayerMap.get(socket).getPlayerData().setReady();
        for(PlayerManager player : playerManagers) {
            if(!player.getPlayerData().getReady() || playerManagers.size() != playerN || playerManagers.size() <= 1) {
                return;
            }
        }
        // Check if there is a match saved with these players
        boolean hasBackup = backupManager.backupCheck();
        // If i manage to arrive here all the players are ready, i can start the divinity selection phase
        if(!hasBackup) {
            matchStatus = MatchStatus.DIVINITIES_SELECTION;
            playerSocketMap.get(playerManagers.get(playerManagers.size()-1)).sendMessage(new DivinityPick(divinities, playerManagers.size()));
        } else {
            backupManager.backupRestore();
        }
    }

    /***
     * Create the divinity of the player that decided which divinity to use
     * If there are no more players that have to choose the divinity start the match
     * If there are other players, ask the next to choose the divinity
     * @param socket the socket reference, used to get the correct player
     * @param divinity string that represent the divinity to be created
     */
    public void divinityCreation(SocketThread socket, String divinity) {
        // Check that the divinity selection is correct
        if(!divinitySelection.contains(divinity)) {
            playerSocketMap.get(playerManagers.get(divinitySelectionIndex)).sendMessage(new DivinityList(divinitySelection));
        }
        socketPlayerMap.get(socket).divinityCreation(divinity); // use to change divinity
        if(divinitySelectionIndex == playerManagers.size()) {
            // Set observers
            for(SocketThread sock : sockets) {
                gameMap.attach(new MapObserver(sock));
            }

            matchStatus = MatchStatus.WORKER_SETUP;
            playerSocketMap.get(playerManagers.get(workerPlacementIndex)).sendMessage(new PlaceReady());
            workerPlacementIndex++;
        } else {
            divinitySelection.remove(divinity);
            playerSocketMap.get(playerManagers.get(divinitySelectionIndex)).sendMessage(new DivinityList(divinitySelection));
            divinitySelectionIndex++;
        }
    }

    public void divinitySelection(ArrayList<String> divinities) {
        for(String divinity : divinities) {
            if(!this.divinities.contains(divinity)) {
                playerSocketMap.get(playerManagers.get(playerManagers.size()-1)).sendMessage(new DivinityPick(divinities, playerManagers.size()));
            }
        }
        divinitySelection = divinities;
        playerSocketMap.get(playerManagers.get(divinitySelectionIndex)).sendMessage(new DivinityList(divinities));
        divinitySelectionIndex++;
    }

    /***
     * method that recieve the worker placed form the player in the client and place them in actual gameMap in the server
     * @param socket the socket we receive message from
     * @param workers the worker the player has placed
     */
    public void workerPlacement(SocketThread socket, WorkerReceiver workers) {
        if(gameMap.getCell(workers.getX1(), workers.getY1()).getWorker() != null || gameMap.getCell(workers.getX2(), workers.getY2()).getWorker() != null) {
            playerSocketMap.get(playerManagers.get(workerPlacementIndex)).sendMessage(new PlaceReady());
        }
        socketPlayerMap.get(socket).placeWorker(workers.getX1(), workers.getY1());
        socketPlayerMap.get(socket).placeWorker(workers.getX2(), workers.getY2());
        if(workerPlacementIndex == playerManagers.size()) {
            startMatch();
        } else {
            playerSocketMap.get(playerManagers.get(workerPlacementIndex)).sendMessage(new PlaceReady());
            workerPlacementIndex++;
        }
    }

    /***
     * Start the match by ordering players and creating the turn manager
     * Has to be called after the creation of players
     * If Athena is in the match create its special turn manager
     */
    private void startMatch() {
        // Sort players by order
        playerManagers.sort(Comparator.comparingInt(o -> o.getPlayerData().getPlayOrder()));
        matchStatus = MatchStatus.MATCH_STARTED;
        for(SocketThread socket : sockets) {
            socket.sendMessage(new StartMatch());
        }
        // Set the backup file path
        String fileName = "Backups/match_";
        ArrayList<String> names = new ArrayList<>();
        for(PlayerManager player : playerManagers) {
            names.add(player.getPlayerData().getPlayerID());
        }
        names.sort(String::compareToIgnoreCase);
        for(String name: names) {
            fileName = fileName.concat(name);
        }
        this.fileName = fileName.concat(".bak");
        // Search for Athena
        for (PlayerManager player : playerManagers) {
            if(player.getDivinityName().equals("Athena")) {
                turnManager = new TurnManagerAthena(this);
                return;
            }
        }
        // If Athena is not found create a standard turn manager
        turnManager = new TurnManager(this);
    }

    public SocketThread getCurrentSocket() {
        return playerSocketMap.get(currentPlayer);
    }

    public void endMatch(PlayerManager winner) {
        if(winner != null) {
            playerSocketMap.get(winner).sendMessage(new MatchWon(winner.getPlayerData().getPlayerID(), true));
            ArrayList<String> loserIDs = new ArrayList<>();
            for(SocketThread socket : sockets) {
                if (socketPlayerMap.get(socket) != winner) {
                    loserIDs.add(socketPlayerMap.get(socket).getPlayerData().getPlayerID());
                }
            }
            for(SocketThread socket : sockets) {
                for(String loserID : loserIDs) {
                    if (socketPlayerMap.get(socket).getPlayerData().getPlayerID().equals(loserID)) {
                        socket.sendMessage(new MatchLost(loserID, true, true));
                    } else {
                        socket.sendMessage(new MatchLost(loserID, false, true));
                    }
                }
            }

            // Cancel the backup file of the match because the match has ended
            File f = new File(fileName);
            f.delete();
        } else {
            for(SocketThread sock : sockets) {
                sock.closeConnection();
            }
        }

        matchStatus = MatchStatus.MATCH_ENDED;
    }
}
