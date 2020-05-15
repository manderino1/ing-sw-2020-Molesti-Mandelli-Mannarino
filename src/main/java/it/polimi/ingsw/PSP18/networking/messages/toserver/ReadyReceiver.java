package it.polimi.ingsw.PSP18.networking.messages.toserver;

/***
 * This message class is used to notify the server that a player is now ready
 */
public class ReadyReceiver extends ServerAbstractMessage {
    /***
     * Message used to notify that the player is ready to play the game
     */
    public ReadyReceiver() {
        this.type = ServerMessageType.READY_RECEIVER;
    }
}
