package it.polimi.ingsw.PSP18.server.controller.divinities;

import com.google.gson.Gson;
import it.polimi.ingsw.PSP18.networking.SocketThread;
import it.polimi.ingsw.PSP18.networking.messages.toclient.BuildList;
import it.polimi.ingsw.PSP18.server.controller.PlayerManager;
import it.polimi.ingsw.PSP18.server.model.Color;
import it.polimi.ingsw.PSP18.server.model.Direction;
import it.polimi.ingsw.PSP18.server.model.PlayerData;
import org.junit.Assert;
import org.junit.Test;

public class TestAtlas extends TestDivinity {
    @Override
    public void createPlayerManager() {
        MatchSocket matchSocket = new MatchSocket(2);;
        SocketThread socketThread = new SocketThread(socket, null, true);
        socketThread.start();
        playerManager = new PlayerManager(matchRun, new PlayerData("Test1", Color.RED, 0), "Atlas");
        matchSocket.addPlayer(playerManager, socketThread);
    }

    /***
     * Testing the GetName method
     */
    @Override
    public void testGetName() {
        Divinity divinity = new Divinity("Atlas", playerManager);
        Assert.assertEquals(playerManager.getDivinityName(), divinity.getName());
    }

    @Test
    public void testBuild() {
        playerManager.getMatch().getMatchSocket().setCurrentPlayer(playerManager);
        playerManager.placeWorker(0,0);
        playerManager.placeWorker(2,1);

        socketOutContent.reset();
        playerManager.getDivinity().build();
        Gson gson = new Gson();
        BuildList buildList = gson.fromJson(socketOutContent.toString(), BuildList.class);
        Assert.assertEquals(playerManager.getDivinity().checkBuildingMoves(0,0), buildList.getBuildlist());

        socketOutContent.reset();
        ((Atlas) playerManager.getDivinity()).buildReceiver(Direction.DOWN, false);
        Assert.assertEquals(Integer.valueOf(1), playerManager.getMatch().getMatchRun().getGameMap().getCell(0,1).getBuilding());
    }
}
