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
        Match match = new Match(true);
        SocketThread socketThread = new SocketThread(socket, match);
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
}
