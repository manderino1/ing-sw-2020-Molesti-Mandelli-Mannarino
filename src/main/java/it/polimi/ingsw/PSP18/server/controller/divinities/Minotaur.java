package it.polimi.ingsw.PSP18.server.controller.divinities;

import it.polimi.ingsw.PSP18.server.controller.DirectionManagement;
import it.polimi.ingsw.PSP18.server.controller.PlayerManager;
import it.polimi.ingsw.PSP18.server.model.Direction;
import it.polimi.ingsw.PSP18.server.model.Worker;

import java.util.ArrayList;

public class Minotaur extends Divinity{

    public Minotaur(String name, PlayerManager playerManager) {
        super(name, playerManager);
    }

    /***
     * function checks if
     * @param oldX
     * @param oldY
     * @param raiseForbidden
     * @return
     */
    @Override
    protected ArrayList<Direction> checkMovementMoves(Integer oldX, Integer oldY, Boolean raiseForbidden) {

        ArrayList<Direction> moves = new ArrayList<Direction>();

        for (Direction dir : Direction.values()) {
            Integer building;
            Integer newX = DirectionManagement.getX(oldX, dir);
            Integer newY = DirectionManagement.getY(oldY, dir);

            if(newX != -1 && newY != -1) {
                boolean xIsInRange = newX + (newX - oldX) <= 5 && newX + (newX - oldX) >= 0;
                boolean yIsInRange = newY + (newY - oldY) <= 5 && newY + (newY - oldY) >= 0;

                if (!raiseForbidden) {
                    if (!playerManager.getGameMap().getCell(newX, newY).getDome() &&
                            (playerManager.getGameMap().getCell(newX, newY).getBuilding() - playerManager.getGameMap().getCell(oldX, oldY).getBuilding() <= 1)) {
                        if ((playerManager.getGameMap().getCell(newX, newY).getWorker() != null) &&
                                xIsInRange &&
                                yIsInRange &&
                                !playerManager.getGameMap().getCell(newX + (newX - oldX), newY + (newY - oldY)).getDome() &&
                                playerManager.getGameMap().getCell(newX + (newX - oldX), newY + (newY - oldY)).getWorker() == null) {
                            moves.add(dir);
                        } else if (playerManager.getGameMap().getCell(newX, newY).getWorker() == null) {
                            moves.add(dir);
                        }
                    }
                } else {
                    if (!playerManager.getGameMap().getCell(newX, newY).getDome() &&
                            (playerManager.getGameMap().getCell(newX, newY).getBuilding() - playerManager.getGameMap().getCell(oldX, oldY).getBuilding() < 1)) {
                        if ((playerManager.getGameMap().getCell(newX, newY).getWorker() != null) &&
                                xIsInRange &&
                                yIsInRange &&
                                !playerManager.getGameMap().getCell(newX + (newX - oldX), newY + (newY - oldY)).getDome() &&
                                playerManager.getGameMap().getCell(newX + (newX - oldX), newY + (newY - oldY)).getWorker() == null) {
                            moves.add(dir);
                        } else if (playerManager.getGameMap().getCell(newX, newY).getWorker() == null) {
                            moves.add(dir);
                        }
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
            playerManager.getGameMap().setCell(oldX, oldY, playerManager.getGameMap().getCell(oldX, oldY).getBuilding(), null);
            playerManager.getGameMap().setCell(newX + (newX - oldX), newY + (newY - oldY), playerManager.getGameMap().getCell(newX + (newX - oldX), newY + (newY - oldY)).getBuilding(), destinationWorker);
            playerManager.getGameMap().getCell(newX + (newX - oldX), newY + (newY - oldY)).getWorker().setPosition(newX + (newX - oldX), newY + (newY - oldY));
        }
    }
}

