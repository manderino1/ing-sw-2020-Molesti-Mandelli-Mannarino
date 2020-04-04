package it.polimi.ingsw.PSP18.model;

import it.polimi.ingsw.PSP18.controller.PlayerManager;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Match {
    private ArrayList<PlayerManager> playerManagers;
    private ArrayList<Socket> sockets;
    private HashMap<Socket, PlayerManager> hashMap;
    private PlayerManager currentPlayer;
    private MatchStatus matchStatus;

    private void Match(){
        playerManagers = new ArrayList<PlayerManager>();
        sockets = new ArrayList<Socket>();
        hashMap = new HashMap<Socket, PlayerManager>();
    }

    public ArrayList<PlayerManager> getPlayerManagers() {
        return playerManagers;
    }

    public ArrayList<Socket> getSockets() {
        return sockets;
    }

    public MatchStatus getMatchStatus() {
        return matchStatus;
    }

    public void setMatchStatus(MatchStatus matchStatus) {
        this.matchStatus = matchStatus;
    }

    private void addPlayer(PlayerManager player){
        playerManagers.add(player);
    }

    private void addSocket(Socket socket){
        sockets.add(socket);
    }

    private void mapping(PlayerManager player, Socket socket){
        hashMap.put(socket, player);
        addPlayer(player);
        addSocket(socket);
    }
}
