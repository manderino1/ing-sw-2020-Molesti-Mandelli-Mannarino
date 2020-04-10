package it.polimi.ingsw.PSP18.controller;

import it.polimi.ingsw.PSP18.model.Color;
import it.polimi.ingsw.PSP18.model.GameMap;
import it.polimi.ingsw.PSP18.model.Match;
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
    public void testManageTurn() {
        Match match = new Match();
        match.addPlayer(new PlayerManager(new Match(), new PlayerData("Test1", Color.RED, 0), "Divinity"));
        match.addPlayer(new PlayerManager(new Match(), new PlayerData("Test2", Color.GREEN, 1), "Divinity"));
        match.getPlayerManagers().get(0).placeWorker(0, 0);
        match.getPlayerManagers().get(0).placeWorker(1, 0);
        match.getPlayerManagers().get(1).placeWorker(0, 1);
        match.getPlayerManagers().get(1).placeWorker(1, 1);
        TurnManager turnManager = new TurnManager(match);
        turnManager.manageTurn();
    }

    /***
     * Test for checking the correct creation of the turn manager class with Athena
     */
    @Test
    public void testManageTurnAthena() {
        Match match = new Match();
        match.addPlayer(new PlayerManager(new Match(), new PlayerData("Test1", Color.RED, 0), "Divinity"));
        match.addPlayer(new PlayerManager(new Match(), new PlayerData("Test2", Color.GREEN, 1), "Divinity"));
        match.getPlayerManagers().get(0).placeWorker(0, 0);
        match.getPlayerManagers().get(0).placeWorker(1, 0);
        match.getPlayerManagers().get(1).placeWorker(0, 1);
        match.getPlayerManagers().get(1).placeWorker(1, 1);
        TurnManager turnManager = new TurnManager(match);
        turnManager.manageTurn();
    }
}
