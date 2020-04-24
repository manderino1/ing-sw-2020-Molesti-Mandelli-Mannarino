package it.polimi.ingsw.PSP18.networking.messages.toserver;

import it.polimi.ingsw.PSP18.server.model.Direction;

public class MoveReceiver extends ServerAbstractMessage {
    private Direction direction;
    private Integer workerID;

    /***
     * Constructor for MoveReceiver object initializing the direction of the movement and the chosen worker id
     * @param direction the chosen movement direction
     * @param workerID the id of the worker, 0 or 1
     */
    public MoveReceiver(Direction direction, Integer workerID) {
        this.type = ServerMessageType.MOVE_RECEIVER;
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
