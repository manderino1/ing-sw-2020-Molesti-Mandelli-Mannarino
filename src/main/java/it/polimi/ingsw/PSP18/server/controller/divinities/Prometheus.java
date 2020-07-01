package it.polimi.ingsw.PSP18.server.controller.divinities;

import it.polimi.ingsw.PSP18.networking.SocketThread;
import it.polimi.ingsw.PSP18.networking.messages.toclient.*;
import it.polimi.ingsw.PSP18.networking.messages.toserver.PrometheusBuildReceiver;
import it.polimi.ingsw.PSP18.server.controller.DirectionManagement;
import it.polimi.ingsw.PSP18.server.controller.MatchRun;
import it.polimi.ingsw.PSP18.server.controller.MatchSocket;
import it.polimi.ingsw.PSP18.server.controller.PlayerManager;
import it.polimi.ingsw.PSP18.server.model.Direction;
import it.polimi.ingsw.PSP18.server.model.Worker;
import java.util.ArrayList;
/***
 * this is the class that implements the Prometheus' powers
 */
public class Prometheus extends Divinity{
    private boolean firstBuild;

    /***
     * Constructor of the class, initialize name and playerManager in Divinity
     * @param name the name of the divinity
     * @param playerManager the object that has this divinity
     * @param matchRun reference of the match running management section
     * @param matchSocket for obtaining info about sockets and players connected to the match
     */
    public Prometheus(String name, PlayerManager playerManager, MatchSocket matchSocket, MatchRun matchRun) {
        super(name, playerManager, matchSocket, matchRun);
    }

    /***
     * Method that initialise the movement phase and keeps track of the movements of Athena
     * @param raiseForbidden true if athena moved up one level
     */
    public void manageTurn(Boolean raiseForbidden) {
        this.raiseForbidden = raiseForbidden;
        ArrayList<Direction> moves1 = checkBuildingMoves(playerManager.getWorker(0).getX(), playerManager.getWorker(0).getY());
        ArrayList<Direction> moves2 = checkBuildingMoves(playerManager.getWorker(1).getX(), playerManager.getWorker(1).getY());
        matchSocket.getCurrentSocket().sendMessage(new PrometheusBuildList(moves1, moves2, playerManager.getWorker(0), playerManager.getWorker(1)));
    }

    /***
     * Receive the worker id that will build and move
     * @param prometheusBuildReceiver the worker id chosen to build and move
     */
    public void receiveWorker(PrometheusBuildReceiver prometheusBuildReceiver) {
        this.workerID = prometheusBuildReceiver.getChosenWorkerID();
        if (prometheusBuildReceiver.getChosenWorkerID() == null) {
            firstBuild = false;
            move();
        } else {
            firstBuild = true;
            build();
        }
    }

    /***
     * Prometheus can decide to build before moving, this function is implemented in buildOpt
     */
    @Override
    protected void move() {
        movesWorker1 = checkMovementMoves(playerManager.getWorker(0).getX(), playerManager.getWorker(0).getY(), raiseForbidden, firstBuild);
        movesWorker2 = checkMovementMoves(playerManager.getWorker(1).getX(), playerManager.getWorker(1).getY(), raiseForbidden, firstBuild);

        // Check if the player has lost
        if (movesWorker1.size() == 0 && movesWorker2.size() == 0) {
            manageLoss();
            return;
        }

        if(firstBuild) {
            firstBuild = false;
            if(workerID == 0) {
                matchSocket.getCurrentSocket().sendMessage(new SingleMoveList(movesWorker1, workerID, false, playerManager.getWorker(workerID)));
            }
            if(workerID == 1) {
                matchSocket.getCurrentSocket().sendMessage(new SingleMoveList(movesWorker2, workerID, false, playerManager.getWorker(workerID)));
            }
        } else {
            matchSocket.getCurrentSocket().sendMessage(new MoveList(movesWorker1, movesWorker2, playerManager.getWorker(0), playerManager.getWorker(1)));
        }
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
    }

    /***
     * Method used to apply the build
     * @param direction the direction of the wanted build
     */
    @Override
    public void buildReceiver(Direction direction) {
        // Check if the build direction is valid
        if(!moves.contains(direction)) {
            build();
        }

        Worker worker = playerManager.getWorker(workerID);
        Integer newX = DirectionManagement.getX(worker.getX(), direction);
        Integer newY = DirectionManagement.getY(worker.getY(), direction);
        boolean dome = false;

        // If the height of the building cell is 3 a dome has to be placed
        if (matchRun.getGameMap().getCell(newX, newY).getBuilding() == 3) {
            dome = true;
        }
        playerManager.setBuild(newX, newY, dome);

        if(firstBuild){
            move();
        }
        else {
            matchSocket.getCurrentSocket().sendMessage(new EndTurnAvaiable());
        }
    }

    /***
     * Checks all the possible moves
     * @param oldX the starting X coordinate of the worker
     * @param oldY the starting Y coordinate of the worker
     * @param raiseForbidden true if athena moved up one level
     * @param buildChoice true if
     * @return all the possible moves
     */
    private ArrayList<Direction> checkMovementMoves(Integer oldX, Integer oldY, Boolean raiseForbidden, Boolean buildChoice) {

        ArrayList<Direction> moves = new ArrayList<>();

        for (Direction dir : Direction.values()) {
            Integer newX = DirectionManagement.getX(oldX, dir);
            Integer newY = DirectionManagement.getY(oldY, dir);

            if(newX != -1 && newY != -1) {
                if (!raiseForbidden && !buildChoice) {
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

