package it.polimi.ingsw.PSP18.server.model;

import it.polimi.ingsw.PSP18.networking.SocketThread;
import it.polimi.ingsw.PSP18.server.controller.Match;
import it.polimi.ingsw.PSP18.server.controller.PlayerManager;
import it.polimi.ingsw.PSP18.server.controller.TurnManager;
import org.junit.Assert;
import org.junit.Test;

import java.net.Socket;

public class TestMatch {

    @Test
    public void TestMatchCreationGettersSetters() {
        Match match = new Match();

        match.setMatchStatus(MatchStatus.MATCH_STARTED);
        Assert.assertEquals(match.getMatchStatus(), MatchStatus.MATCH_STARTED);
        match.getGameMap().setCell(1,1,0,null);
        Assert.assertEquals(match.getGameMap().getCell(1,1).getWorker(), null);

        PlayerData playerData = new PlayerData("cipolla", Color.RED, 0);
        PlayerManager playerManager = new PlayerManager(match, playerData, "Apollo");
        match.addPlayer(playerManager, new SocketThread(new Socket(), match));
        Assert.assertEquals(match.getPlayerManagers().get(0), playerManager);

        match.setCurrentPlayer(playerManager);
        Assert.assertEquals(match.getCurrentPlayer(), playerManager);

        TurnManager turnManager = match.getTurnManager();
    }
}
