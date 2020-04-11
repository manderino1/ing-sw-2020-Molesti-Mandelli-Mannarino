package it.polimi.ingsw.PSP18.networking.messages.toclient;

public class MatchWon extends ClientAbstractMessage{

    private String playerID;

    /***
     * constructor of MatchWon
     * @param playerID The player ID
     */
    public MatchWon(String playerID){
        this.type=ClientMessageType.MATCH_WON;
        this.playerID=playerID;
    }

    /***
     * returns the playerID
     * @return the playerID
     */
    public String getMatchWon() {
        return playerID;
    }
}
