package it.polimi.ingsw.PSP18.networking.messages.toclient;

import it.polimi.ingsw.PSP18.server.model.Direction;

import java.util.ArrayList;

public class AtlasBuildList extends BuildList {
    public AtlasBuildList(ArrayList<Direction> buildlist) {
        super(buildlist);
        this.type = ClientMessageType.ATLAS_BUILD_LIST;
    }
}
