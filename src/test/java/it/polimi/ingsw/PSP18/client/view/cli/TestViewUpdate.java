package it.polimi.ingsw.PSP18.client.view.cli;

import com.google.gson.Gson;
import it.polimi.ingsw.PSP18.networking.SocketClient;
import it.polimi.ingsw.PSP18.networking.messages.toclient.*;
import it.polimi.ingsw.PSP18.networking.messages.toserver.*;
import it.polimi.ingsw.PSP18.server.controller.divinities.Divinity;
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

        Gson gson = new Gson();
        MoveReceiver moveReceiver = gson.fromJson(socketOutContent.toString(), MoveReceiver.class);
        Assert.assertEquals(Direction.UP, moveReceiver.getDirection());
        Assert.assertEquals(Integer.valueOf(0), moveReceiver.getWorkerID());

        socketOutContent.reset();
        testIn = new ByteArrayInputStream("2\ntest\n2\nDOWN\n".getBytes());
        cliViewUpdate = new CliViewUpdate(new BufferedReader(new InputStreamReader(testIn)));
        cliViewUpdate.setInputParser(new InputParser(socketClient));
        cliViewUpdate.moveUpdate(moveList);

        moveReceiver = gson.fromJson(socketOutContent.toString(), MoveReceiver.class);
        Assert.assertEquals(Direction.DOWN, moveReceiver.getDirection());
        Assert.assertEquals(Integer.valueOf(1), moveReceiver.getWorkerID());
    }

    @Test
    public void testBuildUpdate() {
        ArrayList<Direction> list1 = new ArrayList<>();
        list1.add(Direction.UP);
        BuildList buildList = new BuildList(list1);

        ByteArrayInputStream testIn = new ByteArrayInputStream("3\n1\ntest\n1\nUP\n".getBytes());

        CliViewUpdate cliViewUpdate = new CliViewUpdate(new BufferedReader(new InputStreamReader(testIn)));
        SocketClient socketClient = new SocketClient(socket, cliViewUpdate);
        socketClient.start();
        cliViewUpdate.setInputParser(new InputParser(socketClient));

        cliViewUpdate.buildUpdate(buildList);
        Gson gson = new Gson();
        BuildReceiver buildReceiver = gson.fromJson(socketOutContent.toString(), BuildReceiver.class);
        Assert.assertEquals(Direction.UP, buildReceiver.getDirection());
    }

    @Test
    public void testSetWorker() {
        ByteArrayInputStream testIn = new ByteArrayInputStream("W\nA1\nB\nB2\n".getBytes());

        CliViewUpdate cliViewUpdate = new CliViewUpdate(new BufferedReader(new InputStreamReader(testIn)));
        SocketClient socketClient = new SocketClient(socket, cliViewUpdate);
        socketClient.start();
        cliViewUpdate.setInputParser(new InputParser(socketClient));

        cliViewUpdate.updateMap(new GameMapUpdate(map.getMapCells()));
        cliViewUpdate.setWorker(new PlaceReady());

        Gson gson = new Gson();
        WorkerReceiver workerReceiver = gson.fromJson(socketOutContent.toString(), WorkerReceiver.class);
        Assert.assertEquals(Integer.valueOf(0), workerReceiver.getX1());
        Assert.assertEquals(Integer.valueOf(1), workerReceiver.getX2());
        Assert.assertEquals(Integer.valueOf(1), workerReceiver.getY1());
        Assert.assertEquals(Integer.valueOf(2), workerReceiver.getY2());
    }

    @Test
    public void testSelectNick() {
        ByteArrayInputStream testIn = new ByteArrayInputStream("Test\n".getBytes());

        CliViewUpdate cliViewUpdate = new CliViewUpdate(new BufferedReader(new InputStreamReader(testIn)));
        SocketClient socketClient = new SocketClient(socket, cliViewUpdate);
        socketClient.start();
        cliViewUpdate.setInputParser(new InputParser(socketClient));

        cliViewUpdate.selectNick();
        Gson gson = new Gson();
        PlayerDataReceiver playerDataReceiver = gson.fromJson(socketOutContent.toString(), PlayerDataReceiver.class);
        Assert.assertEquals("Test", playerDataReceiver.getPlayerID());
    }

    @Test
    public void testSelectDivinity() {
        ByteArrayInputStream testIn = new ByteArrayInputStream("Test\nApollo\n".getBytes());

        CliViewUpdate cliViewUpdate = new CliViewUpdate(new BufferedReader(new InputStreamReader(testIn)));
        SocketClient socketClient = new SocketClient(socket, cliViewUpdate);
        socketClient.start();
        cliViewUpdate.setInputParser(new InputParser(socketClient));

        ArrayList<String> divinities = new ArrayList<>();
        divinities.add("Apollo");
        divinities.add("Athena");
        cliViewUpdate.selectDivinity(new DivinityList(divinities));
        Gson gson = new Gson();
        DivinityReceiver divinityReceiver = gson.fromJson(socketOutContent.toString(), DivinityReceiver.class);
        Assert.assertEquals("Apollo", divinityReceiver.getDivinity());
    }

    @Test
    public void testMatchLostUpdate() {
        ByteArrayInputStream testIn = new ByteArrayInputStream("".getBytes());

        CliViewUpdate cliViewUpdate = new CliViewUpdate(new BufferedReader(new InputStreamReader(testIn)));
        SocketClient socketClient = new SocketClient(socket, cliViewUpdate);
        socketClient.start();
        cliViewUpdate.setInputParser(new InputParser(socketClient));


        PlayerData playerData = new PlayerData("test1", Color.RED, 0);
        playerData.setDivinity("Apollo");
        cliViewUpdate.updatePlayerData(new PlayerDataUpdate(playerData));

        playerData = new PlayerData("test2", Color.GREEN, 0);
        playerData.setDivinity("Apollo");
        cliViewUpdate.updatePlayerData(new PlayerDataUpdate(playerData));

        playerData = new PlayerData("test3", Color.BLUE, 0);
        playerData.setDivinity("Apollo");
        cliViewUpdate.updatePlayerData(new PlayerDataUpdate(playerData));

        cliViewUpdate.matchLostUpdate(new MatchLost("test1"));
        cliViewUpdate.matchLostUpdate(new MatchLost("test2"));
        cliViewUpdate.matchLostUpdate(new MatchLost("test3"));
    }

    @Test
    public void testMatchWonUpdate() {
        ByteArrayInputStream testIn = new ByteArrayInputStream("".getBytes());

        CliViewUpdate cliViewUpdate = new CliViewUpdate(new BufferedReader(new InputStreamReader(testIn)));
        SocketClient socketClient = new SocketClient(socket, cliViewUpdate);
        socketClient.start();
        cliViewUpdate.setInputParser(new InputParser(socketClient));


        PlayerData playerData = new PlayerData("test1", Color.RED, 0);
        playerData.setDivinity("Apollo");
        cliViewUpdate.updatePlayerData(new PlayerDataUpdate(playerData));

        playerData = new PlayerData("test2", Color.GREEN, 0);
        playerData.setDivinity("Apollo");
        cliViewUpdate.updatePlayerData(new PlayerDataUpdate(playerData));

        playerData = new PlayerData("test3", Color.BLUE, 0);
        playerData.setDivinity("Apollo");
        cliViewUpdate.updatePlayerData(new PlayerDataUpdate(playerData));

        cliViewUpdate.matchWonUpdate(new MatchWon("test1"));
        cliViewUpdate.matchWonUpdate(new MatchWon("test2"));
        cliViewUpdate.matchWonUpdate(new MatchWon("test3"));
    }

    @Test
    public void testMatchReadyUpdate() {
        ByteArrayInputStream testIn = new ByteArrayInputStream("rdy\nready".getBytes());

        CliViewUpdate cliViewUpdate = new CliViewUpdate(new BufferedReader(new InputStreamReader(testIn)));
        SocketClient socketClient = new SocketClient(socket, cliViewUpdate);
        socketClient.start();
        cliViewUpdate.setInputParser(new InputParser(socketClient));

        cliViewUpdate.matchReadyUpdate(new MatchReady());
    }

    @Test
    public void testPrometheusBuildUpdate() {
        ByteArrayInputStream testIn = new ByteArrayInputStream("3\n1\n2\nNO".getBytes());

        CliViewUpdate cliViewUpdate = new CliViewUpdate(new BufferedReader(new InputStreamReader(testIn)));
        SocketClient socketClient = new SocketClient(socket, cliViewUpdate);
        socketClient.start();
        cliViewUpdate.setInputParser(new InputParser(socketClient));

        ArrayList<Direction> list1 = new ArrayList<>();
        list1.add(Direction.UP);
        ArrayList<Direction> list2 = new ArrayList<>();
        list2.add(Direction.DOWN);

        socketOutContent.reset();
        cliViewUpdate.prometheusBuildListUpdate(new PrometheusBuildList(list1, list2));
        Gson gson = new Gson();
        PrometheusBuildReceiver prometheusBuildReceiver = gson.fromJson(socketOutContent.toString(), PrometheusBuildReceiver.class);
        Assert.assertEquals(Integer.valueOf(0), prometheusBuildReceiver.getChosenWorkerID());

        socketOutContent.reset();
        cliViewUpdate.prometheusBuildListUpdate(new PrometheusBuildList(list1, list2));
        gson = new Gson();
        prometheusBuildReceiver = gson.fromJson(socketOutContent.toString(), PrometheusBuildReceiver.class);
        Assert.assertEquals(Integer.valueOf(1), prometheusBuildReceiver.getChosenWorkerID());

        socketOutContent.reset();
        cliViewUpdate.prometheusBuildListUpdate(new PrometheusBuildList(list1, list2));
        gson = new Gson();
        prometheusBuildReceiver = gson.fromJson(socketOutContent.toString(), PrometheusBuildReceiver.class);
        Assert.assertNull(prometheusBuildReceiver.getChosenWorkerID());
    }

    @Test
    public void testAtlasBuildUpdate() {
        ArrayList<Direction> list1 = new ArrayList<>();
        list1.add(Direction.UP);
        AtlasBuildList atlasBuildList = new AtlasBuildList(list1);

        ByteArrayInputStream testIn = new ByteArrayInputStream("3\n1\ntest\n1\nUP\nyes\nUP\nlol\nno".getBytes());

        CliViewUpdate cliViewUpdate = new CliViewUpdate(new BufferedReader(new InputStreamReader(testIn)));
        SocketClient socketClient = new SocketClient(socket, cliViewUpdate);
        socketClient.start();
        cliViewUpdate.setInputParser(new InputParser(socketClient));

        cliViewUpdate.atlasBuildUpdate(atlasBuildList);
        Gson gson = new Gson();
        AtlasBuildReceiver atlasBuildReceiver = gson.fromJson(socketOutContent.toString(), AtlasBuildReceiver.class);
        Assert.assertEquals(Direction.UP, atlasBuildReceiver.getDirection());
        Assert.assertTrue(atlasBuildReceiver.isDome());

        socketOutContent.reset();
        cliViewUpdate.atlasBuildUpdate(atlasBuildList);
        gson = new Gson();
        atlasBuildReceiver = gson.fromJson(socketOutContent.toString(), AtlasBuildReceiver.class);
        Assert.assertEquals(Direction.UP, atlasBuildReceiver.getDirection());
        Assert.assertFalse(atlasBuildReceiver.isDome());
    }

    @Test
    public void testSingleMoveUpdate() {
        ArrayList<Direction> list1 = new ArrayList<>();
        list1.add(Direction.UP);
        SingleMoveList singleMoveList = new SingleMoveList(list1, 0, true);

        ByteArrayInputStream testIn = new ByteArrayInputStream("als\nYes\nasd\nUP\nno\n".getBytes());

        CliViewUpdate cliViewUpdate = new CliViewUpdate(new BufferedReader(new InputStreamReader(testIn)));
        SocketClient socketClient = new SocketClient(socket, cliViewUpdate);
        socketClient.start();
        cliViewUpdate.setInputParser(new InputParser(socketClient));

        cliViewUpdate.singleMoveUpdate(singleMoveList);
        Gson gson = new Gson();
        MoveReceiver moveReceiver = gson.fromJson(socketOutContent.toString(), MoveReceiver.class);
        Assert.assertEquals(Direction.UP, moveReceiver.getDirection());

        socketOutContent.reset();
        singleMoveList = new SingleMoveList(list1, 1, true);
        cliViewUpdate.singleMoveUpdate(singleMoveList);
        gson = new Gson();
        moveReceiver = gson.fromJson(socketOutContent.toString(), MoveReceiver.class);
        Assert.assertNull(moveReceiver.getDirection());
    }

    @Test
    public void testBuildListFlagUpdate() {
        ArrayList<Direction> list1 = new ArrayList<>();
        list1.add(Direction.UP);
        list1.add(Direction.DOWN);
        BuildListFlag buildListFlag = new BuildListFlag(list1);

        ByteArrayInputStream testIn = new ByteArrayInputStream("als\nNo\nUP\n".getBytes());

        CliViewUpdate cliViewUpdate = new CliViewUpdate(new BufferedReader(new InputStreamReader(testIn)));
        SocketClient socketClient = new SocketClient(socket, cliViewUpdate);
        socketClient.start();
        cliViewUpdate.setInputParser(new InputParser(socketClient));

        cliViewUpdate.buildListFlagUpdate(buildListFlag);
        Gson gson = new Gson();
        BuildReceiver buildReceiver = gson.fromJson(socketOutContent.toString(), BuildReceiver.class);
        Assert.assertNull(buildReceiver.getDirection());

        socketOutContent.reset();
        cliViewUpdate.buildListFlagUpdate(buildListFlag);
        gson = new Gson();
        buildReceiver = gson.fromJson(socketOutContent.toString(), BuildReceiver.class);
        Assert.assertEquals(Direction.UP, buildReceiver.getDirection());
    }

    @Test
    public void testBuildListFlagUpdate2() {
        ArrayList<Direction> list1 = new ArrayList<>();
        list1.add(Direction.UP);
        BuildListFlag buildListFlag = new BuildListFlag(list1);

        ByteArrayInputStream testIn = new ByteArrayInputStream("als\nNo\nYes\n".getBytes());

        CliViewUpdate cliViewUpdate = new CliViewUpdate(new BufferedReader(new InputStreamReader(testIn)));
        SocketClient socketClient = new SocketClient(socket, cliViewUpdate);
        socketClient.start();
        cliViewUpdate.setInputParser(new InputParser(socketClient));

        cliViewUpdate.buildListFlagUpdate(buildListFlag);
        Gson gson = new Gson();
        BuildReceiver buildReceiver = gson.fromJson(socketOutContent.toString(), BuildReceiver.class);
        Assert.assertNull(buildReceiver.getDirection());

        socketOutContent.reset();
        cliViewUpdate.buildListFlagUpdate(buildListFlag);
        gson = new Gson();
        buildReceiver = gson.fromJson(socketOutContent.toString(), BuildReceiver.class);
        Assert.assertEquals(Direction.UP, buildReceiver.getDirection());
    }

    @Test
    public void testEndTurn() {
        ByteArrayInputStream testIn = new ByteArrayInputStream("als\nEnd\n".getBytes());

        CliViewUpdate cliViewUpdate = new CliViewUpdate(new BufferedReader(new InputStreamReader(testIn)));
        SocketClient socketClient = new SocketClient(socket, cliViewUpdate);
        socketClient.start();
        cliViewUpdate.setInputParser(new InputParser(socketClient));

        cliViewUpdate.endTurn(new EndTurnAvaiable());
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
