package it.polimi.ingsw.PSP18.server.model;

import it.polimi.ingsw.PSP18.networking.SocketThread;
import it.polimi.ingsw.PSP18.server.view.MapObserver;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestMap {
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

    /***
     * Test for checking the creation of the class
     */
    @Test
    public void testMapCreation() {
        GameMap map = new GameMap();
        for (int i=0; i<5; i++){
            for (int j=0; j<5; j++){
                Assert.assertNotNull(map.getCell(i, j));
            }
        }
    }

    /***
     * Test for checking che correct response of the model after a cell edit
     */
    @Test
    public void testCellEdit() {
        GameMap map = new GameMap();
        map.setCell(0,0, 0, new Worker(0,0,0, Color.RED));
        Assert.assertEquals(Integer.valueOf(0), map.getCell(0,0).getBuilding());
        Assert.assertEquals(Integer.valueOf(0), map.getCell(0,0).getWorker().getX());
        Assert.assertEquals(Integer.valueOf(0), map.getCell(0,0).getWorker().getY());
    }

    @Test
    public void detach() {
        GameMap map = new GameMap();
        SocketThread socketThread = new SocketThread(socket, null, true);
        socketThread.start();
        MapObserver mapObserver = new MapObserver(socketThread);

        map.attach(mapObserver);
        map.detachSocket(socketThread);
    }
}
