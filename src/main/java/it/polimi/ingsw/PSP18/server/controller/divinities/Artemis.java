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
        //checking if the player lost
        if(checkForLose(raiseForbidden, true)){
            for(SocketThread socket : playerManager.getMatch().getSockets()) {
                socket.sendMessage(new MatchLost(playerManager.getPlayerData().getPlayerID()));
                playerManager.getMatch().getPlayerManagers().remove(playerManager.getMatch().getCurrentPlayer());

                Integer x1 = playerManager.getWorker(0).getX();
                Integer y1 = playerManager.getWorker(0).getY();
                Integer x2 = playerManager.getWorker(1).getX();
                Integer y2 = playerManager.getWorker(1).getY();
                playerManager.getGameMap().setCell(x1, y1, playerManager.getGameMap().getCell( x1, y1).getBuilding(), null);
                playerManager.getGameMap().setCell(x2, y2, playerManager.getGameMap().getCell( x2, y2).getBuilding(), null);
            }
        }

        ArrayList<Direction> movesWorker1 = checkMovementMoves(playerManager.getWorker(0).getX(), playerManager.getWorker(0).getY(), raiseForbidden);
        ArrayList<Direction> movesWorker2 = checkMovementMoves(playerManager.getWorker(1).getX(), playerManager.getWorker(1).getY(), raiseForbidden);

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

        if(checkForVictory()){
            for(SocketThread socket : playerManager.getMatch().getSockets()) {
                socket.sendMessage(new MatchWon(playerManager.getPlayerData().getPlayerID()));
                // TODO: end the match
            }
        }

        if(checkForLose(raiseForbidden, true)){
            for(SocketThread socket : playerManager.getMatch().getSockets()) {
                socket.sendMessage(new MatchLost(playerManager.getPlayerData().getPlayerID()));
                playerManager.getMatch().getPlayerManagers().remove(playerManager.getMatch().getCurrentPlayer());

                Integer x1 = playerManager.getWorker(0).getX();
                Integer y1 = playerManager.getWorker(0).getY();
                Integer x2 = playerManager.getWorker(1).getX();
                Integer y2 = playerManager.getWorker(1).getY();
                playerManager.getGameMap().setCell(x1, y1, playerManager.getGameMap().getCell( x1, y1).getBuilding(), null);
                playerManager.getGameMap().setCell(x2, y2, playerManager.getGameMap().getCell( x2, y2).getBuilding(), null);
            }
        }

        if(firstMove) {
            ArrayList<Direction> moves = checkMovementMoves(worker.getX(), worker.getY(), raiseForbidden);
            moves.remove(DirectionManagement.getOppositeDirection(direction));
            firstMove = false;
            playerManager.getMatch().getCurrentSocket().sendMessage(new SingleMoveList(moves, workerID));
        }
        else {
            build();
        }
    }
}


