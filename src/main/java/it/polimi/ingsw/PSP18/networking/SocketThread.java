package it.polimi.ingsw.PSP18.networking;

import java.io.*;
import java.net.Socket;

public class SocketThread extends Thread {
    Socket socket;
    BufferedReader input;
    PrintWriter output;

    public SocketThread(Socket clientSocket) {
        this.socket = clientSocket;
    }

    public void run()
    {
        try
        {
            // Init buffers
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }


        while (true) {
            try {
                String line = input.readLine();    // reads a line of text
                if(line != null) {
                    System.out.println(line);
                }
                // TODO: elabora input
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    /**
     * Sends a message through the socket
     * @param message the message to send
     */
    private void send(String message) {
        output.println(message);
    }

    /**
     * Close the socket connection
     * @param message the message to send
     */
    private void closeConnection(String message) {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
