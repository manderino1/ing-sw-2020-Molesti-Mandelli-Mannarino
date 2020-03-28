package it.polimi.ingsw.PSP18.controller;

import it.polimi.ingsw.PSP18.model.Color;
import it.polimi.ingsw.PSP18.model.GameMap;
import it.polimi.ingsw.PSP18.model.PlayerData;

import java.util.ArrayList;
import java.util.Comparator;

public class GameManager {
    private ArrayList<PlayerManager> players = new ArrayList<PlayerManager>();
    private GameMap gameMap;
    private TurnManager turnManager;

    public GameManager() {
    }

    /***
     * When a player has set up its data from the view, add it to the game
     * @param id The id of the player
     * @param color The color of the player
     * @param playOrder The order of play of the player, 0 is first
     */
    public void addPlayer(String id, Color color, Integer playOrder) {
        PlayerData playerData = new PlayerData(id, color, playOrder);
        players.add(new PlayerManager(gameMap, playerData));
    }

    /***
     * Start the match by ordering players and creating the turn manager
     * Has to be called after the creation of players
     * If Athena is in the match create its special turn manager
     */
    public void startMatch() {
        // Sort players by order
        players.sort(Comparator.comparingInt(o -> o.getPlayerData().getPlayOrder()));

        // Search for Athena
        for (PlayerManager player : players) {
            if(player.getDivinityName().equals("Athena")) {
                turnManager = new TurnManagerAthena(players);
                return;
            }
        }
        // If Athena is not found create a standard turn manager
        turnManager = new TurnManager(players);
    }
}
