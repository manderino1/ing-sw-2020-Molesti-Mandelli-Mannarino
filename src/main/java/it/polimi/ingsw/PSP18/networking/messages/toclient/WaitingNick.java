package it.polimi.ingsw.PSP18.networking.messages.toclient;

public class WaitingNick extends ClientAbstractMessage {
    public WaitingNick() {
        this.type=ClientMessageType.WAITING_NICK;
    }
}
