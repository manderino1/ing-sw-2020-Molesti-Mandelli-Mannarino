package it.polimi.ingsw.PSP18.networking.messages.toclient;

import it.polimi.ingsw.PSP18.server.model.PlayerData;

public class PlayerDataUpdate extends ClientAbstractMessage {
    private PlayerData playerData;

    /***
     * Constructor that initializes the message type and the playerdata attribute
     * @param playerData the player data reference
     */
    public PlayerDataUpdate(PlayerData playerData) {
        this.type = ClientMessageType.PLAYER_DATA_UPDATE;
        this.playerData = playerData;
    }

    /***
     * Returns the player data
     * @return the player data reference
     */
    public PlayerData getPlayerData() {
        return playerData;
    }
}
