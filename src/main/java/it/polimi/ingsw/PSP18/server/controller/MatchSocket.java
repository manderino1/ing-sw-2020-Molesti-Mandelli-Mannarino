package it.polimi.ingsw.PSP18.server.controller;

import it.polimi.ingsw.PSP18.networking.SocketThread;
import it.polimi.ingsw.PSP18.networking.messages.toclient.DivinityPick;
import it.polimi.ingsw.PSP18.networking.messages.toclient.MatchReady;
import it.polimi.ingsw.PSP18.networking.messages.toclient.WaitingNick;
import it.polimi.ingsw.PSP18.server.model.MatchStatus;
import it.polimi.ingsw.PSP18.server.view.PlayerDataObserver;

import java.util.ArrayList;
import java.util.HashMap;

public class MatchSocket {
    private ArrayList<PlayerManager> playerManagers;
    private ArrayList<SocketThread> sockets;
    private HashMap<PlayerManager, SocketThread> playerSocketMap;
    private HashMap<SocketThread, PlayerManager> socketPlayerMap;
    private PlayerManager currentPlayer;
    private int playerN;
    private Match match;

    /***
     * MatchSocket constructor, initializes the sockets, playerManagers, and the two HasMaps
     */
    public MatchSocket(Match match){
        playerManagers = new ArrayList<>();
        sockets = new ArrayList<>();
        playerSocketMap = new HashMap<>();
        socketPlayerMap = new HashMap<>();
        this.match = match;
    }

    /***
     * MatchSocket constructor with the number of players
     */
    public MatchSocket(Match match, int playerN){
        this(match);
        this.playerN = playerN;
    }

    /***
     * Return the list of the player managers into the match
     * @return the player managers list
     */
    public ArrayList<PlayerManager> getPlayerManagers() {
        return playerManagers;
    }

    /***
     * Return the list of the sockets paired to the connected clients into the match
     * @return the list of sockets
     */
    public ArrayList<SocketThread> getSockets() {
        return sockets;
    }

    /***
     * Add a player to the players list
     * @param player the playermanager player reference
     * @param socket the socket reference
     */
    public void addPlayer(PlayerManager player, SocketThread socket){
        for(PlayerManager playerPresent : playerManagers) {
            if (player.getPlayerData().getPlayerID().equals(playerPresent.getPlayerData().getPlayerID())) {
                socket.sendMessage(new WaitingNick());
                return;
            }
        }

        // Subscribe the current player to all the existing players
        for(PlayerManager exPlayer : playerManagers) {
            exPlayer.getPlayerData().attach(new PlayerDataObserver(socket));
        }

        playerManagers.add(player);
        playerSocketMap.put(player, socket);
        socketPlayerMap.put(socket, player);

        // Subscribe all the existing players to the new player
        for(PlayerManager exPlayer : playerManagers) {
            player.getPlayerData().attach(new PlayerDataObserver(playerSocketMap.get(exPlayer)));
        }

        socket.sendMessage(new MatchReady());
    }

    /***
     * Add a socket to the sockets list and register the observers
     * related to the socket and client connection
     * @param socket the socket reference
     */
    public void addSocket(SocketThread socket){
        if(sockets.size() <= 2 && match.getMatchStatus() == MatchStatus.WAITING_FOR_PLAYERS) {
            sockets.add(socket);
            socket.sendMessage(new WaitingNick());
        }
    }

    /***
     * Get the current playing player
     * @return che current player playing the turn
     */
    public PlayerManager getCurrentPlayer() {
        return currentPlayer;
    }

    /***
     * Set the new current player at the turn change
     * @param currentPlayer the new currently playing player
     */
    public void setCurrentPlayer(PlayerManager currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    /***
     * Returns the current player socket
     * @return the current player socket
     */
    public SocketThread getCurrentSocket() {
        return playerSocketMap.get(currentPlayer);
    }

    /***
     * Detaches the observers to a player
     * @param socket the player that needs his observers detached
     */
    public void detachSocket(SocketThread socket) {
        for(PlayerManager player : playerManagers) {
            player.getPlayerData().detachSocket(socket);
        }
        match.getMatchRun().getGameMap().detachSocket(socket);
    }

    public HashMap<PlayerManager, SocketThread> getPlayerSocketMap() {
        return playerSocketMap;
    }

    public HashMap<SocketThread, PlayerManager> getSocketPlayerMap() {
        return socketPlayerMap;
    }
}
