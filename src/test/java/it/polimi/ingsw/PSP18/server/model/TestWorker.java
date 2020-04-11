package it.polimi.ingsw.PSP18.server.model;

import org.junit.Assert;
import org.junit.Test;

public class TestWorker {
    /***
     * testing the worker constructor
     */
    @Test
    public void testWorkerCreation() {
        Worker worker = new Worker(2,1, 0, new PlayerData("Test", Color.BLUE, 0));
        Assert.assertEquals(Integer.valueOf(2), worker.getX());
        Assert.assertEquals(Integer.valueOf(1), worker.getY());
    }

    /***
     * testing the worker position setter
     */
    @Test
    public void testSetWorkerPosition() {
        Worker worker = new Worker(2,1, 0, new PlayerData("Test", Color.BLUE, 0));
        worker.setPosition(2,2);
        Assert.assertEquals(Integer.valueOf(2), worker.getX());
        Assert.assertEquals(Integer.valueOf(2), worker.getY());
    }

}
