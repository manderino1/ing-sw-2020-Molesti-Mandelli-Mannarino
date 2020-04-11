package it.polimi.ingsw.PSP18.view.messages.toclient;

public class StartMatch extends ClientAbstractMessage {

    /***
     * StartMatch constructor method
     */
    public StartMatch() {
        this.type = ClientMessageType.START_MATCH;
    }
}
