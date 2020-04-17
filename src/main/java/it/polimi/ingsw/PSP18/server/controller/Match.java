package it.polimi.ingsw.PSP18.server.controller;

import it.polimi.ingsw.PSP18.networking.SocketClient;
import it.polimi.ingsw.PSP18.networking.messages.toclient.DivinityList;
import it.polimi.ingsw.PSP18.networking.messages.toclient.MatchReady;
import it.polimi.ingsw.PSP18.networking.messages.toclient.PlaceReady;
import it.polimi.ingsw.PSP18.networking.messages.toclient.WaitingNick;
import it.polimi.ingsw.PSP18.networking.messages.toserver.WorkerReceiver;
import it.polimi.ingsw.PSP18.server.controller.PlayerManager;
import it.polimi.ingsw.PSP18.server.controller.TurnManager;
import it.polimi.ingsw.PSP18.networking.SocketServer;
import it.polimi.ingsw.PSP18.networking.SocketThread;
import it.polimi.ingsw.PSP18.server.controller.TurnManagerAthena;
import it.polimi.ingsw.PSP18.server.model.GameMap;
import it.polimi.ingsw.PSP18.server.model.MatchStatus;
import it.polimi.ingsw.PSP18.server.view.MapObserver;
import it.polimi.ingsw.PSP18.server.view.PlayerDataObserver;

import java.util.*;

public class Match {
    private ArrayList<PlayerManager> playerManagers;
    private TurnManager turnManager;
    private ArrayList<SocketThread> sockets;
    private SocketServer socketServer;
    private HashMap<PlayerManager, SocketThread> playerSocketMap;
    private HashMap<SocketThread, PlayerManager> socketPlayerMap;
    private PlayerManager currentPlayer;
    private MatchStatus matchStatus;
    private GameMap gameMap;
    private ArrayList<String> divinitySelection = new ArrayList<String>();
    private Integer divinitySelectionIndex = 0;
    private Integer workerPlacementIndex = 0;

    /***
     * Match constructor, initializes the arrayLists and the game map
     */
    public Match(){
        playerManagers = new ArrayList<PlayerManager>();
        sockets = new ArrayList<SocketThread>();
        playerSocketMap = new HashMap<PlayerManager, SocketThread>();
        socketPlayerMap = new HashMap<SocketThread, PlayerManager>();
        socketServer = new SocketServer(this);
        socketServer.start(); // Wait for connections
        gameMap = new GameMap();
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
     */
    public void addPlayer(PlayerManager player, SocketThread socket){
        for(PlayerManager playerPresent : playerManagers) {
            if(player.getPlayerData().getPlayerID().equals(playerPresent.getPlayerData().getPlayerID())) {
                socket.sendMessage(new WaitingNick());
                return;
            }
        }
        playerManagers.add(player);
        playerSocketMap.put(player, socket);
        socketPlayerMap.put(socket, player);
        if(playerManagers.size() == 2) { // Ask to every connected player to write if ready
            for(PlayerManager playerPresent : playerManagers) {
                playerSocketMap.get(playerPresent).sendMessage(new MatchReady());
            }
        } else if (playerManagers.size() == 3) {
            sockets.get(2).sendMessage(new MatchReady());
        }
    }

    /***
     * Add a socket to the sockets list and register the observers
     * related to the socket and client connection
     * @param socket the socket reference
     */
    public void addSocket(SocketThread socket){
        sockets.add(socket);
        socket.sendMessage(new WaitingNick());
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

    /***
     * Wait for all the players to be ready and then start the divinity selection phase
     * @param socket the reference to the socket
     */
    public void readyManagement(SocketThread socket) {
        socketPlayerMap.get(socket).getPlayerData().setReady();
        for(PlayerManager player : playerManagers) {
            if(!player.getPlayerData().getReady()) {
                return;
            }
        }
        // If i manage to arrive here all the players are ready, i can start the divinity selection phase
        matchStatus = MatchStatus.DIVINITIES_SELECTION;
        String[] divinities = {"Apollo", "Artemis", "Athena", "Atlas", "Demeter", "Ephaestus", "Minotaur", "Pan", "Prometheus"};
        Collections.shuffle(Arrays.asList(divinities));
        divinitySelection.addAll(Arrays.asList(divinities).subList(0, playerManagers.size()));
        playerSocketMap.get(playerManagers.get(divinitySelectionIndex)).sendMessage(new DivinityList(divinitySelection));
        divinitySelectionIndex++;
    }

    /***
     * Create the divinity of the player that decided which divinity to use
     * If there are no more players that have to choose the divinity start the match
     * If there are other players, ask the next to choose the divinity
     * @param socket the socket reference, used to get the correct player
     * @param divinity string that represent the divinity to be created
     */
    public void divinityCreation(SocketThread socket, String divinity) {
        socketPlayerMap.get(socket).divinityCreation(divinity);
        if(divinitySelectionIndex == playerManagers.size()) {
            // Set observers
            // TODO: move in separate function
            for(SocketThread sock : sockets) {
                gameMap.attach(new MapObserver(sock));
                for(PlayerManager player : playerManagers) {
                    player.getPlayerData().attach(new PlayerDataObserver(sock));
                }
            }

            playerSocketMap.get(playerManagers.get(workerPlacementIndex)).sendMessage(new PlaceReady());
            workerPlacementIndex++;
        } else {
            divinitySelection.remove(divinity);
            playerSocketMap.get(playerManagers.get(divinitySelectionIndex)).sendMessage(new DivinityList(divinitySelection));
            divinitySelectionIndex++;
        }
    }

    public void workerPlacement(SocketThread socket, WorkerReceiver workers) {
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

        // Search for Athena
        for (PlayerManager player : playerManagers) {
            if(player.getDivinityName().equals("Athena")) {
                turnManager = new TurnManagerAthena(this);
                return;
            }
        }
        // If Athena is not found create a standard turn manager
        matchStatus = MatchStatus.MATCH_STARTED;
        turnManager = new TurnManager(this);
    }

    public SocketThread getCurrentSocket() {
        return playerSocketMap.get(currentPlayer);
    }

    public void endMatch() {
        matchStatus = MatchStatus.MATCH_ENDED;
        // Detach observers from map
        for(SocketThread sock : sockets) {
            sock.closeConnection();
        }
    }
}
