package it.polimi.ingsw.PSP18.controller;

import it.polimi.ingsw.PSP18.model.Color;
import it.polimi.ingsw.PSP18.model.Direction;
import it.polimi.ingsw.PSP18.model.PlayerData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestGameManager {
    private GameManager gameManager;

    @Before
    public void createGameManager() {
        gameManager = new GameManager();
    }

    /***
     * Test for checking the correct creation of the turn manager class
     * Test for checking the correct sort of the players array by turn
     */
    @Test
    public void testTurnInitAndSort() {
        gameManager.getMatch().addPlayer(new PlayerManager(gameManager.getMatch(), new PlayerData("Test1", Color.GREEN, 1), "Apollo"));
        gameManager.getMatch().addPlayer(new PlayerManager(gameManager.getMatch(), new PlayerData("Test2", Color.GREEN, 0), "Apollo"));
        gameManager.startMatch();
        Assert.assertEquals(Integer.valueOf(0), gameManager.getMatch().getPlayerManagers().get(0).getPlayerData().getPlayOrder());
        Assert.assertEquals("Test2", gameManager.getMatch().getPlayerManagers().get(0).getPlayerData().getPlayerID());
        Assert.assertEquals(Integer.valueOf(1), gameManager.getMatch().getPlayerManagers().get(1).getPlayerData().getPlayOrder());
        Assert.assertEquals("Test1", gameManager.getMatch().getPlayerManagers().get(1).getPlayerData().getPlayerID());
    }

    /***
     * Test for checking the correct creation of the turn manager Athena class
     */
    @Test
    public void testAthenaInit() {
        gameManager.getMatch().addPlayer(new PlayerManager(gameManager.getMatch(), new PlayerData("Test1", Color.GREEN, 1), "Athena"));
        gameManager.startMatch();
        Assert.assertEquals(Integer.valueOf(1), gameManager.getMatch().getPlayerManagers().get(0).getPlayerData().getPlayOrder());
        Assert.assertEquals("Test1", gameManager.getMatch().getPlayerManagers().get(0).getPlayerData().getPlayerID());
    }
}
