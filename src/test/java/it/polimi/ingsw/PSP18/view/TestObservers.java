package it.polimi.ingsw.PSP18.view;

import it.polimi.ingsw.PSP18.model.Color;
import it.polimi.ingsw.PSP18.model.GameMap;
import it.polimi.ingsw.PSP18.model.PlayerData;
import org.junit.Before;
import org.junit.Test;

public class TestObservers {
    /***
     * Testing the game map observer management and return
     */
    @Test
    public void testGameMapObs() {
        GameMap map = new GameMap();
        MapObserver mapObserver = new MapObserver();

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
        PlayerDataObserver playerObserver = new PlayerDataObserver();

        // TODO: add return check assert
        playerData.attach(playerObserver);
        playerData.notifyObservers();
        playerData.detach(playerObserver);
    }
}
