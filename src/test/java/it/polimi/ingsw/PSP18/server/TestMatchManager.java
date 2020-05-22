package it.polimi.ingsw.PSP18.server;

import it.polimi.ingsw.PSP18.networking.SocketThread;
import it.polimi.ingsw.PSP18.server.controller.MatchSocket;
import it.polimi.ingsw.PSP18.server.model.MatchStatus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestMatchManager {
    private ByteArrayOutputStream socketOutContent = new ByteArrayOutputStream();
    private InputStream socketInContent = new InputStream() {
        @Override
        public int read() {
            return -1;  // end of stream
        }
    };
    private Socket socket;

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
    public void testMatchManagement() {
        MatchManager matchManager = new MatchManager();
        MatchSocket match1 = matchManager.getMatchSocket(2);
        matchManager.getMatchSocket(2).setMatchStatus(MatchStatus.MATCH_ENDED);
        SocketThread socketThread = new SocketThread(socket, matchManager);
        socketThread.setMatchSocket(matchManager.getMatchSocket(2));
        socketThread.start();
        Assert.assertNotEquals(match1, matchManager.getMatchSocket(2));
        SocketThread socketThread2 = new SocketThread(socket, matchManager);
        socketThread2.setMatchSocket(matchManager.getMatchSocket(2));
        socketThread2.start();
        SocketThread socketThread3 = new SocketThread(socket, matchManager);
        socketThread3.setMatchSocket(matchManager.getMatchSocket(2));
        socketThread3.start();
        SocketThread socketThread4 = new SocketThread(socket, matchManager);
        socketThread4.setMatchSocket(matchManager.getMatchSocket(2));
        socketThread4.start();
        Assert.assertNotEquals(match1, matchManager.getMatchSocket(2));
        socketThread = new SocketThread(socket, matchManager);
        socketThread.setMatchSocket(matchManager.getMatchSocket(3));
        socketThread.start();
        match1 = matchManager.getMatchSocket(3);
        socketThread2 = new SocketThread(socket, matchManager);
        socketThread2.setMatchSocket(matchManager.getMatchSocket(3));
        socketThread2.start();
        socketThread3 = new SocketThread(socket, matchManager);
        socketThread3.setMatchSocket(matchManager.getMatchSocket(3));
        socketThread3.start();
        socketThread4 = new SocketThread(socket, matchManager);
        socketThread4.setMatchSocket(matchManager.getMatchSocket(3));
        socketThread4.start();
        Assert.assertNotEquals(match1, matchManager.getMatchSocket(3));
    }
}
