package it.polimi.ingsw.PSP18.server;

import it.polimi.ingsw.PSP18.networking.SocketThread;
import it.polimi.ingsw.PSP18.server.controller.Match;
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
        Match match1 = matchManager.getMatch();
        matchManager.getMatch().setMatchStatus(MatchStatus.MATCH_ENDED);
        SocketThread socketThread = new SocketThread(socket, matchManager.getMatch());
        socketThread.start();
        Assert.assertNotEquals(match1, matchManager.getMatch());
        SocketThread socketThread2 = new SocketThread(socket, matchManager.getMatch());
        socketThread2.start();
        SocketThread socketThread3 = new SocketThread(socket, matchManager.getMatch());
        socketThread3.start();
        SocketThread socketThread4 = new SocketThread(socket, matchManager.getMatch());
        socketThread4.start();
        Assert.assertNotEquals(match1, matchManager.getMatch());
    }
}
