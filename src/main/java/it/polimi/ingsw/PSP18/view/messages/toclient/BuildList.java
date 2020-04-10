package it.polimi.ingsw.PSP18.view.messages.toclient;

import it.polimi.ingsw.PSP18.model.Direction;

import java.util.ArrayList;

public class BuildList extends ClientAbstractMessage{

    private ArrayList<Direction> buildlist;

    /***
     * constructor of BuildList
     * @param buildlist array of possibles builds
     */
    public BuildList(ArrayList<Direction> buildlist){
        this.type=ClientMessageType.BUILD_LIST;
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
