package it.polimi.ingsw.PSP18.controller.divinities;

import it.polimi.ingsw.PSP18.controller.DirectionManagement;
import it.polimi.ingsw.PSP18.controller.PlayerManager;
import it.polimi.ingsw.PSP18.model.Direction;
import it.polimi.ingsw.PSP18.model.Move;

public class Prometheus implements Divinity{
    private String name;
    private PlayerManager playerManager;

    public String getName() {
        return name;
    }

    public void manageTurn(Boolean raiseForbidden) {

    }

    private void move() {
    }

    private void build() {
    }

    /***
     *
     * @param oldX the old position of the worker on the x axis
     * @param oldY the old position of the worker on the y axis
     * @param direction the direction of the move
     */
    public void setMove(Integer oldX, Integer oldY, Direction direction) {
        Integer newX = DirectionManagement.getX(oldX, direction);
        Integer newY = DirectionManagement.getY(oldY, direction);
        updateMoveCells(oldX, oldY, newX, newY);

        if(playerManager.getGameMap().getCell(newX, newY).getBuilding() - playerManager.getGameMap().getCell(oldX, oldY).getBuilding() == 1){
            playerManager.getPlayerData().setLastMove(new Move(direction, 1));
        }
        else if(playerManager.getGameMap().getCell(newX, newY).getBuilding() - playerManager.getGameMap().getCell(oldX, oldY).getBuilding() == 0) {
            playerManager.getPlayerData().setLastMove(new Move(direction, 0));
        }
        else if(playerManager.getGameMap().getCell(newX, newY).getBuilding() - playerManager.getGameMap().getCell(oldX, oldY).getBuilding() == -1) {
            playerManager.getPlayerData().setLastMove(new Move(direction, -1));
        }
        else {
            playerManager.getPlayerData().setLastMove(new Move(direction, -2));
        }
    }

    /***
     * Set worker in a cell and remove from the source one
     * If a worker is present in the destination cell (Apollo) switch workers
     * @param oldX the source x position
     * @param oldY the source y position
     * @param newX the destination x position
     * @param newY the destination y position
     */
    private void updateMoveCells(Integer oldX, Integer oldY, Integer newX, Integer newY) {
        playerManager.getGameMap().setCell(newX, newY, playerManager.getGameMap().getCell(newX, newY).getBuilding(), playerManager.getGameMap().getCell(oldX, oldY).getWorker());
        playerManager.getGameMap().setCell(oldX, oldY, playerManager.getGameMap().getCell(oldX, oldY).getBuilding(), null);
    }
}

