package it.polimi.ingsw.PSP18.networking.messages.toclient;

import it.polimi.ingsw.PSP18.client.view.cli.InputParser;

/***
 * This message class is used to tell the client that the server is waiting for the nickname
 */
public class WaitingNick extends ClientAbstractMessage {

    /***
     * Init the message type
     * Used when the player has to write is nickname to the server
     */
    public WaitingNick() {
        this.type=ClientMessageType.WAITING_NICK;
    }
}
