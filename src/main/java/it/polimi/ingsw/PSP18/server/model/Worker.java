package it.polimi.ingsw.PSP18.server.model;

/***
 * Stores the worker coordinates and the player that owns the worker details
 */
public class Worker {
    private Integer x, y;
    private Integer ID;
    private Color playerColor;

    /***
     * Worker constructor method
     * @param x x worker coordinate
     * @param y y worker coordinate
     * @param id id of the player
     * @param playerColor red, green or blue
     */
    public Worker(Integer x, Integer y, Integer id, Color playerColor) {
        this.x = x;
        this.y = y;
        this.ID = id;
        this.playerColor = playerColor;
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

    /***
     * Returns the ID of the worker
     * @return the id of the worker for the player, 0 or 1
     */
    public Integer getID() {
        return ID;
    }

    /***
     * Returns the playerData of the player
     * @return the player data reference
     */
    public Color getPlayerColor() {
        return playerColor;
    }
}
