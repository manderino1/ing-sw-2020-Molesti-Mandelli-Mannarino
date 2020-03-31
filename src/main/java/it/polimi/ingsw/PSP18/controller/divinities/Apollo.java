package it.polimi.ingsw.PSP18.controller.divinities;

import it.polimi.ingsw.PSP18.controller.DirectionManagement;
import it.polimi.ingsw.PSP18.controller.PlayerManager;
import it.polimi.ingsw.PSP18.model.*;

import java.util.ArrayList;

/*
    TODO: nel caso che apollo si muova nella posizione di un worker avversario bisognera scambiare la posizione di questi due
 */

public class Apollo extends Divinity {
     /***
     *
     * @param oldX the starting X coordinate of the worker
     * @param oldY the starting Y coordinate of the worker
     * @param raiseForbidden true if athena moved up one level
     * @return
     */
    @Override
    protected ArrayList<Direction> checkMovementMoves(Integer oldX, Integer oldY, Boolean raiseForbidden) {

        ArrayList<Direction> moves = new ArrayList<Direction>();

        for (Direction dir : Direction.values()) {
            Integer building;
            Integer newX = DirectionManagement.getX(oldX, dir);
            Integer newY = DirectionManagement.getY(oldY, dir);

            if (!raiseForbidden) {
                if (!playerManager.getGameMap().getCell(newX, newY).getDome() && (playerManager.getGameMap().getCell(newX, newY).getBuilding() - playerManager.getGameMap().getCell(oldX, oldY).getBuilding() <= 1)) {
                    moves.add(dir);
                }
            } else {
                if (!playerManager.getGameMap().getCell(newX, newY).getDome() && (playerManager.getGameMap().getCell(newX, newY).getBuilding() - playerManager.getGameMap().getCell(oldX, oldY).getBuilding() < 1)) {
                    moves.add(dir);
                }
            }
        }
        return moves;
    }

    /***
     *
     * @param raiseForbidden true if athena moved up one level
     * @param movementPhase true if curretnly in movement phase, false if currently in building phase
     * @return
     */
    @Override
    protected Boolean checkForLose(Boolean raiseForbidden, Boolean movementPhase){
        for (int i = 0; i < 2; i++) {
            Integer oldX = playerManager.getWorker(i).getX();
            Integer oldY = playerManager.getWorker(i).getY();

            for (Direction dir : Direction.values()) {
                Integer building;
                Integer newX = DirectionManagement.getX(oldX, dir);
                Integer newY = DirectionManagement.getY(oldY, dir);

                if (!raiseForbidden) {
                    if (!playerManager.getGameMap().getCell(newX, newY).getDome() && (playerManager.getGameMap().getCell(newX, newY).getBuilding() - playerManager.getGameMap().getCell(oldX, oldY).getBuilding() <= 1)) {
                        return false;
                    }
                } else {
                    if(movementPhase) {
                        if (!playerManager.getGameMap().getCell(newX, newY).getDome() && (playerManager.getGameMap().getCell(newX, newY).getBuilding() - playerManager.getGameMap().getCell(oldX, oldY).getBuilding() < 1)) {
                            return false;
                        }
                    } else{
                        if(!playerManager.getGameMap().getCell(newX, newY).getDome() && playerManager.getGameMap().getCell(newX, newY).getWorker() == null){
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    /***
     * Set worker in a cell and remove from the source one
     * If a worker is present in the destination cell (Apollo) switch workers
     * @param oldX the source x position
     * @param oldY the source y position
     * @param newX the destination x position
     * @param newY the destination y position
     */
    @Override
    protected void updateMoveCells(Integer oldX, Integer oldY, Integer newX, Integer newY) {
        Worker destinationWorker = playerManager.getGameMap().getCell(newX, newY).getWorker();
        playerManager.getGameMap().setCell(newX, newY, playerManager.getGameMap().getCell(newX, newY).getBuilding(), playerManager.getGameMap().getCell(oldX, oldY).getWorker());
        if(destinationWorker == null) {
            playerManager.getGameMap().setCell(oldX, oldY, playerManager.getGameMap().getCell(oldX, oldY).getBuilding(), null);
        }
        else {
            playerManager.getGameMap().setCell(oldX, oldY, playerManager.getGameMap().getCell(oldX, oldY).getBuilding(), destinationWorker);
        }
    }
}

