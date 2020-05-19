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
    private TurnManager turnManager;
    private MatchStatus matchStatus;
    private GameMap gameMap;
    private Integer workerPlacementIndex = 0;
    private ArrayList<String> divinities;
    private int playerN;

    private MatchSocket matchSocket;
    private BackupManager backupManager;
    private MatchRun matchRun;
    private MatchSetUp matchSetUp;

    /***
     * Match constructor, initializes the arrayLists and the game map
     */
    public Match(){
        backupManager= new BackupManager(this);
        gameMap = new GameMap();
        matchStatus = MatchStatus.WAITING_FOR_PLAYERS;
        String[] divArray = {"Apollo", "Artemis", "Athena", "Atlas", "Demeter", "Hephaestus", "Minotaur", "Pan", "Prometheus"};
        divinities = new ArrayList<>(Arrays.asList(divArray));

        MatchSocket matchSocket = new MatchSocket(this);
        BackupManager backupManager = new BackupManager(this);
        MatchSetUp matchSetUp = new MatchSetUp(this);
        MatchRun matchRun = new MatchRun(this);
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
    public BackupManager getBackupManager(){
        return backupManager;
    }

    public MatchSocket getMatchSocket(){
        return matchSocket;
    }

    public MatchRun getMatchRun() {
        return matchRun;
    }

    public MatchSetUp getMatchSetUp() {
        return matchSetUp;
    }
}
