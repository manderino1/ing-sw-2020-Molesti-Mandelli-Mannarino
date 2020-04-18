package it.polimi.ingsw.PSP18.networking;

import it.polimi.ingsw.PSP18.server.controller.Match;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class SocketServer extends Thread {
    private int port;
    private Match match;
    private boolean listening = true;
    private ServerSocket serverSocket = null;

    public SocketServer(Match match) {
        this.match = match;
        this.port = 9002;

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SocketServer(Match match, boolean debug) {
        this.match = match;

        if(debug) {
            // Randomly generate a port
            Random rand = new Random();
            int upperBound = 65535;
            int lowerBound = 49152;
            this.port = rand.nextInt((upperBound - lowerBound) + 1) + lowerBound;
        } else {
            this.port = 9002;
        }


        try {
            serverSocket = new ServerSocket(port);
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
        }
    }
}
