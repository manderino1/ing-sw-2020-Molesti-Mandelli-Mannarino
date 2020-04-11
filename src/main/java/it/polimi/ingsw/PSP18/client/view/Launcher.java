package it.polimi.ingsw.PSP18.client.view;

import it.polimi.ingsw.PSP18.client.view.cli.CliViewUpdate;
import it.polimi.ingsw.PSP18.networking.SocketClient;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Launcher {

    private final int PORT = 9002;
    private InetAddress host ;

    public Launcher() {
        ViewUpdate cliViewUpdate = new CliViewUpdate();

        try {
            host = InetAddress.getLocalHost();
            Socket clientSocket = new Socket(host, PORT);
            SocketClient NewThread = new SocketClient (clientSocket, cliViewUpdate);
            NewThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
