package it.polimi.ingsw.PSP18.server.controller;

import it.polimi.ingsw.PSP18.server.model.Direction;

/***
 * Class the implements some utility methods useful for TurnManager and divinity classes
 */
public class DirectionManagement {
    /***
     * get the new X coordinate in function of the direction
     * @param sourceX the actual X coordinate
     * @param direction the direction the player want to move
     * @return the future X coordinate after moving
     */
    public static Integer getX(Integer sourceX, Direction direction) {
        Integer x = -1;
        switch (direction) {
            case UP:
            case DOWN:
                x = sourceX;
                break;
            case LEFT:
            case LEFTUP:
            case LEFTDOWN:
                x = sourceX - 1;
                break;
            case RIGHT:
            case RIGHTUP:
            case RIGHTDOWN:
                x = sourceX + 1;
                break;
        }
        if(checkCoordinate(x)) {
            return x;
        } else {
            return -1;
        }
    }
    /***
     * get the new Y coordinate in function of the direction
     * @param sourceY the actual Y coordinate
     * @param direction the direction the player want to move
     * @return the future Y coordinate after moving
     */
    public static Integer getY(Integer sourceY, Direction direction) {
        Integer y = -1;
        switch (direction) {
            case LEFT:
            case RIGHT:
                y = sourceY;
                break;
            case DOWN:
            case RIGHTDOWN:
            case LEFTDOWN:
                y = sourceY + 1;
                break;
            case UP:
            case LEFTUP:
            case RIGHTUP:
                y = sourceY - 1;
                break;
        }
        if(checkCoordinate(y)) {
            return y;
        } else {
            return -1;
        }
    }

    /***
     * Calculate the opposite direction of the direction received in input
     * @param direction the direction we want to calculate the opposite
     * @return the opposite of param direction
     */
    public static Direction getOppositeDirection(Direction direction) {
        Direction dir = Direction.LEFT;
        switch (direction) {
            case LEFT:
                dir = Direction.RIGHT;
                break;
            case RIGHT:
                dir = Direction.LEFT;
                break;
            case DOWN:
                dir = Direction.UP;
                break;
            case RIGHTDOWN:
                dir = Direction.LEFTUP;
                break;
            case LEFTDOWN:
                dir = Direction.RIGHTUP;
                break;
            case UP:
                dir = Direction.DOWN;
                break;
            case LEFTUP:
                dir = Direction.RIGHTDOWN;
                break;
            case RIGHTUP:
                dir = Direction.LEFTDOWN;

                break;
        }
        return dir;
    }

    /***
     * check if the coordinate is legit
     * @param finalCoord the coordinate
     * @return true if the coordinate is legit
     */
    public static Boolean checkCoordinate(Integer finalCoord){
        return finalCoord <= 4 && finalCoord >= 0;
    }
}
