package it.polimi.ingsw.PSP18.client.view;

import it.polimi.ingsw.PSP18.networking.messages.toclient.GameMapUpdate;
import it.polimi.ingsw.PSP18.networking.messages.toclient.PlayerDataUpdate;

public abstract class ViewUpdate {
    public abstract void updateMap(GameMapUpdate gameMapUpdate);

    public abstract void updatePlayerData(PlayerDataUpdate playerDataUpdate);
}
