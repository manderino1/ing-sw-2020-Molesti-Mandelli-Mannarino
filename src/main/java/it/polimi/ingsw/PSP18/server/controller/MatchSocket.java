package it.polimi.ingsw.PSP18.server.controller;

import it.polimi.ingsw.PSP18.networking.SocketThread;
import it.polimi.ingsw.PSP18.networking.messages.toclient.DivinityPick;
import it.polimi.ingsw.PSP18.networking.messages.toclient.MatchReady;
import it.polimi.ingsw.PSP18.networking.messages.toclient.WaitingNick;
import it.polimi.ingsw.PSP18.server.model.GameMap;
import it.polimi.ingsw.PSP18.server.model.MatchStatus;
import it.polimi.ingsw.PSP18.server.view.PlayerDataObserver;

import java.util.ArrayList;
import java.util.Arrays;
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
     * Match constructor, initializes the arrayLists and the game map
     */
    public MatchSocket(Match match){
        playerManagers = new ArrayList<>();
        sockets = new ArrayList<>();
        playerSocketMap = new HashMap<>();
        socketPlayerMap = new HashMap<>();
        this.match = match;
    }

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

    public SocketThread getCurrentSocket() {
        return playerSocketMap.get(currentPlayer);
    }

    public void detachSocket(SocketThread socket) {
        for(PlayerManager player : playerManagers) {
            player.getPlayerData().detachSocket(socket);
        }
        match.getMatchRun().getGameMap().detachSocket(socket);
    }

    /***
     * Wait for all the players to be ready and then start the divinity selection phase
     * @param socket the reference to the socket
     */
    public void readyManagement(SocketThread socket) {
        socketPlayerMap.get(socket).getPlayerData().setReady();
        for(PlayerManager player : playerManagers) {
            if(!player.getPlayerData().getReady() || playerManagers.size() != playerN || playerManagers.size() <= 1) {
                return;
            }
        }
        // Check if there is a match saved with these players
        boolean hasBackup = match.getMatchBackup().backupCheck();
        // If i manage to arrive here all the players are ready, i can start the divinity selection phase
        if(!hasBackup) {
            match.setMatchStatus(MatchStatus.DIVINITIES_SELECTION);
            playerSocketMap.get(playerManagers.get(playerManagers.size()-1)).sendMessage(new DivinityPick(match.getMatchSetup().divinities, playerManagers.size()));
        } else {
            match.getMatchBackup().backupRestore();
        }
    }
}
