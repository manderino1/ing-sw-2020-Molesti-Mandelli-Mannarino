package it.polimi.ingsw.PSP18.server.controller;

import it.polimi.ingsw.PSP18.networking.SocketThread;
import it.polimi.ingsw.PSP18.server.model.Color;
import it.polimi.ingsw.PSP18.server.model.Direction;
import it.polimi.ingsw.PSP18.server.model.Move;
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
        matchSocket = new MatchSocket();
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
        SocketThread socketThread = new SocketThread(socket, null);
        socketThread.start();
        match.getMatchSocket().addPlayer(new PlayerManager(match, new PlayerData("Test1", Color.RED, 0), "Divinity"), socketThread);
        match.getMatchSocket().addPlayer(new PlayerManager(match, new PlayerData("Test2", Color.GREEN, 1), "Divinity"), socketThread);
        match.getMatchSocket().getPlayerManagers().get(0).placeWorker(0, 0);
        match.getMatchSocket().getPlayerManagers().get(0).placeWorker(1, 0);
        match.getMatchSocket().getPlayerManagers().get(1).placeWorker(0, 1);
        match.getMatchSocket().getPlayerManagers().get(1).placeWorker(1, 1);
        TurnManager turnManager = new TurnManager(match);
        turnManager.manageTurn();
    }

    /***
     * Test for checking the correct creation of the turn manager class with Athena
     */
    @Test
    public void testManageTurnAthena() {
        Match match = new Match();
        SocketThread socketThread1 = new SocketThread(socket, null);
        socketThread1.start();
        match.getMatchSocket().addPlayer(new PlayerManager(match, new PlayerData("Test1", Color.RED, 0), "Divinity"), socketThread1);
        SocketThread socketThread2 = new SocketThread(socket, null);
        socketThread2.start();
        match.getMatchSocket().addPlayer(new PlayerManager(match, new PlayerData("Test2", Color.GREEN, 1), "Divinity"), socketThread2);
        match.getMatchSocket().getPlayerManagers().get(0).placeWorker(0, 0);
        match.getMatchSocket().getPlayerManagers().get(0).placeWorker(4, 0);
        match.getMatchSocket().getPlayerManagers().get(1).placeWorker(0, 4);
        match.getMatchSocket().getPlayerManagers().get(1).placeWorker(4, 4);
        TurnManager turnManager = new TurnManagerAthena(match);
        turnManager.manageTurn();
        Move move = new Move(Direction.DOWN, 2);
        match.getMatchSocket().getPlayerManagers().get(0).getPlayerData().setLastMove(move);
        match.getMatchSocket().getPlayerManagers().get(1).getPlayerData().setLastMove(move);
        turnManager.manageTurn();
    }
    @Test
    public void testPassTurn(){
        Match match = new Match();
        SocketThread socketThread1 = new SocketThread(socket, null);
        socketThread1.start();
        match.getMatchSocket().addPlayer(new PlayerManager(match, new PlayerData("Test1", Color.RED, 0), "Divinity"), socketThread1);
        SocketThread socketThread2 = new SocketThread(socket, null);
        socketThread2.start();
        match.getMatchSocket().addPlayer(new PlayerManager(match, new PlayerData("Test2", Color.GREEN, 1), "Divinity"), socketThread2);
        match.getMatchSocket().getPlayerManagers().get(0).placeWorker(0, 0);
        match.getMatchSocket().getPlayerManagers().get(0).placeWorker(4, 0);
        match.getMatchSocket().getPlayerManagers().get(1).placeWorker(0, 4);
        match.getMatchSocket().getPlayerManagers().get(1).placeWorker(4, 4);
        TurnManager turnManager = new TurnManagerAthena(match);
        turnManager.passTurn();
        turnManager.passTurn();
    }
}
