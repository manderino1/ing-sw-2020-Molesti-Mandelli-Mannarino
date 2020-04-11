package it.polimi.ingsw.PSP18.networking.messages.toserver;

import it.polimi.ingsw.PSP18.server.model.Direction;

public class BuildReceiver extends ServerAbstractMessage {
    private Direction direction;

    /***
     * Constructor for MoveReceiver object initializing all the attributes
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
