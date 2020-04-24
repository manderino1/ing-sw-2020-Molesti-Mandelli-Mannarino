package it.polimi.ingsw.PSP18.networking.messages.toclient;

public class MatchReady extends ClientAbstractMessage{

    /***
     * Init the message type
     */
    public MatchReady() {
        this.type = ClientMessageType.READY;
    }
}
