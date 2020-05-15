package it.polimi.ingsw.PSP18.networking.messages.toclient;

/***
 * This message class is used to tell the client that it's time to place the workers on the map
 */
public class PlaceReady extends ClientAbstractMessage {

    /***
     * Init the message type
     * Used for telling the client that it's time to place the workers on the map
     */
    public PlaceReady() {
        this.type = ClientMessageType.PLACE_READY;
    }
}
