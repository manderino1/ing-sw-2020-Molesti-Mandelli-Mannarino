package it.polimi.ingsw.PSP18.server.controller.divinities;

import it.polimi.ingsw.PSP18.server.controller.DirectionManagement;
import it.polimi.ingsw.PSP18.server.controller.MatchRun;
import it.polimi.ingsw.PSP18.server.controller.MatchSocket;
import it.polimi.ingsw.PSP18.server.controller.PlayerManager;
import it.polimi.ingsw.PSP18.server.model.Direction;
/***
 * this is the class that implements Athena's powers
 */
public class Athena extends Divinity {
    /***
     * Constructor of the class, initialize divinity parameters name and playerManager
     * @param name the name of the divinity
     * @param playerManager the playerManager reference
     * @param matchRun reference of the match running management section
     * @param matchSocket for obtaining info about sockets and players connected to the match
     */
    public Athena(String name, PlayerManager playerManager, MatchSocket matchSocket, MatchRun matchRun) {
        super(name, playerManager, matchSocket, matchRun);
    }
}

