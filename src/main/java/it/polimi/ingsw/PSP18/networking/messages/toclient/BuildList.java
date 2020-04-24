package it.polimi.ingsw.PSP18.networking.messages.toclient;

import it.polimi.ingsw.PSP18.server.model.Direction;

import java.util.ArrayList;

/***
 * This message class is used to send the list of possible builds
 */
public class BuildList extends ClientAbstractMessage{

    protected ArrayList<Direction> buildList;

    /***
     * Constructor of BuildList, init the move list
     * @param buildList the list of possible moves
     */
    public BuildList(ArrayList<Direction> buildList){
        this.type=ClientMessageType.BUILD_LIST;
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
