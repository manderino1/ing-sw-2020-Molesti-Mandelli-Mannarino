package it.polimi.ingsw.PSP18.client.view;

import it.polimi.ingsw.PSP18.client.view.cli.CliViewUpdate;
import it.polimi.ingsw.PSP18.client.view.cli.InputParser;
import it.polimi.ingsw.PSP18.networking.SocketClient;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/***
 * This is the class used to launch the clients
 */
public class Launcher {

    private final int PORT = 9002;
    private InetAddress host;
    SocketClient socketClient;
    CliViewUpdate cliViewUpdate;

    /***
     * Contructor of Launcher. Creates the socket and start the thread
     * @param address the ip address of the server
     */
    public Launcher(String address) {
        cliViewUpdate = new CliViewUpdate();

        try {
            host = InetAddress.getByName(address);
            Socket socket = new Socket(host, PORT);
            socketClient = new SocketClient (socket, cliViewUpdate);
            cliViewUpdate.setInputParser(new InputParser(socketClient));
            socketClient.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /***
     * Returns the client socket
     * @return the client socket
     */
    public SocketClient getSocketClient() {
        return socketClient;
    }

    /***
     * Returns the cliViewUpdate object
     * @return class that contains all the methods used for the command line interface
     */
    public CliViewUpdate getCliViewUpdate() {
        return cliViewUpdate;
    }

    /***
     * Main method, launches the client
     * @param args launch arguments, not used
     */
    public static void main(String[] args) {
        System.out.println("Insert the server ip address:");
        java.io.BufferedReader console = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
        try {
            String ip = console.readLine();
            Launcher launcher = new Launcher(ip);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
