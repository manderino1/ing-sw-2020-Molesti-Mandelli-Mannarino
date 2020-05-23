package it.polimi.ingsw.PSP18.server.model;

import it.polimi.ingsw.PSP18.networking.SocketThread;
import it.polimi.ingsw.PSP18.server.view.MapObserver;
import it.polimi.ingsw.PSP18.server.view.PlayerDataObserver;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestPlayerData {
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
    public void testPlayerData(){
        PlayerData playerdata = new PlayerData("8mann", Color.RED, 3);
        playerdata.setPlayerColor(Color.RED);
        playerdata.setPlayOrder(3);
        Assert.assertEquals("8mann", playerdata.getPlayerID());
        Assert.assertEquals(Color.RED, playerdata.getPlayerColor());
        Assert.assertEquals(Integer.valueOf(3), playerdata.getPlayOrder());
    }
    @Test
    public void testGets(){
        PlayerData playerdata = new PlayerData("8mann", Color.RED, 3);
        Move lastmove= new Move(Direction.UP, 0);
        playerdata.setLastMove(lastmove);
        Assert.assertEquals(lastmove, playerdata.getLastMove());
        Assert.assertEquals(Color.RED, playerdata.getPlayerColor());
        Assert.assertEquals(Integer.valueOf(3), playerdata.getPlayOrder());
    }

    @Test
    public void detach() {
        PlayerData playerData = new PlayerData("test", Color.RED, 0);
        SocketThread socketThread = new SocketThread(socket, null, true);
        socketThread.start();
        PlayerDataObserver playerDataObserver = new PlayerDataObserver(socketThread);

        playerData.attach(playerDataObserver);
        playerData.detachSocket(socketThread);
    }
}
