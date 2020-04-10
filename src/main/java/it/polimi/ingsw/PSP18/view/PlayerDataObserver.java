package it.polimi.ingsw.PSP18.view;

import it.polimi.ingsw.PSP18.model.PlayerData;
import it.polimi.ingsw.PSP18.networking.SocketThread;
import it.polimi.ingsw.PSP18.view.messages.toclient.PlayerDataUpdate;

public class PlayerDataObserver {
    private SocketThread socket;

    public PlayerDataObserver(SocketThread socket) {
        this.socket = socket;
    }

    public void update(PlayerData playerData) {
        socket.sendMessage(new PlayerDataUpdate(playerData));
    }
}
