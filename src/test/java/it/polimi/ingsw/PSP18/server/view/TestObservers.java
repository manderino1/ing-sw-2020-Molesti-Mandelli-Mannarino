package it.polimi.ingsw.PSP18.server.view;

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
        Match match = new Match(true);
        SocketThread socketThread = new SocketThread(socket, match);
        socketThread.start();
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        MapObserver mapObserver = new MapObserver(socketThread);

        map.attach(mapObserver);
        Assert.assertEquals("{\"type\":\"WAITING_NICK\"}\r\n" +
                "{\"gameMap\":[[{\"building\":0,\"dome\":false},{\"building\":0,\"dome\":false},{\"building\":0,\"dome\":false},{\"building\":0,\"dome\":false},{\"building\":0,\"dome\":false}],[{\"building\":0,\"dome\":false},{\"building\":0,\"dome\":false},{\"building\":0,\"dome\":false},{\"building\":0,\"dome\":false},{\"building\":0,\"dome\":false}],[{\"building\":0,\"dome\":false},{\"building\":0,\"dome\":false},{\"building\":0,\"dome\":false},{\"building\":0,\"dome\":false},{\"building\":0,\"dome\":false}],[{\"building\":0,\"dome\":false},{\"building\":0,\"dome\":false},{\"building\":0,\"dome\":false},{\"building\":0,\"dome\":false},{\"building\":0,\"dome\":false}],[{\"building\":0,\"dome\":false},{\"building\":0,\"dome\":false},{\"building\":0,\"dome\":false},{\"building\":0,\"dome\":false},{\"building\":0,\"dome\":false}]],\"type\":\"GAME_MAP_UPDATE\"}\r\n", socketOutContent.toString());
        map.detach(mapObserver);
    }

    /***
     * Testing the player data observer management and return
     */
    @Test
    public void testPlayerDataObs() {
        PlayerData playerData = new PlayerData("Test Player", Color.BLUE, 0);
        Match match = new Match(true);
        SocketThread socketThread = new SocketThread(socket, match);
        socketThread.start();
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        PlayerDataObserver playerObserver = new PlayerDataObserver(socketThread);

        playerData.attach(playerObserver);
        Assert.assertEquals("{\"type\":\"WAITING_NICK\"}\r\n" +
                "{\"playerID\":\"Test Player\",\"playerColor\":\"BLUE\",\"playOrder\":0,\"type\":\"PLAYER_DATA_UPDATE\"}\r\n", socketOutContent.toString());
        playerData.detach(playerObserver);
    }
}
