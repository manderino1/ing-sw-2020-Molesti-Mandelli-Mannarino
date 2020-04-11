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

    public void Launcher() throws IOException {
        ViewUpdate cliviewupdate = new CliViewUpdate();

        host = InetAddress.getLocalHost();
        Socket clientsocket = new Socket(host, PORT);

        SocketClient NewThread = new SocketClient (clientsocket, cliviewupdate);
        NewThread.start();
    }

}
