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
        MatchSocket matchSocket = new MatchSocket(2);;

        match.setMatchStatus(MatchStatus.MATCH_STARTED);
        Assert.assertEquals(match.getMatchStatus(), MatchStatus.MATCH_STARTED);
        match.getMatchRun().getGameMap().setCell(1,1,0,null);
        Assert.assertNull(match.getMatchRun().getGameMap().getCell(1, 1).getWorker());

        PlayerData playerData = new PlayerData("cipolla", Color.RED, 0);
        PlayerManager playerManager = new PlayerManager(matchRun, playerData, "Apollo");
        matchSocket.addPlayer(playerManager, new SocketThread(socket, null));
        Assert.assertEquals(matchSocket.getPlayerManagers().get(0), playerManager);

        matchSocket.setCurrentPlayer(playerManager);
        Assert.assertEquals(matchSocket.getCurrentPlayer(), playerManager);

        TurnManager turnManager = match.getMatchRun().getTurnManager();
    }

    @Test
    public void testReady() {
        Match match = new Match(2);

        match.setMatchStatus(MatchStatus.WAITING_FOR_PLAYERS);
        SocketThread socketThread = new SocketThread(socket, null, true);
        socketThread.setMatch(match);
        SocketThread socketThread1 = new SocketThread(socket, null, true);
        socketThread1.setMatch(match);

        PlayerData playerData = new PlayerData("cipolla", Color.RED, 0);
        PlayerManager playerManager = new PlayerManager(matchRun, playerData, "Apollo");
        matchSocket.addPlayer(playerManager, socketThread);

        PlayerData playerData1 = new PlayerData("cipolla2", Color.RED, 1);
        PlayerManager playerManager1 = new PlayerManager(matchRun, playerData1, "Apollo");
        matchSocket.addPlayer(playerManager1, socketThread1);

        match.getMatchSetUp().readyManagement(matchSocket.getSockets().get(0));
        match.getMatchSetUp().readyManagement(matchSocket.getSockets().get(1));
        Assert.assertEquals(match.getMatchStatus(), MatchStatus.DIVINITIES_SELECTION);
    }

    @Test
    public void testWorkerPlacementAthena () {
        MatchSocket matchSocket = new MatchSocket(2);;
        SocketThread socketThread = new SocketThread(socket, null, true);
        socketThread.setMatch(match);
        SocketThread socketThread1 = new SocketThread(socket, null, true);
        socketThread1.setMatch(match);

        PlayerData playerData = new PlayerData("cipolla", Color.RED, 0);
        PlayerManager playerManager = new PlayerManager(matchRun, playerData);
        PlayerData playerData1 = new PlayerData("cipolla2", Color.BLUE, 1);
        PlayerManager playerManager1 = new PlayerManager(matchRun, playerData1);

        matchSocket.addPlayer(playerManager, socketThread);
        matchSocket.addPlayer(playerManager1, socketThread1);

        match.getMatchSetUp().readyManagement(matchSocket.getSockets().get(0));
        match.getMatchSetUp().readyManagement(matchSocket.getSockets().get(1));

        ArrayList<String> div = new ArrayList<>();
        div.add("Athena");
        div.add("Apollo");
        match.getMatchSetUp().divinitySelection(div);

        match.getMatchSetUp().divinityCreation(matchSocket.getSockets().get(0), "Athena");
        match.getMatchSetUp().divinityCreation(matchSocket.getSockets().get(1), "Apollo");

        match.getMatchRun().workerPlacement(matchSocket.getSockets().get(0), new WorkerReceiver(1,0, 2, 1));
        match.getMatchRun().workerPlacement(matchSocket.getSockets().get(1), new WorkerReceiver(2,0, 3, 1));

        Assert.assertEquals(MatchStatus.MATCH_STARTED, match.getMatchStatus());
    }

    @Test
    public void testWorkerPlacement () {
        MatchSocket matchSocket = new MatchSocket(2);;
        SocketThread socketThread = new SocketThread(socket, null, true);
        socketThread.setMatch(match);
        SocketThread socketThread1 = new SocketThread(socket, null, true);
        socketThread1.setMatch(match);

        PlayerData playerData = new PlayerData("cipolla", Color.RED, 0);
        PlayerManager playerManager = new PlayerManager(matchRun, playerData);
        PlayerData playerData1 = new PlayerData("cipolla2", Color.BLUE, 1);
        PlayerManager playerManager1 = new PlayerManager(matchRun, playerData1);

        matchSocket.addPlayer(playerManager, socketThread);
        matchSocket.addPlayer(playerManager1, socketThread1);

        match.getMatchSetUp().readyManagement(matchSocket.getSockets().get(0));
        match.getMatchSetUp().readyManagement(matchSocket.getSockets().get(1));

        ArrayList<String> div = new ArrayList<>();
        div.add("Atlas");
        div.add("Apollo");
        match.getMatchSetUp().divinitySelection(div);

        match.getMatchSetUp().divinityCreation(matchSocket.getSockets().get(0), "Atlas");
        match.getMatchSetUp().divinityCreation(matchSocket.getSockets().get(1), "Apollo");

        match.getMatchRun().workerPlacement(matchSocket.getSockets().get(0), new WorkerReceiver(1,0, 2, 1));
        match.getMatchRun().workerPlacement(matchSocket.getSockets().get(1), new WorkerReceiver(2,0, 3, 1));

        Assert.assertEquals(MatchStatus.MATCH_STARTED, match.getMatchStatus());

        match.getMatchRun().endMatch(null);
        Assert.assertEquals(MatchStatus.MATCH_ENDED, match.getMatchStatus());
    }
}
