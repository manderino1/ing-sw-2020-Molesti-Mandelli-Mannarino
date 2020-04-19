package it.polimi.ingsw.PSP18.client.view.cli;

import it.polimi.ingsw.PSP18.networking.SocketClient;
import it.polimi.ingsw.PSP18.networking.messages.toserver.*;
import it.polimi.ingsw.PSP18.server.model.Direction;

public class InputParser {
    private SocketClient socket;

    public InputParser(SocketClient socket) {
        this.socket = socket;
    }

    public void selectMove(String worker, String move) {
        Integer workerID = 0;
        if(worker.equals("1")) {
            workerID = 0;
        } else if (worker.equals("2")){
            workerID = 1;
        }

        socket.sendMessage(new MoveReceiver(stringToDirection(move), workerID));
    }

    public void selectBuild(String move) {
        socket.sendMessage(new BuildReceiver(stringToDirection(move)));
    }

    public void endTurnSignal() {
        socket.sendMessage(new EndTurnReceiver());
    }

    private Direction stringToDirection(String dir) {
        if(dir == null || dir == "NO") {
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

    public void selectDivinity(String divinity) {
        socket.sendMessage(new DivinityReceiver(divinity));
    }

    public void selectPlayerData(String playerID) {
        socket.sendMessage(new PlayerDataReceiver(playerID));
    }

    public void selectReady() {
        socket.sendMessage(new ReadyReceiver());
    }

    public void selectWorkers(Integer x1, Integer y1, Integer x2, Integer y2) {
        socket.sendMessage(new WorkerReceiver(x1, y1, x2, y2));
    }

    public void selectPrometheus(Integer workerID){
        socket.sendMessage(new PrometheusBuildReceiver(workerID));
    }

    public void atlasBuild(String move, boolean dome) {
        socket.sendMessage(new AtlasBuildReceiver(stringToDirection(move), dome));
    }
}
