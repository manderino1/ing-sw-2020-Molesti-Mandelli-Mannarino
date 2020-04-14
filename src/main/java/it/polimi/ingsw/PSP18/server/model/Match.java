package it.polimi.ingsw.PSP18.server.model;

import it.polimi.ingsw.PSP18.networking.SocketClient;
import it.polimi.ingsw.PSP18.networking.messages.toclient.WaitingNick;
import it.polimi.ingsw.PSP18.server.controller.PlayerManager;
import it.polimi.ingsw.PSP18.server.controller.TurnManager;
import it.polimi.ingsw.PSP18.networking.SocketServer;
import it.polimi.ingsw.PSP18.networking.SocketThread;
import it.polimi.ingsw.PSP18.server.view.MapObserver;
import it.polimi.ingsw.PSP18.server.view.PlayerDataObserver;

import java.util.ArrayList;
import java.util.HashMap;

public class Match {
    private ArrayList<PlayerManager> playerManagers;
    private TurnManager turnManager;
    private ArrayList<SocketThread> sockets;
    private SocketServer socketServer;
    private HashMap<PlayerManager, SocketThread> playerSocketMap;
    private PlayerManager currentPlayer;
    private MatchStatus matchStatus;
    private GameMap gameMap;

    /***
     * Match constructor, initializes the arrayLists and the game map
     */
    public Match(){
        playerManagers = new ArrayList<PlayerManager>();
        sockets = new ArrayList<SocketThread>();
        playerSocketMap = new HashMap<PlayerManager, SocketThread>();
        socketServer = new SocketServer(this);
        socketServer.start(); // Wait for connections
        gameMap = new GameMap();
    }

    /***
     * Returns the game map
     * @return the game map, unique for all players
     */
    public GameMap getGameMap() {
        return gameMap;
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

    /***
     * Add a player to the players list
     * @param player the playermanager player reference
     */
    public void addPlayer(PlayerManager player, SocketThread socket){
        for(PlayerManager playerPresent : playerManagers) {
            if(player.getPlayerData().getPlayerID().equals(playerPresent.getPlayerData().getPlayerID())) {
                socket.sendMessage(new WaitingNick());
                return;
            }
        }
        playerManagers.add(player);
        playerSocketMap.put(player, socket);
    }

    /***
     * Add a socket to the sockets list and register the observers
     * related to the socket and client connection
     * @param socket the socket reference
     */
    public void addSocket(SocketThread socket){
        sockets.add(socket);
        gameMap.attach(new MapObserver(socket));
        for(PlayerManager player : playerManagers) {
            player.getPlayerData().attach(new PlayerDataObserver(socket));
        }
        socket.sendMessage(new WaitingNick());
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
     * Get the turn manager
     * @return the turn manager reference
     */
    public TurnManager getTurnManager() {
        return turnManager;
    }

    /***
     * Set the new turn manager
     * @param turnManager the turn manager reference
     */
    public void setTurnManager(TurnManager turnManager) {
        this.turnManager = turnManager;
    }
}
