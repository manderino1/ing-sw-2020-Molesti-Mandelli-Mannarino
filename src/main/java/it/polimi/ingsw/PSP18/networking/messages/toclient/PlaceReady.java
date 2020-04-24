package it.polimi.ingsw.PSP18.networking.messages.toclient;

public class PlaceReady extends ClientAbstractMessage {

    /***
     * Init the message type
     * Used for telling the client that it's time to place the workers on the map
     */
    public PlaceReady() {
        this.type = ClientMessageType.PLACE_READY;
    }
}
