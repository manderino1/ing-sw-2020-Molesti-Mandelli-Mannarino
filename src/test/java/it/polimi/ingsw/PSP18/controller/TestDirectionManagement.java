package it.polimi.ingsw.PSP18.controller;

import it.polimi.ingsw.PSP18.model.Direction;
import it.polimi.ingsw.PSP18.model.Move;
import org.junit.Assert;
import org.junit.Test;

public class TestDirectionManagement {
    /***
     * Test for checking the correct calculation of the final x into the DirectionManagement class
     */
    @Test
    public void testGetXAllDir() {
        Integer x = 1;

        // Check where x=x+1
        Assert.assertEquals(Integer.valueOf(2), DirectionManagement.getX(x, Direction.RIGHT));
        Assert.assertEquals(Integer.valueOf(2), DirectionManagement.getX(x, Direction.RIGHTDOWN));
        Assert.assertEquals(Integer.valueOf(2), DirectionManagement.getX(x, Direction.RIGHTUP));

        // Check where x=x
        Assert.assertEquals(Integer.valueOf(1), DirectionManagement.getX(x, Direction.UP));
        Assert.assertEquals(Integer.valueOf(1), DirectionManagement.getX(x, Direction.DOWN));

        // Check where x=x-1
        Assert.assertEquals(Integer.valueOf(0), DirectionManagement.getX(x, Direction.LEFT));
        Assert.assertEquals(Integer.valueOf(0), DirectionManagement.getX(x, Direction.LEFTDOWN));
        Assert.assertEquals(Integer.valueOf(0), DirectionManagement.getX(x, Direction.LEFTUP));
    }

    /***
     * Test for checking the correct calculation of the final y into the DirectionManagement class
     */
    @Test
    public void testGetYAllDir() {
        Integer y = 1;

        // Check where y=y+1
        Assert.assertEquals(Integer.valueOf(2), DirectionManagement.getY(y, Direction.UP));
        Assert.assertEquals(Integer.valueOf(2), DirectionManagement.getY(y, Direction.LEFTUP));
        Assert.assertEquals(Integer.valueOf(2), DirectionManagement.getY(y, Direction.RIGHTUP));

        // Check where y=y
        Assert.assertEquals(Integer.valueOf(1), DirectionManagement.getY(y, Direction.LEFT));
        Assert.assertEquals(Integer.valueOf(1), DirectionManagement.getY(y, Direction.RIGHT));

        // Check where y=y-1
        Assert.assertEquals(Integer.valueOf(0), DirectionManagement.getY(y, Direction.DOWN));
        Assert.assertEquals(Integer.valueOf(0), DirectionManagement.getY(y, Direction.LEFTDOWN));
        Assert.assertEquals(Integer.valueOf(0), DirectionManagement.getY(y, Direction.RIGHTDOWN));
    }

    /***
     * Test for checking the correct calculation of the opposite direction
     */
    @Test
    public void testGetOppositeDir() {
        Integer y = 1;

        Assert.assertEquals(Direction.LEFT, DirectionManagement.getOppositeDirection(Direction.RIGHT));
        Assert.assertEquals(Direction.RIGHT, DirectionManagement.getOppositeDirection(Direction.LEFT));
        Assert.assertEquals(Direction.UP, DirectionManagement.getOppositeDirection(Direction.DOWN));
        Assert.assertEquals(Direction.DOWN, DirectionManagement.getOppositeDirection(Direction.UP));

        Assert.assertEquals(Direction.LEFTUP, DirectionManagement.getOppositeDirection(Direction.RIGHTDOWN));
        Assert.assertEquals(Direction.LEFTDOWN, DirectionManagement.getOppositeDirection(Direction.RIGHTUP));
        Assert.assertEquals(Direction.RIGHTUP, DirectionManagement.getOppositeDirection(Direction.LEFTDOWN));
        Assert.assertEquals(Direction.RIGHTDOWN, DirectionManagement.getOppositeDirection(Direction.LEFTUP));
    }
}
