package it.polimi.ingsw.PSP18.model;

import it.polimi.ingsw.PSP18.view.MapObserver;

import java.util.ArrayList;

public class GameMap {
    private Cell[][] mapCells = new Cell[5][5];
    private ArrayList<MapObserver> observers = new ArrayList<>();

    public GameMap() {
        for (int i=0; i<5; i++){
            for (int j=0; j<5; j++){
                mapCells[i][j]= new Cell();
            }
        }
    }

    public Cell getCell(int x, int y) {
        return mapCells[x][y];
    }

    public void setCell(int x, int y, Integer building, Worker worker) {
        mapCells[x][y].setBuilding(building);
        mapCells[x][y].setWorker(worker);
    }

    public void attach(MapObserver observer) {
        observers.add(observer);
    }

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
