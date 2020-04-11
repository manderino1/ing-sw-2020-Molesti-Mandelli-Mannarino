package it.polimi.ingsw.PSP18.networking.messages.toclient;


public class MatchLost extends ClientAbstractMessage{

    private String playerID;

    /***
     * constructor of MatchLost
     * @param playerID the player ID
     */
    public MatchLost(String playerID){
        this.type=ClientMessageType.MATCH_LOST;
        this.playerID=playerID;
    }

    /***
     * returns the player ID
     * @return the player ID
     */
    public String getMatchLost() {
        return playerID;
    }

}
