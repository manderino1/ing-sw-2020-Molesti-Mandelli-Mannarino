package it.polimi.ingsw.PSP18.server.controller.divinities;

import it.polimi.ingsw.PSP18.networking.SocketThread;
import it.polimi.ingsw.PSP18.networking.messages.toclient.BuildList;
import it.polimi.ingsw.PSP18.networking.messages.toclient.MatchLost;
import it.polimi.ingsw.PSP18.server.controller.DirectionManagement;
import it.polimi.ingsw.PSP18.server.controller.PlayerManager;
import it.polimi.ingsw.PSP18.server.model.Direction;
import it.polimi.ingsw.PSP18.server.model.Worker;

import java.util.ArrayList;

public class Ephaestus extends Divinity{
    boolean firstBuild;

    public Ephaestus(String name, PlayerManager playerManager) {
        super(name, playerManager);
    }

    /***
     * Method used to build
     */
    @Override
    protected void build() {
        if (checkForLose(true, false)) {
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

        // If the height of the building cell is 3 a dome has to be placed
        if (playerManager.getGameMap().getCell(newX, newY).getBuilding() == 3) {
            dome = true;
        }

        playerManager.setBuild(newX, newY, dome);

        if (firstBuild && playerManager.getGameMap().getCell(newX, newY).getBuilding() != 3) {
            ArrayList<Direction> moves = new ArrayList<>();
            moves.add(direction);
            firstBuild = false;

            playerManager.getMatch().getCurrentSocket().sendMessage(new BuildList(moves));
        }
    }
}

