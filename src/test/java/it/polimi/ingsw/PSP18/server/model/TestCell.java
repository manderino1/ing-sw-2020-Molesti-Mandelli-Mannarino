package it.polimi.ingsw.PSP18.server.model;

import org.junit.Assert;
import org.junit.Test;

public class TestCell {
    /***
     * testing the dome's methods in the cell class
     */
    @Test
    public void testDome() {
        Cell cell = new Cell();
        Assert.assertEquals(false, cell.getDome());
        cell.setDome();
        Assert.assertEquals(true, cell.getDome());
        cell.removeDome();
        Assert.assertEquals(false, cell.getDome());
    }

    /***
     * testing the worker's methods in the cell class
     */
    @Test
    public void testCellWorker() {
        Cell cell = new Cell();
        Worker worker = new Worker(2,1);
        Assert.assertNull(cell.getWorker());
        cell.setWorker(worker);
        Assert.assertEquals(worker, cell.getWorker());
    }

    /***
     * testing the building's methods in the cell class
     */
    @Test
    public void testCellBuilding() {
        Cell cell = new Cell();
        Assert.assertEquals(Integer.valueOf(0), cell.getBuilding());
        cell.setBuilding(1);
        Assert.assertEquals(Integer.valueOf(1), cell.getBuilding());
    }
}

