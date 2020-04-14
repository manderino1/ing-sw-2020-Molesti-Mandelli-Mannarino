package it.polimi.ingsw.PSP18.server.controller;

import it.polimi.ingsw.PSP18.networking.SocketThread;
import it.polimi.ingsw.PSP18.server.model.Color;
import it.polimi.ingsw.PSP18.server.model.PlayerData;
import org.junit.Before;
import org.junit.Test;

import java.net.Socket;

public class TestTurnManager {
    private Match match;

    @Before
    public void createGameManager() {
        match = new Match();
    }

    /***
     * Test for checking the correct creation of the turn manager class
     */
    @Test
    public void testManageTurn() {
        Match match = new Match();
        match.addPlayer(new PlayerManager(new Match(), new PlayerData("Test1", Color.RED, 0), "Divinity"), new SocketThread(new Socket(), match));
        match.addPlayer(new PlayerManager(new Match(), new PlayerData("Test2", Color.GREEN, 1), "Divinity"), new SocketThread(new Socket(), match));
        match.getPlayerManagers().get(0).placeWorker(0, 0);
        match.getPlayerManagers().get(0).placeWorker(1, 0);
        match.getPlayerManagers().get(1).placeWorker(0, 1);
        match.getPlayerManagers().get(1).placeWorker(1, 1);
        TurnManager turnManager = new TurnManager(match);
        turnManager.manageTurn();
    }

    /***
     * Test for checking the correct creation of the turn manager class with Athena
     */
    @Test
    public void testManageTurnAthena() {
        Match match = new Match();
        match.addPlayer(new PlayerManager(new Match(), new PlayerData("Test1", Color.RED, 0), "Divinity"), new SocketThread(new Socket(), match));
        match.addPlayer(new PlayerManager(new Match(), new PlayerData("Test2", Color.GREEN, 1), "Divinity"), new SocketThread(new Socket(), match));
        match.getPlayerManagers().get(0).placeWorker(0, 0);
        match.getPlayerManagers().get(0).placeWorker(1, 0);
        match.getPlayerManagers().get(1).placeWorker(0, 1);
        match.getPlayerManagers().get(1).placeWorker(1, 1);
        TurnManager turnManager = new TurnManager(match);
        turnManager.manageTurn();
    }
}
