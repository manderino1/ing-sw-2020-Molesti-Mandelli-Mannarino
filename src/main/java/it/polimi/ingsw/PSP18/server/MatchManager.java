package it.polimi.ingsw.PSP18.server;

import it.polimi.ingsw.PSP18.networking.SocketServer;
import it.polimi.ingsw.PSP18.server.controller.Match;
import it.polimi.ingsw.PSP18.server.model.MatchStatus;

import java.util.ArrayList;

/***
 * Manages the multiple matches running into the server
 */
public class MatchManager {
    private SocketServer socketServer;
    private ArrayList<Match> matches2 = new ArrayList<>();
    private ArrayList<Match> matches3 = new ArrayList<>();

    /***
     * Constructor that launch the socket server listener and init the first match
     */
    public MatchManager() {
        this.socketServer = new SocketServer(this);
        socketServer.start();
    }

    /***
     * Returns che match to add the socket to
     * @return returns the active match to add the socket to
     */
    public Match getMatch(int size) {
        matches2.removeIf(match -> match.getMatchStatus() == MatchStatus.MATCH_ENDED); // Remove ended matches
        matches3.removeIf(match -> match.getMatchStatus() == MatchStatus.MATCH_ENDED); // Remove ended matches

        if(matches2.size() == 0 && size == 2) {
            matches2.add(new Match(2));
            return matches2.get(matches2.size()-1);
        }

        if(matches3.size() == 0 && size == 3) {
            matches3.add(new Match(3));
            return matches3.get(matches3.size()-1);
        }

        if(size == 2) {
            if(matches2.get(matches2.size()-1).getSockets().size() > 1) {
                matches2.add(new Match(2));
            }
            return matches2.get(matches2.size()-1);
        }

        if(size == 3) {
            if(matches3.get(matches3.size()-1).getSockets().size() > 2) {
                matches3.add(new Match(3));
            }
            return matches3.get(matches3.size()-1);
        }

        return null;
    }
}
