package it.polimi.ingsw.PSP18.networking;

import it.polimi.ingsw.PSP18.controller.GameManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer extends Thread {
    private final int PORT = 9002;
    private GameManager gameManager;
    private boolean listening = true;
    private ServerSocket serverSocket = null;

    public SocketServer(GameManager gameManager) {
        this.gameManager = gameManager;

        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        Socket socket = null;

        while (listening) {
            if (serverSocket != null) {
                try {
                    socket = serverSocket.accept();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // new thread for a client
            SocketThread newThread = new SocketThread(socket);
            newThread.start();
            gameManager.addConnectedPlayer(socket);
        }
    }
}
