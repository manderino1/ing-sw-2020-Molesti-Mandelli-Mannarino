package it.polimi.ingsw.PSP18.server.controller;
/***
 * class that articulates the players' turns when Athena is in the game
 */
public class TurnManagerAthena extends TurnManager {
    public boolean bool = false;

    /***
     * constructor of the class, start managing the turn of the players in the current match
     * @param match the object that deals with the current match
     */
    public TurnManagerAthena(Match match) {
        super(match);
    }

    /***
     * constructor of the class, start managing the turn of the players in the current match
     * @param match the object that deals with the current match
     * @param indexCurrentPlayer the index of the player that has to play, for restoring
     */
    public TurnManagerAthena(Match match, int indexCurrentPlayer) {
        super(match, indexCurrentPlayer);
    }

    /***
     * When the following turn starts call the manage turn function
     * bool is true if athena moved up last turn
     */
    @Override
    public void manageTurn(){
        for(PlayerManager player : match.getMatchSocket().getPlayerManagers()) {
            if(player.getDivinityName().equals("Athena")) { // Update Athena movement
                bool = (player.getPlayerData().getLastMove().getLevel() == 1);
            }
        }

        if(match.getMatchSocket().getPlayerManagers().get(indexCurrentPlayer).getDivinityName().equals("Athena")) { // Athena can go up
            match.getMatchSocket().getPlayerManagers().get(indexCurrentPlayer).manageTurn(false);
        } else { // Call the manager with the correct raiseForbidden parameter
            match.getMatchSocket().getPlayerManagers().get(indexCurrentPlayer).manageTurn(bool);
        }
    }
}
