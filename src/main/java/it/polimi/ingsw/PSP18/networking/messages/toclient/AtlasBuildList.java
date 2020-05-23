package it.polimi.ingsw.PSP18.networking.messages.toclient;

import it.polimi.ingsw.PSP18.server.model.Direction;
import it.polimi.ingsw.PSP18.server.model.Worker;

import java.util.ArrayList;

/***
 * This message class is used for Atlas to send the list of possible builds
 */
public class AtlasBuildList extends BuildList {
    /***
     * Init the message type and the move list array
     * @param buildlist the possible moves
     * @param worker the worker base where the build is done
     */
    public AtlasBuildList(ArrayList<Direction> buildlist, Worker worker) {
        super(buildlist, worker);
        this.type = ClientMessageType.ATLAS_BUILD_LIST;
    }
}
