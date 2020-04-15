package it.polimi.ingsw.PSP18.networking.messages.toserver;

import it.polimi.ingsw.PSP18.server.model.Direction;

public class BuildFlagReceiver extends ServerAbstractMessage {
    private Direction direction;
    private Boolean flag;
    /***
     * Constructor for MoveReceiver object initializing all the attributes
     * @param direction the chosen build direction
     */
    public BuildFlagReceiver(Direction direction, Boolean flag) {
        this.type = ServerMessageType.BUILD_RECEIVER;
        this.direction = direction;
        this.flag=flag;
    }

    /***
     * return the flag of the message
     * @return flag
     */
    public Boolean getFlag(){ return flag;}

    /***
     * Returns the chosen build direction
     * @return the chosen build direction
     */
    public Direction getDirection() {
        return direction;
    }
}
