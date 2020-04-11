package it.polimi.ingsw.PSP18.server.controller;

import it.polimi.ingsw.PSP18.server.model.Match;

import java.util.Comparator;

public class GameManager {
    private Match match;

    public GameManager() {
        match = new Match();
    }

    /***
     * Start the match by ordering players and creating the turn manager
     * Has to be called after the creation of players
     * If Athena is in the match create its special turn manager
     */
    public void startMatch() {
        // Sort players by order
        match.getPlayerManagers().sort(Comparator.comparingInt(o -> o.getPlayerData().getPlayOrder()));

        // Search for Athena
        for (PlayerManager player : match.getPlayerManagers()) {
            if(player.getDivinityName().equals("Athena")) {
                match.setTurnManager(new TurnManagerAthena(match));
                return;
            }
        }
        // If Athena is not found create a standard turn manager
        match.setTurnManager(new TurnManager(match));
    }

    public Match getMatch() {
        return match;
    }
}
