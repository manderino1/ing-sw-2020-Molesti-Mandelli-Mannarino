package it.polimi.ingsw.PSP18.server.controller.divinities;

import it.polimi.ingsw.PSP18.networking.messages.toclient.AtlasBuildList;
import it.polimi.ingsw.PSP18.networking.messages.toclient.BuildList;
import it.polimi.ingsw.PSP18.networking.messages.toclient.EndTurnAvaiable;
import it.polimi.ingsw.PSP18.server.controller.DirectionManagement;
import it.polimi.ingsw.PSP18.server.controller.PlayerManager;
import it.polimi.ingsw.PSP18.server.model.Direction;
import it.polimi.ingsw.PSP18.server.model.Worker;

import java.util.ArrayList;

public class Atlas extends Divinity{
    public Atlas(String name, PlayerManager playerManager) {
        super(name, playerManager);
    }

    @Override
    /***
     * method that check for the possible building moves and ask to the client the one the player wants to move
     */
    protected void build() {
        Worker worker = playerManager.getWorker(workerID);
        ArrayList<Direction> moves = checkBuildingMoves(worker.getX(), worker.getY());

        if (moves.size() == 0) {
            manageLoss();
            return;
        }

        playerManager.getMatch().getCurrentSocket().sendMessage(new AtlasBuildList(moves));
    }

    /***
     * This method is called after the method "move"
     * @param direction the direction of the movement
     * @param dome true if a dome has to be placed
     */
    public void buildReceiver(Direction direction, Boolean dome) {
        Worker worker = playerManager.getWorker(workerID);
        Integer newX = DirectionManagement.getX(worker.getX(), direction);
        Integer newY = DirectionManagement.getY(worker.getY(), direction);

        /*
            in base alla direzione passatami dalla view
            se costruisco una cupola allora aggiorno il valore del flag dome controllando che la costruzione avvenga sopra il livello 3 di una torre
         */
        if (playerManager.getGameMap().getCell(newX, newY).getBuilding() == 3) {
            dome = true;
        }

        playerManager.setBuild(newX, newY, dome);
        playerManager.getMatch().getCurrentSocket().sendMessage(new EndTurnAvaiable());
    }
}

