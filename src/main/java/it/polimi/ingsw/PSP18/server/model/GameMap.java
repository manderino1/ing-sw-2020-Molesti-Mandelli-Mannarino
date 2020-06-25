package it.polimi.ingsw.PSP18.server.model;

import it.polimi.ingsw.PSP18.networking.SocketThread;
import it.polimi.ingsw.PSP18.server.view.MapObserver;
import it.polimi.ingsw.PSP18.server.view.PlayerDataObserver;

import java.util.ArrayList;

/***
 * This is the class that contains all the methods to manage the game map
 */
public class GameMap {
    private Cell[][] mapCells = new Cell[5][5];
    private boolean lastActionIsBuild;
    private int lastActionX = -1, lastActionY = -1;
    private ArrayList<MapObserver> observers = new ArrayList<>();

    /***
     * GameMap constructor, initializes the map matrix
     */
    public GameMap() {
        for (int i=0; i<5; i++){
            for (int j=0; j<5; j++){
                mapCells[i][j]= new Cell();
            }
        }
    }

    /***
     * Return map cells
     * @return 5x5 map matrix
     */
    public Cell[][] getMapCells() {
        return mapCells;
    }

    /***
     * Get a chosen cell from the game map matrix
     * @param x the x coordinate of the cell
     * @param y the y coordinate of the cell
     * @return the chosen map cell
     */
    public Cell getCell(int x, int y) {
        return mapCells[x][y];
    }

    /***
     * Set new parameters of a cell into the game matrix
     * @param x the x coordinate of the cell to modify
     * @param y the y coordinate of the cell to modify
     * @param building the reference to the building in the cell, 0 if ground
     * @param worker the reference to the eventual worker in the cell, null if not present
     */
    public void setCell(int x, int y, Integer building, Worker worker) {
        this.lastActionX = x;
        this.lastActionY = y;
        this.lastActionIsBuild = !mapCells[x][y].getBuilding().equals(building);
        mapCells[x][y].setBuilding(building);
        mapCells[x][y].setWorker(worker);
        notifyObservers();
    }

    /***
     * Set a new map into the game
     * @param mapCells the array of map cells
     */
    public void setMapCells(Cell[][] mapCells) {
        this.mapCells = mapCells;
    }

    /***
     * Place a dome into the selected cell
     * @param x the x coordinate of the cell to modify
     * @param y the y coordinate of the cell to modify
     */
    public void setDome(int x, int y) {
        this.lastActionX = x;
        this.lastActionY = y;
        this.lastActionIsBuild = true;
        mapCells[x][y].setDome();
        notifyObservers();
    }

    /***
     * Attach a new observer into the observers list
     * @param observer the new observer reference
     */
    public void attach(MapObserver observer) {
        observers.add(observer);
        notifyObservers();
    }

    /***
     * Detach the observer from the observers list
     * @param observer the observer to remove
     */
    public void detach(MapObserver observer) {
        observers.remove(observer);
    }

    /***
     * The function notifies all the observers that a change is happened in its model
     */
    public void notifyObservers() {
        for(MapObserver observer : observers) {
            observer.update(this);
        }
    }

    /***
     * Detach the observers corresponding with the given socket
     * @param socket the socket to remove
     */
    public void detachSocket(SocketThread socket) {
        ArrayList<MapObserver> toRemove = new ArrayList<>();
        for(MapObserver observer : observers) {
            if(observer.getSocket().equals(socket)) {
                toRemove.add(observer);
            }
        }

        for(MapObserver observer : toRemove) {
            detach(observer);
        }
    }

    /***
     * Get the x coordinate of the last action
     * @return the x coordinate of the last action
     */
    public int getLastActionX() {
        return lastActionX;
    }

    /***
     * Get the y coordinate of the last action
     * @return the y coordinate of the last action
     */
    public int getLastActionY() {
        return lastActionY;
    }

    /***
     * True if the last action is a build
     * @return true if last action is a build
     */
    public boolean isLastActionIsBuild() {
        return lastActionIsBuild;
    }
}
