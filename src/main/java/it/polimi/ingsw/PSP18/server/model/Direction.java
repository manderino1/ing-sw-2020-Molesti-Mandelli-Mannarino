package it.polimi.ingsw.PSP18.server.model;

public enum Direction {
    LEFT("LEFT"),
    LEFTUP("LEFTUP"),
    LEFTDOWN("LEFTDOWN"),
    RIGHT("LEFTDOWN"),
    RIGHTUP("LEFTDOWN"),
    RIGHTDOWN("LEFTDOWN"),
    UP("LEFTDOWN"),
    DOWN("LEFTDOWN");

    private final String name;

    Direction(String name){
        this.name = name;
    }

    public String toString(){return name;}
}
