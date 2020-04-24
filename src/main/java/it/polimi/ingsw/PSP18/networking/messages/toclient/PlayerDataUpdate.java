package it.polimi.ingsw.PSP18.networking.messages.toclient;

import it.polimi.ingsw.PSP18.server.model.Color;
import it.polimi.ingsw.PSP18.server.model.PlayerData;

/***
 * This message class is used to sent the client an update to the data of a player
 */
public class PlayerDataUpdate extends ClientAbstractMessage {
    private String playerID;
    private Color playerColor;
    private Integer playOrder;
    private String divinity;

    /***
     * Constructor that initializes the message type and the playerData attribute
     * @param playerData the player data reference
     */
    public PlayerDataUpdate(PlayerData playerData) {
        this.type = ClientMessageType.PLAYER_DATA_UPDATE;
        this.playerID = playerData.getPlayerID();
        this.playerColor = playerData.getPlayerColor();
        this.playOrder = playerData.getPlayOrder();
        this.divinity = playerData.getDivinity();
    }

    /***
     * Returns the ID of the player
     * @return the id of the player
     */
    public String getPlayerID() {
        return playerID;
    }

    /***
     * Returns the color of the player, red, green or blue
     * @return the color of the player
     */
    public Color getPlayerColor() {
        return playerColor;
    }

    /***
     * Returns the order of play, 0 is first
     * @return the order of play of the player
     */
    public Integer getPlayOrder() {
        return playOrder;
    }

    /***
     * Returns the player divinity name
     * @return the player divinity
     */
    public String getDivinity() {
        return divinity;
    }
}
