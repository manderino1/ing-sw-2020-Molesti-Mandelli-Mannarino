package it.polimi.ingsw.PSP18.server.model;

import it.polimi.ingsw.PSP18.server.view.MapObserver;

import java.util.ArrayList;

public class GameMap {
    private Cell[][] mapCells = new Cell[5][5];
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
        mapCells[x][y].setBuilding(building);
        mapCells[x][y].setWorker(worker);
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
            observer.update(mapCells);
        }
    }
}
