package it.polimi.ingsw.PSP18.client.view;

import it.polimi.ingsw.PSP18.networking.messages.toclient.GameMapUpdate;

public abstract class ViewUpdate {
    public abstract void updateMap(GameMapUpdate gameMapUpdate);
}
