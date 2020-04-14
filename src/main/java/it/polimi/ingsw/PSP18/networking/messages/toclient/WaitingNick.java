package it.polimi.ingsw.PSP18.networking.messages.toclient;

import it.polimi.ingsw.PSP18.client.view.cli.InputParser;

public class WaitingNick extends ClientAbstractMessage {

    public WaitingNick() {
        this.type=ClientMessageType.WAITING_NICK;
    }

}
