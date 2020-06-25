package it.polimi.ingsw.PSP18.server.controller.divinities;

import it.polimi.ingsw.PSP18.networking.SocketThread;
import it.polimi.ingsw.PSP18.server.controller.MatchRun;
import it.polimi.ingsw.PSP18.server.controller.MatchSocket;
import it.polimi.ingsw.PSP18.server.controller.PlayerManager;
import it.polimi.ingsw.PSP18.server.model.*;
import org.junit.Assert;
import org.junit.Test;

public class TestPan extends TestDivinity {
    @Override
    public void createPlayerManager() {
        matchSocket = new MatchSocket(2);
        matchRun = new MatchRun(matchSocket);
        SocketThread socketThread = new SocketThread(socket, null, true);
        socketThread.start();
        playerManager = new PlayerManager(matchRun, new PlayerData("Test1", Color.RED, 0), "Pan", matchSocket);
        matchSocket.addPlayer(playerManager, socketThread);
    }

    /***
     * Testing the GetName method
     */
    @Override
    public void testGetName() {
        Divinity divinity = new Divinity("Pan", playerManager, matchSocket, matchRun);
        Assert.assertEquals(playerManager.getDivinityName(), divinity.getName());
    }

    @Test
    public void testCheckForVictory() {
        matchSocket.setCurrentPlayer(playerManager);
        playerManager.placeWorker(0,0);
        playerManager.placeWorker(2,1);
        matchRun.getGameMap().getCell(2, 1).setBuilding(3);
        playerManager.getPlayerData().setLastMove(new Move(Direction.UP, 1));

        Assert.assertEquals(false, playerManager.getDivinity().checkForVictory(0));
        Assert.assertEquals(true, playerManager.getDivinity().checkForVictory(1));
    }
}
