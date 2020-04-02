package it.polimi.ingsw.PSP18.controller.divinities;

import it.polimi.ingsw.PSP18.controller.DirectionManagement;
import it.polimi.ingsw.PSP18.controller.PlayerManager;
import it.polimi.ingsw.PSP18.model.Direction;
import it.polimi.ingsw.PSP18.model.Move;
import it.polimi.ingsw.PSP18.model.Worker;

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
                    }
                    else if(playerManager.getGameMap().getCell(newX, newY).getWorker() == null){
                        moves.add(dir);
                    }
                }
            }
            else {
                if (!playerManager.getGameMap().getCell(newX, newY).getDome() &&
                        (playerManager.getGameMap().getCell(newX, newY).getBuilding() - playerManager.getGameMap().getCell(oldX, oldY).getBuilding() < 1)) {
                    if ((playerManager.getGameMap().getCell(newX, newY).getWorker() != null) &&
                            xIsInRange &&
                            yIsInRange &&
                            !playerManager.getGameMap().getCell(newX + (newX - oldX), newY + (newY - oldY)).getDome() &&
                            playerManager.getGameMap().getCell(newX + (newX - oldX), newY + (newY - oldY)).getWorker() == null) {
                        moves.add(dir);
                    }
                    else if(playerManager.getGameMap().getCell(newX, newY).getWorker() == null){
                        moves.add(dir);
                    }
                }
            }

        }
        return moves;
    }
}

