package it.polimi.ingsw.PSP18.controller;

import it.polimi.ingsw.PSP18.model.Direction;

public class DirectionManagement {
    public static Integer getX(Integer sourceX, Direction direction) {
        Integer x = 0;
        switch (direction) {
            case UP:
            case DOWN:
                x = sourceX;
                break;
            case LEFT:
            case LEFTUP:
            case LEFTDOWN:
                x = sourceX-1;
                break;
            case RIGHT:
            case RIGHTUP:
            case RIGHTDOWN:
                x = sourceX+1;
                break;
        }
        return x;
    }

    public static Integer getY(Integer sourceY, Direction direction) {
        Integer y = 0;
        switch (direction) {
            case LEFT:
            case RIGHT:
                y = sourceY;
                break;
            case DOWN:
            case RIGHTDOWN:
            case LEFTDOWN:
                y = sourceY-1;
                break;
            case UP:
            case LEFTUP:
            case RIGHTUP:
                y = sourceY+1;
                break;
        }
        return y;
    }
}
