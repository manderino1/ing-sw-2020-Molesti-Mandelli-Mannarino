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
        Worker worker = playerManager.getWorker(workerID);
        ArrayList<Direction> moves = checkBuildingMoves(worker.getX(), worker.getY());

        if (moves.size() == 0) {
            manageLoss();
            return;
        }

        playerManager.getMatch().getCurrentSocket().sendMessage(new BuildList(moves));

        firstBuild = true;
    }

    /***
     * Build in the selected direction
     * @param direction the direction of the wanted build
     */
    public void buildReceiver(Direction direction) {
        if (direction == null) { // If he doesn't want to move
            playerManager.getMatch().getCurrentSocket().sendMessage(new EndTurnAvaiable());
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
        else{
            firstBuild=true;
            playerManager.getMatch().getCurrentSocket().sendMessage(new EndTurnAvaiable());
        }
    }
}
