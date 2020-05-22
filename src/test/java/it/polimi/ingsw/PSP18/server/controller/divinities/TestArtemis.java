package it.polimi.ingsw.PSP18.server.controller.divinities;

import com.google.gson.Gson;
import it.polimi.ingsw.PSP18.networking.SocketThread;
import it.polimi.ingsw.PSP18.networking.messages.toclient.MoveList;
import it.polimi.ingsw.PSP18.server.controller.MatchRun;
import it.polimi.ingsw.PSP18.server.controller.MatchSocket;
import it.polimi.ingsw.PSP18.server.controller.PlayerManager;
import it.polimi.ingsw.PSP18.server.model.Color;
import it.polimi.ingsw.PSP18.server.model.Direction;
import it.polimi.ingsw.PSP18.server.model.PlayerData;
import org.junit.Assert;
import org.junit.Test;

public class TestArtemis extends TestDivinity {
    private PlayerManager playerManager1;
    @Override
    public void createPlayerManager() {
        MatchSocket matchSocket = new MatchSocket(2);
        MatchRun matchRun = new MatchRun(matchSocket);
        SocketThread socketThread = new SocketThread(socket, null);
        socketThread.start();
        playerManager = new PlayerManager(matchRun, new PlayerData("Test1", Color.RED, 0), "Artemis", matchSocket);
        matchSocket.addPlayer(playerManager, socketThread);
        playerManager1 = new PlayerManager(matchRun, new PlayerData("Test11", Color.BLUE, 1), "Artemis", matchSocket);
        matchSocket.addPlayer(playerManager1, socketThread);
    }

    /***
     * Testing the GetName method
     */
    @Override
    public void testGetName() {
        MatchSocket matchSocket = new MatchSocket(2);
        MatchRun matchRun = new MatchRun(matchSocket);
        Divinity artemis = new Artemis("Artemis", playerManager, matchSocket, matchRun);
        Assert.assertEquals(playerManager.getDivinityName(), artemis.getName());
    }

    @Test
    public void testMove() {
        MatchSocket matchSocket = new MatchSocket(2);
        MatchRun matchRun = new MatchRun(matchSocket);
        matchSocket.setCurrentPlayer(playerManager);
        playerManager.placeWorker(0,0);
        playerManager.placeWorker(3,1);

        socketOutContent.reset();
        playerManager.getDivinity().move();
        Gson gson = new Gson();
        MoveList moveList = gson.fromJson(socketOutContent.toString(), MoveList.class);
        Assert.assertEquals(playerManager.getDivinity().checkBuildingMoves(0,0), moveList.getMoveList1());
    }

    @Test
    public void testMoveReceiver() {
        MatchSocket matchSocket = new MatchSocket(2);
        MatchRun matchRun = new MatchRun(matchSocket);
        
        matchRun.getGameMap().getCell(2,3).setBuilding(1);
        matchRun.getGameMap().getCell(2,2).setBuilding(2);
        matchRun.getGameMap().getCell(2,1).setBuilding(3);


        matchSocket.setCurrentPlayer(playerManager);
        playerManager.placeWorker(2, 4);
        playerManager.placeWorker(3, 2);

        playerManager.getDivinity().move();
        playerManager.getDivinity().moveReceiver(Direction.UP, 0);
        Assert.assertEquals(playerManager.getWorker(0), matchRun.getGameMap().getCell(2,3).getWorker());

        playerManager.getDivinity().move();
        playerManager.getDivinity().moveReceiver(Direction.UP, 0);
        Assert.assertEquals(playerManager.getWorker(0), matchRun.getGameMap().getCell(2,2).getWorker());

        playerManager.getDivinity().moveReceiver(null, 1);
        Assert.assertEquals(playerManager.getWorker(1), matchRun.getGameMap().getCell(3,2).getWorker());

        playerManager.getDivinity().moveReceiver(Direction.UP, 0);
    }
}
