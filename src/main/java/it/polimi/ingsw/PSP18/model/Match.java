package it.polimi.ingsw.PSP18.model;

import it.polimi.ingsw.PSP18.controller.PlayerManager;
import it.polimi.ingsw.PSP18.networking.SocketServer;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Match {
    private ArrayList<PlayerManager> playerManagers;
    private ArrayList<Socket> sockets;
    private SocketServer socketServer;
    private HashMap<Socket, PlayerManager> hashMap;
    private PlayerManager currentPlayer;
    private MatchStatus matchStatus;
    private GameMap gameMap;

    public Match(){
        playerManagers = new ArrayList<PlayerManager>();
        sockets = new ArrayList<Socket>();
        hashMap = new HashMap<Socket, PlayerManager>();
        socketServer = new SocketServer(this);
        socketServer.start(); // Wait for connections
        gameMap = new GameMap();
    }

    public GameMap getGameMap() {
        return gameMap;
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

    public void setSocketServer(SocketServer socketServer) {
        this.socketServer = socketServer;
    }

    /***
     * Add a player to the players list
     * @param player the playermanager player reference
     */
    public void addPlayer(PlayerManager player){
        playerManagers.add(player);
    }

    /***
     * Add a socket to the sockets list
     * @param socket the socket reference
     */
    public void addSocket(Socket socket){
        sockets.add(socket);
    }

    public PlayerManager getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(PlayerManager currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    /***
     * Map a socket to its player
     * @param player the playermanager reference
     * @param socket the socket related to the player reference
     */
    private void mapping(PlayerManager player, Socket socket){
        hashMap.put(socket, player);
        addPlayer(player);
        addSocket(socket);
    }
}
