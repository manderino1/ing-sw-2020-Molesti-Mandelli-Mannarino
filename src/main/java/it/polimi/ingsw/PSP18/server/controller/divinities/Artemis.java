package it.polimi.ingsw.PSP18.server.controller.divinities;

import it.polimi.ingsw.PSP18.networking.SocketThread;
import it.polimi.ingsw.PSP18.networking.messages.toclient.*;
import it.polimi.ingsw.PSP18.server.controller.DirectionManagement;
import it.polimi.ingsw.PSP18.server.controller.PlayerManager;
import it.polimi.ingsw.PSP18.server.model.Direction;
import it.polimi.ingsw.PSP18.server.model.Worker;

import java.util.ArrayList;

public class Artemis extends Divinity {
    boolean raiseForbidden, firstMove;

    public Artemis(String name, PlayerManager playerManager) {
        super(name, playerManager);
    }

    /***
     *  First part of the movement phase
     */
    @Override
    protected void move() {
        ArrayList<Direction> movesWorker1 = checkMovementMoves(playerManager.getWorker(0).getX(), playerManager.getWorker(0).getY(), raiseForbidden);
        ArrayList<Direction> movesWorker2 = checkMovementMoves(playerManager.getWorker(1).getX(), playerManager.getWorker(1).getY(), raiseForbidden);

        // Check if the player has lost
        if (movesWorker1.size() == 0 && movesWorker2.size() == 0) {
            manageLoss();
            return;
        }

        playerManager.getMatch().getCurrentSocket().sendMessage(new MoveList(movesWorker1, movesWorker2));
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

        Worker worker = playerManager.getWorker(workerID);
        this.workerID = workerID;
        setMove(worker.getX(), worker.getY(), direction);

        if(checkForVictory(workerID)){
            for(SocketThread socket : playerManager.getMatch().getSockets()) {
                socket.sendMessage(new MatchWon(playerManager.getPlayerData().getPlayerID()));
                // TODO: end the match
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
            playerManager.getMatch().getCurrentSocket().sendMessage(new SingleMoveList(moves, workerID, true));
        }
        else {
            build();
        }
    }
}


