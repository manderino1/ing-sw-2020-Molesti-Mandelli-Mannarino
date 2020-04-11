package it.polimi.ingsw.PSP18.server.controller.divinities;

import it.polimi.ingsw.PSP18.server.controller.PlayerManager;
import it.polimi.ingsw.PSP18.server.model.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestPan {
    private PlayerManager playerManager;
    @Before
    public void createPlayerManager() {playerManager = new PlayerManager(new Match(), new PlayerData("Test1", Color.RED, 0), "Pan");}

    @Test
    public void TestPanConstructor(){
        Pan divinity = new Pan("Pan", playerManager);
        Assert.assertEquals(playerManager.getDivinityName(), "Pan");
    }
    @Test
    public void testManageTurn() {
        playerManager.placeWorker(2,1);
        playerManager.placeWorker(3,2);
        playerManager.manageTurn(false);
        playerManager.manageTurn(true);
        /*
        TODO: In order to have a more complete test we need to generate some buildings and some workers
         */
        //TODO: still gives nullpointerex with getWorker method
    }
}
