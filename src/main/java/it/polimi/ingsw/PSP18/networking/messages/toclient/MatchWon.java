package it.polimi.ingsw.PSP18.networking.messages.toclient;

/***
 * This message class is used to tell the players that a player has won
 */
public class MatchWon extends ClientAbstractMessage{

    private String playerID;
    private boolean me;

    /***
     * Init the type and the ID of the player that has won
     * @param playerID The player ID
     */
    public MatchWon(String playerID, boolean me){
        this.type=ClientMessageType.MATCH_WON;
        this.playerID=playerID;
        this.me = me;
    }

    /***
     * Returns the ID of the player that has won
     * @return the ID of the player that has won
     */
    public String getMatchWon() {
        return playerID;
    }

    /***
     * True if i'm the one that has lost
     * @return true if is me
     */
    public boolean isMe() {
        return me;
    }
}
