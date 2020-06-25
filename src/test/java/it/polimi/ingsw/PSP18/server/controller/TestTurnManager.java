package it.polimi.ingsw.PSP18.server.controller;

import it.polimi.ingsw.PSP18.networking.SocketThread;
import it.polimi.ingsw.PSP18.server.model.Color;
import it.polimi.ingsw.PSP18.server.model.Direction;
import it.polimi.ingsw.PSP18.server.model.Move;
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

public class TestTurnManager {
    private MatchSocket matchSocket;
    private ByteArrayOutputStream socketOutContent = new ByteArrayOutputStream();
    private InputStream socketInContent = new InputStream() {
        @Override
        public int read() {
            return -1;  // end of stream
        }
    };
    private Socket socket;

    @Before
    public void createGameManager() {
        matchSocket = new MatchSocket(2);
    }

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

    /***
     * Test for checking the correct creation of the turn manager class
     */
    @Test
    public void testManageTurn() {
        MatchSocket matchSocket = new MatchSocket(2);
        MatchRun matchRun = new MatchRun(matchSocket);
        SocketThread socketThread = new SocketThread(socket, null, true);
        socketThread.start();
        matchSocket.addPlayer(new PlayerManager(matchRun, new PlayerData("Test1", Color.RED, 0), "Divinity", matchSocket), socketThread);
        matchSocket.addPlayer(new PlayerManager(matchRun, new PlayerData("Test2", Color.GREEN, 1), "Divinity", matchSocket), socketThread);
        matchSocket.getPlayerManagers().get(0).placeWorker(0, 0);
        matchSocket.getPlayerManagers().get(0).placeWorker(1, 0);
        matchSocket.getPlayerManagers().get(1).placeWorker(0, 1);
        matchSocket.getPlayerManagers().get(1).placeWorker(1, 1);
        TurnManager turnManager = new TurnManager(matchSocket, new BackupManager(matchSocket, matchRun));
        turnManager.manageTurn();
        turnManager = new TurnManager(matchSocket, new BackupManager(matchSocket, matchRun),0);
        Assert.assertEquals( Integer.valueOf(0), turnManager.getIndexCurrentPlayer());
        turnManager.manageTurn();
    }

    /***
     * Test for checking the correct creation of the turn manager class with Athena
     */
    @Test
    public void testManageTurnAthena() {
        MatchSocket matchSocket = new MatchSocket(2);
        MatchRun matchRun = new MatchRun(matchSocket);
        SocketThread socketThread1 = new SocketThread(socket, null, true);
        socketThread1.start();
        matchSocket.addPlayer(new PlayerManager(matchRun, new PlayerData("Test1", Color.RED, 0), "Athena", matchSocket), socketThread1);
        SocketThread socketThread2 = new SocketThread(socket, null, true);
        socketThread2.start();
        matchSocket.addPlayer(new PlayerManager(matchRun, new PlayerData("Test2", Color.GREEN, 1), "Apollo", matchSocket), socketThread2);
        matchSocket.getPlayerManagers().get(0).placeWorker(0, 0);
        matchSocket.getPlayerManagers().get(0).placeWorker(4, 0);
        matchSocket.getPlayerManagers().get(1).placeWorker(0, 4);
        matchSocket.getPlayerManagers().get(1).placeWorker(4, 4);
        TurnManager turnManager = new TurnManagerAthena(matchSocket, new BackupManager(matchSocket, matchRun));
        turnManager.manageTurn();
        Move move = new Move(Direction.DOWN, 2);
        matchSocket.getPlayerManagers().get(0).getPlayerData().setLastMove(move);
        matchSocket.getPlayerManagers().get(1).getPlayerData().setLastMove(move);
        turnManager.manageTurn();
        turnManager = new TurnManagerAthena(matchSocket, new BackupManager(matchSocket, matchRun),0);
        matchSocket.getPlayerManagers().get(0).getPlayerData().setLastMove(new Move(Direction.UP,0));
        turnManager.manageTurn();
    }
    @Test
    public void testPassTurn(){
        MatchSocket matchSocket = new MatchSocket(2);
        MatchRun matchRun = new MatchRun(matchSocket);
        SocketThread socketThread1 = new SocketThread(socket, null, true);
        socketThread1.start();
        matchSocket.addPlayer(new PlayerManager(matchRun, new PlayerData("Test1", Color.RED, 0), "Divinity", matchSocket), socketThread1);
        SocketThread socketThread2 = new SocketThread(socket, null, true);
        socketThread2.start();
        matchSocket.addPlayer(new PlayerManager(matchRun, new PlayerData("Test2", Color.GREEN, 1), "Divinity", matchSocket), socketThread2);
        matchSocket.getPlayerManagers().get(0).placeWorker(0, 0);
        matchSocket.getPlayerManagers().get(0).placeWorker(4, 0);
        matchSocket.getPlayerManagers().get(1).placeWorker(0, 4);
        matchSocket.getPlayerManagers().get(1).placeWorker(4, 4);
        TurnManager turnManager = new TurnManagerAthena(matchSocket, new BackupManager(matchSocket, matchRun));
        turnManager.passTurn();
        turnManager.passTurn();
    }
}
