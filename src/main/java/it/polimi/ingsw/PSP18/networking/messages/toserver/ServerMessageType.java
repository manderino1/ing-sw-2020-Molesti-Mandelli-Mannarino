package it.polimi.ingsw.PSP18.networking.messages.toserver;

/***
 * The class contains the list of all the messages used that will be sent to the server
 */
public enum ServerMessageType {
    MOVE_RECEIVER,
    PLAYER_DATA_RECEIVER,
    BUILD_RECEIVER,
    DIVINITY_RECEIVER,
    ENDTURN_RECEIVER,
    READY_RECEIVER,
    WORKER_RECEIVER,
    FLAG_MOVE_RECEIVER,
    PROMETHEUS_BUILD_RECEIVER,
    ATLAS_BUILD_RECEIVER
}
