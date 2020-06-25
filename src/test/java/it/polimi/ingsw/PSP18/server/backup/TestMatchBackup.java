package it.polimi.ingsw.PSP18.server.backup;


import it.polimi.ingsw.PSP18.server.controller.MatchRun;
import it.polimi.ingsw.PSP18.server.controller.MatchSocket;
import it.polimi.ingsw.PSP18.server.controller.PlayerManager;
import it.polimi.ingsw.PSP18.server.controller.divinities.Divinity;
import it.polimi.ingsw.PSP18.server.model.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class TestMatchBackup {
    private ArrayList<PlayerManagerBackup> playerManagers;
    private ArrayList<PlayerManager> playerManager;
    private int indexCurrentPlayer;
    private MatchStatus matchStatus;
    private Cell[][] gameMap;
    private MatchRun matchRun;
    private MatchSocket matchSocket;

    @Before
    public void setting() {
        playerManager = new ArrayList<PlayerManager>();
        playerManagers = new ArrayList<PlayerManagerBackup>();
        indexCurrentPlayer = 0;
        matchStatus = MatchStatus.MATCH_STARTED;
        gameMap = new Cell[5][5];
        matchSocket = new MatchSocket(2);
        matchRun = new MatchRun(matchSocket);
    }

    @Test
    public void testMatchBackup(){
        PlayerData playerData = new PlayerData("A", Color.RED, 0);
        playerManager.add(new PlayerManager(playerData));
        playerManagers.add(new PlayerManagerBackup(new Worker(1, 1, 0, Color.RED),
                new Worker(1, 0, 1, Color.RED),
                playerData, new Divinity("Apollo", playerManager.get(0), matchSocket, matchRun)));

        MatchBackup matchBackup = new MatchBackup(playerManager, indexCurrentPlayer, matchStatus, gameMap);
        Assert.assertEquals(matchBackup.getIndexCurrentPlayer(), indexCurrentPlayer);
        Assert.assertEquals(matchBackup.getMatchStatus(), matchStatus);
        Assert.assertArrayEquals(matchBackup.getGameMap(), gameMap);
        Assert.assertEquals(matchBackup.getPlayerManagers().get(0).getPlayerData().getPlayerID(),
                playerManagers.get(0).getPlayerData().getPlayerID());
        Assert.assertEquals(matchBackup.getPlayerManagers().get(0).getPlayerData().getDivinity(),
                playerManagers.get(0).getPlayerData().getDivinity());
        Assert.assertEquals(matchBackup.getPlayerManagers().get(0).getPlayerData().getHasLost(),
                playerManagers.get(0).getPlayerData().getHasLost());
        Assert.assertEquals(matchBackup.getPlayerManagers().get(0).getPlayerData().getLastMove(),
                playerManagers.get(0).getPlayerData().getLastMove());
        Assert.assertEquals(matchBackup.getPlayerManagers().get(0).getPlayerData().getPlayOrder(),
                playerManagers.get(0).getPlayerData().getPlayOrder());
        Assert.assertEquals(matchBackup.getPlayerManagers().get(0).getPlayerData().getReady(),
                playerManagers.get(0).getPlayerData().getReady());
        Assert.assertEquals(matchBackup.getPlayerManagers().get(0).getPlayerData().getPlayerColor(),
                playerManagers.get(0).getPlayerData().getPlayerColor());
    }
}
