package it.polimi.ingsw.PSP18.networking;

import it.polimi.ingsw.PSP18.server.controller.Match;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer extends Thread {
    private final int PORT = 9002;
    private Match match;
    private boolean listening = true;
    private ServerSocket serverSocket = null;

    public SocketServer(Match match) {
        this.match = match;

        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /***
     * Wait for a new connection, start the socket thread and give the object reference to the model
     */
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
            SocketThread newThread = new SocketThread(socket, match);
            newThread.start();
            match.addSocket(newThread);
        }
    }
}
