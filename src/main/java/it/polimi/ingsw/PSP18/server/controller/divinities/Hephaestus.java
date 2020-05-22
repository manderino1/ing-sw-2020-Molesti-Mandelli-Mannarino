package it.polimi.ingsw.PSP18.server.controller.divinities;

import it.polimi.ingsw.PSP18.networking.SocketThread;
import it.polimi.ingsw.PSP18.networking.messages.toclient.*;
import it.polimi.ingsw.PSP18.server.controller.DirectionManagement;
import it.polimi.ingsw.PSP18.server.controller.PlayerManager;
import it.polimi.ingsw.PSP18.server.model.Direction;
import it.polimi.ingsw.PSP18.server.model.Worker;

import java.util.ArrayList;
/***
 * this is the class that implements Hephaestus' powers
 */
public class Hephaestus extends Divinity{
    private boolean firstBuild;
    /***
     * Constructor of the class, initialize name and playerManager in Divinity
     * @param name the name of the divinity
     * @param playerManager the object that has this divinity
     */
    public Hephaestus(String name, PlayerManager playerManager) {
        super(name, playerManager);
    }

    /***
     * Method used to build
     */
    @Override
    protected void build() {
        Worker worker = playerManager.getWorker(workerID);
        moves = checkBuildingMoves(worker.getX(), worker.getY());

        if (moves.size() == 0) {
            manageLoss();
            return;
        }

        matchSocket.getCurrentSocket().sendMessage(new BuildList(moves, worker));

        firstBuild = true;
    }

    /***
     * Build in the selected direction
     * @param direction the direction of the wanted build
     */
    public void buildReceiver(Direction direction) {
        if (direction == null) { // If he doesn't want to move
            matchSocket.getCurrentSocket().sendMessage(new EndTurnAvaiable());
            return;
        }

        // Check if the build direction is valid
        if(!moves.contains(direction)) {
            if(firstBuild) {
                build();
            } else {
                matchSocket.getCurrentSocket().sendMessage(new BuildListFlag(moves, playerManager.getWorker(workerID)));
            }
        }

        Worker worker = playerManager.getWorker(workerID);
        Integer newX = DirectionManagement.getX(worker.getX(), direction);
        Integer newY = DirectionManagement.getY(worker.getY(), direction);
        boolean dome = false;

        // If the height of the building cell is 3 a dome has to be placed
        if (playerManager.getGameMap().getCell(newX, newY).getBuilding() == 3) {
            dome = true;
        }

        playerManager.setBuild(newX, newY, dome);

        if (firstBuild && playerManager.getGameMap().getCell(newX, newY).getBuilding() != 3) {
            moves = new ArrayList<>();
            moves.add(direction);
            firstBuild = false;

            matchSocket.getCurrentSocket().sendMessage(new BuildListFlag(moves, worker));
        }
        else{
            firstBuild=true;
            matchSocket.getCurrentSocket().sendMessage(new EndTurnAvaiable());
        }
    }
}

