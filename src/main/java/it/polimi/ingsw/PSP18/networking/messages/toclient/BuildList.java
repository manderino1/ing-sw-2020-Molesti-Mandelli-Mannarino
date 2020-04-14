package it.polimi.ingsw.PSP18.networking.messages.toclient;

import it.polimi.ingsw.PSP18.server.model.Direction;

import java.util.ArrayList;

public class BuildList extends ClientAbstractMessage{

    private ArrayList<Direction> buildlist;
    private Integer workerID;

    /***
     * constructor of BuildList
     * @param buildlist
     * @param workerID
     */
    public BuildList(ArrayList<Direction> buildlist, Integer workerID){
        this.type=ClientMessageType.BUILD_LIST;
        this.buildlist= buildlist;
        this.workerID = workerID;
    }

    /***
     * returns buildlist
     * @return the array of possibles moves
     */
    public ArrayList<Direction> getBuildlist() {
        return buildlist;
    }

    public Integer getWorkerID() { return workerID; }

}
