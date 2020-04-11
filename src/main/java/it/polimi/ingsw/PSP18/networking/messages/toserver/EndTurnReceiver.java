package it.polimi.ingsw.PSP18.view.messages.toserver;

public class EndTurnReceiver extends ServerAbstractMessage {

    /***
     * constructor of EndTurnReceiver
     */
    public EndTurnReceiver(){
        this.type=ServerMessageType.ENDTURN_RECEIVER;
    }
}
