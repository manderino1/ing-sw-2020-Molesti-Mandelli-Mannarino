package it.polimi.ingsw.PSP18.server.backup;

import it.polimi.ingsw.PSP18.server.controller.MatchRun;
import it.polimi.ingsw.PSP18.server.controller.MatchSocket;
import it.polimi.ingsw.PSP18.server.controller.PlayerManager;
import it.polimi.ingsw.PSP18.server.controller.divinities.Divinity;
import it.polimi.ingsw.PSP18.server.model.Color;
import it.polimi.ingsw.PSP18.server.model.PlayerData;
import it.polimi.ingsw.PSP18.server.model.Worker;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestPlayerManagerBackup {

    private Worker worker1 = new Worker(0,0,0, Color.RED);
    private Worker worker2 = new Worker(0,1,1,Color.RED);
    private MatchSocket matchSocket = new MatchSocket(3);
    private MatchRun matchRun = new MatchRun(matchSocket);
    private PlayerData playerData = new PlayerData("bilbo", Color.RED, 1);
    private Divinity divinity = new Divinity("Apollo", new PlayerManager(matchRun,playerData,"Apollo",
            matchSocket ),matchSocket, matchRun);
    private PlayerManagerBackup playerManagerBackup;
    private PlayerDataBackup playerDataBackup;

    @Before
    public void testSetup(){
        this.playerManagerBackup= new PlayerManagerBackup(worker1, worker2, playerData, divinity);
    }
    @Test
    public void testGetPlayerData(){
        playerDataBackup = playerManagerBackup.getPlayerData();
        PlayerDataBackup test= new PlayerDataBackup(playerData);
        Assert.assertEquals(test.getPlayerID(), playerDataBackup.getPlayerID());
        Assert.assertEquals(test.getPlayerColor(), playerDataBackup.getPlayerColor());
    }

    @Test
    public void testGetWorkers(){
        Worker[] workers = playerManagerBackup.getWorkers();
        Worker[] test = new Worker[2];
        test[0] = worker1;
        test[1] = worker2;
        Assert.assertEquals(workers[0], test[0]);
        Assert.assertEquals(workers[1], test[1]);
    }
}
