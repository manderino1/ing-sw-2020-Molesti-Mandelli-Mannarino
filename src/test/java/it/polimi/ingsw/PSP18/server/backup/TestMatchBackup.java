package it.polimi.ingsw.PSP18.server.backup;


import org.junit.Test;

public class TestMatchBackup {
    @Test
    public void testMoveCreation() {
        Assert.assertEquals(Direction.UP, move.getDirection());
        Assert.assertEquals(Integer.valueOf(0), move.getLevel());
    }

}
