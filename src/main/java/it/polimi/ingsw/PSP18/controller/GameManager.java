package it.polimi.ingsw.PSP18.controller;

import it.polimi.ingsw.PSP18.model.Color;
import it.polimi.ingsw.PSP18.model.GameMap;
import it.polimi.ingsw.PSP18.model.PlayerData;
import it.polimi.ingsw.PSP18.networking.SocketServer;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GameManager {
    private ArrayList<PlayerManager> players = new ArrayList<PlayerManager>();
    private GameMap gameMap;
    private TurnManager turnManager;
    private SocketServer socketServer;
    private ArrayList<Socket> connectedPlayers = new ArrayList<Socket>();

    public GameManager() {
        gameMap = new GameMap();
        socketServer = new SocketServer(this);
        socketServer.start();
    }

    /***
     * When a player has set up its data from the view, add it to the game
     * @param id The id of the player
     * @param color The color of the player
     * @param playOrder The order of play of the player, 0 is first
     */
    public void addPlayer(String id, Color color, Integer playOrder, String divinity) {
        PlayerData playerData = new PlayerData(id, color, playOrder);
        players.add(new PlayerManager(gameMap, playerData, divinity));
    }

    /***
     * Function that returns a copy of the player array
     * TO CHECK: is this only useful for testing?
     * @return A copy of the players array
     */
    public List<PlayerManager> getPlayers() {
        return List.copyOf(players);
    }

    /***
     * Start the match by ordering players and creating the turn manager
     * Has to be called after the creation of players
     * If Athena is in the match create its special turn manager
     */
    public void startMatch() {
        // Sort players by order
        players.sort(Comparator.comparingInt(o -> o.getPlayerData().getPlayOrder()));

        // Search for Athena
        for (PlayerManager player : players) {
            if(player.getDivinityName().equals("Athena")) {
                turnManager = new TurnManagerAthena(players);
                return;
            }
        }
        // If Athena is not found create a standard turn manager
        turnManager = new TurnManager(players);
    }

    /***
     * Adding the socket of the new connection to the list of the players
     * @param socket the reference to the socket for communicating with the client
     */
    public void addConnectedPlayer(Socket socket) {
        if(connectedPlayers.size() == 1) {
            connectedPlayers.add(socket);
            //TODO: mostra al client la possibilitá di iniziare la partita
        }
        else if (connectedPlayers.size() == 2) {
            connectedPlayers.add(socket);
            //TODO: mostra al client la possibilitá di iniziare la partita
        }
        // If there are already 3 players do nothing, you can only start the match
    }
}
