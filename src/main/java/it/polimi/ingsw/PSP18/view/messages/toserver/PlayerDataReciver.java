package it.polimi.ingsw.PSP18.view.messages.toserver;

import it.polimi.ingsw.PSP18.model.Color;

public class PlayerDataReciver extends ServerAbstractMessage {
    private String playerID;
    private Color playerColor;
    private Integer playOrder;

    /***
     * Constructor method for the PlaeyerDataReciver class
     * @param playerID string that rappresents the player id
     * @param playerColor Player color
     * @param playOrder Player turn order
     */
    public PlayerDataReciver(String playerID, Color playerColor, Integer playOrder) {
        this.type = ServerMessageType.PLAYER_DATA_RECEIVER;
        this.playerID = playerID;
        this.playerColor = playerColor;
        this.playOrder = playOrder;
    }

    /***
     * Return a string with playerID
     * @return playerID
     */
    public String getPlayerID() {
        return playerID;
    }

    /***
     * Return the player Color
     * @return playerID
     */
    public Color getPlayerColor() {
        return playerColor;
    }

    /***
     * Return player turn order
     * * @return player turn order
     */
    public Integer getPlayOrder() {
        return playOrder;
    }
}
