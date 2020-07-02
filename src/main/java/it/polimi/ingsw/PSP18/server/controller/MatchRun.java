package it.polimi.ingsw.PSP18.server.controller;

import it.polimi.ingsw.PSP18.networking.SocketThread;
import it.polimi.ingsw.PSP18.networking.messages.toclient.MatchLost;
import it.polimi.ingsw.PSP18.networking.messages.toclient.MatchWon;
import it.polimi.ingsw.PSP18.networking.messages.toclient.PlaceReady;
import it.polimi.ingsw.PSP18.networking.messages.toclient.StartMatch;
import it.polimi.ingsw.PSP18.networking.messages.toserver.WorkerReceiver;
import it.polimi.ingsw.PSP18.server.model.GameMap;
import it.polimi.ingsw.PSP18.server.model.MatchStatus;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;

/***
 * class that deals with all the request of the game map, workers, turn manager, and the backUp file name
 */
public class MatchRun {
    private TurnManager turnManager;
    private GameMap gameMap;
    private Integer workerPlacementIndex = 1;
    private MatchSocket matchSocket;
    private String fileName;

    /***
     * MatchRun constructor, initializes all the attributes used during a game game
     * @param matchSocket the object dealing with the socket interface
     */
    public MatchRun(MatchSocket matchSocket){
        gameMap = new GameMap();
        this.matchSocket = matchSocket;
    }

    /***
     * Returns the game map
     * @return the game map, unique for all players
     */
    public GameMap getGameMap() {
        return gameMap;
    }

    /***
     * Get the turn manager reference
     * @return the turn manager reference
     */
    public TurnManager getTurnManager() {
        return turnManager;
    }

    /***
     * Set the turn manager reference
     * @param turnManager the turn manager reference
     */
    public void setTurnManager(TurnManager turnManager) {
        this.turnManager = turnManager;
    }

    /***
     * method that receive the worker placed form the player in the client and place them in actual gameMap in the server
     * @param socket the socket we receive message from
     * @param workers the worker the player has placed
     */
    public void workerPlacement(SocketThread socket, WorkerReceiver workers) {
        // If cell occupied or out of bounds (check already in client, just hack check) re-prompt
        if(gameMap.getCell(workers.getX1(), workers.getY1()).getWorker() != null ||
                gameMap.getCell(workers.getX2(), workers.getY2()).getWorker() != null ||
                workers.getX1() < 0 || workers.getX1() > 4 || workers.getX2() < 0 ||
                workers.getX2() > 4 || workers.getY1() < 0 || workers.getY1() > 4 ||
                workers.getY2() < 0 || workers.getY2() > 4) {
            matchSocket.getPlayerSocketMap().get(matchSocket.getPlayerManagers().get(workerPlacementIndex)).sendMessage(new PlaceReady());
        }
        matchSocket.getSocketPlayerMap().get(socket).placeWorker(workers.getX1(), workers.getY1());
        matchSocket.getSocketPlayerMap().get(socket).placeWorker(workers.getX2(), workers.getY2());
        if(workerPlacementIndex == matchSocket.getPlayerManagers().size()) {
            startMatch();
        } else {
            matchSocket.getPlayerSocketMap().get(matchSocket.getPlayerManagers().get(workerPlacementIndex)).sendMessage(new PlaceReady());
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
        matchSocket.getPlayerManagers().sort(Comparator.comparingInt(o -> o.getPlayerData().getPlayOrder()));
        matchSocket.setMatchStatus(MatchStatus.MATCH_STARTED);
        for(SocketThread socket : matchSocket.getSockets()) {
            socket.sendMessage(new StartMatch());
        }
        // Set the backup file path
        String fileName = "Backups/match_";
        ArrayList<String> names = new ArrayList<>();
        for(PlayerManager player : matchSocket.getPlayerManagers()) {
            names.add(player.getPlayerData().getPlayerID());
        }
        names.sort(String::compareToIgnoreCase);
        for(String name: names) {
            fileName = fileName.concat(name);
        }

        this.fileName = (fileName.concat(".bak"));
        BackupManager backupManager = new BackupManager(matchSocket, this);
        // Search for Athena
        for (PlayerManager player : matchSocket.getPlayerManagers()) {
            if(player.getDivinityName().equals("Athena")) {
                turnManager = new TurnManagerAthena(matchSocket, backupManager);
                return;
            }
        }
        // If Athena is not found create a standard turn manager
        turnManager = new TurnManager(matchSocket, backupManager);
    }

    /***
     * Method used to end the match, sends to the winner the WinningPopUp and to the losers the LostPopUp
     * @param winner the player that has won
     */
    public void endMatch(PlayerManager winner) {
        if(winner != null) {
            matchSocket.getPlayerSocketMap().get(winner).sendMessage(new MatchWon(winner.getPlayerData().getPlayerID(), true));
            ArrayList<String> loserIDs = new ArrayList<>();
            for(SocketThread socket : matchSocket.getSockets()) {
                if (matchSocket.getSocketPlayerMap().get(socket) != winner) {
                    loserIDs.add(matchSocket.getSocketPlayerMap().get(socket).getPlayerData().getPlayerID());
                }
            }
            for(SocketThread socket : matchSocket.getSockets()) {
                for(String loserID : loserIDs) {
                    if (matchSocket.getSocketPlayerMap().get(socket).getPlayerData().getPlayerID().equals(loserID)) {
                        socket.sendMessage(new MatchLost(loserID, true, true));
                    } else {
                        socket.sendMessage(new MatchLost(loserID, false, true));
                    }
                }
            }

            // Clean the socket list
            matchSocket.getSockets().clear();

            // Cancel the backup file of the match because the match has ended
            if(fileName != null) {
                File f = new File(fileName);
                f.delete();
            }
        }

        matchSocket.setMatchStatus(MatchStatus.MATCH_ENDED);
    }

    /***
     * Detaches the observers to a player
     * @param socket the player that needs his observers detached
     */
    public void detachSocket(SocketThread socket) {
        for(PlayerManager player : matchSocket.getPlayerManagers()) {
            player.getPlayerData().detachSocket(socket);
        }
        gameMap.detachSocket(socket);
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
