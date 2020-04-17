package it.polimi.ingsw.PSP18.server.controller.divinities;

import it.polimi.ingsw.PSP18.networking.SocketThread;
import it.polimi.ingsw.PSP18.networking.messages.toclient.*;
import it.polimi.ingsw.PSP18.server.controller.DirectionManagement;
import it.polimi.ingsw.PSP18.server.controller.PlayerManager;
import it.polimi.ingsw.PSP18.server.model.Direction;
import it.polimi.ingsw.PSP18.server.model.Move;
import it.polimi.ingsw.PSP18.server.model.Worker;

import java.util.ArrayList;

public class Divinity {
    protected String name;
    protected PlayerManager playerManager;
    protected boolean raiseForbidden;

    // TODO : REMOVE IT
    protected Direction direction = Direction.UP;
    protected Integer workerID = 0;

    /***
     * Class constructor
     * @param name name of the divinity
     * @param playerManager player manager that has this divinity
     */
    public Divinity(String name, PlayerManager playerManager) {
        this.name = name;
        this.playerManager = playerManager;
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
        // Check if the player has lost
        if (checkForLose(true, false)) {
            for(SocketThread socket : playerManager.getMatch().getSockets()) {
                socket.sendMessage(new MatchLost(playerManager.getPlayerData().getPlayerID()));
            }

            playerManager.getMatch().getPlayerManagers().remove(playerManager.getMatch().getCurrentPlayer());

            Integer x1 = playerManager.getWorker(0).getX();
            Integer y1 = playerManager.getWorker(0).getY();
            Integer x2 = playerManager.getWorker(1).getX();
            Integer y2 = playerManager.getWorker(1).getY();
            playerManager.getGameMap().setCell(x1, y1, playerManager.getGameMap().getCell( x1, y1).getBuilding(), null);
            playerManager.getGameMap().setCell(x2, y2, playerManager.getGameMap().getCell( x2, y2).getBuilding(), null);

            if(playerManager.getMatch().getPlayerManagers().size() == 1) {
                for(SocketThread socket : playerManager.getMatch().getSockets()) {
                    socket.sendMessage(new MatchWon(playerManager.getMatch().getPlayerManagers().get(0).getPlayerData().getPlayerID()));
                }
            }
        }

        ArrayList<Direction> movesWorker1 = checkMovementMoves(playerManager.getWorker(0).getX(), playerManager.getWorker(0).getY(), raiseForbidden);
        ArrayList<Direction> movesWorker2 = checkMovementMoves(playerManager.getWorker(1).getX(), playerManager.getWorker(1).getY(), raiseForbidden);

        playerManager.getMatch().getCurrentSocket().sendMessage(new MoveList(movesWorker1, movesWorker2));
    }

    /***
     * Moves in the selected direction
     * @param direction the direction of the movement
     * @param workerID the ID of the worker that we want to move
     */
    public void moveReceiver(Direction direction, Integer workerID) {
        Worker worker = playerManager.getWorker(workerID);
        this.workerID = workerID;
        setMove(worker.getX(), worker.getY(), direction);

        if(checkForVictory()){
            for(SocketThread socket : playerManager.getMatch().getSockets()) {
                socket.sendMessage(new MatchWon(playerManager.getPlayerData().getPlayerID()));
            }
            playerManager.getMatch().endMatch();
            return;
        }

        build();
    }

    /***
     * Pass to the client the array of the possible build direction moves
     */
    protected void build() {

        if (checkForLose(true, false)) {
            for(SocketThread socket : playerManager.getMatch().getSockets()) {
                socket.sendMessage(new MatchLost(playerManager.getPlayerData().getPlayerID()));
            }

            playerManager.getMatch().getPlayerManagers().remove(playerManager.getMatch().getCurrentPlayer());

            Integer x1 = playerManager.getWorker(0).getX();
            Integer y1 = playerManager.getWorker(0).getY();
            Integer x2 = playerManager.getWorker(1).getX();
            Integer y2 = playerManager.getWorker(1).getY();
            playerManager.getGameMap().setCell(x1, y1, playerManager.getGameMap().getCell( x1, y1).getBuilding(), null);
            playerManager.getGameMap().setCell(x2, y2, playerManager.getGameMap().getCell( x2, y2).getBuilding(), null);

            if(playerManager.getMatch().getPlayerManagers().size() == 1) {
                for(SocketThread socket : playerManager.getMatch().getSockets()) {
                    socket.sendMessage(new MatchWon(playerManager.getMatch().getPlayerManagers().get(0).getPlayerData().getPlayerID()));
                }
            }
        }

        Worker worker = playerManager.getWorker(workerID);
        ArrayList<Direction> moves = checkBuildingMoves(worker.getX(), worker.getY());

        playerManager.getMatch().getCurrentSocket().sendMessage(new BuildList(moves));
    }

    /***
     * Build in the selected direction
     * @param direction the direction of the wanted build
     */
    public void buildReceiver(Direction direction) {
        Worker worker = playerManager.getWorker(workerID);
        Integer newX = DirectionManagement.getX(worker.getX(), direction);
        Integer newY = DirectionManagement.getY(worker.getY(), direction);
        boolean dome = false;

        // If the height of the building cell is 3 a dome has to be placed
        if (playerManager.getGameMap().getCell(newX, newY).getBuilding() == 3) {
            dome = true;
        }

        playerManager.setBuild(newX, newY, dome);
        playerManager.getMatch().getCurrentSocket().sendMessage(new EndTurnAvaiable());
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
     * @return true if the player has won
     */
    protected Boolean checkForVictory(){

        for (int i = 0; i < 2; i++) {
            Integer oldX = playerManager.getWorker(i).getX();
            Integer oldY = playerManager.getWorker(i).getY();

            if (playerManager.getGameMap().getCell(oldX, oldY).getBuilding() == 3) {
                return true;
            }
        }
        return false;
    }

    /***
     *  Checks if the player has lost
     * @param raiseForbidden true if athena moved up one level
     * @param movementPhase true if curretnly in movement phase, false if currently in building phase
     * @return true if the player has lost
     */
    protected Boolean checkForLose(Boolean raiseForbidden, Boolean movementPhase){
        for (int i = 0; i < 2; i++) {
            Integer oldX = playerManager.getWorker(i).getX();
            Integer oldY = playerManager.getWorker(i).getY();

            for (Direction dir : Direction.values()) {
                Integer newX = DirectionManagement.getX(oldX, dir);
                Integer newY = DirectionManagement.getY(oldY, dir);

                if(newX != -1 && newY != -1) {
                    if (!raiseForbidden) {
                        if (!playerManager.getGameMap().getCell(newX, newY).getDome() && (playerManager.getGameMap().getCell(newX, newY).getBuilding() - playerManager.getGameMap().getCell(oldX, oldY).getBuilding() <= 1) && playerManager.getGameMap().getCell(newX, newY).getWorker() == null) {
                            return false;
                        }
                    } else {
                        if (movementPhase) {
                            if (!playerManager.getGameMap().getCell(newX, newY).getDome() && (playerManager.getGameMap().getCell(newX, newY).getBuilding() - playerManager.getGameMap().getCell(oldX, oldY).getBuilding() < 1) && playerManager.getGameMap().getCell(newX, newY).getWorker() == null) {
                                return false;
                            }
                        } else {
                            if (!playerManager.getGameMap().getCell(newX, newY).getDome() && playerManager.getGameMap().getCell(newX, newY).getWorker() == null) {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
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
}
