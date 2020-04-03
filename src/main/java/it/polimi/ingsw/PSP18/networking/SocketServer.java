package it.polimi.ingsw.PSP18.networking;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {
    final int PORT = 9002;

    ServerSocket serverSocket = null;

    public SocketServer() {
        Socket socket = null;

        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            if (serverSocket != null) {
                try {
                    socket = serverSocket.accept();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // new thread for a client
            new SocketThread(socket).start();
        }
    }
}
