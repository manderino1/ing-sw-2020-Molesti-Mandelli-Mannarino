package it.polimi.ingsw.PSP18.view.messages.toclient;

public class MatchReady extends ClientAbstractMessage{

    /***
     * MatchReady constructor method
     */
    public MatchReady() {
        this.type = ClientMessageType.READY;
    }
}
