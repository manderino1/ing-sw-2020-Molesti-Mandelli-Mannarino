package it.polimi.ingsw.PSP18.networking.messages.toclient;

/***
 * Every field in the enum is a different message to be sent to client
 */
public enum ClientMessageType {
    GAME_MAP_UPDATE,
    PLAYER_DATA_UPDATE,
    MOVE_LIST,
    BUILD_LIST,
    MATCH_WON,
    MATCH_LOST,
    START_MATCH,
    READY,
    DIVINITY_LIST,
    WAITING_NICK,
    PLACE_READY,
    SINGLE_MOVE_LIST,
    PROMETHEUS_BUILD_LIST,
    BUILD_LIST_FLAG,
    END_TURN,
    ATLAS_BUILD_LIST,
    PING,
    DIVINITY_PICK,
    PLAYER_NUMBER_READY
}
