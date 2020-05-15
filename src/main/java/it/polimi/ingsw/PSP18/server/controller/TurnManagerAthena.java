package it.polimi.ingsw.PSP18.server.controller;
/***
 * class that articulates the players' turns when Athena is in the game
 */
public class TurnManagerAthena extends TurnManager {
    public boolean bool;

    /***
     * constructor of the class, start managing the turn of the players in the current match
     * @param match the object that deals with the current match
     */
    public TurnManagerAthena(Match match) {
        super(match);
    }

    /***
     * When the following turn starts call the manage turn function
     * bool is true if athena moved up last turn
     */
    @Override
    public void manageTurn(){
        int indexPreviousPlayer;
        if(indexCurrentPlayer == 0) {
            indexPreviousPlayer = match.getPlayerManagers().size() - 1;
        } else {
            indexPreviousPlayer = indexCurrentPlayer - 1;
        }
        if (match.getPlayerManagers().get(indexPreviousPlayer).getPlayerData().getLastMove()==null){
            bool = false;
        }
        else{
            if(match.getPlayerManagers().get(indexPreviousPlayer).getDivinityName().equals("Athena")) {
                bool = (match.getPlayerManagers().get(indexPreviousPlayer).getPlayerData().getLastMove().getLevel() == 1);
            }
        }
        match.getPlayerManagers().get(indexCurrentPlayer).manageTurn(bool);
    }
}
