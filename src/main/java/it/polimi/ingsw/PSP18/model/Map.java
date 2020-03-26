package it.polimi.ingsw.PSP18.model;

public class Map {
    private Cell[][] mapCells = new Cell[5][5];

    public Map() {
    }

    public Cell getCell(int x, int y) {
        return mapCells[x][y];
    }

    public void setCell(int x, int y, Integer building, Worker worker) {
        mapCells[x][y].setBuilding(building);
        mapCells[x][y].setWorker(worker);
    }
}
