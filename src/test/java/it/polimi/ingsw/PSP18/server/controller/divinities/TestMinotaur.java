package it.polimi.ingsw.PSP18.server.controller.divinities;

import com.google.gson.Gson;
import it.polimi.ingsw.PSP18.networking.SocketThread;
import it.polimi.ingsw.PSP18.networking.messages.toclient.BuildList;
import it.polimi.ingsw.PSP18.networking.messages.toclient.MoveList;
import it.polimi.ingsw.PSP18.server.controller.PlayerManager;
import it.polimi.ingsw.PSP18.server.model.*;
import it.polimi.ingsw.PSP18.server.controller.Match;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestMinotaur extends TestDivinity {
    private PlayerManager playerManager1;
    @Override
    public void createPlayerManager() {
        Match match = new Match(true);
        SocketThread socketThread = new SocketThread(socket, match);
        socketThread.start();
        playerManager = new PlayerManager(match, new PlayerData("Test1", Color.RED, 0), "Minotaur");
        match.addPlayer(playerManager, socketThread);

        playerManager1 = new PlayerManager(match, new PlayerData("Test1", Color.BLUE, 1), "Apollo");
        match.addPlayer(playerManager1, socketThread);
    }

    /***
     * Testing the GetName method
     */
    @Override
    public void testGetName() {
        Divinity divinity = new Divinity("Minotaur", playerManager);
        Assert.assertEquals(playerManager.getDivinityName(), divinity.getName());
    }

    @Test
    public void testCheckMovementMoves() {
        playerManager.getMatch().setCurrentPlayer(playerManager);
        playerManager.placeWorker(0,0);
        playerManager.placeWorker(0,1);

        socketOutContent.reset();
        playerManager.manageTurn(false);
        Gson gson = new Gson();
        MoveList moveList = gson.fromJson(socketOutContent.toString(), MoveList.class);
        Assert.assertEquals(playerManager.getDivinity().checkMovementMoves(0,0, false), moveList.getMoveList1());

        socketOutContent.reset();
        playerManager.manageTurn(true);
        moveList = gson.fromJson(socketOutContent.toString(), MoveList.class);
        Assert.assertEquals(playerManager.getDivinity().checkMovementMoves(0,0, true), moveList.getMoveList1());
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
        Assert.assertEquals(playerManager1.getWorker(0), playerManager.getMatch().getGameMap().getCell(1,2).getWorker());
    }
}
