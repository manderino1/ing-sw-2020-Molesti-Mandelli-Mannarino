package it.polimi.ingsw.PSP18.networking.messages.toserver;

public class ReadyReceiver extends ServerAbstractMessage {
    /***
     * Message used to notify that the player is ready to play the game
     */
    public ReadyReceiver() {
        this.type = ServerMessageType.READY_RECEIVER;
    }
}
