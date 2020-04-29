package it.polimi.ingsw.PSP18.client.view.cli;

import it.polimi.ingsw.PSP18.networking.SocketClient;
import it.polimi.ingsw.PSP18.networking.messages.toserver.*;
import it.polimi.ingsw.PSP18.server.model.Direction;

import java.util.ArrayList;

/***
 * This is the class that contains all the methods used to manage the messages sent by the client and notify the server with other type of messages
 */
public class InputParser {
    private SocketClient socket;

    /***
     * Constructor of the InputParser class
     * @param socket socket used by the client
     */
    public InputParser(SocketClient socket) {
        this.socket = socket;
    }

    /***
     * Method used to apply the move
     * @param worker worker who wants to move
     * @param move move to apply to the worker
     */
    public void selectMove(String worker, String move) {
        int workerID = 0;
        if(worker.equals("1")) {
            workerID = 0;
        } else if (worker.equals("2")){
            workerID = 1;
        }

        socket.sendMessage(new MoveReceiver(stringToDirection(move), workerID));
    }

    /***
     * Method used to apply the building move
     * @param move move to apply to the worker
     */
    public void selectBuild(String move) {
        socket.sendMessage(new BuildReceiver(stringToDirection(move)));
    }

    /***
     * Method used to end the turn, sends an EndTurnReceiver message
     */
    public void endTurnSignal() {
        socket.sendMessage(new EndTurnReceiver());
    }

    /***
     * Method used to convert a string into a direction
     * @param dir string that contains the direction
     * @return returns the direction as a Direction object
     */
    private Direction stringToDirection(String dir) {
        if(dir == null || dir.equals("NO")) {
            return null;
        }
        dir = dir.toUpperCase();
        switch (dir) {
            case "UP":
                return Direction.UP;
            case "DOWN":
                return Direction.DOWN;
            case "LEFT":
                return Direction.LEFT;
            case "RIGHT":
                return Direction.RIGHT;
            case "LEFTUP":
                return Direction.LEFTUP;
            case "RIGHTUP":
                return Direction.RIGHTUP;
            case "LEFTDOWN":
                return Direction.LEFTDOWN;
            case "RIGHTDOWN":
                return Direction.RIGHTDOWN;
        }
        return null; // Never reach this point
    }

    /***
     * Method used to set the Divinity
     * @param divinity string that contains the divinity name
     */
    public void selectDivinity(String divinity) {
        socket.sendMessage(new DivinityReceiver(divinity));
    }

    /***
     * Method used to set the PlayerData
     * @param playerID string that contains the player nickname
     */
    public void selectPlayerData(String playerID) {
        socket.sendMessage(new PlayerDataReceiver(playerID));
    }

    /***
     * Method used to set the player status to ready
     */
    public void selectReady() {
        socket.sendMessage(new ReadyReceiver());
    }

    /***
     * Method used to set the workers by inserting their coordinates
     * @param x1 x coordinate of the first worker
     * @param y1 y coordinate of the first worker
     * @param x2 x coordinate of the second worker
     * @param y2 y coordinate of the second worker
     */
    public void selectWorkers(Integer x1, Integer y1, Integer x2, Integer y2) {
        socket.sendMessage(new WorkerReceiver(x1, y1, x2, y2));
    }

    /***
     * Method used to select the worker that is going to move for the prometheus divinity
     * @param workerID the selected worker id
     */
    public void selectPrometheus(Integer workerID){
        socket.sendMessage(new PrometheusBuildReceiver(workerID));
    }

    /***
     * Method used to select the next move for the atlas divinity
     * @param move string that contains the move direction
     * @param dome flag used that signals if a dome is built, true = altlas will build a dome
     */
    public void atlasBuild(String move, boolean dome) {
        socket.sendMessage(new AtlasBuildReceiver(stringToDirection(move), dome));
    }

    /***
     * Send the list of picked divinities to the server
     * @param divinities the list of picked divinities
     */
    public void sendDivinities(ArrayList<String> divinities) {
        socket.sendMessage(new DivinitySelection(divinities));
    }
}
