package it.polimi.ingsw.PSP18.server.controller.divinities;

import it.polimi.ingsw.PSP18.networking.SocketThread;
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
        Match match = new Match(true);
        SocketThread socketThread = new SocketThread(socket, match);
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
        playerManager.placeWorker(0,0);
        playerManager.placeWorker(0,1);
        playerManager.getDivinity().manageTurn(false);
        Assert.assertEquals("{\"type\":\"WAITING_NICK\"}\r\n" +
                "{\"buildlist1\":[\"RIGHT\",\"RIGHTDOWN\"],\"buildlist2\":[\"RIGHT\",\"RIGHTUP\",\"RIGHTDOWN\",\"DOWN\"],\"type\":\"PROMETHEUS_BUILD_LIST\"}\r\n", socketOutContent.toString());
        playerManager.getDivinity().manageTurn(true);
        Assert.assertEquals("{\"type\":\"WAITING_NICK\"}\r\n" +
                "{\"buildlist1\":[\"RIGHT\",\"RIGHTDOWN\"],\"buildlist2\":[\"RIGHT\",\"RIGHTUP\",\"RIGHTDOWN\",\"DOWN\"],\"type\":\"PROMETHEUS_BUILD_LIST\"}\r\n{\"buildlist1\":[\"RIGHT\",\"RIGHTDOWN\"],\"buildlist2\":[\"RIGHT\",\"RIGHTUP\",\"RIGHTDOWN\",\"DOWN\"],\"type\":\"PROMETHEUS_BUILD_LIST\"}\r\n", socketOutContent.toString());
    }
    @Test
    public void testReceiveWorker(){
        playerManager.getMatch().setCurrentPlayer(playerManager);
        playerManager.placeWorker(0,0);
        playerManager.placeWorker(0,1);
        ((Prometheus)playerManager.getDivinity()).receiveWorker(promMexNull);
        playerManager.getDivinity().move();
        Assert.assertEquals("{\"type\":\"WAITING_NICK\"}\r\n" +
                "{\"moveList1\":[\"RIGHT\",\"RIGHTDOWN\"],\"moveList2\":[\"RIGHT\",\"RIGHTUP\",\"RIGHTDOWN\",\"DOWN\"],\"type\":\"MOVE_LIST\"}\r\n{\"moveList1\":[\"RIGHT\",\"RIGHTDOWN\"],\"moveList2\":[\"RIGHT\",\"RIGHTUP\",\"RIGHTDOWN\",\"DOWN\"],\"type\":\"MOVE_LIST\"}\r\n", socketOutContent.toString());
        ((Prometheus)playerManager.getDivinity()).receiveWorker(promMex);
        playerManager.getDivinity().move();
        Assert.assertEquals("{\"type\":\"WAITING_NICK\"}\r\n" +
                "{\"moveList1\":[\"RIGHT\",\"RIGHTDOWN\"],\"moveList2\":[\"RIGHT\",\"RIGHTUP\",\"RIGHTDOWN\",\"DOWN\"],\"type\":\"MOVE_LIST\"}\r\n{\"moveList1\":[\"RIGHT\",\"RIGHTDOWN\"],\"moveList2\":[\"RIGHT\",\"RIGHTUP\",\"RIGHTDOWN\",\"DOWN\"],\"type\":\"MOVE_LIST\"}\r\n{\"buildlist\":[\"RIGHT\",\"RIGHTDOWN\"],\"type\":\"BUILD_LIST\"}\r\n{\"moveList\":[\"RIGHT\",\"RIGHTDOWN\"],\"workerID\":0,\"optional\":false,\"type\":\"SINGLE_MOVE_LIST\"}\r\n", socketOutContent.toString());
        playerManager.getMatch().getGameMap().setCell(0,1,3,new Worker(0,1, 0,Color.RED));
        playerManager.getMatch().getGameMap().setCell(1,1,3,new Worker(0,1, 0,Color.GREEN));
        playerManager.getMatch().getGameMap().setCell(1,0,3,new Worker(0,1, 0,Color.BLUE));
        playerManager.getDivinity().move();
        Assert.assertEquals("{\"type\":\"WAITING_NICK\"}\r\n" +
                "{\"moveList1\":[\"RIGHT\",\"RIGHTDOWN\"],\"moveList2\":[\"RIGHT\",\"RIGHTUP\",\"RIGHTDOWN\",\"DOWN\"],\"type\":\"MOVE_LIST\"}\r\n{\"moveList1\":[\"RIGHT\",\"RIGHTDOWN\"],\"moveList2\":[\"RIGHT\",\"RIGHTUP\",\"RIGHTDOWN\",\"DOWN\"],\"type\":\"MOVE_LIST\"}\r\n{\"buildlist\":[\"RIGHT\",\"RIGHTDOWN\"],\"type\":\"BUILD_LIST\"}\r\n{\"moveList\":[\"RIGHT\",\"RIGHTDOWN\"],\"workerID\":0,\"optional\":false,\"type\":\"SINGLE_MOVE_LIST\"}\r\n{\"moveList1\":[],\"moveList2\":[\"RIGHTDOWN\",\"DOWN\"],\"type\":\"MOVE_LIST\"}\r\n", socketOutContent.toString());
    }
    @Test
    public void build(){
        playerManager.getMatch().setCurrentPlayer(playerManager);
        playerManager.placeWorker(0,0);
        playerManager.placeWorker(0,1);
        playerManager.getDivinity().build();
        Assert.assertEquals("{\"type\":\"WAITING_NICK\"}\r\n" +
                "{\"buildlist\":[\"RIGHT\",\"RIGHTDOWN\"],\"type\":\"BUILD_LIST\"}\r\n", socketOutContent.toString());
    }
    @Test
    public void buildReceiver(){
        playerManager.getMatch().setCurrentPlayer(playerManager);
        playerManager.placeWorker(0,0);
        playerManager.placeWorker(0,1);
        playerManager.getDivinity().buildReceiver(Direction.DOWN);
        Assert.assertEquals("{\"type\":\"WAITING_NICK\"}\r\n" +
                "{\"type\":\"END_TURN\"}\r\n", socketOutContent.toString());
    }
}
