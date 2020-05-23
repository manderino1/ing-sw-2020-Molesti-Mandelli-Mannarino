package it.polimi.ingsw.PSP18.server.controller;

import it.polimi.ingsw.PSP18.networking.SocketThread;
import it.polimi.ingsw.PSP18.networking.messages.toserver.WorkerReceiver;
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
import java.util.ArrayList;

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
        MatchSocket matchSocket = new MatchSocket(2);
        MatchRun matchRun = new MatchRun(matchSocket);

        matchSocket.setMatchStatus(MatchStatus.MATCH_STARTED);
        Assert.assertEquals(matchSocket.getMatchStatus(), MatchStatus.MATCH_STARTED);
        matchRun.getGameMap().setCell(1,1,0,null);
        Assert.assertNull(matchRun.getGameMap().getCell(1, 1).getWorker());

        PlayerData playerData = new PlayerData("cipolla", Color.RED, 0);
        PlayerManager playerManager = new PlayerManager(matchRun, playerData, "Apollo", matchSocket);
        matchSocket.addPlayer(playerManager, new SocketThread(socket, null));
        Assert.assertEquals(matchSocket.getPlayerManagers().get(0), playerManager);

        matchSocket.setCurrentPlayer(playerManager);
        Assert.assertEquals(matchSocket.getCurrentPlayer(), playerManager);

        TurnManager turnManager = matchRun.getTurnManager();
    }

    @Test
    public void testReady() {
        MatchSocket match = new MatchSocket(2);
        MatchSetUp matchSetUp = new MatchSetUp(match, 2);
        MatchRun matchRun = new MatchRun(match);

        match.setMatchStatus(MatchStatus.WAITING_FOR_PLAYERS);
        SocketThread socketThread = new SocketThread(socket, null, true);
        socketThread.setMatchSocket(match);
        SocketThread socketThread1 = new SocketThread(socket, null, true);
        socketThread1.setMatchSocket(match);

        PlayerData playerData = new PlayerData("cipolla", Color.RED, 0);
        PlayerManager playerManager = new PlayerManager(matchRun, playerData, "Apollo", match);
        match.addPlayer(playerManager, socketThread);

        PlayerData playerData1 = new PlayerData("cipolla2", Color.RED, 1);
        PlayerManager playerManager1 = new PlayerManager(matchRun, playerData1, "Apollo", match);
        match.addPlayer(playerManager1, socketThread1);

        matchSetUp.readyManagement(match.getSockets().get(0));
        matchSetUp.readyManagement(match.getSockets().get(1));
        Assert.assertEquals(match.getMatchStatus(), MatchStatus.DIVINITIES_SELECTION);
    }

    @Test
    public void testWorkerPlacementAthena () {
        MatchSocket matchSocket = new MatchSocket(2);
        MatchRun matchRun = new MatchRun(matchSocket);
        MatchSetUp matchSetUp = new MatchSetUp(matchSocket, 2);
        SocketThread socketThread = new SocketThread(socket, null, true);
        socketThread.setMatchSocket(matchSocket);
        SocketThread socketThread1 = new SocketThread(socket, null, true);
        socketThread1.setMatchSocket(matchSocket);

        PlayerData playerData = new PlayerData("cipolla", Color.RED, 0);
        PlayerManager playerManager = new PlayerManager(playerData);
        PlayerData playerData1 = new PlayerData("cipolla2", Color.BLUE, 1);
        PlayerManager playerManager1 = new PlayerManager(playerData1);

        matchSocket.addPlayer(playerManager, socketThread);
        matchSocket.addPlayer(playerManager1, socketThread1);

        matchSetUp.readyManagement(matchSocket.getSockets().get(0));
        matchSetUp.readyManagement(matchSocket.getSockets().get(1));

        ArrayList<String> div = new ArrayList<>();
        div.add("Athena");
        div.add("Apollo");
        matchSetUp.divinitySelection(div);

        matchSetUp.divinityCreation(matchSocket.getSockets().get(0), "Athena");
        matchSetUp.divinityCreation(matchSocket.getSockets().get(1), "Apollo");

        matchRun.workerPlacement(matchSocket.getSockets().get(0), new WorkerReceiver(1,0, 2, 1));
        matchRun.workerPlacement(matchSocket.getSockets().get(1), new WorkerReceiver(2,0, 3, 1));

        Assert.assertEquals(MatchStatus.MATCH_STARTED, matchSocket.getMatchStatus());
    }

    @Test
    public void testWorkerPlacement () {
        MatchSocket matchSocket = new MatchSocket(2);
        MatchRun matchRun = new MatchRun(matchSocket);
        MatchSetUp matchSetUp = new MatchSetUp(matchSocket, 2);
        SocketThread socketThread = new SocketThread(socket, null, true);
        socketThread.setMatchSocket(matchSocket);
        SocketThread socketThread1 = new SocketThread(socket, null, true);
        socketThread1.setMatchSocket(matchSocket);

        PlayerData playerData = new PlayerData("cipolla", Color.RED, 0);
        PlayerManager playerManager = new PlayerManager(playerData);
        PlayerData playerData1 = new PlayerData("cipolla2", Color.BLUE, 1);
        PlayerManager playerManager1 = new PlayerManager(playerData1);

        matchSocket.addPlayer(playerManager, socketThread);
        matchSocket.addPlayer(playerManager1, socketThread1);

        matchSetUp.readyManagement(matchSocket.getSockets().get(0));
        matchSetUp.readyManagement(matchSocket.getSockets().get(1));

        ArrayList<String> div = new ArrayList<>();
        div.add("Atlas");
        div.add("Apollo");
        matchSetUp.divinitySelection(div);

        matchSetUp.divinityCreation(matchSocket.getSockets().get(0), "Atlas");
        matchSetUp.divinityCreation(matchSocket.getSockets().get(1), "Apollo");

        matchRun.workerPlacement(matchSocket.getSockets().get(0), new WorkerReceiver(1,0, 2, 1));
        matchRun.workerPlacement(matchSocket.getSockets().get(1), new WorkerReceiver(2,0, 3, 1));

        Assert.assertEquals(MatchStatus.MATCH_STARTED, matchSocket.getMatchStatus());

        matchRun.endMatch(null);
        Assert.assertEquals(MatchStatus.MATCH_ENDED, matchSocket.getMatchStatus());
    }

    @Test
    public void detach() {
        MatchSocket matchSocket = new MatchSocket(2);
        MatchRun matchRun = new MatchRun(matchSocket);
        SocketThread socketThread = new SocketThread(socket, null, true);
        matchSocket.addPlayer(new PlayerManager(new PlayerData("Test", Color.RED, 0)), socketThread);
        matchSocket.addSocket(socketThread);
        matchRun.detachSocket(socketThread);
    }
}
