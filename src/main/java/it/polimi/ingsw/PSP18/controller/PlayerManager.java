package it.polimi.ingsw.PSP18.controller;

import it.polimi.ingsw.PSP18.controller.divinities.Divinity;
import it.polimi.ingsw.PSP18.model.*;

public class PlayerManager {
    private Map map;
    private Worker[] workers = new Worker[2];
    private PlayerData playerData;
    private Divinity divinity;

    /***
     * Class constructor, initializes the  and the playerData
     * @param map the game map (unique for all players)
     * @param playerData data of the player
     */
    public PlayerManager(Map map, PlayerData playerData) {
        this.map = map;
        this.playerData = playerData;
    }

    /***
     *
     * @param workerID the index of the worker to return (0 or 1)
     * @return a copy of the selected worker
     */
    public Worker getWorker(Integer workerID) {
        return new Worker(workers[workerID].getX(), workers[workerID].getY());
    }

    /***
     *
     * @param oldX the old position of the worker on the x axis
     * @param oldY the old position of the worker on the y axis
     * @param direction the direction of the move
     */
    public void setMove(Integer oldX, Integer oldY, Direction direction) {
        Integer newX = DirectionManagement.getX(oldX, direction);
        Integer newY = DirectionManagement.getY(oldY, direction);
        updateMoveCells(oldX, oldY, newX, newY);
    }

    /***
     * Set worker in a cell and remove from the source one
     * If a worker is present in the destination cell (Apollo) switch workers
     * @param oldX the source x position
     * @param oldY the source y position
     * @param newX the destination x position
     * @param newY the destination y position
     */
    private void updateMoveCells(Integer oldX, Integer oldY, Integer newX, Integer newY) {
        Worker destinationWorker = map.getCell(oldX, oldY+1).getWorker();
        map.setCell(newX, newY, map.getCell(newX, newY).getBuilding(), map.getCell(oldX, oldY).getWorker());
        if (destinationWorker == null) {
            map.setCell(oldX, oldY, map.getCell(oldX, oldY).getBuilding(), null);
        }
        else {
            map.setCell(oldX, oldY, map.getCell(oldX, oldY).getBuilding(), destinationWorker);
        }
    }

    /***
     * Build up a floor on the selected cell or place a dome on it
     * @param X the x coordinate of the building cell
     * @param Y the y coordinate of the building cell
     * @param dome true if a dome is to be built, false if a normal building is to be built
     */
    public void setBuild(Integer X, Integer Y, Boolean dome) {
        if(dome) {
            map.getCell(X, Y).setDome();
        }
        else {
            map.getCell(X, Y).setBuilding(map.getCell(X, Y).getBuilding()+1);
        }
    }

    /***
     * Build up a floor on the selected cell
     * @param X the x coordinate of the building cell
     * @param Y the y coordinate of the building cell
     */
    public void setBuild(Integer X, Integer Y) {
        setBuild(X, Y, false);
    }

    /***
     * TODO: return a copy and not the actual MAP
     * @return a copy of the map
     */
    public Map getMap() {
        return map;
    }

    /***
     * TODO: return a copy and not the actual DATA
     * @return a copy of the player data
     */
    public PlayerData getPlayerData() {
        return playerData;
    }

    /***
     *
     * @return the name of the divinity
     */
    public String getDivinityName() {
        return divinity.getName();
    }

    /***
     *
     * @param raiseForbidden true if Athena moved up and so you cannot move up in the turn
     */
    public void manageTurn(boolean raiseForbidden) {
        divinity.manageTurn(raiseForbidden);
    }
}
