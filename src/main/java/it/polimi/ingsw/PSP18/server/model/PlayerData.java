package it.polimi.ingsw.PSP18.server.model;

import it.polimi.ingsw.PSP18.server.view.PlayerDataObserver;

import java.util.ArrayList;

public class PlayerData {
    private String playerID;
    private Color playerColor;
    private Integer playOrder;
    private Move lastMove;
    private String divinity;
    private ArrayList<PlayerDataObserver> observers = new ArrayList<>();

    /***
     * PlayerDatq constructor method
     * @param playerID the id of the player
     * @param playerColor the color of the player
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
     * @param lastMove direction and level of the move
     */
    public void setLastMove(Move lastMove) {
        this.lastMove = lastMove;
        notifyObservers();
    }


    /***
     * Get the divinity of the player as string
     * @return the divinity of the player in game, null if not chosen
     */
    public String getDivinity() {
        return divinity;
    }

    /***
     * Set the player divinity
     * @param divinity the name of the divinity to be set
     */
    public void setDivinity(String divinity) {
        this.divinity = divinity;
        notifyObservers();
    }

    /***
     * Adds an observer
     * @param observer the new observer reference
     */
    public void attach(PlayerDataObserver observer) {
        observers.add(observer);
        notifyObservers();
    }

    /***
     * Removes the observer
     * @param observer the observer to be removed reference
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
