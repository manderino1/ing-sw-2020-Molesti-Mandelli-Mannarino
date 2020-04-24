package it.polimi.ingsw.PSP18.networking.messages.toserver;

/***
 * This is the class that represents a message used to signal the current player end of the turn
 */
public class EndTurnReceiver extends ServerAbstractMessage {

    /***
     * Constructor of EndTurnReceiver, message used to end the current player turn
     */
    public EndTurnReceiver(){
        this.type=ServerMessageType.ENDTURN_RECEIVER;
    }
}
