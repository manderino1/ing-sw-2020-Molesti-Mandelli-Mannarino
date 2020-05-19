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


    private String fileName;
    private Match match;

    public BackupManager(Match match){
        this.match = match;
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
            FileWriter myWriter = new FileWriter(fileName, false);
            Gson gson = new Gson();
            myWriter.write(gson.toJson(new it.polimi.ingsw.PSP18.server.backup.MatchBackup(match.getMatchSocket().getPlayerManagers(), match.getMatchRun().getTurnManager().getIndexCurrentPlayer(), match.getMatchStatus(), match.getMatchRun().getGameMap().getMapCells())));
            myWriter.flush();
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /***
     * Check for an existent backup
     * @return true if there is a backup in memory
     */
    public boolean backupCheck() {
        try {
            String fileName = "Backups/match_";
            ArrayList<String> names = new ArrayList<>();
            for(PlayerManager player : match.getMatchSocket().getPlayerManagers()) {
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
            for(PlayerManager player : match.getMatchSocket().getPlayerManagers()) {
                names.add(player.getPlayerData().getPlayerID());
            }
            names.sort(String::compareToIgnoreCase);
            for(String name: names) {
                fileName = fileName.concat(name);
            }
            this.fileName = fileName.concat(".bak");
            FileReader fileReader = new FileReader(this.fileName);
            Gson gson = new Gson();
            it.polimi.ingsw.PSP18.server.backup.MatchBackup matchBackup = gson.fromJson(fileReader, it.polimi.ingsw.PSP18.server.backup.MatchBackup.class);
            boolean athena = false;
            // Match backupped with same nicknames, restore it
            for (PlayerManagerBackup playerBackupped : matchBackup.getPlayerManagers()) {
                for (PlayerManager playerConnected : match.getMatchSocket().getPlayerManagers()) {
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
                        PlayerManager playerManager = new PlayerManager(match, playerData, playerBackupped.getPlayerData().getDivinity());
                        match.getMatchSocket().getPlayerManagers().add(playerManager);
                        SocketThread socket = match.getMatchSocket().getPlayerSocketMap().get(playerConnected);
                        match.getMatchSocket().getPlayerSocketMap().remove(playerConnected);
                        match.getMatchSocket().getPlayerSocketMap().put(playerManager, socket);
                        match.getMatchSocket().getSocketPlayerMap().remove(socket);
                        match.getMatchSocket().getSocketPlayerMap().put(match.getMatchSocket().getSocketPlayerMap().get(playerConnected), playerManager);
                        match.getMatchSocket().getPlayerManagers().remove(playerConnected);
                        break;
                    }
                }
            }
            match.setMatchStatus(matchBackup.getMatchStatus());
            match.getMatchRun().getGameMap().setMapCells(matchBackup.getGameMap());

            // Find the workers and insert them
            HashMap<Color, PlayerManager> hashColor = new HashMap<>();
            for(PlayerManager player : match.getMatchSocket().getPlayerManagers()) {
                hashColor.put(player.getPlayerData().getPlayerColor(), player);
            }
            for(int i=0; i<=4; i++) {
                for(int j=0; j<=4; j++) {
                    if(match.getMatchRun().getGameMap().getCell(i,j).getWorker() != null) {
                        hashColor.get(match.getMatchRun().getGameMap().getCell(i,j).getWorker().getPlayerColor()).setWorkers(match.getMatchRun().getGameMap().getCell(i,j).getWorker(), match.getMatchRun().getGameMap().getCell(i,j).getWorker().getID());
                    }
                }
            }

            // Reset the observers
            for(SocketThread sock : match.getMatchSocket().getSockets()) {
                for(PlayerManager player : match.getMatchSocket().getPlayerManagers()) {
                    player.getPlayerData().attach(new PlayerDataObserver(sock));
                }
                match.getMatchRun().getGameMap().attach(new MapObserver(sock));
                sock.sendMessage(new StartMatch());
            }

            if(athena) {
                match.getMatchRun().setTurnManager( new TurnManagerAthena(match, matchBackup.getIndexCurrentPlayer()));
            } else {
                match.getMatchRun().setTurnManager( new TurnManager(match, matchBackup.getIndexCurrentPlayer()));
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

    public void detachSocket(SocketThread socket) {
        for(PlayerManager player : match.getMatchSocket().getPlayerManagers()) {
            player.getPlayerData().detachSocket(socket);
        }
        match.getMatchRun().getGameMap().detachSocket(socket);
    }
    public void setFileName(String fileName){
        this.fileName = fileName;
    }
    public String getFileName(){
        return fileName;
    }
}
