package it.polimi.ingsw.PSP18.server.controller.divinities;

import it.polimi.ingsw.PSP18.client.view.cli.CliViewUpdate;
import it.polimi.ingsw.PSP18.networking.SocketThread;
import it.polimi.ingsw.PSP18.server.controller.PlayerManager;
import it.polimi.ingsw.PSP18.server.model.*;
import it.polimi.ingsw.PSP18.server.controller.Match;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class TestApollo extends TestDivinity {
    private PlayerManager playerManager1;
    @Override
    public void createPlayerManager() {
        Match match = new Match(true);
        SocketThread socketThread = new SocketThread(socket, match);
        socketThread.start();
        playerManager = new PlayerManager(match, new PlayerData("Test", Color.RED, 0), "Apollo");
        match.addPlayer(playerManager, socketThread);

        playerManager1 = new PlayerManager(match, new PlayerData("Test1", Color.BLUE, 1), "Apollo");
        match.addPlayer(playerManager1, socketThread);
    }

    /***
     * Testing the GetName method
     */
    @Override
    public void testGetName() {
        Divinity divinity = new Divinity("Apollo", playerManager);
        Assert.assertEquals(playerManager.getDivinityName(), divinity.getName());
    }

    @Test
    public void testCheckMovementMoves() {
        playerManager.getMatch().setCurrentPlayer(playerManager);
        playerManager.placeWorker(0,1);
        playerManager.placeWorker(0,0);

        playerManager.getDivinity().move();
        Assert.assertEquals("{\"type\":\"WAITING_NICK\"}\r\n" +
                "{\"moveList1\":[\"RIGHT\",\"RIGHTUP\",\"RIGHTDOWN\",\"DOWN\"],\"moveList2\":[\"RIGHT\",\"RIGHTDOWN\"],\"type\":\"MOVE_LIST\"}\r\n", socketOutContent.toString());

        playerManager.manageTurn(true);
        Assert.assertEquals("{\"type\":\"WAITING_NICK\"}\r\n" +
                "{\"moveList1\":[\"RIGHT\",\"RIGHTUP\",\"RIGHTDOWN\",\"DOWN\"],\"moveList2\":[\"RIGHT\",\"RIGHTDOWN\"],\"type\":\"MOVE_LIST\"}\r\n" +
                "{\"moveList1\":[\"RIGHT\",\"RIGHTUP\",\"RIGHTDOWN\",\"DOWN\"],\"moveList2\":[\"RIGHT\",\"RIGHTDOWN\"],\"type\":\"MOVE_LIST\"}\r\n", socketOutContent.toString());

    }

    @Test
    public void testUpdateMoveCells() {

        playerManager.getMatch().setCurrentPlayer(playerManager);
        playerManager.placeWorker(1,1);
        playerManager.placeWorker(0,0);

        playerManager.getDivinity().setMove(1,1, Direction.UP);
        Assert.assertEquals(playerManager.getWorker(0), playerManager.getMatch().getGameMap().getCell(1,0).getWorker());

        playerManager1.placeWorker(1,1);
        playerManager1.placeWorker(1,4);

        playerManager.getDivinity().setMove(1,0, Direction.DOWN);
        Assert.assertEquals(playerManager.getWorker(0), playerManager.getMatch().getGameMap().getCell(1,1).getWorker());
        Assert.assertEquals(playerManager1.getWorker(0), playerManager.getMatch().getGameMap().getCell(1,0).getWorker());
    }
}