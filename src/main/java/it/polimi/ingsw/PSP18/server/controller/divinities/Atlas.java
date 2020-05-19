package it.polimi.ingsw.PSP18.server.controller.divinities;

import it.polimi.ingsw.PSP18.networking.messages.toclient.AtlasBuildList;
import it.polimi.ingsw.PSP18.networking.messages.toclient.BuildList;
import it.polimi.ingsw.PSP18.networking.messages.toclient.EndTurnAvaiable;
import it.polimi.ingsw.PSP18.server.controller.DirectionManagement;
import it.polimi.ingsw.PSP18.server.controller.PlayerManager;
import it.polimi.ingsw.PSP18.server.model.Direction;
import it.polimi.ingsw.PSP18.server.model.Worker;

import java.util.ArrayList;
/***
 * this is the class that implements Atlas's powers
 */
public class Atlas extends Divinity{
    /***
     * Constructor of the class, initialize name and playerManager in Divinity
     * @param name the name of the divinity
     * @param playerManager the object that has this divinity
     */
    public Atlas(String name, PlayerManager playerManager) {
        super(name, playerManager);
    }

    @Override
    /***
     * method that check for the possible building moves and ask to the client the one the player wants to move
     */
    protected void build() {
        Worker worker = playerManager.getWorker(workerID);
        moves = checkBuildingMoves(worker.getX(), worker.getY());

        if (moves.size() == 0) {
            manageLoss();
            return;
        }

        playerManager.getMatch().getMatchSocket().getCurrentSocket().sendMessage(new AtlasBuildList(moves, worker));
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

        // Check if the build direction is valid
        if(!moves.contains(direction)) {
            build();
        }

        /*
            in base alla direzione passatami dalla view
            se costruisco una cupola allora aggiorno il valore del flag dome controllando che la costruzione avvenga sopra il livello 3 di una torre
         */
        if (playerManager.getGameMap().getCell(newX, newY).getBuilding() == 3) {
            dome = true;
        }

        playerManager.setBuild(newX, newY, dome);
        playerManager.getMatch().getMatchSocket().getCurrentSocket().sendMessage(new EndTurnAvaiable());
    }
}

