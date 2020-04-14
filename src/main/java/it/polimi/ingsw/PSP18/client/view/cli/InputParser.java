package it.polimi.ingsw.PSP18.client.view.cli;

import it.polimi.ingsw.PSP18.networking.SocketClient;
import it.polimi.ingsw.PSP18.networking.messages.toserver.DivinityReceiver;
import it.polimi.ingsw.PSP18.networking.messages.toserver.BuildReceiver;
import it.polimi.ingsw.PSP18.networking.messages.toserver.EndTurnReceiver;
import it.polimi.ingsw.PSP18.networking.messages.toserver.MoveReceiver;
import it.polimi.ingsw.PSP18.networking.messages.toserver.PlayerDataReceiver;
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
            case "UP-LEFT":
                return Direction.LEFTUP;
            case "UP-RIGHT":
                return Direction.RIGHTUP;
            case "DOWN-LEFT":
                return Direction.LEFTDOWN;
            case "DOWN-RIGHT":
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
}
