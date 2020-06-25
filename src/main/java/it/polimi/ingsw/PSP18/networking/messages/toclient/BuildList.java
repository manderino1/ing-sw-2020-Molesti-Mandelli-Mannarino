package it.polimi.ingsw.PSP18.networking.messages.toclient;

import it.polimi.ingsw.PSP18.server.model.Direction;
import it.polimi.ingsw.PSP18.server.model.Worker;

import java.util.ArrayList;

/***
 * This message class is used to send the list of possible builds
 */
public class BuildList extends ClientAbstractMessage{

    protected ArrayList<Direction> buildList;
    protected Worker worker;

    /***
     * Constructor of BuildList, init the move list
     * @param buildList the list of possible moves
     * @param worker reference of the worker from who the build is done
     */
    public BuildList(ArrayList<Direction> buildList, Worker worker){
        this.type=ClientMessageType.BUILD_LIST;
        this.buildList= buildList;
        this.worker = worker;
    }

    /***
     * Returns the list of possible moves
     * @return the array of possibles moves
     */
    public ArrayList<Direction> getBuildlist() {
        return buildList;
    }

    /***
     * Return the worker position reference
     * @return the worker reference
     */
    public Worker getWorker() {
        return worker;
    }
}
