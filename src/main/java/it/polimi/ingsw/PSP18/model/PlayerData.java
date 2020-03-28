package it.polimi.ingsw.PSP18.model;

import it.polimi.ingsw.PSP18.view.MapObserver;
import it.polimi.ingsw.PSP18.view.PlayerDataObserver;

import java.util.ArrayList;

public class PlayerData {
    private String playerID;
    private Color playerColor;
    private Integer playOrder;
    private Move lastMove;
    private ArrayList<PlayerDataObserver> observers;

    public PlayerData(String playerID, Color playerColor, Integer playOrder) {
        this.playerID = playerID;
        this.playerColor = playerColor;
        this.playOrder = playOrder;
    }

    public String getPlayerID() {
        return playerID;
    }

    public Color getPlayerColor() {
        return playerColor;
    }

    public Integer getPlayOrder() {
        return playOrder;
    }

    public Move getLastMove() {
        return lastMove;
    }

    public void setLastMove(Move lastMove) {
        this.lastMove = lastMove;
    }

    public void attach(PlayerDataObserver observer) {
        observers.add(observer);
    }

    public void detach(PlayerDataObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(PlayerData map) {
        for(PlayerDataObserver observer : observers) {
            observer.update(map);
        }
    }
}
