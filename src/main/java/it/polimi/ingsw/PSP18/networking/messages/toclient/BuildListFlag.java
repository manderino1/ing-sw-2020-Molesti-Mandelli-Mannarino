package it.polimi.ingsw.PSP18.networking.messages.toclient;

import it.polimi.ingsw.PSP18.server.model.Direction;

import java.util.ArrayList;

public class BuildListFlag extends ClientAbstractMessage{

    private ArrayList<Direction> buildList;

    /***
     * Constructor of BuildListFlag, init the move list
     * @param buildList the list of possible moves
     */
    public BuildListFlag(ArrayList<Direction> buildList){
        this.type = ClientMessageType.BUILD_LIST_FLAG;
        this.buildList= buildList;
    }

    /***
     * Returns the list of possible moves
     * @return the array of possibles moves
     */
    public ArrayList<Direction> getBuildlist() {
            return buildList;
        }

}
