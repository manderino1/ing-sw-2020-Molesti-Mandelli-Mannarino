package it.polimi.ingsw.PSP18.server.controller.divinities;

import it.polimi.ingsw.PSP18.networking.SocketThread;
import it.polimi.ingsw.PSP18.server.controller.PlayerManager;
import it.polimi.ingsw.PSP18.server.model.Color;
import it.polimi.ingsw.PSP18.server.model.Direction;
import it.polimi.ingsw.PSP18.server.model.GameMap;
import it.polimi.ingsw.PSP18.server.controller.Match;
import it.polimi.ingsw.PSP18.server.model.PlayerData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestAtlas extends TestDivinity {
    @Override
    public void createPlayerManager() {
        Match match = new Match(true);
        SocketThread socketThread = new SocketThread(socket, match);
        socketThread.start();
        playerManager = new PlayerManager(match, new PlayerData("Test1", Color.RED, 0), "Atlas");
        match.addPlayer(playerManager, socketThread);
    }

    /***
     * Testing the GetName method
     */
    @Override
    public void testGetName() {
        Divinity divinity = new Divinity("Atlas", playerManager);
        Assert.assertEquals(playerManager.getDivinityName(), divinity.getName());
    }

    @Test
    public void testBuild() {
        playerManager.getMatch().setCurrentPlayer(playerManager);
        playerManager.placeWorker(0,0);
        playerManager.placeWorker(0,1);
        playerManager.getDivinity().build();
        Assert.assertEquals("{\"type\":\"WAITING_NICK\"}\r\n" +
                "{\"buildlist\":[\"RIGHT\",\"RIGHTDOWN\"],\"type\":\"ATLAS_BUILD_LIST\"}\r\n", socketOutContent.toString());

        ((Atlas) playerManager.getDivinity()).buildReceiver(Direction.DOWN, true);
        Assert.assertEquals("{\"type\":\"WAITING_NICK\"}\r\n" +
                "{\"buildlist\":[\"RIGHT\",\"RIGHTDOWN\"],\"type\":\"ATLAS_BUILD_LIST\"}\r\n" +
                "{\"type\":\"END_TURN\"}\r\n", socketOutContent.toString());
    }
}
