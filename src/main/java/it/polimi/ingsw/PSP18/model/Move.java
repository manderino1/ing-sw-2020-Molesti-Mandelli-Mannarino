package it.polimi.ingsw.PSP18.model;

public class Move {
    private Direction direction;
    private Integer level;

    public Move(Direction direction, Integer level) {
        this.direction = direction;
        this.level = level;
    }

    public Integer getLevel(){
        return level;
    }

    public Direction getDirection() {
        return direction;
    }
}
