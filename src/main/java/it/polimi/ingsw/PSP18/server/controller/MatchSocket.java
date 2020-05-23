package it.polimi.ingsw.PSP18.server.controller;

import it.polimi.ingsw.PSP18.networking.SocketThread;
import it.polimi.ingsw.PSP18.networking.messages.toclient.DivinityPick;
import it.polimi.ingsw.PSP18.networking.messages.toclient.MatchReady;
import it.polimi.ingsw.PSP18.networking.messages.toclient.WaitingNick;
import it.polimi.ingsw.PSP18.server.model.MatchStatus;
import it.polimi.ingsw.PSP18.server.view.PlayerDataObserver;

import java.util.ArrayList;
import java.util.HashMap;

/***
 * class that contains all information about the players in the match, their sockets and also all the methods to
 * manage them
 */
public class MatchSocket {
    private MatchStatus matchStatus;
    private ArrayList<PlayerManager> playerManagers;
    private ArrayList<SocketThread> sockets;
    private HashMap<PlayerManager, SocketThread> playerSocketMap;
    private HashMap<SocketThread, PlayerManager> socketPlayerMap;
    private PlayerManager currentPlayer;
    private MatchSetUp matchSetUp;

    /***
     * MatchSocket constructor, initializes the sockets, playerManagers, and the two HasMaps
     * @param playerN the number of players into the game
     */
    public MatchSocket(int playerN){
        matchStatus = MatchStatus.WAITING_FOR_PLAYERS;
        playerManagers = new ArrayList<>();
        sockets = new ArrayList<>();
        playerSocketMap = new HashMap<>();
        socketPlayerMap = new HashMap<>();
        matchSetUp = new MatchSetUp(this, playerN);
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
        if(sockets.size() <= 2 && matchStatus == MatchStatus.WAITING_FOR_PLAYERS) {
            sockets.add(socket);
            socket.setMatchSetup(matchSetUp);
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
     * @return the hash map that relates a playerManager with his socketThread
     */
    public HashMap<PlayerManager, SocketThread> getPlayerSocketMap() {
        return playerSocketMap;
    }

    /***
     * @return the hash map that relates a socketThread with his playerManager
     */
    public HashMap<SocketThread, PlayerManager> getSocketPlayerMap() {
        return socketPlayerMap;
    }

    /***
     * Set the state of the match in pre-defined states as described in MatchStatus class
     * @param matchStatus the new state of the match
     */
    public void setMatchStatus(MatchStatus matchStatus) {
        this.matchStatus = matchStatus;
    }

    /***
     * Return the state of the match, there are some pre-defined states as enum in MatchStatus class
     * @return the match state
     */
    public MatchStatus getMatchStatus() {
        return matchStatus;
    }
}
