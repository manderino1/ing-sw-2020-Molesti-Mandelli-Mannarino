package it.polimi.ingsw.PSP18.server.controller.divinities;

import it.polimi.ingsw.PSP18.server.controller.PlayerManager;
import it.polimi.ingsw.PSP18.server.model.Color;
import it.polimi.ingsw.PSP18.server.model.GameMap;
import it.polimi.ingsw.PSP18.server.model.Match;
import it.polimi.ingsw.PSP18.server.model.PlayerData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestMinotaur {
    private GameMap map;
    private PlayerManager playerManager;
    @Before
    public void createPlayerManager() {playerManager = new PlayerManager(new Match(), new PlayerData("Test1", Color.RED, 0), "Minotaur");}

    /***
     * Testing the GetName method
     */
    @Test
    public void testGetName() {
        Divinity minotaur = new Minotaur("Minotaur", playerManager);
        Assert.assertTrue(playerManager.getDivinityName().equals(minotaur.getName()));
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
