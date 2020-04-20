package it.polimi.ingsw.PSP18.server.controller.divinities;

import it.polimi.ingsw.PSP18.networking.SocketThread;
import it.polimi.ingsw.PSP18.server.controller.PlayerManager;
import it.polimi.ingsw.PSP18.server.model.Color;
import it.polimi.ingsw.PSP18.server.model.GameMap;
import it.polimi.ingsw.PSP18.server.controller.Match;
import it.polimi.ingsw.PSP18.server.model.PlayerData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestArtemis extends TestDivinity {
    @Override
    public void createPlayerManager() {
        Match match = new Match(true);
        SocketThread socketThread = new SocketThread(socket, match);
        socketThread.start();
        playerManager = new PlayerManager(match, new PlayerData("Test1", Color.RED, 0), "Artemis");
        match.addPlayer(playerManager, socketThread);
    }

    /***
     * Testing the GetName method
     */
    @Override
    public void testGetName() {
        Divinity artemis = new Artemis("Artemis", playerManager);
        Assert.assertEquals(playerManager.getDivinityName(), artemis.getName());
    }

    @Test
    public void testMove() {
        playerManager.getMatch().setCurrentPlayer(playerManager);
        playerManager.placeWorker(0,0);
        playerManager.placeWorker(0,1);
        playerManager.getDivinity().move();
        Assert.assertEquals("{\"type\":\"WAITING_NICK\"}\r\n" +
                "{\"moveList1\":[\"RIGHT\",\"RIGHTDOWN\"],\"moveList2\":[\"RIGHT\",\"RIGHTUP\",\"RIGHTDOWN\",\"DOWN\"],\"type\":\"MOVE_LIST\"}\r\n", socketOutContent.toString());
    }
}
