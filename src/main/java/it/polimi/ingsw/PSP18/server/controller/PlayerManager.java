package it.polimi.ingsw.PSP18.server.controller;

import it.polimi.ingsw.PSP18.server.controller.divinities.*;
import it.polimi.ingsw.PSP18.server.model.*;

public class PlayerManager {
    private Match match;
    private Worker[] workers = new Worker[2];
    private PlayerData playerData;
    private Divinity divinity;

    /***
     * Class constructor, initializes the  and the playerData
     * @param match the match reference (unique for all players)
     * @param playerData data of the player
     */
    public PlayerManager(Match match, PlayerData playerData) {
        this.match = match;
        this.playerData = playerData;
    }

    /***
     * Class constructor, initializes the  and the playerData
     * @param match the match reference (unique for all players)
     * @param playerData data of the player
     * @param divinity the name of the choosen divinity
     */
    public PlayerManager(Match match, PlayerData playerData, String divinity) {
        this.match = match;
        this.playerData = playerData;
        divinityCreation(divinity);
    }

    /***
     * Called from the constructor, create a divinity of the correct type given by the player
     * @param divinityName string representing the name of the divinity to be created
     */
    public void divinityCreation(String divinityName) {
        switch(divinityName) {
            case "Divinity":
                divinity = new Divinity(divinityName, this);
                break;
            case "Apollo":
                divinity = new Apollo(divinityName, this);
                break;
            case "Artemis":
                divinity = new Artemis(divinityName, this);
                break;
            case "Athena":
                divinity = new Athena(divinityName, this);
                break;
            case "Atlas":
                divinity = new Atlas(divinityName, this);
                break;
            case "Demeter":
                divinity = new Demeter(divinityName, this);
                break;
            case "Ephaestus":
                divinity = new Ephaestus(divinityName, this);
                break;
            case "Minotaur":
                divinity = new Minotaur(divinityName, this);
                break;
            case "Pan":
                divinity = new Pan(divinityName, this);
                break;
            case "Prometheus":
                divinity = new Prometheus(divinityName, this);
                break;
        }
        playerData.setDivinity(divinityName);
    }

    /***
     * After creating the player, place a worker in the given position
     * do nothing if there are already 2 workers placed
     * @param x the x coordinate in the map of the worker to be placed
     * @param y the y coordinate in the map of the worker to be placed
     */
    //TODO: check if somehow you can place a worker in a spot out of bounds, use checkCoordinate from DirectionManagement if needed
    public void placeWorker(Integer x, Integer y) {
        if(workers[0] == null) {
            workers[0] = new Worker(x, y, 0, playerData.getPlayerColor());
            match.getGameMap().setCell(x, y, match.getGameMap().getCell(x,y).getBuilding(), workers[0]);
            //TODO: throw exception if cell is occupied
        }
        else if (workers[1] == null) {
            workers[1] = new Worker(x, y, 1, playerData.getPlayerColor());
            match.getGameMap().setCell(x, y, match.getGameMap().getCell(x,y).getBuilding(), workers[1]);
        }
        else {
            //TODO: throw exception if too many workers
        }
    }

    /***
     * Get a worker from the model with the given id
     * @param workerID the index of the worker to return (0 or 1)
     * @return the selected worker
     */
    public Worker getWorker(Integer workerID) {
        return new Worker(workers[workerID].getX(), workers[workerID].getY(), workerID, playerData.getPlayerColor());
    }

    /***
     * Build up a floor on the selected cell or place a dome on it
     * @param X the x coordinate of the building cell
     * @param Y the y coordinate of the building cell
     * @param dome true if a dome is to be built, false if a normal building is to be built
     */
    public void setBuild(Integer X, Integer Y, Boolean dome) {
        if(dome) {
            match.getGameMap().getCell(X, Y).setDome();
        }
        else {
            match.getGameMap().getCell(X, Y).setBuilding(match.getGameMap().getCell(X, Y).getBuilding()+1);
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
     * @return a copy of the map
     */
    public GameMap getGameMap() {
        return match.getGameMap();
    }

    /***
     * @return the player data object
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
     * Call the relative function into the specific player divinity controller
     * @param raiseForbidden true if Athena moved up and so you cannot move up in the turn
     */
    public void manageTurn(boolean raiseForbidden) {
        divinity.manageTurn(raiseForbidden);
    }

    /***
     * @return the divinity object reference
     */
    public Divinity getDivinity() {
        return divinity;
    }

    /***
     * Returns the match reference
     * @return match reference
     */
    public Match getMatch() {
        return match;
    }
}
