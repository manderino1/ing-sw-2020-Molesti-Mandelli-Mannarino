package it.polimi.ingsw.PSP18.server.controller.divinities;

import it.polimi.ingsw.PSP18.networking.SocketThread;
import it.polimi.ingsw.PSP18.networking.messages.toclient.*;
import it.polimi.ingsw.PSP18.server.controller.DirectionManagement;
import it.polimi.ingsw.PSP18.server.controller.PlayerManager;
import it.polimi.ingsw.PSP18.server.controller.exceptions.InvalidMoveException;
import it.polimi.ingsw.PSP18.server.model.Direction;
import it.polimi.ingsw.PSP18.server.model.Worker;

import java.util.ArrayList;
/***
 * this is the class that implements Artemis's powers
 */
public class Artemis extends Divinity {
    boolean raiseForbidden, firstMove;
    /***
     * Constructor of the class, initialize name and playerManager in Divinity
     * @param name the name of the divinity
     * @param playerManager the object that has this divinity
     */
    public Artemis(String name, PlayerManager playerManager) {
        super(name, playerManager);
    }

    /***
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

        playerManager.getMatch().getCurrentSocket().sendMessage(new MoveList(movesWorker1, movesWorker2, playerManager.getWorker(0), playerManager.getWorker(1)));
        this.firstMove = true;
    }

    /***
     * Moves in the selected direction
     * @param direction the direction of the movement
     * @param workerID the ID of the worker that we want to move
     */
    @Override
    public void moveReceiver(Direction direction, Integer workerID) {
        if(direction == null) { // If he doesn't want to move
            build();
            return;
        }

        // Check that the move is valid
        if((workerID == 0 && !movesWorker1.contains(direction)) || (workerID == 1 && !movesWorker2.contains(direction))) {
            try {
                throw new InvalidMoveException();
            } catch (InvalidMoveException e) {
                e.printStackTrace();
                move();
                return;
            }
        }

        Worker worker = playerManager.getWorker(workerID);
        this.workerID = workerID;
        setMove(worker.getX(), worker.getY(), direction);

        if(checkForVictory(workerID)){
            for(SocketThread socket : playerManager.getMatch().getSockets()) {
                socket.sendMessage(new MatchWon(playerManager.getPlayerData().getPlayerID()));
            }
            playerManager.getMatch().endMatch();
            return;
        }
        if(firstMove) {
            moves = checkMovementMoves(worker.getX(), worker.getY(), raiseForbidden);
            moves.remove(DirectionManagement.getOppositeDirection(direction));
            if (moves.size() == 0) {
                manageLoss();
                return;
            }
            firstMove = false;
            playerManager.getMatch().getCurrentSocket().sendMessage(new SingleMoveList(moves, workerID, true, playerManager.getWorker(workerID)));
        }
        else {
            build();
        }
    }
}


