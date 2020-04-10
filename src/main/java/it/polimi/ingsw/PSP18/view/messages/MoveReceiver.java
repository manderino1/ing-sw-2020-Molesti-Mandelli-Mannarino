package it.polimi.ingsw.PSP18.view.messages;

import it.polimi.ingsw.PSP18.model.Direction;

public class MoveReceiver extends  AbstractMessage {
    private Direction direction;
    private Integer workerID;

    /***
     * Constructor for MoveReceiver object initializing all the attributes
     * @param direction the chosen movement direction
     * @param workerID the id of the worker, 0 or 1
     */
    public MoveReceiver(Direction direction, Integer workerID) {
        this.direction = direction;
        this.workerID = workerID;
    }

    /***
     * Returns the ID of the worker that has to be moved
     * @return the worker id, 0 or 1
     */
    public Integer getWorkerID() {
        return workerID;
    }

    /***
     * Returns the chosen movement direction
     * @return the chosen movement direction
     */
    public Direction getDirection() {
        return direction;
    }
}
