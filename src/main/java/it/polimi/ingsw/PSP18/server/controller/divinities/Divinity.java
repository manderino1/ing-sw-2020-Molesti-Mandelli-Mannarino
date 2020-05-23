package it.polimi.ingsw.PSP18.server.controller.divinities;

import it.polimi.ingsw.PSP18.networking.SocketThread;
import it.polimi.ingsw.PSP18.networking.messages.toclient.*;
import it.polimi.ingsw.PSP18.server.controller.DirectionManagement;
import it.polimi.ingsw.PSP18.server.controller.MatchRun;
import it.polimi.ingsw.PSP18.server.controller.MatchSocket;
import it.polimi.ingsw.PSP18.server.controller.PlayerManager;
import it.polimi.ingsw.PSP18.server.model.Direction;
import it.polimi.ingsw.PSP18.server.model.Move;
import it.polimi.ingsw.PSP18.server.model.Worker;

import java.util.ArrayList;
/***
 * this is the class that implements the generic move and build methods for the players
 */
public class Divinity {
    protected String name;
    protected PlayerManager playerManager;
    protected boolean raiseForbidden;
    protected ArrayList<Direction> movesWorker1, movesWorker2;
    protected ArrayList<Direction> moves;
    protected MatchSocket matchSocket;
    protected MatchRun matchRun;

    // TODO : REMOVE IT
    protected Direction direction = Direction.UP;
    protected Integer workerID = 0;

    /***
     * Class constructor
     * @param name name of the divinity
     * @param playerManager player manager that has this divinity
     * @param matchRun reference of the match running management section
     * @param matchSocket for obtaining info about sockets and players connected to the match
     */
    public Divinity(String name, PlayerManager playerManager, MatchSocket matchSocket, MatchRun matchRun) {
        this.name = name;
        this.playerManager = playerManager;
        this.matchSocket = matchSocket;
        this.matchRun = matchRun;
    }

    /***
     *
     * @return name of the divinity in use
     */
    public String getName() {
        return name;
    }

    /***
     *
     * @param raiseForbidden true if athena moved up one level
     */
    public void manageTurn(Boolean raiseForbidden) {
        this.raiseForbidden = raiseForbidden;
        move();
    }

    /***
     *  First part of the movement phase
     */
    protected void move() {
        movesWorker1 = checkMovementMoves(playerManager.getWorker(0).getX(), playerManager.getWorker(0).getY(), raiseForbidden);
        movesWorker2 = checkMovementMoves(playerManager.getWorker(1).getX(), playerManager.getWorker(1).getY(), raiseForbidden);

        // Check if the player has lost
        if (movesWorker1.size() == 0 && movesWorker2.size() == 0) {
            manageLoss();
            return;
        }

        matchSocket.getCurrentSocket().sendMessage(new MoveList(movesWorker1, movesWorker2, playerManager.getWorker(0), playerManager.getWorker(1)));
    }

    /***
     * Moves in the selected direction
     * @param direction the direction of the movement
     * @param workerID the ID of the worker that we want to move
     */
    public void moveReceiver(Direction direction, Integer workerID) {
        Worker worker = playerManager.getWorker(workerID);
        this.workerID = workerID;

        // Check that the move is valid
        if((workerID == 0 && !movesWorker1.contains(direction)) || (workerID == 1 && !movesWorker2.contains(direction))) {
            move();
        }

        // If it's valid start the program
        setMove(worker.getX(), worker.getY(), direction);

        if(checkForVictory(workerID)){
            matchRun.endMatch(playerManager);
            return;
        }

        build();
    }

    /***
     * Pass to the client the array of the possible build direction moves
     */
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
     * Build in the selected direction
     * @param direction the direction of the wanted build
     */
    public void buildReceiver(Direction direction) {
        Worker worker = playerManager.getWorker(workerID);

        // Check if the build direction is valid
        if(!moves.contains(direction)) {
            build();
        }

        Integer newX = DirectionManagement.getX(worker.getX(), direction);
        Integer newY = DirectionManagement.getY(worker.getY(), direction);
        boolean dome = false;

        // If the height of the building cell is 3 a dome has to be placed
        if (playerManager.getGameMap().getCell(newX, newY).getBuilding() == 3) {
            dome = true;
        }

        playerManager.setBuild(newX, newY, dome);
        matchSocket.getCurrentSocket().sendMessage(new EndTurnAvaiable());
    }

