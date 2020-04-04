package it.polimi.ingsw.PSP18.controller.divinities;

import it.polimi.ingsw.PSP18.controller.DirectionManagement;
import it.polimi.ingsw.PSP18.controller.PlayerManager;
import it.polimi.ingsw.PSP18.model.Direction;
import it.polimi.ingsw.PSP18.model.Worker;
import it.polimi.ingsw.PSP18.model.Move;

import java.util.ArrayList;

public class Ephaestus extends Divinity{
    boolean firstBuild;

    public Ephaestus(String name, PlayerManager playerManager) {
        super(name, playerManager);
    }

    /***
     *
     */
    @Override
    protected void build() {
        if (checkForLose(true, false)) {
            /*
               TODO: lost, jump to the end
            */
        }

        Worker worker = playerManager.getWorker(workerID);
        ArrayList<Direction> moves = checkBuildingMoves(worker.getX(), worker.getY());

        /*
            TODO: qui bisogna passare alla view l'arraylist moves
         */

        firstBuild = true;
    }

    /***
     * Build in the selected direction
     * @param direction the direction of the wanted build
     */
    public void buildReceiver(Direction direction) {
        if (direction == null) { // If he doesn't want to move
            return;
        }

        Worker worker = playerManager.getWorker(workerID);
        Integer newX = DirectionManagement.getX(worker.getX(), direction);
        Integer newY = DirectionManagement.getY(worker.getY(), direction);
        Boolean dome = false;

        /*
            in base alla direzione passatami dalla view
            se costruisco una cupola allora aggiorno il valore del flag dome controllando che la costruzione avvenga sopra il livello 3 di una torre
         */
        if (playerManager.getGameMap().getCell(newX, newY).getBuilding() == 3) {
            dome = true;
        }

        playerManager.setBuild(newX, newY, dome);

        if (firstBuild && playerManager.getGameMap().getCell(newX, newY).getBuilding() != 3) {
            ArrayList<Direction> moves = new ArrayList<Direction>();
            moves.add(direction);
            firstBuild = false;

            /*
            TODO: qui bisogna passare al client l'arraylist moves
             */
        }
    }
}

