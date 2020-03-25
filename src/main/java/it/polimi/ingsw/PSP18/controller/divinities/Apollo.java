package it.polimi.ingsw.PSP18.controller.divinities;

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
           switch (dir){
               case UP:
                    if(playerManager.getMap().getCell(oldX, oldY + 1).getDome() || (playerManager.getMap().getCell(oldX,oldY+1).getBuilding() - playerManager.getMap().getCell(oldX,oldY).getBuilding() <= 1)){
                        moves.add(dir);
                    }
                   break;
               case DOWN:
                   if((playerManager.getMap().getCell(oldX,oldY+1).getBuilding() != ) || playerManager.getMap().getCell(oldX,oldY+1).getBuilding() ){
                       moves.add(dir);
                   }
                   break;
               case LEFT:

                   break;
               case RIGHT:

                   break;
               case LEFTUP:

                   break;
               case RIGHTUP:

                   break;
               case LEFTDOWN:

                   break;
               case RIGHTDOWN:

                   break;
        }

            moves.add(dir);
        }

    }

    private Boolean checkHeight(Integer oldX, Integer oldY, Direction dir){
        if(playerManager.getMap().getCell(oldX,oldY).getBuilding() = ){

        }
    }
}

