package it.polimi.ingsw.PSP18.server.controller;

import com.google.gson.Gson;
import it.polimi.ingsw.PSP18.networking.SocketThread;
import it.polimi.ingsw.PSP18.networking.messages.toclient.StartMatch;
import it.polimi.ingsw.PSP18.server.backup.PlayerManagerBackup;
import it.polimi.ingsw.PSP18.server.model.Color;
import it.polimi.ingsw.PSP18.server.model.GameMap;
import it.polimi.ingsw.PSP18.server.model.MatchStatus;
import it.polimi.ingsw.PSP18.server.model.PlayerData;
import it.polimi.ingsw.PSP18.server.view.MapObserver;
import it.polimi.ingsw.PSP18.server.view.PlayerDataObserver;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class BackupManager {
    private MatchSocket matchSocket;
    private MatchRun matchRun;

    public BackupManager(MatchSocket matchSocket, MatchRun matchRun){
        this.matchSocket = matchSocket;
        this.matchRun = matchRun;
    }

    /***
     * Every end of turn update the match backup
     */
    public void updateFile() {
        try {
            File directory = new File("Backups");
            if (! directory.exists()){
                directory.mkdir();
            }
            if(matchRun.getFileName() != null) {
                FileWriter myWriter = new FileWriter(matchRun.getFileName(), false);
                Gson gson = new Gson();
                myWriter.write(gson.toJson(new it.polimi.ingsw.PSP18.server.backup.MatchBackup(matchSocket.getPlayerManagers(), matchRun.getTurnManager().getIndexCurrentPlayer(), matchSocket.getMatchStatus(), matchRun.getGameMap().getMapCells())));
                myWriter.flush();
                myWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /***
     * Check for an existent backup
     * @return true if there is a backup in memory
     */
    public static boolean backupCheck(ArrayList<PlayerManager> players) {
        try {
            String fileName = "Backups/match_";
            ArrayList<String> names = new ArrayList<>();
            for(PlayerManager player : players) {
                names.add(player.getPlayerData().getPlayerID());
            }
            names.sort(String::compareToIgnoreCase);
            for(String name: names) {
                fileName = fileName.concat(name);
            }
            fileName = fileName.concat(".bak");
            new FileReader(fileName);
        } catch (FileNotFoundException e) {
            return false;
        }
        return true;
    }

    public void backupRestore() {
        try {
            String fileName = "Backups/match_";
            ArrayList<String> names = new ArrayList<>();
            for(PlayerManager player : matchSocket.getPlayerManagers()) {
                names.add(player.getPlayerData().getPlayerID());
            }
            names.sort(String::compareToIgnoreCase);
            for(String name: names) {
                fileName = fileName.concat(name);
            }
            matchRun.setFileName(fileName.concat(".bak"));
            FileReader fileReader = new FileReader(this.matchRun.getFileName());
            Gson gson = new Gson();
            it.polimi.ingsw.PSP18.server.backup.MatchBackup matchBackup = gson.fromJson(fileReader, it.polimi.ingsw.PSP18.server.backup.MatchBackup.class);
            boolean athena = false;
            // Match backupped with same nicknames, restore it
            for (PlayerManagerBackup playerBackupped : matchBackup.getPlayerManagers()) {
                for (PlayerManager playerConnected : matchSocket.getPlayerManagers()) {
                    if (playerConnected.getPlayerData().getPlayerID().equals(playerBackupped.getPlayerData().getPlayerID())) {
                        if(playerBackupped.getPlayerData().getDivinity().equals("Athena")) {
                            athena = true;
                        }
                        PlayerData playerData = new PlayerData(playerBackupped.getPlayerData().getPlayerID(), playerBackupped.getPlayerData().getPlayerColor(), playerBackupped.getPlayerData().getPlayOrder());
                        if(playerBackupped.getPlayerData().getReady()) {
                            playerData.setReady();
                        }
                        playerData.setDivinity(playerBackupped.getPlayerData().getDivinity());
                        if(playerBackupped.getPlayerData().getHasLost()) {
                            playerData.setLost();
                        }
                        playerData.setLastMove(playerBackupped.getPlayerData().getLastMove());
                        PlayerManager playerManager = new PlayerManager(matchRun, playerData, playerBackupped.getPlayerData().getDivinity());
                        matchSocket.getPlayerManagers().add(playerManager);
                        SocketThread socket = matchSocket.getPlayerSocketMap().get(playerConnected);
                        matchSocket.getPlayerSocketMap().remove(playerConnected);
                        matchSocket.getPlayerSocketMap().put(playerManager, socket);
                        matchSocket.getSocketPlayerMap().remove(socket);
                        matchSocket.getSocketPlayerMap().put(matchSocket.getPlayerSocketMap().get(playerConnected), playerManager);
                        matchSocket.getPlayerManagers().remove(playerConnected);
                        break;
                    }
                }
            }
            matchSocket.setMatchStatus(matchBackup.getMatchStatus());
            matchRun.getGameMap().setMapCells(matchBackup.getGameMap());

            // Find the workers and insert them
            HashMap<Color, PlayerManager> hashColor = new HashMap<>();
            for(PlayerManager player : matchSocket.getPlayerManagers()) {
                hashColor.put(player.getPlayerData().getPlayerColor(), player);
            }
            for(int i=0; i<=4; i++) {
                for(int j=0; j<=4; j++) {
                    if(matchRun.getGameMap().getCell(i,j).getWorker() != null) {
                        hashColor.get(matchRun.getGameMap().getCell(i,j).getWorker().getPlayerColor()).setWorkers(matchRun.getGameMap().getCell(i,j).getWorker(), matchRun.getGameMap().getCell(i,j).getWorker().getID());
                    }
                }
            }

            // Reset the observers
            for(SocketThread sock : matchSocket.getSockets()) {
                for(PlayerManager player : matchSocket.getPlayerManagers()) {
                    player.getPlayerData().attach(new PlayerDataObserver(sock));
                }
                matchRun.getGameMap().attach(new MapObserver(sock));
                sock.sendMessage(new StartMatch());
            }

            if(athena) {
                matchRun.setTurnManager( new TurnManagerAthena(matchSocket, this, matchBackup.getIndexCurrentPlayer()));
            } else {
                matchRun.setTurnManager( new TurnManager(matchSocket, this, matchBackup.getIndexCurrentPlayer()));
            }
            try {
                fileReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
