package it.polimi.ingsw.PSP18.networking.messages.toclient;

/***
 * This message class is used when the match starts to notify the client of this
 */
public class StartMatch extends ClientAbstractMessage {

    /***
     * StartMatch constructor method
     * Used when the match starts to tell the client
     */
    public StartMatch() {
        this.type = ClientMessageType.START_MATCH;
    }
}
