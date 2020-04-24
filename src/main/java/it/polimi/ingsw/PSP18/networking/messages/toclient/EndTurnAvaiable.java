package it.polimi.ingsw.PSP18.networking.messages.toclient;

/***
 * This message class is used when there is the possibility for the client to end the turn
 */

public class EndTurnAvaiable extends ClientAbstractMessage {
    /***
     * Init the message type
     */
    public EndTurnAvaiable() {
        this.type = ClientMessageType.END_TURN;
    }
}
