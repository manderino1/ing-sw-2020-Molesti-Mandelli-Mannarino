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
    public void TestPanCounstructor(){
        Pan divinity = new Pan("Pan", playerManager);
        Assert.assertEquals(playerManager.getDivinityName(), "Pan");
    }
}
