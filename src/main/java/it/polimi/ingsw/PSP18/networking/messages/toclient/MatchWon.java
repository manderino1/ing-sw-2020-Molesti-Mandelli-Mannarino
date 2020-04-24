package it.polimi.ingsw.PSP18.networking.messages.toclient;

/***
 * This message class is used to tell the players that a player has won
 */
public class MatchWon extends ClientAbstractMessage{

    private String playerID;

    /***
     * Init the type and the ID of the player that has won
     * @param playerID The player ID
     */
    public MatchWon(String playerID){
        this.type=ClientMessageType.MATCH_WON;
        this.playerID=playerID;
    }

    /***
     * Returns the ID of the player that has won
     * @return the ID of the player that has won
     */
    public String getMatchWon() {
        return playerID;
    }
}
