package it.polimi.ingsw.PSP18.server.controller;

import it.polimi.ingsw.PSP18.networking.SocketThread;
import it.polimi.ingsw.PSP18.server.model.Color;
import it.polimi.ingsw.PSP18.server.model.PlayerData;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestTurnManager {
    private Match match;
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
        match = new Match(true);
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
        Match match = new Match();
        SocketThread socketThread = new SocketThread(socket, match);
        socketThread.start();
        match.addPlayer(new PlayerManager(match, new PlayerData("Test1", Color.RED, 0), "Divinity"), socketThread);
        match.addPlayer(new PlayerManager(match, new PlayerData("Test2", Color.GREEN, 1), "Divinity"), socketThread);
        match.getPlayerManagers().get(0).placeWorker(0, 0);
        match.getPlayerManagers().get(0).placeWorker(1, 0);
        match.getPlayerManagers().get(1).placeWorker(0, 1);
        match.getPlayerManagers().get(1).placeWorker(1, 1);
        TurnManager turnManager = new TurnManager(match);
        turnManager.manageTurn();
    }

    /***
     * Test for checking the correct creation of the turn manager class with Athena
     */
    @Test
    public void testManageTurnAthena() {
        Match match = new Match(true);
        SocketThread socketThread1 = new SocketThread(socket, match);
        socketThread1.start();
        match.addSocket(socketThread1);
        match.addPlayer(new PlayerManager(match, new PlayerData("Test1", Color.RED, 0), "Divinity"), socketThread1);
        SocketThread socketThread2 = new SocketThread(socket, match);
        socketThread2.start();
        match.addSocket(socketThread2);
        match.addPlayer(new PlayerManager(match, new PlayerData("Test2", Color.GREEN, 1), "Divinity"), socketThread2);
        match.getPlayerManagers().get(0).placeWorker(0, 0);
        match.getPlayerManagers().get(0).placeWorker(1, 0);
        match.getPlayerManagers().get(1).placeWorker(0, 1);
        match.getPlayerManagers().get(1).placeWorker(1, 1);
        TurnManager turnManager = new TurnManager(match);
        turnManager.manageTurn();
    }
}
