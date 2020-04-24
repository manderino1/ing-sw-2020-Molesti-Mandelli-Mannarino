package it.polimi.ingsw.PSP18.networking.messages.toserver;

import it.polimi.ingsw.PSP18.server.model.Direction;

/***
 * This is the class that represents a message used to make a worker build
 */
public class BuildReceiver extends ServerAbstractMessage {
    protected Direction direction;

    /***
     * Constructor for MoveReceiver object initializing the direction of the building move
     * @param direction the chosen build direction
     */
    public BuildReceiver(Direction direction) {
        this.type = ServerMessageType.BUILD_RECEIVER;
        this.direction = direction;
    }

    /***
     * Returns the chosen build direction
     * @return the chosen build direction
     */
    public Direction getDirection() {
        return direction;
    }
}
