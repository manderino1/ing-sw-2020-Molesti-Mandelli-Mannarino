package it.polimi.ingsw.PSP18.server;

import it.polimi.ingsw.PSP18.server.controller.MatchManager;

/***
 * Runs a server instance
 */
public class Server {
    /***
     * Start the match manager
     * @param args cli args, not used
     */
    public static void main(String[] args) {
        MatchManager matchManager = new MatchManager();
    }
}
