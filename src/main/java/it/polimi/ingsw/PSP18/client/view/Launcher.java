package it.polimi.ingsw.PSP18.client.view;

import it.polimi.ingsw.PSP18.client.view.cli.CliViewUpdate;
import it.polimi.ingsw.PSP18.client.view.cli.InputParser;
import it.polimi.ingsw.PSP18.networking.SocketClient;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Launcher {

    private final int PORT = 9002;
    private InetAddress host;
    SocketClient socketClient;
    CliViewUpdate cliViewUpdate;

    /***
     * Contructor of Launcher. Creates the socket and start the thread
     */
    public Launcher() {
        cliViewUpdate = new CliViewUpdate();

        try {
            host = InetAddress.getLocalHost();
            Socket socket = new Socket(host, PORT);
            socketClient = new SocketClient (socket, cliViewUpdate);
            cliViewUpdate.setInputParser(new InputParser(socketClient));
            socketClient.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SocketClient getSocketClient() {
        return socketClient;
    }

    public CliViewUpdate getCliViewUpdate() {
        return cliViewUpdate;
    }
}
