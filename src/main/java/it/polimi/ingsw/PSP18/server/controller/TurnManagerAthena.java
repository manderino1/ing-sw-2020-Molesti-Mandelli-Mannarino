package it.polimi.ingsw.PSP18.server.controller;

import it.polimi.ingsw.PSP18.server.model.Match;

public class TurnManagerAthena extends TurnManager {
    public boolean bool;
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
        bool = (match.getPlayerManagers().get(indexPreviousPlayer).getPlayerData().getLastMove().getLevel() == 1) && (match.getPlayerManagers().get(indexPreviousPlayer).getDivinityName().equals("Athena"));
        match.getPlayerManagers().get(indexCurrentPlayer).manageTurn(bool);
    }
}
