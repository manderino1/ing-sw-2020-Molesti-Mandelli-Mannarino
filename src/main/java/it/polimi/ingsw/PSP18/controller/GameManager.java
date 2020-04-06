package it.polimi.ingsw.PSP18.controller;

import it.polimi.ingsw.PSP18.model.Color;
import it.polimi.ingsw.PSP18.model.Match;
import it.polimi.ingsw.PSP18.model.PlayerData;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GameManager {
    private TurnManager turnManager;
    private Match match;

    public GameManager() {
        match = new Match();
    }

    /***
     * When a player has set up its data from the view, add it to the game
     * @param id The id of the player
     * @param color The color of the player
     * @param playOrder The order of play of the player, 0 is first
     */
    public void addPlayer(String id, Color color, Integer playOrder, String divinity) {
        PlayerData playerData = new PlayerData(id, color, playOrder);
        match.addPlayer(new PlayerManager(match, playerData, divinity));
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
                turnManager = new TurnManagerAthena(match);
                return;
            }
        }
        // If Athena is not found create a standard turn manager
        turnManager = new TurnManager(match);
    }
}
