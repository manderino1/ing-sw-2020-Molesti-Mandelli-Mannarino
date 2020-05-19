package it.polimi.ingsw.PSP18.server.controller;

import com.google.gson.Gson;
import it.polimi.ingsw.PSP18.networking.messages.toclient.*;
import it.polimi.ingsw.PSP18.networking.messages.toserver.WorkerReceiver;
import it.polimi.ingsw.PSP18.server.backup.MatchBackup;
import it.polimi.ingsw.PSP18.networking.SocketThread;
import it.polimi.ingsw.PSP18.server.backup.PlayerManagerBackup;
import it.polimi.ingsw.PSP18.server.model.Color;
import it.polimi.ingsw.PSP18.server.model.GameMap;
import it.polimi.ingsw.PSP18.server.model.MatchStatus;
import it.polimi.ingsw.PSP18.server.model.PlayerData;
import it.polimi.ingsw.PSP18.server.view.MapObserver;
import it.polimi.ingsw.PSP18.server.view.PlayerDataObserver;

import java.io.*;
import java.util.*;

/***
 * class that deals with the information of the current match
 */
public class Match {
    private MatchStatus matchStatus;
    private int playerN;
    private MatchSocket matchSocket;
    private BackupManager backupManager;
    private MatchRun matchRun;
    private MatchSetUp matchSetUp;

    /***
     * Match constructor, initializes the arrayLists and the game map
     */
    public Match(){
        matchStatus = MatchStatus.WAITING_FOR_PLAYERS;
        MatchSocket matchSocket = new MatchSocket(this);
        BackupManager backupManager = new BackupManager(this);
        MatchSetUp matchSetUp = new MatchSetUp(this);
        MatchRun matchRun = new MatchRun(this);
    }

    public Match(int playerN){
        this();
        this.playerN = playerN;
    }

    /***
     * Return the state of the match, there are some pre-defined states as enum in MatchStatus class
     * @return the match state
     */
    public MatchStatus getMatchStatus() {
        return matchStatus;
    }

    /***
     * Set the state of the match in pre-defined states as described in MatchStatus class
     * @param matchStatus the new state of the match
     */
    public void setMatchStatus(MatchStatus matchStatus) {
        this.matchStatus = matchStatus;
    }

    public BackupManager getBackupManager(){
        return backupManager;
    }

    public MatchSocket getMatchSocket(){
        return matchSocket;
    }

    public MatchRun getMatchRun() {
        return matchRun;
    }

    public MatchSetUp getMatchSetUp() {
        return matchSetUp;
    }
}
