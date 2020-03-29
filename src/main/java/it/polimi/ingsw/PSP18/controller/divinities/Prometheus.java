package it.polimi.ingsw.PSP18.controller.divinities;

import it.polimi.ingsw.PSP18.controller.DirectionManagement;
import it.polimi.ingsw.PSP18.controller.PlayerManager;
import it.polimi.ingsw.PSP18.model.Direction;
import it.polimi.ingsw.PSP18.model.Worker;

import java.util.ArrayList;

public class Prometheus implements Divinity{
    private String name;
    private PlayerManager playerManager;

    // TODO : REMOVE IT
    private Direction direction;
    private Integer workerID;

    public String getName() {
        return name;
    }

    public void manageTurn(Boolean raiseForbidden) {
        Integer workerID = move(raiseForbidden);
        build(workerID);
    }

    /***
     * Prometeo prima di muovere può decidere se costruire, tale funzione è implementata da buildopt
     * @param raiseForbidden true if athena moved up one level
     */
    private Integer move(Boolean raiseForbidden) {

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
        Boolean buildchoice=buildopt(workerID);

        ArrayList<Direction> moves = checkMovementMoves(worker.getX(), worker.getY(), raiseForbidden, buildchoice);

         /*
            TODO: qui bisogna passare alla view l'arraylist moves
         */

        /*
            TODO: qui bisogna chiedere alla view la direzione dove voglio muovere il worker e la salvo in direction

                    Direction direction = ;
         */

        playerManager.setMove(worker.getX(), worker.getY(), direction);

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

        moves = checkMovementMoves(worker.getX(), worker.getY(), raiseForbidden, buildchoice);
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

        playerManager.setMove(worker.getX(), worker.getY(), direction);
        return workerID;
    }

    /***
     *
     * @param workerID ID of the worker that moved in the movement phase
     */
    private void build(Integer workerID) {

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
    }

    /***
     *
     * @param workerID ID of the worker that moved in the movement phase
     */
    private Boolean buildopt(Integer workerID) {

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
            TODO: qui bisogna chiedere alla view la direzione dove voglio costruire e la salvo in direction e se il giocatore non vuole costruire salva null in direction

                    Direction direction = ;
         */
        if (direction!=null) {
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
            return true;
        }
        else{
            return false;
        }
    }

    /***
     *
     * @param oldX the starting X coordinate of the worker
     * @param oldY the starting Y coordinate of the worker
     * @param raiseForbidden true if athena moved up one level
     * @return
     */
    private ArrayList<Direction> checkMovementMoves(Integer oldX, Integer oldY, Boolean raiseForbidden, Boolean buildchoice) {

        ArrayList<Direction> moves = new ArrayList<Direction>();

        for (Direction dir : Direction.values()) {
            Integer building;
            Integer newX = DirectionManagement.getX(oldX, dir);
            Integer newY = DirectionManagement.getY(oldY, dir);

            if (!raiseForbidden && !buildchoice) {
                if (!playerManager.getGameMap().getCell(newX, newY).getDome() && (playerManager.getGameMap().getCell(newX, newY).getBuilding() - playerManager.getGameMap().getCell(oldX, oldY).getBuilding() <= 1) && playerManager.getGameMap().getCell(newX, newY).getWorker() == null) {
                    moves.add(dir);
                }
            }

            else {
                if (!playerManager.getGameMap().getCell(newX, newY).getDome() && (playerManager.getGameMap().getCell(newX, newY).getBuilding() - playerManager.getGameMap().getCell(oldX, oldY).getBuilding() < 1) && playerManager.getGameMap().getCell(newX, newY).getWorker() == null) {
                    moves.add(dir);
                }
            }

        }
        return moves;
    }

    /***
     *
     * @param oldX the starting X coordinate of the worker
     * @param oldY the starting Y coordinate of the worker
     * @return
     */
    private ArrayList<Direction> checkBuildingMoves(Integer oldX, Integer oldY) {
        ArrayList<Direction> moves = new ArrayList<Direction>();

        for (Direction dir : Direction.values()) {
            Integer building;
            Integer newX = DirectionManagement.getX(oldX, dir);
            Integer newY = DirectionManagement.getY(oldY, dir);

            if(!playerManager.getGameMap().getCell(newX, newY).getDome() && playerManager.getGameMap().getCell(newX, newY).getWorker() == null){
                moves.add(dir);
            }
        }
        return moves;
    }

    /***
     *
     * @param raiseForbidden true if Athena moved up one level
     * @return
     */
    private Boolean checkForVictory(Boolean raiseForbidden){

        for (int i = 0; i < 2; i++) {
            Integer oldX = playerManager.getWorker(i).getX();
            Integer oldY = playerManager.getWorker(i).getY();

            if (!raiseForbidden) {
                if (playerManager.getGameMap().getCell(oldX, oldY).getBuilding() == 3) {
                    return true;
                }
            }
        }
        return false;
    }

    /***
     *
     * @param raiseForbidden true if athena moved up one level
     * @param movementPhase true if curretnly in movement phase, false if currently in building phase
     * @return
     */
    private Boolean checkForLose(Boolean raiseForbidden, Boolean movementPhase){
        for (int i = 0; i < 2; i++) {
            Integer oldX = playerManager.getWorker(i).getX();
            Integer oldY = playerManager.getWorker(i).getY();

            for (Direction dir : Direction.values()) {
                Integer building;
                Integer newX = DirectionManagement.getX(oldX, dir);
                Integer newY = DirectionManagement.getY(oldY, dir);

                if (!raiseForbidden) {
                    if (!playerManager.getGameMap().getCell(newX, newY).getDome() && (playerManager.getGameMap().getCell(newX, newY).getBuilding() - playerManager.getGameMap().getCell(oldX, oldY).getBuilding() <= 1) && playerManager.getGameMap().getCell(newX, newY).getWorker() == null) {
                        return false;
                    }
                } else {
                    if(movementPhase) {
                        if (!playerManager.getGameMap().getCell(newX, newY).getDome() && (playerManager.getGameMap().getCell(newX, newY).getBuilding() - playerManager.getGameMap().getCell(oldX, oldY).getBuilding() < 1) && playerManager.getGameMap().getCell(newX, newY).getWorker() == null) {
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
}

