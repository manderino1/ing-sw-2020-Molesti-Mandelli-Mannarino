package it.polimi.ingsw.PSP18.controller;

import it.polimi.ingsw.PSP18.model.Color;
import it.polimi.ingsw.PSP18.model.GameMap;
import it.polimi.ingsw.PSP18.model.PlayerData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class TestTurnManager {
    private GameManager gameManager;

    @Before
    public void createGameManager() {
        gameManager = new GameManager();
    }

    /***
     * Test for checking the correct creation of the turn manager class
     */
    @Test
    public void testTurnInit() {
        gameManager.addPlayer("Test1", Color.RED, 0, "Apollo");
        gameManager.addPlayer("Test2", Color.GREEN, 1, "Apollo");
        gameManager.startMatch();
    }

    /***
     * Test for checking the correct creation of the turn manager class
     */
    @Test
    public void testTurnInitAthena() {
        gameManager.addPlayer("Test1", Color.RED, 0, "Apollo");
        gameManager.addPlayer("Test2", Color.GREEN, 1, "Athena");
        gameManager.startMatch();
    }

    /***
     * Test for checking the correct creation of the turn manager class
     */
    @Test
    public void testManageTurn() {
        ArrayList<PlayerManager> players = new ArrayList<PlayerManager>();
        players.add(new PlayerManager(new GameMap(), new PlayerData("Test1", Color.RED, 0), "Divinity"));
        players.add(new PlayerManager(new GameMap(), new PlayerData("Test2", Color.GREEN, 1), "Divinity"));
        players.get(0).placeWorker(0, 0);
        players.get(0).placeWorker(1, 0);
        players.get(1).placeWorker(0, 1);
        players.get(1).placeWorker(1, 1);
        TurnManager turnManager = new TurnManager(players);
        turnManager.ManageTurn();
    }

    /***
     * Test for checking the correct creation of the turn manager class with Athena
     */
    @Test
    public void testManageTurnAthena() {
        ArrayList<PlayerManager> players = new ArrayList<PlayerManager>();
        players.add(new PlayerManager(new GameMap(), new PlayerData("Test1", Color.RED, 0), "Divinity"));
        players.add(new PlayerManager(new GameMap(), new PlayerData("Test2", Color.GREEN, 1), "Divinity"));
        players.get(0).placeWorker(0, 0);
        players.get(0).placeWorker(1, 0);
        players.get(1).placeWorker(0, 1);
        players.get(1).placeWorker(1, 1);
        TurnManager turnManager = new TurnManager(players);
        turnManager.ManageTurn();
    }
}
