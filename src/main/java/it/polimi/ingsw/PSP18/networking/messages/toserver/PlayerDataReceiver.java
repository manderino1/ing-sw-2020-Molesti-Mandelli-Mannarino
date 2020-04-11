package it.polimi.ingsw.PSP18.networking.messages.toserver;

import it.polimi.ingsw.PSP18.server.model.Color;

public class PlayerDataReceiver extends ServerAbstractMessage {
    private String playerID;

    /***
     * Constructor method for the PlaeyerDataReciver class
     * @param playerID string that rappresents the player id
     */
    public PlayerDataReceiver(String playerID) {
        this.type = ServerMessageType.PLAYER_DATA_RECEIVER;
        this.playerID = playerID;
    }

    /***
     * Return a string with playerID
     * @return playerID
     */
    public String getPlayerID() {
        return playerID;
    }

}
