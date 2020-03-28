package it.polimi.ingsw.PSP18.controller.divinities;

import it.polimi.ingsw.PSP18.controller.DirectionManagement;
import it.polimi.ingsw.PSP18.controller.PlayerManager;
import it.polimi.ingsw.PSP18.model.Direction;
import it.polimi.ingsw.PSP18.model.Level;
import it.polimi.ingsw.PSP18.model.Move;
import it.polimi.ingsw.PSP18.model.Worker;

import java.util.ArrayList;

public class Minotaur implements Divinity{
    private String name;
    private PlayerManager playerManager;

    public String getName() {
        return name;
    }

    /***
     *
     * @param raiseForbidden true if athena moved up one level
     */
    public void manageTurn(Boolean raiseForbidden) {
        Integer workerID = move(raiseForbidden);
        build(workerID);
    }

    /***
     *
     * @param raiseForbidden
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
        ArrayList<Direction> moves = checkMovementMoves(worker.getX(), worker.getY(), raiseForbidden);

         /*
            TODO: qui bisogna passare alla view l'arraylist moves
         */

        /*
            TODO: qui bisogna chiedere alla view la direzione dove voglio muovere il worker e la salvo in direction

                    Direction direction = ;
         */

        Integer newX = DirectionManagement.getX(worker.getX(), direction);
        Integer newY = DirectionManagement.getY(worker.getY(), direction);

        if(playerManager.getGameMap().getCell(newX, newY).getBuilding() - playerManager.getGameMap().getCell(worker.getX(), worker.getY()).getBuilding() == 1){
            playerManager.getPlayerData().setLastMove(new Move(direction, Level.UP));
        }
        else if(playerManager.getGameMap().getCell(newX, newY).getBuilding() - playerManager.getGameMap().getCell(worker.getX(), worker.getY()).getBuilding() == 0) {
            playerManager.getPlayerData().setLastMove(new Move(direction, Level.SAME));
        }
        else {
            playerManager.getPlayerData().setLastMove(new Move(direction, Level.DOWN));
        }
        playerManager.setMove(worker.getX(), worker.getY(), direction);
        
        /*
            checking for  victory
         */
        if(checkForVictory(raiseForbidden)){
            /*
               TODO: victory, jump to the end
            */
        }

        return workerID;
    }

    /***
     *
     * @param workerID
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
     * @param oldX
     * @param oldY
     * @param raiseForbidden
     * @return
     */
    private ArrayList<Direction> checkMovementMoves(Integer oldX, Integer oldY, Boolean raiseForbidden) {

        ArrayList<Direction> moves = new ArrayList<Direction>();

        for (Direction dir : Direction.values()) {
            Integer building;
            Integer newX = DirectionManagement.getX(oldX, dir);
            Integer newY = DirectionManagement.getY(oldY, dir);

            if(newX + (newX- oldX)<=5 && newX + (newX-oldX)>=0 && newY + (newY- oldY)<=5 && newY + (newY-oldY)>=0){
                if (!raiseForbidden) {
                    if (!playerManager.getGameMap().getCell(newX, newY).getDome() && (playerManager.getGameMap().getCell(newX, newY).getBuilding() - playerManager.getGameMap().getCell(oldX, oldY).getBuilding() <= 1)) {
                        moves.add(dir);
                    }
                }
                else {
                    if (!playerManager.getGameMap().getCell(newX, newY).getDome() && (playerManager.getGameMap().getCell(newX, newY).getBuilding() - playerManager.getGameMap().getCell(oldX, oldY).getBuilding() < 1)) {
                        moves.add(dir);
                    }
                }
            }
        }
        return moves;
    }

    /***
     *
     * @param oldX
     * @param oldY
     * @return
     */
    private ArrayList<Direction> checkBuildingMoves(Integer oldX, Integer oldY) {
        ArrayList<Direction> moves = new ArrayList<Direction>();

        for (Direction dir : Direction.values()) {
            Integer building;
            Integer newX = DirectionManagement.getX(oldX, dir);
            Integer newY = DirectionManagement.getY(oldY, dir);

            if(!playerManager.getGameMap().getCell(newX, newY).getDome()){
                moves.add(dir);
            }
        }
    }

    /***
     *
     * @param raiseForbidden
     * @return
     */
    private Boolean checkForVictory(Boolean raiseForbidden){

        for (int i = 0; i < 2; i++) {
            Integer oldX = playerManager.getWorker(i).getX();
            Integer oldY = playerManager.getWorker(i).getY();

            for (Direction dir : Direction.values()) {
                Integer building;
                Integer newX = DirectionManagement.getX(oldX, dir);
                Integer newY = DirectionManagement.getY(oldY, dir);

                if (!raiseForbidden) {
                    if (!playerManager.getGameMap().getCell(newX, newY).getDome() && (playerManager.getGameMap().getCell(newX, newY).getBuilding() == 3)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /***
     *
     * @param raiseForbidden
     * @param movementPhase
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
                    if (!playerManager.getGameMap().getCell(newX, newY).getDome() && (playerManager.getGameMap().getCell(newX, newY).getBuilding() - playerManager.getGameMap().getCell(oldX, oldY).getBuilding() <= 1)) {
                        return false;
                    }
                } else {
                    if(movementPhase) {
                        if (!playerManager.getGameMap().getCell(newX, newY).getDome() && (playerManager.getGameMap().getCell(newX, newY).getBuilding() - playerManager.getGameMap().getCell(oldX, oldY).getBuilding() < 1)) {
                            return false;
                        }
                    } else{
                        if(!playerManager.getGameMap().getCell(newX, newY).getDome()){
                            return false;
                        }
                    }
                }
            }
            return true;
        }
    }
}