    /***
     *
     * @param oldX the starting X coordinate of the worker
     * @param oldY the starting Y coordinate of the worker
     * @param raiseForbidden true if athena moved up one level
     * @return the list of possible moves
     */
    protected ArrayList<Direction> checkMovementMoves(Integer oldX, Integer oldY, Boolean raiseForbidden) {

        ArrayList<Direction> moves = new ArrayList<>();

        for (Direction dir : Direction.values()) {
            Integer newX = DirectionManagement.getX(oldX, dir);
            Integer newY = DirectionManagement.getY(oldY, dir);

            if(newX != -1 && newY != -1) {
                if (!raiseForbidden) {
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

    /***
     *
     * @param oldX the starting X coordinate of the worker
     * @param oldY the starting Y coordinate of the worker
     * @return the list of possible moves
     */
    protected ArrayList<Direction> checkBuildingMoves(Integer oldX, Integer oldY) {
        ArrayList<Direction> moves = new ArrayList<>();

        for (Direction dir : Direction.values()) {
            Integer newX = DirectionManagement.getX(oldX, dir);
            Integer newY = DirectionManagement.getY(oldY, dir);

            if(newX != -1 && newY != -1) {
                if (!playerManager.getGameMap().getCell(newX, newY).getDome() && playerManager.getGameMap().getCell(newX, newY).getWorker() == null) {
                    moves.add(dir);
                }
            }
        }
        return moves;
    }

    /***
     *  Checks if the player has won
     * @param workerID the id of the worker that has moved
     * @return true if the player has won
     */
    protected Boolean checkForVictory(int workerID){

        Integer oldX = playerManager.getWorker(workerID).getX();
        Integer oldY = playerManager.getWorker(workerID).getY();

        if (playerManager.getGameMap().getCell(oldX, oldY).getBuilding() == 3 && playerManager.getPlayerData().getLastMove().getLevel() >= 1) {
            return true;
        }
        return false;
    }

    /***
     *  Set the last move in the player data class in the model package
     * @param oldX the old position of the worker on the x axis
     * @param oldY the old position of the worker on the y axis
     * @param direction the direction of the move
     */
    protected void setMove(Integer oldX, Integer oldY, Direction direction) {
        Integer newX = DirectionManagement.getX(oldX, direction);
        Integer newY = DirectionManagement.getY(oldY, direction);
        updateMoveCells(oldX, oldY, newX, newY);

        if(playerManager.getGameMap().getCell(newX, newY).getBuilding() - playerManager.getGameMap().getCell(oldX, oldY).getBuilding() == 1){
            playerManager.getPlayerData().setLastMove(new Move(direction, 1));
        }
        else if(playerManager.getGameMap().getCell(newX, newY).getBuilding() - playerManager.getGameMap().getCell(oldX, oldY).getBuilding() == 0) {
            playerManager.getPlayerData().setLastMove(new Move(direction, 0));
        }
        else if(playerManager.getGameMap().getCell(newX, newY).getBuilding() - playerManager.getGameMap().getCell(oldX, oldY).getBuilding() == -1) {
            playerManager.getPlayerData().setLastMove(new Move(direction, -1));
        }
        else {
            playerManager.getPlayerData().setLastMove(new Move(direction, -2));
        }
    }

    /***
     * Set worker in a cell and remove from the source one
     * If a worker is present in the destination cell (Apollo) switch workers
     * @param oldX the source x position
     * @param oldY the source y position
     * @param newX the destination x position
     * @param newY the destination y position
     */
    protected void updateMoveCells(Integer oldX, Integer oldY, Integer newX, Integer newY) {
        playerManager.getGameMap().setCell(newX, newY, playerManager.getGameMap().getCell(newX, newY).getBuilding(), playerManager.getGameMap().getCell(oldX, oldY).getWorker());
        playerManager.getGameMap().setCell(oldX, oldY, playerManager.getGameMap().getCell(oldX, oldY).getBuilding(), null);
        playerManager.getGameMap().getCell(newX, newY).getWorker().setPosition(newX, newY);
    }

    /***
     * Send the match loss message to all players and remove workers, skip the player turn
     */
    protected void manageLoss() {
        if(matchSocket.getPlayerManagers().size() == 2) {
            if(matchSocket.getPlayerManagers().get(0) == playerManager){
                matchRun.endMatch(matchSocket.getPlayerManagers().get(1));
            } else {
                matchRun.endMatch(matchSocket.getPlayerManagers().get(0));
            }
            return;
        }

        matchSocket.getPlayerManagers().remove(matchSocket.getCurrentPlayer());

        Integer x1 = playerManager.getWorker(0).getX();
        Integer y1 = playerManager.getWorker(0).getY();
        Integer x2 = playerManager.getWorker(1).getX();
        Integer y2 = playerManager.getWorker(1).getY();
        playerManager.getGameMap().setCell(x1, y1, playerManager.getGameMap().getCell( x1, y1).getBuilding(), null);
        playerManager.getGameMap().setCell(x2, y2, playerManager.getGameMap().getCell( x2, y2).getBuilding(), null);


        for(SocketThread socket : matchSocket.getSockets()) {
            if(socket == matchSocket.getCurrentSocket()) {
                socket.sendMessage(new MatchLost(playerManager.getPlayerData().getPlayerID(), true, false));
            } else {
                socket.sendMessage(new MatchLost(playerManager.getPlayerData().getPlayerID(), false, false));
            }
        }

        if(matchRun.getTurnManager() != null) {
            matchRun.getTurnManager().passTurn();
        }
    }
}
