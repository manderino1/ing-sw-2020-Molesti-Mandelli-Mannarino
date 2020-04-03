package it.polimi.ingsw.PSP18.controller.divinities;

import it.polimi.ingsw.PSP18.controller.DirectionManagement;
import it.polimi.ingsw.PSP18.controller.PlayerManager;
import it.polimi.ingsw.PSP18.model.Direction;
import it.polimi.ingsw.PSP18.model.Worker;
import java.util.ArrayList;

public class Prometheus extends Divinity{
    boolean buildChoice, firstBuild;

    public Prometheus(String name, PlayerManager playerManager) {
        super(name, playerManager);
    }

    /***
     *
     * @param raiseForbidden true if athena moved up one level
     */
    public void manageTurn(Boolean raiseForbidden) {
        this.raiseForbidden = raiseForbidden;
        /*
            TODO: richiedi al giocatore quale worker usare
         */
    }

    private void workerSelect(Integer workerID) {
        this.workerID = workerID;
    }

    /***
     * Prometeo prima di muovere può decidere se costruire, tale funzione è implementata da buildopt
     */
    @Override
    protected void move() {
        if(checkForLose(raiseForbidden, true)){
            /*
               TODO: lost, jump to the end
            */
        }

        Worker worker = playerManager.getWorker(workerID);
        ArrayList<Direction> movesWorker1 = checkMovementMoves(playerManager.getWorker(0).getX(), playerManager.getWorker(0).getY(), raiseForbidden, buildChoice);
        ArrayList<Direction> movesWorker2 = checkMovementMoves(playerManager.getWorker(1).getX(), playerManager.getWorker(1).getY(), raiseForbidden, buildChoice);

         /*
            TODO: qui bisogna passare alla view l'arraylist moves
         */
    }

    /***
     *
     */
    @Override
    protected void build() {

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
    }

    @Override
    protected void buildReceiver(Direction direction) {
        /*
            TODO: qui bisogna chiedere alla view la direzione dove voglio costruire e la salvo in direction e se il giocatore non vuole costruire salva null in direction

                    Direction direction = ;
         */
        if (direction!=null) {
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
            buildChoice = true;
            if(firstBuild) move();
        }
        else {
            buildChoice = false;
            move();
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

            if(newX != -1 && newY != -1) {
                if (!raiseForbidden && !buildchoice) {
                    if (!playerManager.getGameMap().getCell(newX, newY).getDome() && (playerManager.getGameMap().getCell(newX, newY).getBuilding() - playerManager.getGameMap().getCell(oldX, oldY).getBuilding() <= 1) && playerManager.getGameMap().getCell(newX, newY).getWorker() == null) {
                        moves.add(dir);
                    }
                } else {
                    if (!playerManager.getGameMap().getCell(newX, newY).getDome() && (playerManager.getGameMap().getCell(newX, newY).getBuilding() - playerManager.getGameMap().getCell(oldX, oldY).getBuilding() < 1) && playerManager.getGameMap().getCell(newX, newY).getWorker() == null) {
                        moves.add(dir);
                    }
                }
            }
        }
        return moves;
    }
}

