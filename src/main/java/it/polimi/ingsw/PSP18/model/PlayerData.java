package it.polimi.ingsw.PSP18.model;

import it.polimi.ingsw.PSP18.view.PlayerDataObserver;

import java.util.ArrayList;

public class PlayerData {
    private String playerID;
    private Color playerColor;
    private Integer playOrder;
    private Move lastMove;
    private ArrayList<PlayerDataObserver> observers = new ArrayList<>();

    /***
     * PlayerDatq constructor method
     * @param playerID
     * @param playerColor
     * @param playOrder player turn order
     */
    public PlayerData(String playerID, Color playerColor, Integer playOrder) {
        this.playerID = playerID;
        this.playerColor = playerColor;
        this.playOrder = playOrder;
    }

    /***
     * Returns playerID value
     * @return playerID
     */
    public String getPlayerID() {
        return playerID;
    }

    /***
     * Returns player color
     * @return player color
     */
    public Color getPlayerColor() {
        return playerColor;
    }

    /***
     * Returns player turn order
     * @return player order
     */
    public Integer getPlayOrder() {
        return playOrder;
    }

    /***
     * Returns player last move
     * @return last move
     */
    public Move getLastMove() {
        return lastMove;
    }

    /***
     * Sets player move as last move
     * @param lastMove
     */
    public void setLastMove(Move lastMove) {
        this.lastMove = lastMove;
    }

    /***
     * Adds an observer
     * @param observer
     */
    public void attach(PlayerDataObserver observer) {
        observers.add(observer);
    }

    /***
     * Removes the observer
     * @param observer
     */
    public void detach(PlayerDataObserver observer) {
        observers.remove(observer);
    }

    /***
     * Notifies the observers
     */
    public void notifyObservers() {
        for(PlayerDataObserver observer : observers) {
            observer.update(this);
        }
    }
}
