package it.polimi.ingsw.PSP18.client.view;

import it.polimi.ingsw.PSP18.networking.messages.toclient.*;

import java.io.IOException;

public abstract class ViewUpdate {

    public abstract void updateMap(GameMapUpdate gameMapUpdate);
    public abstract void moveUpdate(MoveList movelist);
    public abstract void updatePlayerData(PlayerDataUpdate playerDataUpdate);
    public abstract void selectNick();
    public abstract void selectDivinity(DivinityList divinityList);
    public abstract void buildUpdate(BuildList buildList);
    public abstract void matchLostUpdate(MatchLost matchLost);
    public abstract void matchWonUpdate(MatchWon matchWon);
    public abstract void startMatch(StartMatch startMatch);
    public abstract void matchReadyUpdate(MatchReady matchReady);
    public abstract void setWorker(PlaceReady placeReady) throws IOException;
}
