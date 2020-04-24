package it.polimi.ingsw.PSP18.server.controller;

import it.polimi.ingsw.PSP18.networking.SocketThread;
import it.polimi.ingsw.PSP18.networking.messages.toserver.WorkerReceiver;
import it.polimi.ingsw.PSP18.server.controller.Match;
import it.polimi.ingsw.PSP18.server.controller.PlayerManager;
import it.polimi.ingsw.PSP18.server.controller.TurnManager;
import it.polimi.ingsw.PSP18.server.model.Color;
import it.polimi.ingsw.PSP18.server.model.MatchStatus;
import it.polimi.ingsw.PSP18.server.model.PlayerData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestMatch {
    protected ByteArrayOutputStream socketOutContent = new ByteArrayOutputStream();
    protected InputStream socketInContent = new InputStream() {
        @Override
        public int read() {
            return -1;  // end of stream
        }
    };
    protected Socket socket;

    @Before
    public void socketMock() {
        socketOutContent = new ByteArrayOutputStream();

        // Create mock sockets
        socket = mock(Socket.class);
        try {
            when(socket.getOutputStream()).thenReturn(socketOutContent);
            when(socket.getInputStream()).thenReturn(socketInContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMatchCreationGettersSetters() {
        Match match = new Match();

        match.setMatchStatus(MatchStatus.MATCH_STARTED);
        Assert.assertEquals(match.getMatchStatus(), MatchStatus.MATCH_STARTED);
        match.getGameMap().setCell(1,1,0,null);
        Assert.assertNull(match.getGameMap().getCell(1, 1).getWorker());

        PlayerData playerData = new PlayerData("cipolla", Color.RED, 0);
        PlayerManager playerManager = new PlayerManager(match, playerData, "Apollo");
        match.addPlayer(playerManager, new SocketThread(socket, match));
        Assert.assertEquals(match.getPlayerManagers().get(0), playerManager);

        match.setCurrentPlayer(playerManager);
        Assert.assertEquals(match.getCurrentPlayer(), playerManager);

        TurnManager turnManager = match.getTurnManager();
    }

    @Test
    public void testReady() {
        Match match = new Match();

        match.setMatchStatus(MatchStatus.WAITING_FOR_PLAYERS);

        PlayerData playerData = new PlayerData("cipolla", Color.RED, 0);
        PlayerManager playerManager = new PlayerManager(match, playerData, "Apollo");
        match.addPlayer(playerManager, new SocketThread(socket, match));
        match.setCurrentPlayer(playerManager);

        match.readyManagement(match.getSockets().get(0));
        Assert.assertEquals(match.getMatchStatus(), MatchStatus.DIVINITIES_SELECTION);
    }

    @Test
    public void testWorkerPlacementAthena () {
        Match match = new Match();
        SocketThread socketThread = new SocketThread(socket, match);
        SocketThread socketThread1 = new SocketThread(socket, match);

        PlayerData playerData = new PlayerData("cipolla", Color.RED, 0);
        PlayerManager playerManager = new PlayerManager(match, playerData);
        PlayerData playerData1 = new PlayerData("cipolla2", Color.BLUE, 1);
        PlayerManager playerManager1 = new PlayerManager(match, playerData1);

        match.addPlayer(playerManager, socketThread);
        match.addPlayer(playerManager1, socketThread1);

        match.readyManagement(match.getSockets().get(0));
        match.readyManagement(match.getSockets().get(1));

        match.divinityCreation(match.getSockets().get(0), "Athena");
        match.divinityCreation(match.getSockets().get(1), "Apollo");

        match.workerPlacement(match.getSockets().get(0), new WorkerReceiver(1,0, 2, 1));
        match.workerPlacement(match.getSockets().get(1), new WorkerReceiver(2,0, 3, 1));

        Assert.assertEquals(MatchStatus.MATCH_STARTED, match.getMatchStatus());
    }

    @Test
    public void testWorkerPlacement () {
        Match match = new Match();
        SocketThread socketThread = new SocketThread(socket, match);
        SocketThread socketThread1 = new SocketThread(socket, match);

        PlayerData playerData = new PlayerData("cipolla", Color.RED, 0);
        PlayerManager playerManager = new PlayerManager(match, playerData);
        PlayerData playerData1 = new PlayerData("cipolla2", Color.BLUE, 1);
        PlayerManager playerManager1 = new PlayerManager(match, playerData1);

        match.addPlayer(playerManager, socketThread);
        match.addPlayer(playerManager1, socketThread1);

        match.readyManagement(match.getSockets().get(0));
        match.readyManagement(match.getSockets().get(1));

        match.divinityCreation(match.getSockets().get(0), "Atlas");
        match.divinityCreation(match.getSockets().get(1), "Apollo");

        match.workerPlacement(match.getSockets().get(0), new WorkerReceiver(1,0, 2, 1));
        match.workerPlacement(match.getSockets().get(1), new WorkerReceiver(2,0, 3, 1));

        Assert.assertEquals(MatchStatus.MATCH_STARTED, match.getMatchStatus());
    }
}
