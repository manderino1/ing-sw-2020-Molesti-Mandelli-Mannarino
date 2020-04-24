package it.polimi.ingsw.PSP18.networking.messages.toclient;


public class MatchLost extends ClientAbstractMessage{

    private String playerID;

    /***
     * Init the type of the message and the ID of the player that has lost
     * @param playerID the player ID
     */
    public MatchLost(String playerID){
        this.type=ClientMessageType.MATCH_LOST;
        this.playerID=playerID;
    }

    /***
     * Returns the player ID of the player that has lost
     * @return the ID of the player that has lost
     */
    public String getMatchLost() {
        return playerID;
    }

}
