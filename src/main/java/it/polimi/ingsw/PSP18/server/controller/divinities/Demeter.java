package it.polimi.ingsw.PSP18.server.controller.divinities;

import it.polimi.ingsw.PSP18.networking.SocketThread;
import it.polimi.ingsw.PSP18.networking.messages.toclient.*;
import it.polimi.ingsw.PSP18.server.controller.DirectionManagement;
import it.polimi.ingsw.PSP18.server.controller.PlayerManager;
import it.polimi.ingsw.PSP18.server.model.Direction;
import it.polimi.ingsw.PSP18.server.model.Worker;

import java.util.ArrayList;

public class Demeter extends Divinity {
    boolean firstBuild;

    public Demeter(String name, PlayerManager playerManager) {
        super(name, playerManager);
    }

    /***
     * Method used to move
     */
    @Override
    protected void build() {

        if (checkForLose(raiseForbidden, false)) {
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

        firstBuild = true;
    }

    /***
     * Build in the selected direction
     * @param direction the direction of the wanted build
     */
    public void buildReceiver(Direction direction) {
        if (direction == null) { // If he doesn't want to move
            return;
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
            ArrayList<Direction> moves = checkBuildingMoves(worker.getX(), worker.getY());
            moves.remove(direction);
            firstBuild = false;
            playerManager.getMatch().getCurrentSocket().sendMessage(new BuildListFlag(moves));
        }
    }
}
