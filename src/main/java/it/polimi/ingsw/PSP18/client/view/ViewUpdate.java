package it.polimi.ingsw.PSP18.client.view;

import it.polimi.ingsw.PSP18.networking.messages.toclient.*;

import java.io.IOException;

public abstract class ViewUpdate {

    public abstract void updateMap(GameMapUpdate gameMapUpdate);
    public abstract void moveUpdate(MoveList movelist) throws IOException;
    public abstract void updatePlayerData(PlayerDataUpdate playerDataUpdate);
    public abstract void selectNick() throws IOException;
    public abstract void selectDivinity(DivinityList divinityList) throws IOException;
    public abstract void buildUpdate(BuildList buildList) throws IOException;
}
