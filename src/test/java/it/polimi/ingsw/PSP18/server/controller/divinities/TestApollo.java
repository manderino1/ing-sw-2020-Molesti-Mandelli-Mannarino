package it.polimi.ingsw.PSP18.server.controller.divinities;

import com.google.gson.Gson;
import it.polimi.ingsw.PSP18.networking.SocketThread;
import it.polimi.ingsw.PSP18.networking.messages.toclient.MoveList;
import it.polimi.ingsw.PSP18.server.controller.PlayerManager;
import it.polimi.ingsw.PSP18.server.model.*;
import org.junit.Assert;
import org.junit.Test;

public class TestApollo extends TestDivinity {
    private PlayerManager playerManager1;
    @Override
    public void createPlayerManager() {
        MatchSocket matchSocket = new MatchSocket(2);;
        SocketThread socketThread = new SocketThread(socket, null);
        socketThread.setMatch(match);
        socketThread.start();
        playerManager = new PlayerManager(matchRun, new PlayerData("Test", Color.RED, 0), "Apollo");
        matchSocket.addPlayer(playerManager, socketThread);

        playerManager1 = new PlayerManager(matchRun, new PlayerData("Test1", Color.BLUE, 1), "Apollo");
        matchSocket.addPlayer(playerManager1, socketThread);
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
        playerManager.getMatch().getMatchSocket().setCurrentPlayer(playerManager);
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

        playerManager.getMatch().getMatchSocket().setCurrentPlayer(playerManager);
        playerManager.placeWorker(1,1);
        playerManager.placeWorker(0,0);

        playerManager.getDivinity().setMove(1,1, Direction.UP);
        Assert.assertEquals(playerManager.getWorker(0), playerManager.getMatch().getMatchRun().getGameMap().getCell(1,0).getWorker());

        playerManager1.placeWorker(1,1);
        playerManager1.placeWorker(1,4);

        playerManager.getDivinity().setMove(1,0, Direction.DOWN);
        Assert.assertEquals(playerManager.getWorker(0), playerManager.getMatch().getMatchRun().getGameMap().getCell(1,1).getWorker());
        Assert.assertEquals(playerManager1.getWorker(0), playerManager.getMatch().getMatchRun().getGameMap().getCell(1,0).getWorker());
    }
}