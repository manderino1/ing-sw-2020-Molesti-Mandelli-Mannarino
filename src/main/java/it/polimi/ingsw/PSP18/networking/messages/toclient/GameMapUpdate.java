package it.polimi.ingsw.PSP18.view.messages.toclient;

import it.polimi.ingsw.PSP18.model.Cell;

public class GameMapUpdate extends  ClientAbstractMessage {
    private Cell[][] gameMap;

    public GameMapUpdate(Cell[][] gameMap) {
        this.type = ClientMessageType.GAME_MAP_UPDATE;
        this.gameMap = gameMap;
    }

    public Cell[][] getGameMap() {
        return gameMap;
    }
}
