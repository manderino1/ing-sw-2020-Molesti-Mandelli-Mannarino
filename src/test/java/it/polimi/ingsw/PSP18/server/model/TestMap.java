package it.polimi.ingsw.PSP18.server.model;

import org.junit.Assert;
import org.junit.Test;

public class TestMap {
    /***
     * Test for checking the creation of the class
     */
    @Test
    public void testMapCreation() {
        GameMap map = new GameMap();
        for (int i=0; i<5; i++){
            for (int j=0; j<5; j++){
                Assert.assertNotNull(map.getCell(i, j));
            }
        }
    }

    /***
     * Test for checking che correct response of the model after a cell edit
     */
    @Test
    public void testCellEdit() {
        GameMap map = new GameMap();
        map.setCell(0,0, 0, new Worker(0,0,0, Color.RED));
        Assert.assertEquals(Integer.valueOf(0), map.getCell(0,0).getBuilding());
        Assert.assertEquals(Integer.valueOf(0), map.getCell(0,0).getWorker().getX());
        Assert.assertEquals(Integer.valueOf(0), map.getCell(0,0).getWorker().getY());
    }
}
