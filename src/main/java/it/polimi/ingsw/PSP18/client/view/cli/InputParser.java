package it.polimi.ingsw.PSP18.client.view.cli;

import it.polimi.ingsw.PSP18.networking.SocketClient;
import it.polimi.ingsw.PSP18.networking.messages.toserver.MoveReceiver;
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

        switch (move) {
            case "UP":
                socket.sendMessage(new MoveReceiver(Direction.UP, workerID));
                break;
            case "DOWN":
                socket.sendMessage(new MoveReceiver(Direction.DOWN, workerID));
                break;
            case "LEFT":
                socket.sendMessage(new MoveReceiver(Direction.LEFT, workerID));
                break;
            case "RIGHT":
                socket.sendMessage(new MoveReceiver(Direction.RIGHT, workerID));
                break;
            case "UP-LEFT":
                socket.sendMessage(new MoveReceiver(Direction.LEFTUP, workerID));
                break;
            case "UP-RIGHT":
                socket.sendMessage(new MoveReceiver(Direction.RIGHTUP, workerID));
                break;
            case "DOWN_LEFT":
                socket.sendMessage(new MoveReceiver(Direction.LEFTDOWN, workerID));
                break;
            case "DOWN_RIGHT":
                socket.sendMessage(new MoveReceiver(Direction.RIGHTDOWN, workerID));
                break;
        }
    }
}
