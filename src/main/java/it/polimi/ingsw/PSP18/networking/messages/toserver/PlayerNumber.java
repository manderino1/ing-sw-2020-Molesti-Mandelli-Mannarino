package it.polimi.ingsw.PSP18.networking.messages.toserver;

public class PlayerNumber extends ServerAbstractMessage {
    int n;

    /***
     * Constructor of the WorkReceiver class, a message used to set the workers coordinates for a player
     * @param n x coordinate of the first worker
     */
    public PlayerNumber(int n) {
        this.n = n;
        this.type = ServerMessageType.PLAYER_NUMBER;
    }

    /***
     * Get the number of players chosen
     * @return the number of players
     */
    public int getN() {
        return n;
    }
}
