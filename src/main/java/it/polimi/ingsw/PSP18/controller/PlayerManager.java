package it.polimi.ingsw.PSP18.controller;

import it.polimi.ingsw.PSP18.controller.divinities.Divinity;
import it.polimi.ingsw.PSP18.model.*;

public class PlayerManager {
    private GameMap gameMap;
    private Worker[] workers = new Worker[2];
    private PlayerData playerData;
    private Divinity divinity;

    /***
     * Class constructor, initializes the  and the playerData
     * @param gameMap the game map (unique for all players)
     * @param playerData data of the player
     */
    public PlayerManager(GameMap gameMap, PlayerData playerData) {
        this.gameMap = gameMap;
        this.playerData = playerData;
    }

    /***
     *
     * @param workerID the index of the worker to return (0 or 1)
     * @return a copy of the selected worker
     */
    public Worker getWorker(Integer workerID) {
        return new Worker(workers[workerID].getX(), workers[workerID].getY());
    }

    /***
     * Build up a floor on the selected cell or place a dome on it
     * @param X the x coordinate of the building cell
     * @param Y the y coordinate of the building cell
     * @param dome true if a dome is to be built, false if a normal building is to be built
     */
    public void setBuild(Integer X, Integer Y, Boolean dome) {
        if(dome) {
            gameMap.getCell(X, Y).setDome();
        }
        else {
            gameMap.getCell(X, Y).setBuilding(gameMap.getCell(X, Y).getBuilding()+1);
        }
    }

    /***
     * Build up a floor on the selected cell
     * @param X the x coordinate of the building cell
     * @param Y the y coordinate of the building cell
     */
    public void setBuild(Integer X, Integer Y) {
        setBuild(X, Y, false);
    }

    /***
     * TODO: return a copy and not the actual MAP
     * @return a copy of the map
     */
    public GameMap getGameMap() {
        return gameMap;
    }

    /***
     * TODO: return a copy and not the actual DATA
     * @return a copy of the player data
     */
    public PlayerData getPlayerData() {
        return playerData;
    }

    /***
     *
     * @return the name of the divinity
     */
    public String getDivinityName() {
        return divinity.getName();
    }

    /***
     *
     * @param raiseForbidden true if Athena moved up and so you cannot move up in the turn
     */
    public void manageTurn(boolean raiseForbidden) {
        divinity.manageTurn(raiseForbidden);
    }
}
