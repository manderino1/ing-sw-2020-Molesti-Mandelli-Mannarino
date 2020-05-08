package it.polimi.ingsw.PSP18.networking.messages.toclient;

import it.polimi.ingsw.PSP18.server.model.Cell;
import it.polimi.ingsw.PSP18.server.model.Direction;
import it.polimi.ingsw.PSP18.server.model.GameMap;

/***
 * This message class is used to send the map matrix when there is a map update
 */

public class GameMapUpdate extends  ClientAbstractMessage {
    private Cell[][] gameMap;
    private Direction lastActionDirection;
    private int lastActionX, lastActionY;
    private boolean lastActionIsBuild;

    /***
     * Init the message type and the map matrix
     * @param map the map matrix, 5x5 array
     */
    public GameMapUpdate(GameMap map) {
        this.type = ClientMessageType.GAME_MAP_UPDATE;
        this.gameMap = map.getMapCells();
        this.lastActionDirection = map.getLastActionDirection();
        this.lastActionX = map.getLastActionX();
        this.lastActionY = map.getLastActionY();
        this.lastActionIsBuild = map.isLastActionIsBuild();
    }

    /***
     * Returns the map matrix
     * @return the 5x5 map matrix
     */
    public Cell[][] getGameMap() {
        return gameMap;
    }

    public Direction getLastActionDirection() {
        return lastActionDirection;
    }

    public int getLastActionX() {
        return lastActionX;
    }

    public int getLastActionY() {
        return lastActionY;
    }

    public boolean isLastActionIsBuild() {
        return lastActionIsBuild;
    }
}
