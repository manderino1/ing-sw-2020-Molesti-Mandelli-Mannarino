package it.polimi.ingsw.PSP18.server.model;

/***
 * This is the class that contains all the methods to manage the map's cells
 * Every cell is identified by a building level (ranging from 0 to 3), a worker reference and a flag if a dome is present
 */
public class Cell {
    private Integer building;
    private Worker worker;
    private Boolean dome;

    /***
     * Constructor of the class, initialize building to O, worker to null and dome to null
     */
    public Cell() {
        this.building = 0;
        this.worker=null;
        this.dome=false;
    }

    /***
     * Returns the level of the building in the cell, ranges from 0 to 3
     * @return The level of the building
     */
    public Integer getBuilding() {
        return building;
    }

    /***
     * Sets the level of the building in the cell, ranges from 0 to 3
     * @param building The level of the building
     */
    public void setBuilding(Integer building) {
        this.building = building;
    }

    /***
     * returns the worker in the cell, returns null if there is no worker in the cell
     * @return The worker in the cell
     */
    public Worker getWorker() {
        return worker;
    }

    /***
     * sets the worker in the cell, set at null if the cell is free
     * @param worker The worker player
     */
    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    /***
     * returns true if there is a dome in the cell
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
