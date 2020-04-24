package it.polimi.ingsw.PSP18.networking.messages.toserver;

import it.polimi.ingsw.PSP18.server.model.Color;

public class PlayerDataReceiver extends ServerAbstractMessage {
    private String playerID;

    /***
     * Constructor method for the PlayerDataReceiver class, message used to select the the PlayedData of a certain player
     * @param playerID string that contains the in-game player nickname
     */
    public PlayerDataReceiver(String playerID) {
        this.type = ServerMessageType.PLAYER_DATA_RECEIVER;
        this.playerID = playerID;
    }

    /***
     * Return a string with playerID
     * @return player in-game nick name
     */
    public String getPlayerID() {
        return playerID;
    }

}
