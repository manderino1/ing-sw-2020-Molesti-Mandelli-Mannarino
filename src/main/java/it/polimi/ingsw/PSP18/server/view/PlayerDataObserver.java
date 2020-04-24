package it.polimi.ingsw.PSP18.server.view;

import it.polimi.ingsw.PSP18.server.model.PlayerData;
import it.polimi.ingsw.PSP18.networking.SocketThread;
import it.polimi.ingsw.PSP18.networking.messages.toclient.PlayerDataUpdate;

public class PlayerDataObserver {
    private SocketThread socket;

    /***
     * Constructor of the class, initialize the socket where we want to send the updates
     * @param socket the socket connected to the client
     */
    public PlayerDataObserver(SocketThread socket) {
        this.socket = socket;
    }

    /***
     * method that sends the updates to the playerData Class to the Client
     * @param playerData the class the contains the player information
     */
    public void update(PlayerData playerData) {
        socket.sendMessage(new PlayerDataUpdate(playerData));
    }
}
