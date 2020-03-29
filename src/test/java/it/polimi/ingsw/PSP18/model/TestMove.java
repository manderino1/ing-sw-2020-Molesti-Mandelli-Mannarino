package it.polimi.ingsw.PSP18.model;

import org.junit.Assert;
import org.junit.Test;

public class TestMove {
    /***
     * Test for checking the creation of the class
     */
    @Test
    public void testMoveCreation() {
        Move move = new Move(Direction.UP, 0);
        Assert.assertEquals(Direction.UP, move.getDirection());
        Assert.assertEquals(Integer.valueOf(0), move.getLevel());
    }
}
