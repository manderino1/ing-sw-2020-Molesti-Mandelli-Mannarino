package it.polimi.ingsw.PSP18.networking.messages.toserver;

import it.polimi.ingsw.PSP18.server.model.Direction;

public class FlagMoveReceiver extends ServerAbstractMessage {
    private Direction direction;
    private Integer workerID;
    private Boolean flag;

    /***
     Constructor for FlagMoveReceiver object initializing the direction of the movement, the worker id that will move and a flag that indicates if the worker is going to move
     * @param direction the chosen movement direction
     * @param workerID the id of the worker, 0 or 1
     * @param flag true if the player decides to move
     */
    public FlagMoveReceiver(Direction direction, Integer workerID, Boolean flag) {
        this.type = ServerMessageType.FLAG_MOVE_RECEIVER;
        this.direction = direction;
        this.workerID = workerID;
        this.flag = flag;
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

    /***
     * Returns the moving flag
     * @return true if the player decides to move
     */
    public Boolean getFlag() {
        return flag;
    }
}