package it.polimi.ingsw.PSP18.server.model;

/***
 * This is the class that contains all the possible moves directions
 */
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

    /***
     * initialize name field of the enum
     * @param name the string name of the direction
     */
    Direction(String name){
        this.name = name;
    }

    /***
     * Get the string name of the direction
     * @return the string name of the direction
     */
    public String toString(){return name;}
}
