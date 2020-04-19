package it.polimi.ingsw.PSP18.server.controller.divinities;

import it.polimi.ingsw.PSP18.server.controller.PlayerManager;

public class Pan extends Divinity{

    public Pan(String name, PlayerManager playerManager) {
        super(name, playerManager);
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

