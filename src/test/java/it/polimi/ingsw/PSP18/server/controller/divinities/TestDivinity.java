package it.polimi.ingsw.PSP18.server.controller.divinities;

import com.google.gson.Gson;
import it.polimi.ingsw.PSP18.networking.SocketThread;
import it.polimi.ingsw.PSP18.networking.messages.toclient.BuildList;
import it.polimi.ingsw.PSP18.server.controller.MatchRun;
import it.polimi.ingsw.PSP18.server.controller.MatchSocket;
import it.polimi.ingsw.PSP18.server.controller.PlayerManager;
import it.polimi.ingsw.PSP18.server.model.Color;
import it.polimi.ingsw.PSP18.server.model.Direction;
import it.polimi.ingsw.PSP18.server.model.GameMap;
import it.polimi.ingsw.PSP18.server.model.PlayerData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestDivinity {
    protected ByteArrayOutputStream socketOutContent = new ByteArrayOutputStream();
    protected InputStream socketInContent = new InputStream() {
        @Override
        public int read() {
            return -1;  // end of stream
        }
    };
    protected Socket socket;
    protected GameMap map;
    protected PlayerManager playerManager;
    protected MatchSocket matchSocket;
    protected MatchRun matchRun;

    @Before
    public void socketMock() {
        socketOutContent = new ByteArrayOutputStream();

        // Create mock sockets
        socket = mock(Socket.class);
        try {
            when(socket.getOutputStream()).thenReturn(socketOutContent);
            when(socket.getInputStream()).thenReturn(socketInContent);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.matchSocket = new MatchSocket(2);
        this.matchRun = new MatchRun(matchSocket);
        createPlayerManager();
    }

    public void createPlayerManager() {
        SocketThread socketThread = new SocketThread(socket, null, true);
        socketThread.setMatchSocket(matchSocket);
        socketThread.setMatchRun(matchRun);
        socketThread.start();
        playerManager = new PlayerManager(matchRun, new PlayerData("Test1",Color.RED, 0), "Divinity", matchSocket);
        matchSocket.addPlayer(playerManager, socketThread);
    }

    /***
     * Testing the GetName method
     */
    @Test
    public void testGetName() {
        Divinity divinity = new Divinity("Divinity", playerManager, matchSocket, matchRun);
        Assert.assertEquals(playerManager.getDivinityName(), divinity.getName());
    }

    /***
     * Testing ManageTurn that will lso test all the other Divinty methods that ManageTurn calls
     */
    @Test
    public void testManageTurn() {
        matchSocket.setCurrentPlayer(playerManager);

        playerManager.placeWorker(2,1);
        playerManager.placeWorker(3,2);
        playerManager.manageTurn(false);
        playerManager.manageTurn(true);
    }

    @Test
    public void testMoveReceiver() {
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

        playerManager.getDivinity().moveReceiver(Direction.UP, 0);
    }

    @Test
    public void testBuild() {
        matchSocket.setCurrentPlayer(playerManager);
        playerManager.placeWorker(0,0);
        playerManager.placeWorker(2,1);

        socketOutContent.reset();
        playerManager.getDivinity().build();
        Gson gson = new Gson();
        BuildList buildList = gson.fromJson(socketOutContent.toString(), BuildList.class);
        Assert.assertEquals(playerManager.getDivinity().checkBuildingMoves(0,0), buildList.getBuildlist());

        socketOutContent.reset();
        playerManager.getDivinity().buildReceiver(Direction.DOWN);
        Assert.assertEquals(Integer.valueOf(1), matchRun.getGameMap().getCell(0,1).getBuilding());

        matchRun.getGameMap().getCell(1,0).setBuilding(3);
        matchRun.getGameMap().getCell(0,1).setBuilding(2);
        matchRun.getGameMap().getCell(1,1).setBuilding(3);
    }

    @Test
    public void testManageLoss () {

        matchSocket.setCurrentPlayer(playerManager);
        playerManager.placeWorker(0,0);
        playerManager.placeWorker(2,1);

        matchRun.getGameMap().getCell(1,0).setDome();
        matchRun.getGameMap().getCell(0,1).setDome();
        matchRun.getGameMap().getCell(1,1).setDome();

        socketOutContent.reset();
        playerManager.getDivinity().build();
    }
}