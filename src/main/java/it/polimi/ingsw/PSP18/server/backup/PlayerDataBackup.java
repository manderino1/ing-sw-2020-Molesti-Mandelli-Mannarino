package it.polimi.ingsw.PSP18.server.backup;

import it.polimi.ingsw.PSP18.server.model.Color;
import it.polimi.ingsw.PSP18.server.model.Move;
import it.polimi.ingsw.PSP18.server.model.PlayerData;

/***
 * Class that saves all the player's data
 */
public class PlayerDataBackup {
    private String playerID;
    private Color playerColor;
    private Integer playOrder;
    private Move lastMove;
    private String divinity;
    private Boolean isReady = false;
    private Boolean hasLost = false;

    /***
     * Constructor of the class, initialize the attributes of the class with the backed up players' data
     * @param playerData parameter that contains the player information
     */
    public PlayerDataBackup(PlayerData playerData) {
        this.playerID = playerData.getPlayerID();
        this.playerColor = playerData.getPlayerColor();
        this.playOrder = playerData.getPlayOrder();
        this.lastMove = playerData.getLastMove();
        this.divinity = playerData.getDivinity();
        this.isReady = playerData.getReady();
        this.hasLost = playerData.getLost();
    }
    /***
     * return the player's ID
     * @return the player's ID
     */
    public String getPlayerID() {
        return playerID;
    }
    /***
     * return the player's color
     * @return the player's color
     */
    public Color getPlayerColor() {
        return playerColor;
    }
    /***
     * return the player turn order
     * @return the player turn order
     */
    public Integer getPlayOrder() {
        return playOrder;
    }
    /***
     * return the last move of the player
     * @return the the last move of the player
     */
    public Move getLastMove() {
        return lastMove;
    }
    /***
     * return the player's divinity
     * @return the the player's divinity
     */
    public String getDivinity() {
        return divinity;
    }
    /***
     * return true if the player is ready
     * @return true if the player is ready
     */
    public Boolean getReady() {
        return isReady;
    }
    /***
     * return true if the player lost the game
     * @return true if the player lost the game
     */
    public Boolean getHasLost() {
        return hasLost;
    }
}
