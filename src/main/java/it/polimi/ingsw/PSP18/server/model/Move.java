package it.polimi.ingsw.PSP18.server.model;

/***
 * Stores a move direction and level difference
 * A move is identified by one of eight possible direction and a differential change in level
 */
public class Move {
    private Direction direction;
    private Integer level;

    /***
     * Move constructor method
     * @param direction direction of the movement, one of the eight direction of Direction class
     * @param level the difference in number of levels covered by the move
     */
    public Move(Direction direction, Integer level) {
        this.direction = direction;
        this.level = level;
    }

    /***
     * Returns level difference in the move, ranges from -3 to 3
     * @return the level difference
     */
    public Integer getLevel(){
        return level;
    }

    /***
     * Returns the direction of the movement
     * @return one of the possible 8 directions (ref Direction class)
     */
    public Direction getDirection() {
        return direction;
    }
}
