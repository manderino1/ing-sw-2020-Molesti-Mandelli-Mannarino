package it.polimi.ingsw.PSP18.networking.messages.toserver;

public class Replay extends ServerAbstractMessage {
    /***
     * Message used to notify that the player is ready to play the game
     */
    public Replay() {
        this.type = ServerMessageType.REPLAY;
    }
}
