package it.polimi.ingsw.PSP18.server.model;

import it.polimi.ingsw.PSP18.networking.SocketThread;
import it.polimi.ingsw.PSP18.server.view.PlayerDataObserver;

import java.util.ArrayList;

/***
 * Stores the data of the player and its state
 * A player has an unique id, color and a divinity assigned
 */
public class PlayerData {
    private String playerID;
    private Color playerColor;
    private Integer playOrder;
    private Move lastMove;
    private String divinity;
    private Boolean isReady = false;
    private Boolean hasLost = false;
    private ArrayList<PlayerDataObserver> observers = new ArrayList<>();

    /***
     * PlayerDatq constructor method
     * @param playerID the id of the player, is unique in the match
     * @param playerColor the color of the player, is unique in the match
     * @param playOrder player turn order, assigned by connection order, unique
     */
    public PlayerData(String playerID, Color playerColor, Integer playOrder) {
        this.playerID = playerID;
        this.playerColor = playerColor;
        this.playOrder = playOrder;
    }

    /***
     * Returns playerID value, unique
     * @return player name
     */
    public String getPlayerID() {
        return playerID;
    }

    /***
     * Returns player color, RED, GREEN or BLUE
     * @return player color
     */
    public Color getPlayerColor() {
        return playerColor;
    }

    /***
     * Returns player turn order, maximum is number of players - 1
     * @return player order
     */
    public Integer getPlayOrder() {
        return playOrder;
    }

    /***
     * Returns player last move, contains direction and level differential of the move
     * @return last move
     */
    public Move getLastMove() {
        return lastMove;
    }

    /***
     * Sets player move as last move and notify the observers of the change for updating graphic
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
     * Set the player divinity, has to be one of the divinities implemented into the game
     * @param divinity the name of the divinity to be set
     */
    public void setDivinity(String divinity) {
        this.divinity = divinity;
        notifyObservers();
    }

    /***
     * Set the player as ready
     */
    public void setReady() {
        isReady = true;
        notifyObservers();
    }

    /***
     * Get the player state
     * @return the player state, true if ready
     */
    public Boolean getReady() {
        return isReady;
    }

    /***
     * Set the new player color on backup restore (only for backup)
     * @param playerColor restore the new player backup
     */
    public void setPlayerColor(Color playerColor) {
        this.playerColor = playerColor;
    }

    /***
     * Set the new play order on backup restore (only for backup)
     * @param playOrder restore the new play order
     */
    public void setPlayOrder(Integer playOrder) {
        this.playOrder = playOrder;
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
     * Notifies the observers when a change in the model happens
     */
    public void notifyObservers() {
        for(PlayerDataObserver observer : observers) {
            observer.update(this);
        }
    }

    /***
     * Detach the observers corresponding with the given socket
     * @param socket the socket to remove
     */
    public void detachSocket(SocketThread socket) {
        ArrayList<PlayerDataObserver> toRemove = new ArrayList<>();
        for(PlayerDataObserver observer : observers) {
            if(observer.getSocket().equals(socket)) {
                toRemove.add(observer);
            }
        }

        for(PlayerDataObserver observer : toRemove) {
            detach(observer);
        }
    }

    /***
     * True if the player has lost
     * @return lost value
     */
    public Boolean getLost() {
        return hasLost;
    }

    /***
     * Set the value as true, the player has lost the match
     */
    public void setLost() {
        this.hasLost = true;
    }
}
