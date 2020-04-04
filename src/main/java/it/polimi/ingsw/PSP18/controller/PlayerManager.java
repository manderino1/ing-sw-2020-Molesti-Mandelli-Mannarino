package it.polimi.ingsw.PSP18.controller;

import it.polimi.ingsw.PSP18.controller.divinities.*;
import it.polimi.ingsw.PSP18.model.*;

public class PlayerManager {
    private Match match;
    private Worker[] workers = new Worker[2];
    private PlayerData playerData;
    private Divinity divinity;

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

    private void divinityCreation(String divinityName) {
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
    }

    //TODO: check if somehow you can place a worker in a spot out of bounds, use checkCoordinate from DirectionManagement if needed
    public void placeWorker(Integer x, Integer y) {
        if(workers[0] == null) {
            workers[0] = new Worker(x, y);
            match.getGameMap().setCell(x, y, match.getGameMap().getCell(x,y).getBuilding(), workers[0]);
            //TODO: throw exception if cell is occupied
        }
        else if (workers[1] == null) {
            workers[1] = new Worker(x, y);
            match.getGameMap().setCell(x, y, match.getGameMap().getCell(x,y).getBuilding(), workers[1]);
        }
        else {
            //TODO: throw exception if too many workers
        }
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
     * TODO: return a copy and not the actual MAP
     * @return a copy of the map
     */
    public GameMap getGameMap() {
        return match.getGameMap();
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

    /***
     * @return the divinity reference
     */
    public Divinity getDivinity() {
        return divinity;
    }
}
