package it.polimi.ingsw.PSP18.server.controller.divinities;

import com.google.gson.Gson;
import it.polimi.ingsw.PSP18.networking.SocketThread;
import it.polimi.ingsw.PSP18.networking.messages.toclient.BuildList;
import it.polimi.ingsw.PSP18.networking.messages.toclient.MoveList;
import it.polimi.ingsw.PSP18.networking.messages.toclient.PrometheusBuildList;
import it.polimi.ingsw.PSP18.networking.messages.toserver.MoveReceiver;
import it.polimi.ingsw.PSP18.networking.messages.toserver.PrometheusBuildReceiver;
import it.polimi.ingsw.PSP18.server.controller.PlayerManager;
import it.polimi.ingsw.PSP18.server.model.*;
import it.polimi.ingsw.PSP18.server.controller.Match;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class TestPrometheus extends TestDivinity {

    private PrometheusBuildReceiver promMexNull = new PrometheusBuildReceiver(null);
    private PrometheusBuildReceiver promMex = new PrometheusBuildReceiver(0);

    @Override
    public void createPlayerManager() {
        Match match = new Match();
        SocketThread socketThread = new SocketThread(socket, null);
        socketThread.setMatch(match);
        socketThread.start();
        playerManager = new PlayerManager(match, new PlayerData("Test1", Color.RED, 0), "Prometheus");
        match.addPlayer(playerManager, socketThread);
    }

    /***
     * Testing the GetName method
     */
    @Override
    public void testGetName() {
        Divinity divinity = new Divinity("Prometheus", playerManager);
        Assert.assertEquals(playerManager.getDivinityName(), divinity.getName());
    }

    @Test
    public void testManageTurn() {
        playerManager.getMatch().setCurrentPlayer(playerManager);
        playerManager.placeWorker(0, 0);
        playerManager.placeWorker(0, 1);
        socketOutContent.reset();
        playerManager.getDivinity().manageTurn(false);
        Gson gson = new Gson();
        PrometheusBuildList prometheusBuildList = gson.fromJson(socketOutContent.toString(), PrometheusBuildList.class);
        ArrayList<Direction> list = new ArrayList<Direction>();
        list.add(Direction.RIGHT);
        list.add(Direction.RIGHTDOWN);
        Assert.assertEquals(list, prometheusBuildList.getBuildlist1());
        socketOutContent.reset();
        playerManager.getDivinity().manageTurn(false);
        PrometheusBuildList prometheusBuildList1 = gson.fromJson(socketOutContent.toString(), PrometheusBuildList.class);
        ArrayList<Direction> list1 = new ArrayList<Direction>();
        list1.add(Direction.RIGHT);
        list1.add(Direction.RIGHTDOWN);
        Assert.assertEquals(list1, prometheusBuildList1.getBuildlist1());
    }
    @Test
    public void testReceiveWorker(){
        playerManager.getMatch().setCurrentPlayer(playerManager);
        playerManager.placeWorker(0,0);
        playerManager.placeWorker(0,1);
        socketOutContent.reset();
        ((Prometheus)playerManager.getDivinity()).receiveWorker(promMexNull);
        Gson gson = new Gson();
        MoveList moveList = gson.fromJson(socketOutContent.toString(), MoveList.class);
        ArrayList<Direction> list = new ArrayList<Direction>();
        list.add(Direction.RIGHT);
        list.add(Direction.RIGHTDOWN);
        Assert.assertEquals(list, moveList.getMoveList1());
        socketOutContent.reset();
        ((Prometheus)playerManager.getDivinity()).receiveWorker(promMex);

        gson = new Gson();
        BuildList buildList = gson.fromJson(socketOutContent.toString(), BuildList.class);
        Assert.assertEquals(playerManager.getDivinity().checkBuildingMoves(0,0), buildList.getBuildlist());


    }
    @Test
    public void build(){
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
        Assert.assertEquals(Integer.valueOf(1), playerManager.getMatch().getMatchRun().getGameMap().getCell(0,1).getBuilding());

        playerManager.getMatch().getMatchRun().getGameMap().getCell(1,0).setBuilding(3);
        playerManager.getMatch().getMatchRun().getGameMap().getCell(0,1).setBuilding(2);
        playerManager.getMatch().getMatchRun().getGameMap().getCell(1,1).setBuilding(3);
    }
    @Test
    public void buildReceiver(){
        playerManager.getMatch().setCurrentPlayer(playerManager);
        playerManager.placeWorker(0,0);
        playerManager.placeWorker(0,2);
        socketOutContent.reset();
        playerManager.getDivinity().build();
        playerManager.getDivinity().buildReceiver(Direction.DOWN);
    }
}
