package it.polimi.ingsw.PSP18.networking.messages.toclient;

public class PlaceReady extends ClientAbstractMessage {
    public PlaceReady() {
        this.type = ClientMessageType.PLACE_READY;
    }
}
