package it.polimi.ingsw.PSP18.server.view;

import it.polimi.ingsw.PSP18.server.model.Color;
import it.polimi.ingsw.PSP18.server.model.GameMap;
import it.polimi.ingsw.PSP18.server.model.Match;
import it.polimi.ingsw.PSP18.server.model.PlayerData;
import it.polimi.ingsw.PSP18.networking.SocketThread;
import org.junit.Test;

import java.net.Socket;

public class TestObservers {
    /***
     * Testing the game map observer management and return
     */
    @Test
    public void testGameMapObs() {
        GameMap map = new GameMap();
        MapObserver mapObserver = new MapObserver(new SocketThread(new Socket(), new Match()));

        // TODO: add return check assert
        map.attach(mapObserver);
        map.notifyObservers();
        map.detach(mapObserver);
    }

    /***
     * Testing the player data observer management and return
     */
    @Test
    public void testPlayerDataObs() {
        PlayerData playerData = new PlayerData("Test Player", Color.BLUE, 0);
        PlayerDataObserver playerObserver = new PlayerDataObserver(new SocketThread(new Socket(), new Match()));

        // TODO: add return check assert
        playerData.attach(playerObserver);
        playerData.notifyObservers();
        playerData.detach(playerObserver);
    }
}
