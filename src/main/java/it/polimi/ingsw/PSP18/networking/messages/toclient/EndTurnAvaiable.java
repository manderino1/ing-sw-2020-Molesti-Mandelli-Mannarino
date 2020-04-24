package it.polimi.ingsw.PSP18.networking.messages.toclient;

public class EndTurnAvaiable extends ClientAbstractMessage {
    /***
     * Init the message type
     */
    public EndTurnAvaiable() {
        this.type = ClientMessageType.END_TURN;
    }
}
