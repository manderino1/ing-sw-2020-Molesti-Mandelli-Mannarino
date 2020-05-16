package it.polimi.ingsw.PSP18.server.view;

import com.google.gson.Gson;
import it.polimi.ingsw.PSP18.networking.messages.toclient.BuildList;
import it.polimi.ingsw.PSP18.networking.messages.toclient.GameMapUpdate;
import it.polimi.ingsw.PSP18.networking.messages.toclient.PlayerDataUpdate;
import it.polimi.ingsw.PSP18.server.model.Color;
import it.polimi.ingsw.PSP18.server.model.GameMap;
import it.polimi.ingsw.PSP18.server.controller.Match;
import it.polimi.ingsw.PSP18.server.model.PlayerData;
import it.polimi.ingsw.PSP18.networking.SocketThread;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestObservers {
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
     * Testing the game map observer management and return
     */
    @Test
    public void testGameMapObs() {
        GameMap map = new GameMap();
        Match match = new Match();
        SocketThread socketThread = new SocketThread(socket, null);
        socketThread.start();
        MapObserver mapObserver = new MapObserver(socketThread);

        socketOutContent.reset();
        map.attach(mapObserver);
        Gson gson = new Gson();
        GameMapUpdate gameMapUpdate = gson.fromJson(socketOutContent.toString(), GameMapUpdate.class);
        for(int i=0; i<5; i++) {
            for(int j=0; j<5; j++) {
                Assert.assertEquals(map.getMapCells()[i][j].getBuilding(), gameMapUpdate.getGameMap()[i][j].getBuilding());
                Assert.assertEquals(map.getMapCells()[i][j].getWorker(), gameMapUpdate.getGameMap()[i][j].getWorker());
            }
        }
        map.detach(mapObserver);
    }

    /***
     * Testing the player data observer management and return
     */
    @Test
    public void testPlayerDataObs() {
        PlayerData playerData = new PlayerData("Test Player", Color.BLUE, 0);
        Match match = new Match();
        SocketThread socketThread = new SocketThread(socket, null);
        socketThread.start();
        PlayerDataObserver playerObserver = new PlayerDataObserver(socketThread);

        socketOutContent.reset();
        playerData.attach(playerObserver);
        Gson gson = new Gson();
        PlayerDataUpdate playerDataUpdate = gson.fromJson(socketOutContent.toString(), PlayerDataUpdate.class);
        Assert.assertEquals(playerDataUpdate.getDivinity(), playerData.getDivinity());
        Assert.assertEquals(playerDataUpdate.getPlayerID(), playerData.getPlayerID());
        Assert.assertEquals(playerDataUpdate.getPlayerColor(), playerData.getPlayerColor());
        Assert.assertEquals(playerDataUpdate.getPlayOrder(), playerData.getPlayOrder());
        Assert.assertEquals(playerDataUpdate.getReady(), playerData.getReady());
        playerData.detach(playerObserver);
    }
}
