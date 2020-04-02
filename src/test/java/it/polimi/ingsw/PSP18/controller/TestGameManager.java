package it.polimi.ingsw.PSP18.controller;

import it.polimi.ingsw.PSP18.model.Color;
import it.polimi.ingsw.PSP18.model.Direction;
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
     * Test for checking the correct creation of a player into the game
     */
    @Test
    public void testAddPlayer() {
        gameManager.addPlayer("Test1", Color.RED, 0, "Apollo");
        gameManager.addPlayer("Test2", Color.GREEN, 1, "Apollo");
        Assert.assertEquals("Test1", gameManager.getPlayers().get(0).getPlayerData().getPlayerID());
        Assert.assertEquals(Color.RED, gameManager.getPlayers().get(0).getPlayerData().getPlayerColor());
        Assert.assertEquals(Integer.valueOf(0), gameManager.getPlayers().get(0).getPlayerData().getPlayOrder());
        Assert.assertEquals("Test2", gameManager.getPlayers().get(1).getPlayerData().getPlayerID());
        Assert.assertEquals(Color.GREEN, gameManager.getPlayers().get(1).getPlayerData().getPlayerColor());
        Assert.assertEquals(Integer.valueOf(1), gameManager.getPlayers().get(1).getPlayerData().getPlayOrder());
    }

    /***
     * Test for checking the correct creation of the map
     */
    @Test
    public void testMapCreation() {
        gameManager.addPlayer("Test1", Color.RED, 0, "Apollo");
        Assert.assertNotNull(gameManager.getPlayers().get(0).getGameMap());
    }

    /***
     * Test for checking the correct creation of the turn manager class
     * Test for checking the correct sort of the players array by turn
     */
    @Test
    public void testTurnInitAndSort() {
        gameManager.addPlayer("Test1", Color.RED, 1, "Apollo");
        gameManager.addPlayer("Test2", Color.GREEN, 0, "Apollo");
        gameManager.startMatch();
        Assert.assertEquals(Integer.valueOf(0), gameManager.getPlayers().get(0).getPlayerData().getPlayOrder());
        Assert.assertEquals("Test2", gameManager.getPlayers().get(0).getPlayerData().getPlayerID());
        Assert.assertEquals(Integer.valueOf(1), gameManager.getPlayers().get(1).getPlayerData().getPlayOrder());
        Assert.assertEquals("Test1", gameManager.getPlayers().get(1).getPlayerData().getPlayerID());
    }
}
