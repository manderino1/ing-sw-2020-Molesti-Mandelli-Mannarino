package it.polimi.ingsw.PSP18.controller.divinities;

import it.polimi.ingsw.PSP18.controller.DirectionManagement;
import it.polimi.ingsw.PSP18.controller.PlayerManager;
import it.polimi.ingsw.PSP18.model.Direction;
import it.polimi.ingsw.PSP18.model.Map;
import it.polimi.ingsw.PSP18.model.Worker;

import java.util.ArrayList;

public class Apollo implements Divinity{
    private String name;
    private PlayerManager playerManager;

    /***
     *
     * @param raiseForbidden true if athena moved up one level
     */

    public void manageTurn(Boolean raiseForbidden) {
        /*
            TODO: eseguire controllo vittoria
         */
        move(raiseForbidden);
        build();
    }

    private void move(Boolean raiseForbidden) {

        /*
            TODO: qui bisogna chiedere alla view quale worker si ha intenzione di muovere e lo salvo in workerID

             Integer workerID = ;
         */

        Worker worker = playerManager.getWorker(workerID);
        Integer oldX = worker.getX();
        Integer oldY = worker.getY();

        ArrayList<Direction> moves = checkMovementMoves(oldX, oldY, raiseForbidden);

         /*
            TODO: qui bisogna passare alla view l'arraylist moves
         */

        /*
            TODO: qui bisogna chiedere alla view la direzione dove voglio muovere il worker e la salvo in direction

                    Direction direction = ;
         */

        playerManager.setMove(oldX,oldY,direction);
    }

    private void build() {

        Worker worker = playerManager.getWorker(workerID);
        Integer oldX = worker.getX();
        Integer oldY = worker.getY();

        ArrayList<Direction> moves = checkBuildingMoves(oldX, oldY);
        /*
            TODO: qui bisogna passare alla view l'arraylist moves
         */

         /*
            TODO: qui bisogna chiedere alla view la direzione dove voglio costruire e la salvo in direction

                    Direction direction = ;
         */

        Integer newX = DirectionManagement.getX(oldX, direction);
        Integer newY = DirectionManagement.getY(oldY, direction);
        Boolean dome = false;

        /*
            in base alla direzione passatami dalla view
            se costruisco una cupola allora aggiorno il valore del flag dome controllando che la costruzione avvenga sopra il livello 3 di una torre
         */
        if(playerManager.getMap().getCell(newX, newY).getBuilding() == 3){
            dome = true;
        }

        /*
            TODO: è richiesta una funzione per la fase di costruzione costruzione setBuilding( destinazioneX, destinazioneY, domeFlag)
         */
        playerManager.setBuilding(newX, newY, dome);
    }


    private ArrayList<Direction> checkMovementMoves(Integer oldX, Integer oldY, Boolean raiseForbidden) {

        ArrayList<Direction> moves = new ArrayList<Direction>();

        for (Direction dir: Direction.values()) {
            Integer building;
            Integer newX = DirectionManagement.getX(oldX, dir);
            Integer newY = DirectionManagement.getY(oldY, dir);

            if (!raiseForbidden) {
                if (!playerManager.getMap().getCell(newX, newY).getDome() && (playerManager.getMap().getCell(newX, newY).getBuilding() - playerManager.getMap().getCell(oldX, oldY).getBuilding() <= 1)) {
                    moves.add(dir);
                }
            } else {
                if (!playerManager.getMap().getCell(newX, newY).getDome() && (playerManager.getMap().getCell(newX, newY).getBuilding() - playerManager.getMap().getCell(oldX, oldY).getBuilding() < 1)) {
                    moves.add(dir);
                }
            }
        }
    }

    private ArrayList<Direction> checkBuildingMoves(Integer oldX, Integer oldY){

    }
}

