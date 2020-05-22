package it.polimi.ingsw.PSP18.server.controller.divinities;

import it.polimi.ingsw.PSP18.networking.SocketThread;
import it.polimi.ingsw.PSP18.server.controller.PlayerManager;
import it.polimi.ingsw.PSP18.server.model.Color;
import it.polimi.ingsw.PSP18.server.model.PlayerData;
import org.junit.Assert;

public class TestAthena extends TestDivinity {
    @Override
    public void createPlayerManager() {
        Match match = new Match();
        SocketThread socketThread = new SocketThread(socket, null);
        socketThread.start();
        playerManager = new PlayerManager(match, new PlayerData("Test1", Color.RED, 0), "Athena");
        match.getMatchSocket().addPlayer(playerManager, socketThread);
    }

    /***
     * Testing the GetName method
     */
    @Override
    public void testGetName() {
        Divinity divinity = new Divinity("Athena", playerManager);
        Assert.assertEquals(playerManager.getDivinityName(), divinity.getName());
    }
}
