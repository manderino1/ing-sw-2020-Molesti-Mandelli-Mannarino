package it.polimi.ingsw.PSP18.server.backup;

import it.polimi.ingsw.PSP18.networking.messages.toclient.PlayerDataUpdate;
import it.polimi.ingsw.PSP18.server.controller.PlayerManager;
import it.polimi.ingsw.PSP18.server.controller.TurnManager;
import it.polimi.ingsw.PSP18.server.model.Cell;
import it.polimi.ingsw.PSP18.server.model.MatchStatus;

import java.io.Serializable;
import java.util.ArrayList;

/***
 * Class that saves all the match information
 */
public class MatchBackup {
    private ArrayList<PlayerManagerBackup> playerManagers = new ArrayList<>();
    private int indexCurrentPlayer;
    private MatchStatus matchStatus;
    private Cell[][] gameMap;

    /***
     * Constructor of the class, initialize the attributes of the class and save the players data
     * @param playerManagers backup of the players data
     * @param indexCurrentPlayer index of the current player playing
     * @param matchStatus current match status
     * @param gameMap current game map
     */
    public MatchBackup(ArrayList<PlayerManager> playerManagers, int indexCurrentPlayer, MatchStatus matchStatus, Cell[][] gameMap) {
        for(PlayerManager player : playerManagers) {
            this.playerManagers.add(new PlayerManagerBackup(player.getWorker(0), player.getWorker(1), player.getPlayerData(), player.getDivinity()));
        }
        this.matchStatus = matchStatus;
        this.gameMap = gameMap;
        this.indexCurrentPlayer = indexCurrentPlayer;
    }

    /***
     * return the list of players managers
     * @return the list of players managers
     */
    public ArrayList<PlayerManagerBackup> getPlayerManagers() {
        return playerManagers;
    }

    /***
     * return the index of the last player turn
     * @return the index of the last player turn
     */
    public int getIndexCurrentPlayer() {
        return indexCurrentPlayer;
    }

    public MatchStatus getMatchStatus() {
        return matchStatus;
    }

    public Cell[][] getGameMap() {
        return gameMap;
    }
}
