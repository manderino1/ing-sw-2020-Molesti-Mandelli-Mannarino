package it.polimi.ingsw.PSP18.model;

public class Cell {
    private Building building;
    private Worker worker;

    public Cell() {
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }
}
