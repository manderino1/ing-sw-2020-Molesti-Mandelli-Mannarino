package it.polimi.ingsw.PSP18.server.controller;

import it.polimi.ingsw.PSP18.server.model.Direction;

public class DirectionManagement {
    public static Integer getX(Integer sourceX, Direction direction) {
        Integer x = -1;
        if(checkCoordinate(sourceX)) {
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
        }
        return x;
    }

    public static Integer getY(Integer sourceY, Direction direction) {
        Integer y = -1;
        if(checkCoordinate(sourceY)) {
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
        }
        return y;
    }

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

    public static Boolean checkCoordinate(Integer x){
        if (x < 4 && x >= 0){
            return true;
        } else {
            return false;
        }
    }
}
