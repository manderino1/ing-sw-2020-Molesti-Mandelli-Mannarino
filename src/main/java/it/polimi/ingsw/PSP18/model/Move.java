package it.polimi.ingsw.PSP18.model;

public class Move {
    private Direction direction;
    private Level level;

    public Move(Direction direction, Level level) {
        this.direction = direction;
        this.level = level;
    }

    public Level getLevel(){
        return level;
    }

    public Direction getDirection() {
        return direction;
    }
}
