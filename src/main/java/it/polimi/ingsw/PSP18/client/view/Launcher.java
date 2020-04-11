package it.polimi.ingsw.PSP18.client.view;

import it.polimi.ingsw.PSP18.client.view.cli.CliViewUpdate;
import it.polimi.ingsw.PSP18.networking.SocketClient;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Launcher {

    private final int PORT = 9002;
    private final InetAddress HOST = InetAddress.getLocalHost();

    Launcher() throws IOException {
        ViewUpdate cliviewupdate = new CliViewUpdate();


        Socket clientsocket = new Socket(HOST, PORT);

        SocketClient NewThread = new SocketClient (clientsocket, cliviewupdate);
        NewThread.start();
    }

}
