package it.polimi.ingsw.PSP18.controller;

import it.polimi.ingsw.PSP18.controller.divinities.Divinity;
import it.polimi.ingsw.PSP18.model.*;

public class PlayerManager {
    private Map map;
    private Worker[] workers = new Worker[2];
    private PlayerData playerData;
    private Divinity divinity;

    public PlayerManager() {
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
        map.setCell(newX, newY, map.getCell(newX, newY).getBuilding(), map.getCell(oldX, oldY).getWorker(), map.getCell(newX, newY).getDome());
        if (destinationWorker == null) {
            map.setCell(oldX, oldY, map.getCell(oldX, oldY).getBuilding(), null, map.getCell(oldX, oldY).getDome());
        }
        else {
            map.setCell(oldX, oldY, map.getCell(oldX, oldY).getBuilding(), destinationWorker, map.getCell(oldX, oldY).getDome());
        }
    }

    /***
     * TODO: return a copy and not the actual MAP
     * @return a copy of the map
     */
    public Map getMap() {
        return map;
    }

    /***
     *
     * @param raiseForbidden true if Athena moved up and so you cannot move up in the turn
     */
    public void manageTurn(boolean raiseForbidden) {
        divinity.manageTurn(raiseForbidden);
    }
}
