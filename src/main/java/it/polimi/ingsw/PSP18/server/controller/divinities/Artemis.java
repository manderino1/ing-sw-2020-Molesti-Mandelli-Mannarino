package it.polimi.ingsw.PSP18.server.controller.divinities;

import it.polimi.ingsw.PSP18.networking.messages.toclient.*;
import it.polimi.ingsw.PSP18.server.controller.DirectionManagement;
import it.polimi.ingsw.PSP18.server.controller.MatchRun;
import it.polimi.ingsw.PSP18.server.controller.MatchSocket;
import it.polimi.ingsw.PSP18.server.controller.PlayerManager;
import it.polimi.ingsw.PSP18.server.model.Direction;
import it.polimi.ingsw.PSP18.server.model.Worker;

import java.util.ArrayList;
/***
 * this is the class that implements Artemis's powers
 */
public class Artemis extends Divinity {
    private boolean firstMove;
    /***
     * Constructor of the class, initialize name and playerManager in Divinity
     * @param name the name of the divinity
     * @param playerManager the object that has this divinity
     * @param matchRun reference of the match running management section
     * @param matchSocket for obtaining info about sockets and players connected to the match
     */
    public Artemis(String name, PlayerManager playerManager, MatchSocket matchSocket, MatchRun matchRun) {
        super(name, playerManager, matchSocket, matchRun);
    }

    /***
     *  Artemis can move twice in a row, this methods manages the first half of the movement
     *  First part of the movement phase
     */
    @Override
    protected void move() {
        movesWorker1 = checkMovementMoves(playerManager.getWorker(0).getX(), playerManager.getWorker(0).getY(), raiseForbidden);
        movesWorker2 = checkMovementMoves(playerManager.getWorker(1).getX(), playerManager.getWorker(1).getY(), raiseForbidden);

        // Check if the player has lost
        if (movesWorker1.size() == 0 && movesWorker2.size() == 0) {
            manageLoss();
            return;
        }

        matchSocket.getCurrentSocket().sendMessage(new MoveList(movesWorker1, movesWorker2, playerManager.getWorker(0), playerManager.getWorker(1)));
        this.firstMove = true;
    }

    /***
     * Artemis can move twice in a row, this methods manages both the first and second move using the
     * firstMove parameter
     * Moves in the selected direction
     * @param direction the direction of the movement, if null, the players wants to skip the second move
     * @param workerID the ID of the worker that we want to move
     */
    @Override
    public void moveReceiver(Direction direction, Integer workerID) {
        if(direction == null) { // If he doesn't want to move
            build();
            return;
        }

        Worker worker = playerManager.getWorker(workerID);
        this.workerID = workerID;
        setMove(worker.getX(), worker.getY(), direction);

        if(checkForVictory(workerID)){
            if(checkForVictory(workerID)){
                matchRun.endMatch(playerManager);
                return;
            }
        }
        if(firstMove) {
            ArrayList<Direction> moves = checkMovementMoves(worker.getX(), worker.getY(), raiseForbidden);
            moves.remove(DirectionManagement.getOppositeDirection(direction));
            if (moves.size() == 0) {
                manageLoss();
                return;
            }
            firstMove = false;
            matchSocket.getCurrentSocket().sendMessage(new SingleMoveList(moves, workerID, true, playerManager.getWorker(workerID)));
        }
        else {
            build();
        }
    }
}


