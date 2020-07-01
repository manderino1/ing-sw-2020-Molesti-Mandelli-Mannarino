package it.polimi.ingsw.PSP18.networking;

import it.polimi.ingsw.PSP18.networking.messages.toclient.PlayerNumberReady;
import it.polimi.ingsw.PSP18.server.controller.MatchManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

/***
 * The class manages the connections of sockets to the server
 */
public class SocketServer extends Thread {
    private int port;
    private MatchManager manager;
    private boolean listening = true;
    private boolean debug;
    private ServerSocket serverSocket = null;

    /***
     * Init the port and the match reference into the constructor
     * @param manager the match manager reference
     */
    public SocketServer(MatchManager manager) {
        this.manager = manager;
        this.port = 9002;
        this.debug = false;

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /***
     * A debug constructor with random port between 49152 and 65535
     * Used for tests for exploiting address already in use exception
     * @param manager the match manager reference
     * @param debug if true the port is random
     */
    public SocketServer(MatchManager manager, boolean debug) {
        this.manager = manager;
        this.debug = debug;

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
                    socket.setSoTimeout(10000); // 10 s timeout
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // new thread for a client
            if(!debug) {
                SocketThread newThread = new SocketThread(socket, manager);
                newThread.start();
                newThread.sendMessage(new PlayerNumberReady());
            } else {
                SocketThread newThread = new SocketThread(socket, manager, true);
                newThread.start();
                newThread.sendMessage(new PlayerNumberReady());
            }
        }
    }
}
