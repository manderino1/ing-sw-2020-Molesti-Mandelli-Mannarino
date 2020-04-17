package it.polimi.ingsw.PSP18.server.model;

public enum Direction {
    LEFT("LEFT"),
    LEFTUP("LEFTUP"),
    LEFTDOWN("LEFTDOWN"),
    RIGHT("RIGHT"),
    RIGHTUP("RIGHTUP"),
    RIGHTDOWN("RIGHTDOWN"),
    UP("UP"),
    DOWN("DOWN");

    private final String name;

    Direction(String name){
        this.name = name;
    }

    public String toString(){return name;}
}
