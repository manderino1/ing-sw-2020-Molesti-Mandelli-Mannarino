package it.polimi.ingsw.PSP18.controller.divinities;

import it.polimi.ingsw.PSP18.controller.DirectionManagement;
import it.polimi.ingsw.PSP18.controller.PlayerManager;
import it.polimi.ingsw.PSP18.model.Direction;
import it.polimi.ingsw.PSP18.model.Worker;

import java.util.ArrayList;

public class Artemis extends Divinity {
    boolean raiseForbidden, firstMove;

    public Artemis(String name, PlayerManager playerManager) {
        super(name, playerManager);
    }

    @Override
    protected void move(Boolean raiseForbidden) {
        /*
            checking if the player lost
         */
        if(checkForLose(raiseForbidden, true)){
            /*
               TODO: lost, jump to the end
            */
        }

        ArrayList<Direction> movesWorker1 = checkMovementMoves(playerManager.getWorker(0).getX(), playerManager.getWorker(0).getY(), raiseForbidden);
        ArrayList<Direction> movesWorker2 = checkMovementMoves(playerManager.getWorker(1).getX(), playerManager.getWorker(1).getY(), raiseForbidden);

         /*
            TODO: qui bisogna passare al client l'arraylist moves
         */

         this.raiseForbidden = raiseForbidden;
         this.firstMove = true;
    }

    /***
     *
     */
    @Override
    protected void moveReceiver(Direction direction, Integer workerID) {
        if(direction == null) { // If he doesn't want to move
            build();
            return;
        }

        Worker worker = playerManager.getWorker(workerID);
        this.workerID = workerID;
        setMove(worker.getX(), worker.getY(), direction);

        if(checkForVictory()){
            /*
               TODO: victory, jump to the end
            */
        }

        if(checkForLose(raiseForbidden, true)){
            /*
               TODO: can't move again
            */
        }

        if(firstMove) {
            ArrayList<Direction> moves = checkMovementMoves(worker.getX(), worker.getY(), raiseForbidden);
            moves.remove(DirectionManagement.getOppositeDirection(direction));
            firstMove = false;

            /*
            TODO: qui bisogna passare al client l'arraylist moves
             */
        }
        else {
            build();
        }
    }
}


