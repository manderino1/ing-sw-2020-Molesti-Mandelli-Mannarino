package it.polimi.ingsw.PSP18.controller.divinities;

import it.polimi.ingsw.PSP18.controller.DirectionManagement;
import it.polimi.ingsw.PSP18.controller.PlayerManager;
import it.polimi.ingsw.PSP18.model.Direction;
import it.polimi.ingsw.PSP18.model.Worker;
import it.polimi.ingsw.PSP18.model.Move;

import java.util.ArrayList;

public class Ephaestus extends Divinity{

    public Ephaestus(String name, PlayerManager playerManager) {
        super(name, playerManager);
    }

    /***
     *
     * @param workerID ID of the worker that moved in the movement phase
     */
    @Override
    protected void build(Integer workerID) {

        if(checkForLose(true, false)){
            /*
               TODO: lost, jump to the end
            */
        }

        Worker worker = playerManager.getWorker(workerID);
        ArrayList<Direction> moves = checkBuildingMoves(worker.getX(), worker.getY());

        /*
            TODO: qui bisogna passare alla view l'arraylist moves
         */

         /*
            TODO: qui bisogna chiedere alla view la direzione dove voglio costruire e la salvo in direction

                    Direction direction = ;
         */

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

        // checking if she can build again

        if(checkForLose(true, false) || playerManager.getGameMap().getCell(newX, newY).getBuilding() == 3) {
            /*
               TODO: Can't build again run to the end of turn
            */
        }

        moves = new ArrayList<Direction>();
        moves.add(direction);

        /*
            TODO: qui bisogna passare alla view l'arraylist moves
         */

         /*
            TODO: qui bisogna chiedere alla view la direzione dove voglio costruire e la salvo in direction, se non vuole costruire ancora verrà restituito null in direction

                    Direction direction = ;
         */

         if(direction == null){
             // TODO: player doesn't want to build another block, so end turn
         }

        dome = false;

        playerManager.setBuild(newX, newY, dome);

    }
}

