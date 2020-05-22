package it.polimi.ingsw.PSP18.server;

import it.polimi.ingsw.PSP18.networking.SocketServer;
import it.polimi.ingsw.PSP18.server.controller.MatchSocket;
import it.polimi.ingsw.PSP18.server.model.MatchStatus;

import java.util.ArrayList;
import java.util.HashMap;

/***
 * Manages the multiple matches running into the server
 */
public class MatchManager {
    private HashMap<Integer, ArrayList<MatchSocket>> sizeMap = new HashMap<>();

    /***
     * Constructor that launch the socket server listener and init the first match
     */
    public MatchManager() {
        SocketServer socketServer = new SocketServer(this);
        sizeMap.put(2, new ArrayList<>());
        sizeMap.put(3, new ArrayList<>());
        socketServer.start();
    }

    /***
     * Returns che match to add the socket to
     * @return returns the active match to add the socket to
     */
    public MatchSocket getMatchSocket(int size) {
        sizeMap.get(size).removeIf(match -> match.getMatchStatus() == MatchStatus.MATCH_ENDED); // Remove ended 2 player matches

        if(sizeMap.get(size).size() == 0) { // If there isn't a match create it
            sizeMap.get(size).add(new MatchSocket(size));
            return sizeMap.get(size).get(sizeMap.get(size).size()-1);
        }

        if(sizeMap.get(size).get(sizeMap.get(size).size()-1).getSockets().size() >= size) { // If the match is full create a new one
            sizeMap.get(size).add(new MatchSocket(size));
        }
        return sizeMap.get(size).get(sizeMap.get(size).size()-1); // Return the last match in the array list
    }
}
