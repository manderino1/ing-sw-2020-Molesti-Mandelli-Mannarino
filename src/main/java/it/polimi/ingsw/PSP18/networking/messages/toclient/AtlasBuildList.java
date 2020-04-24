package it.polimi.ingsw.PSP18.networking.messages.toclient;

import it.polimi.ingsw.PSP18.server.model.Direction;

import java.util.ArrayList;

/***
 * This message class is used for Atlas to send the list of possible builds
 */
public class AtlasBuildList extends BuildList {
    /***
     * Init the message type and the move list array
     * @param buildlist the possible moves
     */
    public AtlasBuildList(ArrayList<Direction> buildlist) {
        super(buildlist);
        this.type = ClientMessageType.ATLAS_BUILD_LIST;
    }
}
