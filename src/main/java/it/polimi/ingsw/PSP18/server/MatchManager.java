package it.polimi.ingsw.PSP18.server;

import it.polimi.ingsw.PSP18.networking.SocketServer;
import it.polimi.ingsw.PSP18.server.controller.Match;
import it.polimi.ingsw.PSP18.server.model.MatchStatus;

import java.util.ArrayList;

public class MatchManager {
    private SocketServer socketServer;
    private ArrayList<Match> matches = new ArrayList<>();

    public MatchManager() {
        this.matches.add(new Match());
        this.socketServer = new SocketServer(this);
        socketServer.start();
    }

    public Match getMatch() {
        matches.removeIf(match -> match.getMatchStatus() == MatchStatus.MATCH_ENDED); // Remove ended matches

        if(matches.get(matches.size()-1).getSockets().size() > 2 || matches.get(matches.size()-1).getMatchStatus() != MatchStatus.WAITING_FOR_PLAYERS) {  // Create a new match and return it
            matches.add(new Match());
        }

        return matches.get(matches.size()-1);
    }
}
