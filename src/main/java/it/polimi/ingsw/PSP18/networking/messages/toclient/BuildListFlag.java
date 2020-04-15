package it.polimi.ingsw.PSP18.networking.messages.toclient;

import it.polimi.ingsw.PSP18.server.model.Direction;

import java.util.ArrayList;

public class BuildListFlag extends ClientAbstractMessage{

    private ArrayList<Direction> buildlist;

    /***
     * constructor of BuildList
     * @param buildlist
     */
    public BuildListFlag(ArrayList<Direction> buildlist){
        this.type = ClientMessageType.BUILD_LIST_FLAG;
        this.buildlist= buildlist;
    }

    /***
     * returns buildlist
     * @return the array of possibles moves
     */
    public ArrayList<Direction> getBuildlist() {
            return buildlist;
        }

}
