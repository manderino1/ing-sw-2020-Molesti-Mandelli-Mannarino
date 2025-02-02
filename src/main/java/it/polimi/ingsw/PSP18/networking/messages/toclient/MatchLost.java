package it.polimi.ingsw.PSP18.networking.messages.toclient;

/***
 * This message class is used to tell the players that a player has lost
 */
public class MatchLost extends ClientAbstractMessage{

    private String playerID;
    private boolean me;
    private boolean isFinished;

    /***
     * Init the type of the message and the ID of the player that has lost
     * @param playerID the player ID
     * @param isFinished true if the match has ended
     * @param me true if i'm the one that has lost
     */
    public MatchLost(String playerID, boolean me, boolean isFinished){
        this.type=ClientMessageType.MATCH_LOST;
        this.playerID=playerID;
        this.me = me;
        this.isFinished = isFinished;
    }

    /***
     * Returns boolean isFinished
     * @return true if the match is ended
     */
    public boolean isFinished() {
        return isFinished;
    }

    /***
     * Returns the player ID of the player that has lost
     * @return the ID of the player that has lost
     */
    public String getMatchLost() {
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
