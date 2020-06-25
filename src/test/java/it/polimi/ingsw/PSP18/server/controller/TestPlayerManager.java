package it.polimi.ingsw.PSP18.server.controller;

import it.polimi.ingsw.PSP18.server.model.Color;
import it.polimi.ingsw.PSP18.server.model.PlayerData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestPlayerManager {
    private PlayerManager playerManager;

    @Before
    public void createPlayerManager() {
        MatchSocket matchSocket = new MatchSocket(2);
        MatchRun matchRun = new MatchRun(matchSocket);
        playerManager = new PlayerManager(matchRun, new PlayerData("Test1", Color.RED, 0), "Divinity", matchSocket);
        playerManager.placeWorker(0,0);
        playerManager.placeWorker(0,1);
    }

    /***
     * Test for checking the correct setup of a building
     */
    @Test
    public void testSetBuild() {
        Integer oldLevel = playerManager.getGameMap().getCell(2,3).getBuilding();
        playerManager.setBuild(2,3);
        Integer newLevel = playerManager.getGameMap().getCell(2,3).getBuilding();
        Assert.assertEquals(Integer.valueOf(oldLevel + 1), newLevel);

        oldLevel = playerManager.getGameMap().getCell(2,4).getBuilding();
        playerManager.setBuild(2,4, false);
        newLevel = playerManager.getGameMap().getCell(2,4).getBuilding();
        Assert.assertEquals(Integer.valueOf(oldLevel + 1), newLevel);

        oldLevel = playerManager.getGameMap().getCell(3,4).getBuilding();
        playerManager.setBuild(3,4, true);
        newLevel = playerManager.getGameMap().getCell(3,4).getBuilding();
        Assert.assertEquals(oldLevel, newLevel);
        Assert.assertTrue(playerManager.getGameMap().getCell(3, 4).getDome());
    }
}
