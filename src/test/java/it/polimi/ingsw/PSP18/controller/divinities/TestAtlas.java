package it.polimi.ingsw.PSP18.controller.divinities;

import it.polimi.ingsw.PSP18.controller.PlayerManager;
import it.polimi.ingsw.PSP18.controller.divinities.Atlas;
import it.polimi.ingsw.PSP18.controller.divinities.Divinity;
import it.polimi.ingsw.PSP18.model.Color;
import it.polimi.ingsw.PSP18.model.GameMap;
import it.polimi.ingsw.PSP18.model.PlayerData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestAtlas {
    private GameMap map;
    private PlayerManager playerManager;
    @Before
    public void createPlayerManager() {playerManager = new PlayerManager(new GameMap(), new PlayerData("Test1", Color.RED, 0), "Atlas");}

    /***
     * Testing the GetName method
     */
    @Test
    public void testGetName() {
        Divinity atlas = new Atlas("Atlas", playerManager);
        Assert.assertTrue(playerManager.getDivinityName().equals(atlas.getName()));
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
