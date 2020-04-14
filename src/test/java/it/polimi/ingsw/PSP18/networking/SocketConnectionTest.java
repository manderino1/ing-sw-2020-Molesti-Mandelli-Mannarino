package it.polimi.ingsw.PSP18.networking;

import it.polimi.ingsw.PSP18.client.view.Launcher;
import it.polimi.ingsw.PSP18.server.controller.PlayerManager;
import it.polimi.ingsw.PSP18.server.model.Color;
import it.polimi.ingsw.PSP18.server.controller.Match;
import it.polimi.ingsw.PSP18.server.model.PlayerData;
import org.junit.Test;

import java.io.IOException;
import java.net.Socket;

public class SocketConnectionTest {
    @Test
    public void connectionTest() throws IOException {
        Match match = new Match();
        Launcher launcher = new Launcher();

        PlayerData playerData1 = new PlayerData("ottavio", Color.RED, 0);
        PlayerData playerData2 = new PlayerData("marco", Color.GREEN, 1);
        PlayerData playerData3 = new PlayerData("mole", Color.BLUE, 2);

        PlayerManager playerManager1 = new PlayerManager(match, playerData1);
        PlayerManager playerManager2 = new PlayerManager(match, playerData2);
        PlayerManager playerManager3 = new PlayerManager(match, playerData3);

        match.addPlayer(playerManager1, new SocketThread(new Socket(), match));
        playerManager1.placeWorker(2,1);
        match.addPlayer(playerManager2, new SocketThread(new Socket(), match));
        playerManager2.placeWorker(3,2);
        match.addPlayer(playerManager3, new SocketThread(new Socket(), match));
        playerManager3.placeWorker(4,1);
        playerManager3.placeWorker(3,1);

        while(true) {
            int i = 1;
        }
    }
}
