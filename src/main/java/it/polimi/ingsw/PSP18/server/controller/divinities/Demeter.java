package it.polimi.ingsw.PSP18.server.controller.divinities;

import it.polimi.ingsw.PSP18.networking.SocketThread;
import it.polimi.ingsw.PSP18.networking.messages.toclient.*;
import it.polimi.ingsw.PSP18.server.controller.DirectionManagement;
import it.polimi.ingsw.PSP18.server.controller.PlayerManager;
import it.polimi.ingsw.PSP18.server.controller.exceptions.InvalidBuildException;
import it.polimi.ingsw.PSP18.server.model.Direction;
import it.polimi.ingsw.PSP18.server.model.Worker;

import java.util.ArrayList;
/***
 * this is the class that implements Demeter's powers
 */
public class Demeter extends Divinity {
    boolean firstBuild;
    /***
     * Constructor of the class, initialize name and playerManager in Divinity
     * @param name the name of the divinity
     * @param playerManager the object that has this divinity
     */
    public Demeter(String name, PlayerManager playerManager) {
        super(name, playerManager);
    }

    /***
     * Method used to move
     */
    @Override
    protected void build() {
        Worker worker = playerManager.getWorker(workerID);
        moves = checkBuildingMoves(worker.getX(), worker.getY());

        if (moves.size() == 0) {
            manageLoss();
            return;
        }

        playerManager.getMatch().getCurrentSocket().sendMessage(new BuildList(moves, worker));

        firstBuild = true;
    }

    /***
     * Build in the selected direction
     * @param direction the direction of the wanted build
     */
    public void buildReceiver(Direction direction) {
        if (direction == null) { // If he doesn't want to move
            playerManager.getMatch().getCurrentSocket().sendMessage(new EndTurnAvaiable());
            return;
        }

        // Check if the build direction is valid
        if(!moves.contains(direction)) {
            try {
                throw new InvalidBuildException();
            } catch (InvalidBuildException e) {
                e.printStackTrace();
                if(firstBuild) {
                    build();
                } else {
                    playerManager.getMatch().getCurrentSocket().sendMessage(new BuildListFlag(moves, playerManager.getWorker(workerID)));
                }
                return;
            }
        }

        Worker worker = playerManager.getWorker(workerID);
        Integer newX = DirectionManagement.getX(worker.getX(), direction);
        Integer newY = DirectionManagement.getY(worker.getY(), direction);
        boolean dome = false;

        if (playerManager.getGameMap().getCell(newX, newY).getBuilding() == 3) {
            dome = true;
        }

        playerManager.setBuild(newX, newY, dome);

        if (firstBuild) {
            moves = checkBuildingMoves(worker.getX(), worker.getY());
            moves.remove(direction);
            firstBuild = false;
            playerManager.getMatch().getCurrentSocket().sendMessage(new BuildListFlag(moves, worker));
        }
        else{
            firstBuild=true;
            playerManager.getMatch().getCurrentSocket().sendMessage(new EndTurnAvaiable());
        }
    }
}
