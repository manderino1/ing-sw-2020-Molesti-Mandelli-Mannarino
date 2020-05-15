package it.polimi.ingsw.PSP18.networking.messages.toclient;

import it.polimi.ingsw.PSP18.server.model.Direction;
import it.polimi.ingsw.PSP18.server.model.Worker;

import java.util.ArrayList;

public class PlayerNumberReady extends ClientAbstractMessage {
    /***
     * Tell the client that you are ready to receive the number of messages
     */
    public PlayerNumberReady(){
        this.type=ClientMessageType.PLAYER_NUMBER_READY;
    }
}
