package it.polimi.ingsw.PSP18.networking;

import it.polimi.ingsw.PSP18.client.view.ViewUpdate;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketClient extends Thread {
    Socket socket;
    BufferedReader input;
    PrintWriter output;
    ViewUpdate viewUpdate;
}
