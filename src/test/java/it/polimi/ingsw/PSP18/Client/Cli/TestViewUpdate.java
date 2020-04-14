package it.polimi.ingsw.PSP18.Client.Cli;

import it.polimi.ingsw.PSP18.client.view.cli.CliViewUpdate;
import it.polimi.ingsw.PSP18.networking.messages.toclient.GameMapUpdate;
import it.polimi.ingsw.PSP18.networking.messages.toclient.PlayerDataUpdate;
import it.polimi.ingsw.PSP18.server.controller.PlayerManager;
import it.polimi.ingsw.PSP18.server.model.*;
import it.polimi.ingsw.PSP18.server.view.MapObserver;
import org.junit.Test;

public class TestViewUpdate {

    @Test
    public void TestUpdateMap(){
        Match match = new Match();
        PlayerData playerData1 = new PlayerData("ottavio", Color.RED, 0);
        PlayerData playerData2 = new PlayerData("marco", Color.GREEN, 1);
        PlayerData playerData3 = new PlayerData("mole", Color.BLUE, 2);

        PlayerManager playerManager1 = new PlayerManager(match, playerData1);
        PlayerManager playerManager2 = new PlayerManager(match, playerData2);
        PlayerManager playerManager3 = new PlayerManager(match, playerData3);

        CliViewUpdate cliViewUpdate = new CliViewUpdate();

        match.addPlayer(playerManager1);
        playerManager1.placeWorker(2,1);
        match.addPlayer(playerManager2);
        playerManager2.placeWorker(3,2);
        match.addPlayer(playerManager3);
        playerManager3.placeWorker(4,1);
        playerManager3.placeWorker(3,1);

        GameMapUpdate gameMapUpdate = new GameMapUpdate(match.getGameMap().getMapCells());
        cliViewUpdate.updateMap(gameMapUpdate);
    }

    @Test
    public void TestUpdatePlayerData(){
        PlayerData playerData1 = new PlayerData("ottavio", Color.RED, 0);
        PlayerData playerData2 = new PlayerData("marco", Color.GREEN, 1);
        PlayerData playerData3 = new PlayerData("mole", Color.BLUE, 2);
        playerData1.setDivinity("Apollo");

        PlayerDataUpdate playerDataUpdate1 = new PlayerDataUpdate(playerData1);
        PlayerDataUpdate playerDataUpdate2 = new PlayerDataUpdate(playerData2);
        PlayerDataUpdate playerDataUpdate3 = new PlayerDataUpdate(playerData3);


        CliViewUpdate cliViewUpdate = new CliViewUpdate();
        cliViewUpdate.updatePlayerData(playerDataUpdate1);
        cliViewUpdate.updatePlayerData(playerDataUpdate2);
        cliViewUpdate.updatePlayerData(playerDataUpdate3);
    }
}
