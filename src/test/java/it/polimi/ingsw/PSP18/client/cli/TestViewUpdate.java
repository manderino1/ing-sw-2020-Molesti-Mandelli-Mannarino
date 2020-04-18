package it.polimi.ingsw.PSP18.Client.Cli;

import it.polimi.ingsw.PSP18.client.view.Launcher;
import it.polimi.ingsw.PSP18.client.view.cli.CliViewUpdate;
import it.polimi.ingsw.PSP18.networking.SocketThread;
import it.polimi.ingsw.PSP18.networking.messages.toclient.GameMapUpdate;
import it.polimi.ingsw.PSP18.networking.messages.toclient.PlayerDataUpdate;
import it.polimi.ingsw.PSP18.server.controller.Match;
import it.polimi.ingsw.PSP18.server.controller.PlayerManager;
import it.polimi.ingsw.PSP18.server.model.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.Socket;

public class TestViewUpdate {
    private Launcher launcher;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    @Before
    public void createLauncher() {
        launcher = new Launcher();
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @Test
    public void testUpdateMap() {
        launcher.getCliViewUpdate().updateMap(new GameMapUpdate(new GameMap().getMapCells()));
        Assert.assertEquals("|  -0|  -0|  -0|  -0|  -0| 0\r\n" +
                "|  -0|  -0|  -0|  -0|  -0| 1\r\n" +
                "|  -0|  -0|  -0|  -0|  -0| 2\r\n" +
                "|  -0|  -0|  -0|  -0|  -0| 3\r\n" +
                "|  -0|  -0|  -0|  -0|  -0| 4\r\n" +
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
    }
}
