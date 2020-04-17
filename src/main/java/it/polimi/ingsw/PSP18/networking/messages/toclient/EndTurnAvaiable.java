package it.polimi.ingsw.PSP18.networking.messages.toclient;

public class EndTurnAvaiable extends ClientAbstractMessage {
    public EndTurnAvaiable() {
        this.type = ClientMessageType.END_TURN;
    }
}
