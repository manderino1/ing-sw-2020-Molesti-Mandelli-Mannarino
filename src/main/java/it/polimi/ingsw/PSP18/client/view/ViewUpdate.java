package it.polimi.ingsw.PSP18.client.view;

import it.polimi.ingsw.PSP18.networking.messages.toclient.GameMapUpdate;
import it.polimi.ingsw.PSP18.networking.messages.toclient.MoveList;
import java.io.IOException;
import it.polimi.ingsw.PSP18.networking.messages.toclient.PlayerDataUpdate;

public abstract class ViewUpdate {

    public abstract void updateMap(GameMapUpdate gameMapUpdate);
    public abstract void moveUpdate(MoveList movelist) throws IOException;
    public abstract void updatePlayerData(PlayerDataUpdate playerDataUpdate);
}
