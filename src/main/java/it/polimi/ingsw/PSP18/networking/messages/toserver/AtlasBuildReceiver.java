package it.polimi.ingsw.PSP18.networking.messages.toserver;

import it.polimi.ingsw.PSP18.server.model.Direction;

/***
 * This is the class that represents a message used to signal that atlas wants to move and may use his power
 */
public class AtlasBuildReceiver extends BuildReceiver {
    private boolean dome;

    public AtlasBuildReceiver(Direction direction, boolean dome) {
        super(direction);
        this.type = ServerMessageType.ATLAS_BUILD_RECEIVER;
        this.dome = dome;
    }

    public boolean isDome() {
        return dome;
    }
}
