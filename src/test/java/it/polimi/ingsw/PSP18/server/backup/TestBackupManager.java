package it.polimi.ingsw.PSP18.server.backup;

import it.polimi.ingsw.PSP18.networking.SocketThread;
import it.polimi.ingsw.PSP18.server.controller.*;
import it.polimi.ingsw.PSP18.server.model.Color;
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

public class TestBackupManager {
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
        playerManager = new PlayerManager(matchRun, new PlayerData("bac",Color.RED, 0), "Divinity", matchSocket);
        matchSocket.addPlayer(playerManager, socketThread);
    }

    @Test
    public void testBackupManager(){
        playerManager.placeWorker(1, 2);
        playerManager.placeWorker(1, 3);

        BackupManager backupManager = new BackupManager(matchSocket, matchRun);

        matchRun.setTurnManager(new TurnManager(matchSocket, backupManager, 0));
        matchRun.setFileName("Backups/match_bac.bak");
        backupManager.updateFile();
        Assert.assertEquals("Backups/match_bac.bak", matchRun.getFileName());
        backupManager.backupRestore();
    }
}
