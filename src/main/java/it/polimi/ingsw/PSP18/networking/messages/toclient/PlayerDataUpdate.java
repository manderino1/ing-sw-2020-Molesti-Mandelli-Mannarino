package it.polimi.ingsw.PSP18.networking.messages.toclient;

import it.polimi.ingsw.PSP18.server.model.Color;
import it.polimi.ingsw.PSP18.server.model.PlayerData;

public class PlayerDataUpdate extends ClientAbstractMessage {
    private String playerID;
    private Color playerColor;
    private Integer playOrder;
    private String divinity;

    /***
     * Constructor that initializes the message type and the playerdata attribute
     * @param playerData the player data reference
     */
    public PlayerDataUpdate(PlayerData playerData) {
        this.type = ClientMessageType.PLAYER_DATA_UPDATE;
        this.playerID = playerData.getPlayerID();
        this.playerColor = playerData.getPlayerColor();
        this.playOrder = playerData.getPlayOrder();
        this.divinity = playerData.getDivinity();
    }

    public String getPlayerID() {
        return playerID;
    }

    public Color getPlayerColor() {
        return playerColor;
    }

    public Integer getPlayOrder() {
        return playOrder;
    }

    public String getDivinity() {
        return divinity;
    }
}
