package it.polimi.ingsw.PSP18.model;

public class GameMap {
    private Cell[][] mapCells = new Cell[5][5];

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
}
