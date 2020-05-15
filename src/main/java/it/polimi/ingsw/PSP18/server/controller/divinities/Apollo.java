package it.polimi.ingsw.PSP18.server.controller.divinities;

import it.polimi.ingsw.PSP18.server.controller.DirectionManagement;
import it.polimi.ingsw.PSP18.server.controller.PlayerManager;
import it.polimi.ingsw.PSP18.server.model.*;

import java.util.ArrayList;

/***
 * this is the class that implements Apollo's powers
 */
public class Apollo extends Divinity {
    /***
     * Constructor of the class, initialize name and playerManager in Divinity
     * @param name the name of the divinity
     * @param playerManager the object that has this divinity
     */
    public Apollo(String name, PlayerManager playerManager) {
        super(name, playerManager);
    }

    /***
     *
     * @param oldX the starting X coordinate of the worker
     * @param oldY the starting Y coordinate of the worker
     * @param raiseForbidden true if athena moved up one level
     * @return the list of possible move directions
     */
    @Override
    protected ArrayList<Direction> checkMovementMoves(Integer oldX, Integer oldY, Boolean raiseForbidden) {

        ArrayList<Direction> moves = new ArrayList<Direction>();

        for (Direction dir : Direction.values()) {
            Integer newX = DirectionManagement.getX(oldX, dir);
            Integer newY = DirectionManagement.getY(oldY, dir);

            if(newX != -1 && newY != -1) {
                if (!raiseForbidden) {
                    if (!playerManager.getGameMap().getCell(newX, newY).getDome()
                            && (playerManager.getGameMap().getCell(newX, newY).getBuilding() - playerManager.getGameMap().getCell(oldX, oldY).getBuilding() <= 1)
                            && playerManager.getGameMap().getCell(newX, newY).getWorker() != playerManager.getWorker(0)
                            && playerManager.getGameMap().getCell(newX, newY).getWorker() != playerManager.getWorker(1)) {
                        moves.add(dir);
                    }
                } else {
                    if (!playerManager.getGameMap().getCell(newX, newY).getDome()
                            && (playerManager.getGameMap().getCell(newX, newY).getBuilding() - playerManager.getGameMap().getCell(oldX, oldY).getBuilding() < 1)
                            && playerManager.getGameMap().getCell(newX, newY).getWorker() != playerManager.getWorker(0)
                            && playerManager.getGameMap().getCell(newX, newY).getWorker() != playerManager.getWorker(1)) {
                        moves.add(dir);
                    }
                }
            }
        }
        return moves;
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
        playerManager.getGameMap().getCell(newX, newY).getWorker().setPosition(newX, newY);
        if(destinationWorker == null) {
            playerManager.getGameMap().setCell(oldX, oldY, playerManager.getGameMap().getCell(oldX, oldY).getBuilding(), null);
        }
        else {
            playerManager.getGameMap().setCell(oldX, oldY, playerManager.getGameMap().getCell(oldX, oldY).getBuilding(), destinationWorker);
            playerManager.getGameMap().getCell(oldX, oldY).getWorker().setPosition(oldX, oldY);
        }
    }
}

