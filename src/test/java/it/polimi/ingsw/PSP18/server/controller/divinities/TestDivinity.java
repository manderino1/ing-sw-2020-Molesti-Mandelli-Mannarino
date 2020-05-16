package it.polimi.ingsw.PSP18.server.controller.divinities;

import com.google.gson.Gson;
import it.polimi.ingsw.PSP18.networking.SocketThread;
import it.polimi.ingsw.PSP18.networking.messages.toclient.BuildList;
import it.polimi.ingsw.PSP18.server.controller.PlayerManager;
import it.polimi.ingsw.PSP18.server.model.Color;
import it.polimi.ingsw.PSP18.server.model.Direction;
import it.polimi.ingsw.PSP18.server.model.GameMap;
import it.polimi.ingsw.PSP18.server.controller.Match;
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

        createPlayerManager();
    }

    public void createPlayerManager() {
        Match match = new Match();
        SocketThread socketThread = new SocketThread(socket, null);
        socketThread.setMatch(match);
        socketThread.start();
        playerManager = new PlayerManager(match, new PlayerData("Test1",Color.RED, 0), "Divinity");
        match.addPlayer(playerManager, socketThread);
    }

    /***
     * Testing the GetName method
     */
    @Test
    public void testGetName() {
        Divinity divinity = new Divinity("Divinity", playerManager);
        Assert.assertEquals(playerManager.getDivinityName(), divinity.getName());
    }

    /***
     * Testing ManageTurn that will lso test all the other Divinty methods that ManageTurn calls
     */
    @Test
    public void testManageTurn() {
        playerManager.getMatch().setCurrentPlayer(playerManager);

        playerManager.placeWorker(2,1);
        playerManager.placeWorker(3,2);
        playerManager.manageTurn(false);
        playerManager.manageTurn(true);
        /*
        TODO: In order to have a more complete test we need to generate some buildings and some workers
         */
        //TODO: still gives nullpointerex with getWorker method
    }

    @Test
    public void testMoveReceiver() {
        playerManager.getMatch().getGameMap().getCell(2,3).setBuilding(1);
        playerManager.getMatch().getGameMap().getCell(2,2).setBuilding(2);
        playerManager.getMatch().getGameMap().getCell(2,1).setBuilding(3);


        playerManager.getMatch().setCurrentPlayer(playerManager);
        playerManager.placeWorker(2, 4);
        playerManager.placeWorker(3, 2);


        playerManager.getDivinity().move();
        playerManager.getDivinity().moveReceiver(Direction.UP, 0);
        Assert.assertEquals(playerManager.getWorker(0), playerManager.getMatch().getGameMap().getCell(2,3).getWorker());

        playerManager.getDivinity().move();
        playerManager.getDivinity().moveReceiver(Direction.UP, 0);
        Assert.assertEquals(playerManager.getWorker(0), playerManager.getMatch().getGameMap().getCell(2,2).getWorker());

        playerManager.getDivinity().moveReceiver(Direction.UP, 0);
    }

    @Test
    public void testBuild() {
        playerManager.getMatch().setCurrentPlayer(playerManager);
        playerManager.placeWorker(0,0);
        playerManager.placeWorker(2,1);

        socketOutContent.reset();
        playerManager.getDivinity().build();
        Gson gson = new Gson();
        BuildList buildList = gson.fromJson(socketOutContent.toString(), BuildList.class);
        Assert.assertEquals(playerManager.getDivinity().checkBuildingMoves(0,0), buildList.getBuildlist());

        socketOutContent.reset();
        playerManager.getDivinity().buildReceiver(Direction.DOWN);
        Assert.assertEquals(Integer.valueOf(1), playerManager.getMatch().getGameMap().getCell(0,1).getBuilding());

        playerManager.getMatch().getGameMap().getCell(1,0).setBuilding(3);
        playerManager.getMatch().getGameMap().getCell(0,1).setBuilding(2);
        playerManager.getMatch().getGameMap().getCell(1,1).setBuilding(3);
    }

    @Test
    public void testManageLoss () {
        playerManager.getMatch().setCurrentPlayer(playerManager);
        playerManager.placeWorker(0,0);
        playerManager.placeWorker(2,1);

        playerManager.getMatch().getGameMap().getCell(1,0).setDome();
        playerManager.getMatch().getGameMap().getCell(0,1).setDome();
        playerManager.getMatch().getGameMap().getCell(1,1).setDome();

        socketOutContent.reset();
        playerManager.getDivinity().build();
    }
}