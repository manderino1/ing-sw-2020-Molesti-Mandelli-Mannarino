package it.polimi.ingsw.PSP18.server.model;

public class Move {
    private Direction direction;
    private Integer level;

    /***
     * Move constructor method
     * @param direction direction of the movement
     * @param level the level value of the building i am reaching
     */
    public Move(Direction direction, Integer level) {
        this.direction = direction;
        this.level = level;
    }

    /***
     * Returns level value
     * @return the level value
     */
    public Integer getLevel(){
        return level;
    }

    /***
     * Returns the direction
     * @return direction
     */
    public Direction getDirection() {
        return direction;
    }
}
