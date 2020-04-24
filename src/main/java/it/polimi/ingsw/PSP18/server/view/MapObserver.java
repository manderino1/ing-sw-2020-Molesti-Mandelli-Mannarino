package it.polimi.ingsw.PSP18.server.view;

import it.polimi.ingsw.PSP18.server.model.Cell;
import it.polimi.ingsw.PSP18.networking.SocketThread;
import it.polimi.ingsw.PSP18.networking.messages.toclient.GameMapUpdate;

/***
 * Observes a gameMap and is notified every time there is a change into the map
 */
public class MapObserver {
    private SocketThread socket;

    /***
     * Constructor of the class, initialize the socket where we need to send the updates
     * @param socket the socket connected to the client
     */
    public MapObserver(SocketThread socket) {
        this.socket = socket;
    }

    /***
     * Send to the client the updated map
     * @param mapCells the game map
     */
    public void update(Cell[][] mapCells) {
        socket.sendMessage(new GameMapUpdate(mapCells));
    }
}
