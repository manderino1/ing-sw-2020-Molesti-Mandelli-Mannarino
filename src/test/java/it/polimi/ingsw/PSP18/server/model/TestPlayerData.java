package it.polimi.ingsw.PSP18.server.model;

import org.junit.Assert;
import org.junit.Test;

public class TestPlayerData {
    @Test
    public void testPlayerData(){
        PlayerData playerdata = new PlayerData("8mann", Color.RED, 3);
        Assert.assertEquals("8mann", playerdata.getPlayerID());
        Assert.assertEquals(Color.RED, playerdata.getPlayerColor());
        Assert.assertEquals(Integer.valueOf(3), playerdata.getPlayOrder());
    }
    @Test
    public void testGets(){
        PlayerData playerdata = new PlayerData("8mann", Color.RED, 3);
        Move lastmove= new Move(Direction.UP, 0);
        playerdata.setLastMove(lastmove);
        Assert.assertEquals(lastmove, playerdata.getLastMove());
        Assert.assertEquals(Color.RED, playerdata.getPlayerColor());
        Assert.assertEquals(Integer.valueOf(3), playerdata.getPlayOrder());
    }
}
