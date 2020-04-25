package it.polimi.ingsw.PSP18.networking.messages.toserver;

/***
 * This class is used to reply to the ping
 */
public class ServerPing extends ServerAbstractMessage {
    /***
     * Set pong message type
     */
    public ServerPing() {
        this.type = ServerMessageType.PONG;
    }
}
