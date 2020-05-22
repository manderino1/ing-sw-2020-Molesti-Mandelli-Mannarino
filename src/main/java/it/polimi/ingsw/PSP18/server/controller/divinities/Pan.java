package it.polimi.ingsw.PSP18.server.controller.divinities;

import it.polimi.ingsw.PSP18.server.controller.MatchRun;
import it.polimi.ingsw.PSP18.server.controller.MatchSocket;
import it.polimi.ingsw.PSP18.server.controller.PlayerManager;
/***
 * this is the class that implements Pan's powers
 */
public class Pan extends Divinity{
    /***
     * Constructor of the class, initialize name and playerManager in Divinity
     * @param name the name of the divinity
     * @param playerManager the object that has this divinity
     */
    public Pan(String name, PlayerManager playerManager, MatchSocket matchSocket, MatchRun matchRun) {
        super(name, playerManager, matchSocket, matchRun);
    }

    /***
     * Checks if the player has won
     * @return true if he has won
     */
    @Override
    protected Boolean checkForVictory(int workerID){

        Integer oldX = playerManager.getWorker(workerID).getX();
        Integer oldY = playerManager.getWorker(workerID).getY();

        if ( (playerManager.getGameMap().getCell(oldX, oldY).getBuilding() == 3 && playerManager.getPlayerData().getLastMove().getLevel() >= 1) || (playerManager.getPlayerData().getLastMove().getLevel() <= -2) ) {
            return true;
        }
        return false;
    }
}

