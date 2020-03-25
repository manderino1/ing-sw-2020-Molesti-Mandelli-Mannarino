package it.polimi.ingsw.PSP18.controller.divinities;

import it.polimi.ingsw.PSP18.controller.DirectionManagement;
import it.polimi.ingsw.PSP18.controller.PlayerManager;
import it.polimi.ingsw.PSP18.model.Building;
import it.polimi.ingsw.PSP18.model.Direction;
import it.polimi.ingsw.PSP18.model.Map;
import it.polimi.ingsw.PSP18.model.Worker;

import java.util.ArrayList;

public class Apollo implements Divinity{
    private String name;
    private PlayerManager playerManager;

    public void manageTurn(Boolean raiseForbidden) {

    }

    private void move() {

        /*
            TODO: qui bisogna chiedere alla view quale worker si ha intenzione di muovere e lo salvo in workerID

             Integer workerID = ;
         */

        Worker worker = playerManager.getWorker(workerID);
        Integer oldX = worker.getX();
        Integer oldY = worker.getY();

        Map map = playerManager.getMap();

        ArrayList<Direction> moves = checkMoves(oldX, oldY);

        /*
            TODO: qui bisogna chiedere alla view la direzione dove voglio muovere il worker e la salvo in direction

                    Direction direction = ;

         */

        playerManager.setMove(oldX,oldY,direction);

    }

    private void build() {
        setBuild(playerManager.getMap().getCell(worker.x, worker.y), newCell);
    }

    private ArrayList<Direction> checkMoves(Integer oldX, Integer oldY) {

        ArrayList<Direction> moves = new ArrayList<Direction>();

        for (Direction dir: Direction.values()) {
            Integer building;
            Integer newX = DirectionManagement.getX(oldX, dir);
            Integer newY = DirectionManagement.getY(oldY, dir);
            if(!playerManager.getMap().getCell(newX, newY).getDome() && (playerManager.getMap().getCell(newX,newY).getBuilding() - playerManager.getMap().getCell(oldX,oldY).getBuilding() <= 1)){
                moves.add(dir);
            }
        }

    }

    private Boolean checkHeight(Integer oldX, Integer oldY, Direction dir){
        if(playerManager.getMap().getCell(oldX,oldY).getBuilding() = ){

        }
    }
}

