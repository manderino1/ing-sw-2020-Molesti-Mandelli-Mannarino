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
                // TODO: remove workers from board and check index
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

            /*
            TODO: qui bisogna passare al client l'arraylist moves
             */
        }
    }
}

