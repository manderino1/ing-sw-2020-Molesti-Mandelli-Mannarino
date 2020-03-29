package it.polimi.ingsw.PSP18.model;

import org.junit.Assert;
import org.junit.Test;

public class TestWorker {
    /***
     * testing the worker constructor
     */
    @Test
    public void testWorkerCreation() {
        Worker worker = new Worker(2,1);
        Assert.assertEquals(Integer.valueOf(2), worker.getX());
        Assert.assertEquals(Integer.valueOf(1), worker.getY());
    }

    /***
     * testing the worker position setter
     */
    @Test
    public void testSetWorkerPosition() {
        Worker worker = new Worker(2,1);
        worker.setPosition(2,2);
        Assert.assertEquals(Integer.valueOf(2), worker.getX());
        Assert.assertEquals(Integer.valueOf(2), worker.getY());
    }

}
