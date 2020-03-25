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
