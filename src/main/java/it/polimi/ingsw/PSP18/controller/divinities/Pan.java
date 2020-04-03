package it.polimi.ingsw.PSP18.controller.divinities;

import it.polimi.ingsw.PSP18.controller.DirectionManagement;
import it.polimi.ingsw.PSP18.controller.PlayerManager;
import it.polimi.ingsw.PSP18.model.Direction;
import it.polimi.ingsw.PSP18.model.Move;
import it.polimi.ingsw.PSP18.model.Worker;

import java.util.ArrayList;

public class Pan extends Divinity{

    public Pan(String name, PlayerManager playerManager) {
        super(name, playerManager);
    }

    /***
     *
     * @param raiseForbidden true if Athena moved up one level
     * @return
     */
    @Override
    protected Boolean checkForVictory(Boolean raiseForbidden){

        for (int i = 0; i < 2; i++) {
            Integer oldX = playerManager.getWorker(i).getX();
            Integer oldY = playerManager.getWorker(i).getY();

            if (!raiseForbidden) {
                if ( (playerManager.getGameMap().getCell(oldX, oldY).getBuilding() == 3) || (playerManager.getPlayerData().getLastMove().getLevel() == -2) ) {
                    return true;
                }
            } else {
                if (playerManager.getPlayerData().getLastMove().getLevel() == -2) {
                    return true;
                }
            }
        }
        return false;
    }
}

