package it.polimi.ingsw.PSP18.controller.divinities;

import it.polimi.ingsw.PSP18.controller.PlayerManager;
import it.polimi.ingsw.PSP18.model.Color;
import it.polimi.ingsw.PSP18.model.Direction;
import it.polimi.ingsw.PSP18.model.GameMap;
import it.polimi.ingsw.PSP18.model.PlayerData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestPan {
    private PlayerManager playerManager;
    @Before
    public void createPlayerManager() {playerManager = new PlayerManager(new GameMap(), new PlayerData("Test1", Color.RED, 0), "Pan");}

    @Test
    public void TestCheckForVictory(){
        Divinity divinity = new Divinity("Pan", playerManager);
        playerManager.placeWorker(2,1);
        playerManager.placeWorker(3,2);
        playerManager.setBuild(2,1);
        playerManager.setBuild(2,1);
        Assert.assertEquals(playerManager.getGameMap().getCell(2,1).getBuilding(), Integer.valueOf(2));
        Assert.assertEquals(divinity.checkForVictory(false), false);
        divinity.setMove(2,1, Direction.UP);
        Assert.assertEquals(divinity.checkForVictory(false), true);
        divinity.setMove(2,2, Direction.UP);
        playerManager.setBuild(3,2);
        playerManager.setBuild(3,2);
        playerManager.setBuild(3,2);
        Assert.assertEquals(divinity.checkForVictory(true), true);
    }
}
