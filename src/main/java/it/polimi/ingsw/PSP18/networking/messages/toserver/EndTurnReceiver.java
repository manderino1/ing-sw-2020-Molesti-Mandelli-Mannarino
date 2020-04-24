package it.polimi.ingsw.PSP18.networking.messages.toserver;

public class EndTurnReceiver extends ServerAbstractMessage {

    /***
     * Constructor of EndTurnReceiver, message used to end the current player turn
     */
    public EndTurnReceiver(){
        this.type=ServerMessageType.ENDTURN_RECEIVER;
    }
}
