package it.polimi.ingsw.PSP18.server.controller.divinities;

import it.polimi.ingsw.PSP18.networking.SocketThread;
import it.polimi.ingsw.PSP18.server.controller.MatchRun;
import it.polimi.ingsw.PSP18.server.controller.MatchSocket;
import it.polimi.ingsw.PSP18.server.controller.PlayerManager;
import it.polimi.ingsw.PSP18.server.model.Color;
import it.polimi.ingsw.PSP18.server.model.PlayerData;
import org.junit.Assert;

public class TestAthena extends TestDivinity {
    @Override
    public void createPlayerManager() {
        MatchSocket matchSocket = new MatchSocket(2);
        MatchRun matchRun = new MatchRun(matchSocket);
        SocketThread socketThread = new SocketThread(socket, null);
        socketThread.start();
        playerManager = new PlayerManager(matchRun, new PlayerData("Test1", Color.RED, 0), "Athena", matchSocket);
        matchSocket.addPlayer(playerManager, socketThread);
    }

    /***
     * Testing the GetName method
     */
    @Override
    public void testGetName() {
        MatchSocket matchSocket = new MatchSocket(2);
        MatchRun matchRun = new MatchRun(matchSocket);

        Divinity divinity = new Divinity("Athena", playerManager, matchSocket, matchRun);
        Assert.assertEquals(playerManager.getDivinityName(), divinity.getName());
    }
}
