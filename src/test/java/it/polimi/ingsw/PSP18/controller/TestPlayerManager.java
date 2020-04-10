package it.polimi.ingsw.PSP18.controller;

import it.polimi.ingsw.PSP18.model.Color;
import it.polimi.ingsw.PSP18.model.GameMap;
import it.polimi.ingsw.PSP18.model.Match;
import it.polimi.ingsw.PSP18.model.PlayerData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestPlayerManager {
    private PlayerManager playerManager;

    @Before
    public void createPlayerManager() {
        playerManager = new PlayerManager(new Match(), new PlayerData("Test1", Color.RED, 0), "Divinity");
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
