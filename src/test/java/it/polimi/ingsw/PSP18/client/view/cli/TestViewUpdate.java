package it.polimi.ingsw.PSP18.client.view.cli;

import it.polimi.ingsw.PSP18.networking.SocketClient;
import it.polimi.ingsw.PSP18.networking.messages.toclient.GameMapUpdate;
import it.polimi.ingsw.PSP18.networking.messages.toclient.MoveList;
import it.polimi.ingsw.PSP18.networking.messages.toclient.PlayerDataUpdate;
import it.polimi.ingsw.PSP18.server.model.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestViewUpdate {
    private InputStream systemIn = System.in;
    private PrintStream systemOut = System.out;
    private ByteArrayOutputStream socketOutContent = new ByteArrayOutputStream();
    private InputStream socketInContent = new InputStream() {
        @Override
        public int read() {
            return -1;  // end of stream
        }
    };
    private ByteArrayOutputStream outContent;
    private ByteArrayOutputStream errContent;
    private Socket socket;
    private GameMap map = new GameMap();

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

        outContent = new ByteArrayOutputStream();
        errContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }


    @Test
    public void testMoveUpdate() {
        ArrayList<Direction> list1 = new ArrayList<>();
        list1.add(Direction.UP);
        ArrayList<Direction> list2 = new ArrayList<>();
        list2.add(Direction.DOWN);
        MoveList moveList = new MoveList(list1, list2);

        ByteArrayInputStream testIn = new ByteArrayInputStream("3\n1\ntest\n1\nUP\n".getBytes());

        CliViewUpdate cliViewUpdate = new CliViewUpdate(new BufferedReader(new InputStreamReader(testIn)));
        SocketClient socketClient = new SocketClient(socket, cliViewUpdate);
        socketClient.start();
        cliViewUpdate.setInputParser(new InputParser(socketClient));

        cliViewUpdate.moveUpdate(moveList);
        Assert.assertEquals("{\"direction\":\"UP\",\"workerID\":0,\"type\":\"MOVE_RECEIVER\"}\r\n", socketOutContent.toString());

        socketOutContent.reset();
        testIn = new ByteArrayInputStream("2\ntest\n2\nDOWN\n".getBytes());
        cliViewUpdate = new CliViewUpdate(new BufferedReader(new InputStreamReader(testIn)));
        cliViewUpdate.setInputParser(new InputParser(socketClient));
        cliViewUpdate.moveUpdate(moveList);
        Assert.assertEquals("{\"direction\":\"DOWN\",\"workerID\":1,\"type\":\"MOVE_RECEIVER\"}\r\n", socketOutContent.toString());
    }

    @Test
    public void testUpdateMap() {
        CliViewUpdate cliViewUpdate = new CliViewUpdate();
        SocketClient socketClient = new SocketClient(socket, cliViewUpdate);
        socketClient.start();
        cliViewUpdate.setInputParser(new InputParser(socketClient));

        cliViewUpdate.updateMap(new GameMapUpdate(map.getMapCells()));
        Assert.assertEquals("|  -0|  -0|  -0|  -0|  -0| 0\r\n" +
                "|  -0|  -0|  -0|  -0|  -0| 1\r\n" +
                "|  -0|  -0|  -0|  -0|  -0| 2\r\n" +
                "|  -0|  -0|  -0|  -0|  -0| 3\r\n" +
                "|  -0|  -0|  -0|  -0|  -0| 4\r\n" +
                " a    b    c    d    e  \r\n", outContent.toString());

        outContent.reset();

        map.setCell(1,2,1, new Worker(2, 2 , 1, Color.GREEN));
        map.setCell(3,2,0, null);
        map.setCell(2,2,2, new Worker(2, 2 , 1, Color.RED));
        map.setCell(4,3,1, new Worker(2, 2 , 1, Color.BLUE));
        map.setCell(0,2,1, new Worker(2, 2 , 0, Color.GREEN));
        map.setCell(0,2,2, new Worker(2, 2 , 0, Color.RED));
        map.setCell(0,3,1, new Worker(2, 2 , 1, Color.BLUE));
        map.setCell(0,0,1, new Worker(2, 2 , 1, Color.GREEN));
        map.setCell(1,0,2, new Worker(2, 2 , 1, Color.RED));
        map.setCell(4,0,1, new Worker(2, 2 , 0, Color.BLUE));
        map.setCell(1,0,2, new Worker(2, 2 , 0, Color.RED));
        map.setCell(3,3,1, new Worker(2, 2 , 0, Color.GREEN));
        map.setCell(0,4,2, new Worker(2, 2 , 1, Color.RED));
        map.setCell(0,1,2, new Worker(2, 2 , 1, Color.BLUE));
        map.setCell(0,2,2, new Worker(2, 2 , 1, Color.GREEN));

        cliViewUpdate.updateMap(new GameMapUpdate(map.getMapCells()));
        Assert.assertEquals("|\u001B[32mw2\u001B[0m-1|\u001B[31mw1\u001B[0m-2|  -0|  -0|\u001B[34mw1\u001B[0m-1| 0\r\n" +
                "|\u001B[34mw2\u001B[0m-2|  -0|  -0|  -0|  -0| 1\r\n" +
                "|\u001B[32mw2\u001B[0m-2|\u001B[32mw2\u001B[0m-1|\u001B[31mw2\u001B[0m-2|  -0|  -0| 2\r\n" +
                "|\u001B[34mw2\u001B[0m-1|  -0|  -0|\u001B[32mw1\u001B[0m-1|\u001B[34mw2\u001B[0m-1| 3\r\n" +
                "|\u001B[31mw2\u001B[0m-2|  -0|  -0|  -0|  -0| 4\r\n" +
                " a    b    c    d    e  \r\n", outContent.toString());

        outContent.reset();

        map.setCell(0,4,2, new Worker(2, 2 , 0, Color.RED));
        map.setCell(0,1,2, new Worker(2, 2 , 0, Color.BLUE));
        map.setCell(0,2,2, new Worker(2, 2 , 0, Color.GREEN));
        map.getCell(4,0).setDome();
        map.getCell(0,0).setDome();
        map.getCell(4,4).setDome();
        map.getCell(0,4).setDome();
        map.getCell(3,3).setDome();

        cliViewUpdate.updateMap(new GameMapUpdate(map.getMapCells()));
        Assert.assertEquals("|\u001B[32mw2\u001B[0m-D|\u001B[31mw1\u001B[0m-2|  -0|  -0|\u001B[34mw1\u001B[0m-D| 0\r\n" +
                "|\u001B[34mw1\u001B[0m-2|  -0|  -0|  -0|  -0| 1\r\n" +
                "|\u001B[32mw1\u001B[0m-2|\u001B[32mw2\u001B[0m-1|\u001B[31mw2\u001B[0m-2|  -0|  -0| 2\r\n" +
                "|\u001B[34mw2\u001B[0m-1|  -0|  -0|\u001B[32mw1\u001B[0m-D|\u001B[34mw2\u001B[0m-1| 3\r\n" +
                "|\u001B[31mw1\u001B[0m-D|  -0|  -0|  -0|  -D| 4\r\n" +
                " a    b    c    d    e  \r\n", outContent.toString());
    }

    @Test
    public void testPlayerDataUpdate() {
        CliViewUpdate cliViewUpdate = new CliViewUpdate();
        SocketClient socketClient = new SocketClient(socket, cliViewUpdate);
        socketClient.start();
        cliViewUpdate.setInputParser(new InputParser(socketClient));

        PlayerData playerData = new PlayerData("test", Color.RED, 0);
        playerData.setDivinity("Apollo");

        cliViewUpdate.updatePlayerData(new PlayerDataUpdate(playerData));
        Assert.assertEquals("\u001B[31mNickname: test\r\n" +
                "Play order: 0\r\n" +
                "Divinity: Apollo\r\n" +
                "\u001B[0m\r\n", outContent.toString());

        outContent.reset();

        playerData = new PlayerData("a", Color.GREEN, 0);
        playerData.setDivinity(null);
        cliViewUpdate.updatePlayerData(new PlayerDataUpdate(playerData));
        Assert.assertEquals("\u001B[31mNickname: test\r\n" +
                "Play order: 0\r\n" +
                "Divinity: Apollo\r\n" +
                "\u001B[0m\r\n" +
                "\u001B[32mNickname: a\r\n" +
                "Play order: 0\r\n" +
                "Divinity: null\r\n" +
                "\u001B[0m\r\n", outContent.toString());

        outContent.reset();

        playerData = new PlayerData("as", Color.BLUE, 0);
        cliViewUpdate.updatePlayerData(new PlayerDataUpdate(playerData));
        Assert.assertEquals("\u001B[31mNickname: test\r\n" +
                "Play order: 0\r\n" +
                "Divinity: Apollo\r\n" +
                "\u001B[0m\r\n" +
                "\u001B[32mNickname: a\r\n" +
                "Play order: 0\r\n" +
                "Divinity: null\r\n" +
                "\u001B[0m\r\n" +
                "\u001B[34mNickname: as\r\n" +
                "Play order: 0\r\n" +
                "Divinity: null\r\n" +
                "\u001B[0m\r\n", outContent.toString());

        outContent.reset();

        playerData = new PlayerData("test", Color.RED, 0);
        cliViewUpdate.updatePlayerData(new PlayerDataUpdate(playerData));
        Assert.assertEquals("\u001B[31mNickname: test\r\n" +
                "Play order: 0\r\n" +
                "Divinity: Apollo\r\n" +
                "\u001B[0m\r\n" +
                "\u001B[32mNickname: a\r\n" +
                "Play order: 0\r\n" +
                "Divinity: null\r\n" +
                "\u001B[0m\r\n" +
                "\u001B[34mNickname: as\r\n" +
                "Play order: 0\r\n" +
                "Divinity: null\r\n" +
                "\u001B[0m\r\n", outContent.toString());

        outContent.reset();

    }

    @After
    public void restoreStreams() {
        System.setIn(systemIn);
        System.setOut(systemOut);
    }
}
