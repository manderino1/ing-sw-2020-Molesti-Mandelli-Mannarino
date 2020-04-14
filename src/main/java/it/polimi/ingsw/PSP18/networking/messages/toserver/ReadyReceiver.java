package it.polimi.ingsw.PSP18.networking.messages.toserver;

public class ReadyReceiver extends ServerAbstractMessage {
    public ReadyReceiver() {
        this.type = ServerMessageType.READY_RECEIVER;
    }
}
