package it.polimi.ingsw.PSP18.server.model;

public class Cell {
    private Integer building;
    private Worker worker;
    private Boolean dome;

    public Cell() {
        this.building = 0;
        this.worker=null;
        this.dome=false;
    }

    /***
     * returns the level of the building in the cell
     * @return The level of the building
     */
    public Integer getBuilding() {
        return building;
    }

    /***
     * sets the level of the building in the cell
     * @param building The level of the building
     */
    public void setBuilding(Integer building) {
        this.building = building;
    }

    /***
     * retunrs the worker in the cell, returns null if there is no worker in the cell
     * @return The worker in the cell
     */
    public Worker getWorker() {
        return worker;
    }

    /***
     * sets the worker in the cell
     * @param worker The worker player
     */
    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    /***
     * returns if there is a dome in the cell
     * @return Value that shows if there is a dome in the cell
     */
    public Boolean getDome() {
        return dome;
    }

    /***
     * sets dome to true
     */
    public void setDome() {
        this.dome = true;
    }

    /***
     * sets dome to false
     */
    public void removeDome() {
        this.dome = false;
    }
}
