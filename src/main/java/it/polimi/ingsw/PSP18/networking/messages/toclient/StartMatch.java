package it.polimi.ingsw.PSP18.networking.messages.toclient;

public class StartMatch extends ClientAbstractMessage {

    /***
     * StartMatch constructor method
     */
    public StartMatch() {
        this.type = ClientMessageType.START_MATCH;
    }
}
