package it.polimi.ingsw.PSP18.model;

public class Cell {
    private Integer building;
    private Worker worker;
    private Boolean dome;

    public Cell() {
        this.building=null;
        this.worker=null;
        this.dome=false;
    }

    public Integer getBuilding() {
        return building;
    }

    public void setBuilding(Integer building) {
        this.building = building;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public Boolean getDome() {
        return dome;
    }

    public void setDome() {
        this.dome = true;
    }

    public void removeDome() {
        this.dome = false;
    }
}
