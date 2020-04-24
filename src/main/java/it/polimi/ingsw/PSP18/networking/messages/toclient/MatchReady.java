package it.polimi.ingsw.PSP18.networking.messages.toclient;

/***
 * This message class is used to tell the player that he can set his status as ready
 */

public class MatchReady extends ClientAbstractMessage{

    /***
     * Init the message type
     */
    public MatchReady() {
        this.type = ClientMessageType.READY;
    }
}
