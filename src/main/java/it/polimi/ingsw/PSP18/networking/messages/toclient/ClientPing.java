package it.polimi.ingsw.PSP18.networking.messages.toclient;

/***
 * This class is used to ping the server periodically
 */
public class ClientPing extends ClientAbstractMessage {
    /***
     * Set ping message type
     */
    public ClientPing() {
        this.type = ClientMessageType.PING;
    }
}
