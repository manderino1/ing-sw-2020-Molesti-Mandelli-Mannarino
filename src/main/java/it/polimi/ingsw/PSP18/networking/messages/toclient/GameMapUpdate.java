package it.polimi.ingsw.PSP18.networking.messages.toclient;

import it.polimi.ingsw.PSP18.server.model.Cell;

public class GameMapUpdate extends  ClientAbstractMessage {
    private Cell[][] gameMap;

    /***
     * Init the message type and the map matrix
     * @param gameMap the map matrix, 5x5 array
     */
    public GameMapUpdate(Cell[][] gameMap) {
        this.type = ClientMessageType.GAME_MAP_UPDATE;
        this.gameMap = gameMap;
    }

    /***
     * Returns the map matrix
     * @return the 5x5 map matrix
     */
    public Cell[][] getGameMap() {
        return gameMap;
    }
}
