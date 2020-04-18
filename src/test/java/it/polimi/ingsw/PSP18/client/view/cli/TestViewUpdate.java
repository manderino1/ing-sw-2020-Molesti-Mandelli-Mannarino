package it.polimi.ingsw.PSP18.client.view.cli;

import it.polimi.ingsw.PSP18.client.view.Launcher;
import it.polimi.ingsw.PSP18.networking.SocketThread;
import it.polimi.ingsw.PSP18.networking.messages.toclient.GameMapUpdate;
import it.polimi.ingsw.PSP18.networking.messages.toclient.PlaceReady;
import it.polimi.ingsw.PSP18.networking.messages.toclient.PlayerDataUpdate;
import it.polimi.ingsw.PSP18.server.controller.Match;
import it.polimi.ingsw.PSP18.server.controller.PlayerManager;
import it.polimi.ingsw.PSP18.server.model.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class TestViewUpdate {
    Match match = new Match();
    private Launcher launcher;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private GameMap map = new GameMap();


    @Before
    public void createLauncher() {
        launcher = new Launcher();
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @Test
    public void testUpdateMap() {
         launcher.getCliViewUpdate().updateMap(new GameMapUpdate(map.getMapCells()));
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

        launcher.getCliViewUpdate().updateMap(new GameMapUpdate(map.getMapCells()));
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

        launcher.getCliViewUpdate().updateMap(new GameMapUpdate(map.getMapCells()));
        Assert.assertEquals("|\u001B[32mw2\u001B[0m-D|\u001B[31mw1\u001B[0m-2|  -0|  -0|\u001B[34mw1\u001B[0m-D| 0\r\n" +
                "|\u001B[34mw1\u001B[0m-2|  -0|  -0|  -0|  -0| 1\r\n" +
                "|\u001B[32mw1\u001B[0m-2|\u001B[32mw2\u001B[0m-1|\u001B[31mw2\u001B[0m-2|  -0|  -0| 2\r\n" +
                "|\u001B[34mw2\u001B[0m-1|  -0|  -0|\u001B[32mw1\u001B[0m-D|\u001B[34mw2\u001B[0m-1| 3\r\n" +
                "|\u001B[31mw1\u001B[0m-D|  -0|  -0|  -0|  -D| 4\r\n" +
                " a    b    c    d    e  \r\n", outContent.toString());
    }

    @Test
    public void testPlayerDataUpdate() {
        PlayerData playerData = new PlayerData("test", Color.RED, 0);
        playerData.setDivinity("Apollo");

        launcher.getCliViewUpdate().updatePlayerData(new PlayerDataUpdate(playerData));
        Assert.assertEquals("\u001B[31mNickname: test\r\n" +
                "Play order: 0\r\n" +
                "Divinity: Apollo\r\n" +
                "\u001B[0m\r\n", outContent.toString());

        outContent.reset();

        playerData = new PlayerData("a", Color.GREEN, 0);
        playerData.setDivinity(null);
        launcher.getCliViewUpdate().updatePlayerData(new PlayerDataUpdate(playerData));
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
        launcher.getCliViewUpdate().updatePlayerData(new PlayerDataUpdate(playerData));
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
        launcher.getCliViewUpdate().updatePlayerData(new PlayerDataUpdate(playerData));
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
}
