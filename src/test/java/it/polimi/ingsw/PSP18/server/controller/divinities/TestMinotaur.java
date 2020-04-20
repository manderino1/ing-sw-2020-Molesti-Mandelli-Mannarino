package it.polimi.ingsw.PSP18.server.controller.divinities;

import it.polimi.ingsw.PSP18.networking.SocketThread;
import it.polimi.ingsw.PSP18.server.controller.PlayerManager;
import it.polimi.ingsw.PSP18.server.model.Color;
import it.polimi.ingsw.PSP18.server.model.GameMap;
import it.polimi.ingsw.PSP18.server.controller.Match;
import it.polimi.ingsw.PSP18.server.model.PlayerData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestMinotaur extends TestDivinity {
    @Override
    public void createPlayerManager() {
        Match match = new Match(true);
        SocketThread socketThread = new SocketThread(socket, match);
        socketThread.start();
        playerManager = new PlayerManager(match, new PlayerData("Test1", Color.RED, 0), "Minotaur");
        match.addPlayer(playerManager, socketThread);
    }

    /***
     * Testing the GetName method
     */
    @Override
    public void testGetName() {
        Divinity divinity = new Divinity("Minotaur", playerManager);
        Assert.assertEquals(playerManager.getDivinityName(), divinity.getName());
    }
}
