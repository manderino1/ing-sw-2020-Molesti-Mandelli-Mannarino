package it.polimi.ingsw.PSP18.networking;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.PSP18.server.controller.PlayerManager;
import it.polimi.ingsw.PSP18.server.controller.divinities.Atlas;
import it.polimi.ingsw.PSP18.server.controller.divinities.Prometheus;
import it.polimi.ingsw.PSP18.server.model.Color;
import it.polimi.ingsw.PSP18.server.controller.Match;
import it.polimi.ingsw.PSP18.server.model.PlayerData;
import it.polimi.ingsw.PSP18.networking.messages.toclient.ClientAbstractMessage;
import it.polimi.ingsw.PSP18.networking.messages.toserver.*;

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
            match.addSocket(this);
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
    public void closeConnection() {
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
                break;
            case BUILD_RECEIVER:
                BuildReceiver buildReceiver = gson.fromJson(jsonObj, BuildReceiver.class);
                match.getCurrentPlayer().getDivinity().buildReceiver(buildReceiver.getDirection());
                break;
            case PLAYER_DATA_RECEIVER:
                PlayerDataReceiver playerDataReceiver = gson.fromJson(jsonObj, PlayerDataReceiver.class);
                Color playerColor = Color.RED;
                if(match.getPlayerManagers().size() == 0) {
                    playerColor = Color.RED;
                } else if (match.getPlayerManagers().size() == 1) {
                    playerColor = Color.BLUE;
                } else if(match.getPlayerManagers().size() == 2) {
                    playerColor = Color.GREEN;
                }
                PlayerData playerData = new PlayerData(playerDataReceiver.getPlayerID(), playerColor, match.getPlayerManagers().size());
                match.addPlayer(new PlayerManager(match, playerData), this);
                break;
            case DIVINITY_RECEIVER:
                DivinityReceiver divinityReceiver = gson.fromJson(jsonObj, DivinityReceiver.class);
                match.divinityCreation(this, divinityReceiver.getDivinity());
                break;
            case ENDTURN_RECEIVER:
                EndTurnReceiver endTurnReceiver = gson.fromJson(jsonObj, EndTurnReceiver.class);
                match.getTurnManager().passTurn();
                break;
            case READY_RECEIVER:
                ReadyReceiver readyReceiver = gson.fromJson(jsonObj, ReadyReceiver.class);
                match.readyManagement(this);
                break;
            case WORKER_RECEIVER:
                WorkerReceiver workerReceiver = gson.fromJson(jsonObj, WorkerReceiver.class);
                match.workerPlacement(this, workerReceiver);
                break;
            case PROMETHEUS_BUILD_RECEIVER:
                PrometheusBuildReceiver prometheusBuildReceiver = gson.fromJson(jsonObj, PrometheusBuildReceiver.class);
                if(match.getCurrentPlayer().getDivinity() instanceof Prometheus) {
                    ((Prometheus) match.getCurrentPlayer().getDivinity()).receiveWorker(prometheusBuildReceiver);
                }
                break;
            case ATLAS_BUILD_RECEIVER:
                AtlasBuildReceiver atlasBuildReceiver = gson.fromJson(jsonObj, AtlasBuildReceiver.class);
                if(match.getCurrentPlayer().getDivinity() instanceof Atlas) {
                    ((Atlas) match.getCurrentPlayer().getDivinity()).buildReceiver(atlasBuildReceiver.getDirection(), atlasBuildReceiver.isDome());
                }
                break;
        }
    }

    public void sendMessage(ClientAbstractMessage msg) {
        Gson gson = new Gson();
        output.println(gson.toJson(msg));
    }
}
