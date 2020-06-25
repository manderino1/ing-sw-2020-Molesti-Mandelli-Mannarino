package it.polimi.ingsw.PSP18.server.controller.divinities;

import it.polimi.ingsw.PSP18.networking.SocketThread;
import it.polimi.ingsw.PSP18.networking.messages.toclient.*;
import it.polimi.ingsw.PSP18.server.controller.DirectionManagement;
import it.polimi.ingsw.PSP18.server.controller.MatchRun;
import it.polimi.ingsw.PSP18.server.controller.MatchSocket;
import it.polimi.ingsw.PSP18.server.controller.PlayerManager;
import it.polimi.ingsw.PSP18.server.model.Direction;
import it.polimi.ingsw.PSP18.server.model.Worker;

import java.util.ArrayList;
/***
 * this is the class that implements Demeter's powers
 */
public class Demeter extends Divinity {
    private boolean firstBuild;
    /***
     * Constructor of the class, initialize name and playerManager in Divinity
     * @param name the name of the divinity
     * @param playerManager the object that has this divinity
     * @param matchRun reference of the match running management section
     * @param matchSocket for obtaining info about sockets and players connected to the match
     */
    public Demeter(String name, PlayerManager playerManager, MatchSocket matchSocket, MatchRun matchRun) {
        super(name, playerManager, matchSocket, matchRun);
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

        matchSocket.getCurrentSocket().sendMessage(new BuildList(moves, worker));

        firstBuild = true;
    }

    /***
     * Build in the selected direction
     * @param direction the direction of the wanted build
     */
    public void buildReceiver(Direction direction) {
        if (direction == null) { // If he doesn't want to move
            if(firstBuild) {
                build();
            }
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

        if (playerManager.getGameMap().getCell(newX, newY).getBuilding() == 3) {
            dome = true;
        }

        playerManager.setBuild(newX, newY, dome);

        if (firstBuild) {
            moves = checkBuildingMoves(worker.getX(), worker.getY());
            moves.remove(direction);
            firstBuild = false;
            matchSocket.getCurrentSocket().sendMessage(new BuildListFlag(moves, worker));
        }
        else{
            firstBuild=true;
            matchSocket.getCurrentSocket().sendMessage(new EndTurnAvaiable());
        }
    }
}
