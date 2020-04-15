package it.polimi.ingsw.PSP18.networking.messages.toserver;

import it.polimi.ingsw.PSP18.server.model.Direction;

public class PrometheusBuildReceiver extends ServerAbstractMessage {
    private Integer workerID;

    /***
     * Constructor of PrometheusBuildReceiver
     * @param flag true if the player builds before moving
     * @param workerID the worker to move and build
     */
    public PrometheusBuildReceiver(Integer workerID) {
        this.type = ServerMessageType.PROMETHEUS_BUILD_RECEIVER;
        this.workerID = workerID;
    }

    /***
     * Returns the workerID
     * @return the workerID
     */
    public Integer getChosenWorkerID() {
        return workerID;
    }
}
