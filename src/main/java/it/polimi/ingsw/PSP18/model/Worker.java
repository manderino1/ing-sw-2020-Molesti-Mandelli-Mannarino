package it.polimi.ingsw.PSP18.model;

public class Worker {
    private Integer x, y;

    /***
     * Worker constructor method
     * @param x x worker coordinate
     * @param y y worker coordinate
     */
    public Worker(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }

    /***
     * Sets worker coordinates
     * @param x x worker coordinate
     * @param y y worker coordinate
     */
    public void setPosition(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }

    /***
     * Returns the x worker coordinate
     * @return x worker coordinate
     */
    public Integer getX() {
        return this.x;
    }

    /***
     * Returns the y worker coordinate
     * @return y worker coordinate
     */
    public Integer getY() {
        return this.y;
    }
}
