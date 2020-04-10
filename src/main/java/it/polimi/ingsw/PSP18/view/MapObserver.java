package it.polimi.ingsw.PSP18.view;

import it.polimi.ingsw.PSP18.model.Cell;
import it.polimi.ingsw.PSP18.networking.SocketThread;
import it.polimi.ingsw.PSP18.view.messages.toclient.GameMapUpdate;

public class MapObserver {
    private SocketThread socket;

    public MapObserver(SocketThread socket) {
        this.socket = socket;
    }

    public void update(Cell[][] mapCells) {
        socket.sendMessage(new GameMapUpdate(mapCells));
    }
}
