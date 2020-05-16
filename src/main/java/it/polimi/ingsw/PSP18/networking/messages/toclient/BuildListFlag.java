package it.polimi.ingsw.PSP18.networking.messages.toclient;

import it.polimi.ingsw.PSP18.server.model.Direction;
import it.polimi.ingsw.PSP18.server.model.Worker;

import java.util.ArrayList;

/***
 * This message class is used to send the list of possible builds with possibility to choose to not build
 */
public class BuildListFlag extends BuildList {

    /***
     * Constructor of BuildListFlag, init the move list
     * @param buildList the list of possible moves
     */
    public BuildListFlag(ArrayList<Direction> buildList, Worker worker){
        super(buildList, worker);
        this.type = ClientMessageType.BUILD_LIST_FLAG;
    }
}
