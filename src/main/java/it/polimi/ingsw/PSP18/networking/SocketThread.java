package it.polimi.ingsw.PSP18.networking;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.PSP18.model.Match;
import it.polimi.ingsw.PSP18.view.messages.toserver.ServerMessageType;
import it.polimi.ingsw.PSP18.view.messages.toserver.MoveReceiver;

import java.io.*;
import java.net.Socket;

public class SocketThread extends Thread {
    Socket socket;
    BufferedReader input;
    PrintWriter output;
    Match match;

    public SocketThread(Socket clientSocket, Match match) {
        this.socket = clientSocket;
        this.match = match;
    }

    /***
     * Open the thread and setup the in/out buffers
     */
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
                    messageParse(line);
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
     */
    private void closeConnection() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void messageParse(String msg) {
        Gson gson = new Gson();
        JsonObject jsonObj = JsonParser.parseString(msg).getAsJsonObject();
        String msgTopicString = jsonObj.get("type").getAsString();

        ServerMessageType type = ServerMessageType.valueOf(msgTopicString);

        switch(type) {
            case MOVE_RECEIVER:
                MoveReceiver moveReceiver = gson.fromJson(jsonObj, MoveReceiver.class);
                match.getCurrentPlayer().getDivinity().moveReceiver(moveReceiver.getDirection(), moveReceiver.getWorkerID());
        }
    }
}
