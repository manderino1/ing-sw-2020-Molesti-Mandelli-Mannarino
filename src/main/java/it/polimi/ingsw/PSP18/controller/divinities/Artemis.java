package it.polimi.ingsw.PSP18.controller.divinities;

import it.polimi.ingsw.PSP18.controller.DirectionManagement;
import it.polimi.ingsw.PSP18.controller.PlayerManager;
import it.polimi.ingsw.PSP18.model.Direction;
import it.polimi.ingsw.PSP18.model.Move;
import it.polimi.ingsw.PSP18.model.Worker;

import java.util.ArrayList;

public class Artemis extends Divinity {
    public void manageTurn(Boolean raiseForbidden) {
        Integer workerID = move(raiseForbidden);
        build(workerID);
    }

    /***
     *
     * @param raiseForbidden true if athena moved up one level
     */
    @Override
    protected Integer move(Boolean raiseForbidden) {
        /*
            checking if the player lost
         */
        if(checkForLose(raiseForbidden, true)){
            /*
               TODO: lost, jump to the end
            */
        }

        /*
            TODO: qui bisogna chiedere alla view quale worker si ha intenzione di muovere e lo salvo in workerID

             Integer workerID = ;
         */

        Worker worker = playerManager.getWorker(workerID);
        ArrayList<Direction> moves = checkMovementMoves(worker.getX(), worker.getY(), raiseForbidden);

         /*
            TODO: qui bisogna passare alla view l'arraylist moves
         */

        /*
            TODO: qui bisogna chiedere alla view la direzione dove voglio muovere il worker e la salvo in direction

                    Direction direction = ;
         */

        setMove(worker.getX(), worker.getY(), direction);

        /*
            checking for  victory
         */
        if(checkForVictory(raiseForbidden)){
            /*
               TODO: victory, jump to the end
            */
        }

        /*
         Artemis can move twice
         */

        if(checkForLose(raiseForbidden, true)){
            /*
               TODO: can't move again
            */
            return workerID;
        }

        moves = checkMovementMoves(worker.getX(), worker.getY(), raiseForbidden);
        moves.remove(DirectionManagement.getOppositeDirection(direction));

         /*
            TODO: qui bisogna passare alla view l'arraylist moves
         */

        /*
            TODO: qui bisogna chiedere alla view la direzione dove voglio muovere il worker e la salvo in direction oppure se non vuole eseguire una seconda mossa restituirà null

                    Direction direction = ;
         */

        if(direction == null) {
            return workerID;
        }

        setMove(worker.getX(), worker.getY(), direction);
        return workerID;
    }
}


