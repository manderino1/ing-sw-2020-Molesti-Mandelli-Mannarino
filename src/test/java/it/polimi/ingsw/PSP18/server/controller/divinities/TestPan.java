package it.polimi.ingsw.PSP18.server.controller.divinities;

import it.polimi.ingsw.PSP18.networking.SocketThread;
import it.polimi.ingsw.PSP18.server.controller.Match;
import it.polimi.ingsw.PSP18.server.controller.PlayerManager;
import it.polimi.ingsw.PSP18.server.model.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestPan extends TestDivinity {
    @Override
    public void createPlayerManager() {
        Match match = new Match();
        SocketThread socketThread = new SocketThread(socket, null);
        socketThread.start();
        playerManager = new PlayerManager(match, new PlayerData("Test1", Color.RED, 0), "Pan");
        match.addPlayer(playerManager, socketThread);
    }

    /***
     * Testing the GetName method
     */
    @Override
    public void testGetName() {
        Divinity divinity = new Divinity("Pan", playerManager);
        Assert.assertEquals(playerManager.getDivinityName(), divinity.getName());
    }

    @Test
    public void testCheckForVictory() {
        playerManager.getMatch().setCurrentPlayer(playerManager);
        playerManager.placeWorker(0,0);
        playerManager.placeWorker(2,1);
        playerManager.getMatch().getMatchRun().getGameMap().getCell(2, 1).setBuilding(3);
        playerManager.getPlayerData().setLastMove(new Move(Direction.UP, 1));

        Assert.assertEquals(false, playerManager.getDivinity().checkForVictory(0));
        Assert.assertEquals(true, playerManager.getDivinity().checkForVictory(1));
    }
}
