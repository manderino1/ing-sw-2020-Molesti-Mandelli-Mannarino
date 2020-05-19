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

public class MatchRun {
    private TurnManager turnManager;
    private GameMap gameMap;
    private Integer workerPlacementIndex = 1;
    private Match match;
    private int playerN;

    /***
     * MatchRun constructor, initializes all the attributes used during a game game
     */
    public MatchRun(Match match){
        gameMap = new GameMap();
        this.match = match;
    }

    /***
     * MatchRun constructor with the number of players playing
     */
    public MatchRun(Match match, int playerN){
        this(match);
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
     * Get the turn manager reference
     * @return the turn manager reference
     */
    public TurnManager getTurnManager() {
        return turnManager;
    }

    /***
     * Set the turn manager reference
     */
    public void setTurnManager(TurnManager turnManager) {
        this.turnManager = turnManager;
    }

    /***
     * method that recieve the worker placed form the player in the client and place them in actual gameMap in the server
     * @param socket the socket we receive message from
     * @param workers the worker the player has placed
     */
    public void workerPlacement(SocketThread socket, WorkerReceiver workers) {
        if(gameMap.getCell(workers.getX1(), workers.getY1()).getWorker() != null || gameMap.getCell(workers.getX2(), workers.getY2()).getWorker() != null) {
            match.getMatchSocket().getPlayerSocketMap().get(match.getMatchSocket().getPlayerManagers().get(workerPlacementIndex)).sendMessage(new PlaceReady());
        }
        match.getMatchSocket().getSocketPlayerMap().get(socket).placeWorker(workers.getX1(), workers.getY1());
        match.getMatchSocket().getSocketPlayerMap().get(socket).placeWorker(workers.getX2(), workers.getY2());
        if(workerPlacementIndex == match.getMatchSocket().getPlayerManagers().size()) {
            startMatch();
        } else {
            match.getMatchSocket().getPlayerSocketMap().get(match.getMatchSocket().getPlayerManagers().get(workerPlacementIndex)).sendMessage(new PlaceReady());
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
        match.getMatchSocket().getPlayerManagers().sort(Comparator.comparingInt(o -> o.getPlayerData().getPlayOrder()));
        match.setMatchStatus(MatchStatus.MATCH_STARTED);
        for(SocketThread socket : match.getMatchSocket().getSockets()) {
            socket.sendMessage(new StartMatch());
        }
        // Set the backup file path
        String fileName = "Backups/match_";
        ArrayList<String> names = new ArrayList<>();
        for(PlayerManager player : match.getMatchSocket().getPlayerManagers()) {
            names.add(player.getPlayerData().getPlayerID());
        }
        names.sort(String::compareToIgnoreCase);
        for(String name: names) {
            fileName = fileName.concat(name);
        }
        match.getBackupManager().setFileName(fileName.concat(".bak"));
        // Search for Athena
        for (PlayerManager player : match.getMatchSocket().getPlayerManagers()) {
            if(player.getDivinityName().equals("Athena")) {
                turnManager = new TurnManagerAthena(match);
                return;
            }
        }
        // If Athena is not found create a standard turn manager
        turnManager = new TurnManager(match);
    }

    /***
     * Method used to end the match, sends to the winner the WinningPopUp and to the losers the LostPopUp
     * @param winner the player that has won
     */
    public void endMatch(PlayerManager winner) {
        if(winner != null) {
            match.getMatchSocket().getPlayerSocketMap().get(winner).sendMessage(new MatchWon(winner.getPlayerData().getPlayerID(), true));
            ArrayList<String> loserIDs = new ArrayList<>();
            for(SocketThread socket : match.getMatchSocket().getSockets()) {
                if (match.getMatchSocket().getSocketPlayerMap().get(socket) != winner) {
                    loserIDs.add(match.getMatchSocket().getSocketPlayerMap().get(socket).getPlayerData().getPlayerID());
                }
            }
            for(SocketThread socket : match.getMatchSocket().getSockets()) {
                for(String loserID : loserIDs) {
                    if (match.getMatchSocket().getSocketPlayerMap().get(socket).getPlayerData().getPlayerID().equals(loserID)) {
                        socket.sendMessage(new MatchLost(loserID, true, true));
                    } else {
                        socket.sendMessage(new MatchLost(loserID, false, true));
                    }
                }
            }

            // Cancel the backup file of the match because the match has ended
            if(match.getBackupManager().getFileName() != null) {
                File f = new File(match.getBackupManager().getFileName());
                f.delete();
            }
        } else {
            for(SocketThread sock : match.getMatchSocket().getSockets()) {
                sock.closeConnection();
            }
        }

        match.setMatchStatus(MatchStatus.MATCH_ENDED);
    }
}
