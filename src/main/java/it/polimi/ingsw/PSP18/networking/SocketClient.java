package it.polimi.ingsw.PSP18.networking;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.PSP18.client.view.ViewUpdate;
import it.polimi.ingsw.PSP18.networking.messages.toclient.*;
import it.polimi.ingsw.PSP18.networking.messages.toserver.ServerAbstractMessage;
import it.polimi.ingsw.PSP18.networking.messages.toserver.ServerPing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/***
 * The class implements an instance of the socket on the client side
 */
public class SocketClient extends Thread {
    Socket socket;
    BufferedReader input;
    PrintWriter output;
    ViewUpdate viewUpdate;


    /***
     * Constructor for the client side socket
     * Init the buffers
     * @param clientSocket the socket reference
     * @param viewUpdate the viewUpdate client reference for calling the function on receive
     */
    public SocketClient(Socket clientSocket, ViewUpdate viewUpdate) {
        this.socket = clientSocket;
        this.viewUpdate = viewUpdate;
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

        /*
         * Ping every 5 seconds the connected socket
         */
        new Thread(() -> {
            while (true) {
                sendMessage(new ServerPing());

                // delay 5 seconds
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }).start();
    }

    /***
     * Constructor for the client side socket
     * Init the buffers
     * @param clientSocket the clientsocket reference
     * @param viewUpdate the viewupdate reference
     * @param debug true if debug mode, no timeout
     */
    public SocketClient(Socket clientSocket, ViewUpdate viewUpdate, boolean debug) {
        this.socket = clientSocket;
        this.viewUpdate = viewUpdate;
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

        /*
         * Ping every 5 seconds the connected socket
         */
        if(!debug) {
            new Thread(() -> {
                while (true) {
                    sendMessage(new ServerPing());

                    // delay 5 seconds
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }).start();
        }
    }

    /***
     * Open the thread and cycle waiting for input
     */
    public void run()
    {
        while (true) {
            try {
                String line = input.readLine();    // reads a line of text
                if(line != null) {
                    messageParse(line);
                }
            } catch (IOException e) {
                System.out.println("Server has closed connection with you");
                System.out.println("If the match ended and you want to reconnect just restart the client");
                //TODO: gestire disconnessione lato client
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

    /***
     * Parse the input message from the server and call the correct function into the client view
     * Has a switch case for all the different message types that extends ClientAbstractMessage
     * @param msg the input message to parse
     */
    private void messageParse(String msg) {
        Gson gson = new Gson();
        JsonObject jsonObj = JsonParser.parseString(msg).getAsJsonObject();
        String msgTopicString = jsonObj.get("type").getAsString();

        ClientMessageType type = ClientMessageType.valueOf(msgTopicString);

        switch(type) {
            case GAME_MAP_UPDATE:
                GameMapUpdate gameMapUpdate = gson.fromJson(jsonObj, GameMapUpdate.class);
                viewUpdate.updateMap(gameMapUpdate);
                break;
            case PLAYER_DATA_UPDATE:
                PlayerDataUpdate playerDataUpdate = gson.fromJson(jsonObj, PlayerDataUpdate.class);
                viewUpdate.updatePlayerData(playerDataUpdate);
                break;
            case MATCH_WON:
                MatchWon matchWon = gson.fromJson(jsonObj, MatchWon.class);
                viewUpdate.matchWonUpdate(matchWon);
                break;
            case MATCH_LOST:
                MatchLost matchLost = gson.fromJson(jsonObj, MatchLost.class);
                viewUpdate.matchLostUpdate(matchLost);
                break;
            case MOVE_LIST:
                MoveList moveList = gson.fromJson(jsonObj, MoveList.class);
                viewUpdate.moveUpdate(moveList);
                break;
            case BUILD_LIST:
                BuildList buildList = gson.fromJson(jsonObj, BuildList.class);
                viewUpdate.buildUpdate(buildList);
                break;
            case START_MATCH:
                StartMatch startMatch = gson.fromJson(jsonObj, StartMatch.class);
                viewUpdate.startMatch(startMatch);
                break;
            case DIVINITY_LIST:
                DivinityList divinityList = gson.fromJson(jsonObj, DivinityList.class);
                viewUpdate.selectDivinity(divinityList);
                break;
            case READY:
                MatchReady matchReady = gson.fromJson(jsonObj, MatchReady.class);
                viewUpdate.matchReadyUpdate(matchReady);
                break;
            case WAITING_NICK:
                WaitingNick waitingNick = gson.fromJson(jsonObj, WaitingNick.class);
                viewUpdate.selectNick();
                break;
            case PLACE_READY:
                PlaceReady placeReady = gson.fromJson(jsonObj, PlaceReady.class);
                viewUpdate.setWorker(placeReady);
                break;
            case PROMETHEUS_BUILD_LIST:
                PrometheusBuildList prometheusBuildList = gson.fromJson(jsonObj, PrometheusBuildList.class);
                viewUpdate.prometheusBuildListUpdate(prometheusBuildList);
                break;
            case SINGLE_MOVE_LIST:
                SingleMoveList singleMoveList = gson.fromJson(jsonObj, SingleMoveList.class);
                viewUpdate.singleMoveUpdate(singleMoveList);
                break;
            case BUILD_LIST_FLAG:
                BuildListFlag buildListFlag = gson.fromJson(jsonObj, BuildListFlag.class);
                viewUpdate.buildListFlagUpdate(buildListFlag);
                break;
            case END_TURN:
                EndTurnAvaiable endTurnAvaiable = gson.fromJson(jsonObj, EndTurnAvaiable.class);
                viewUpdate.endTurn(endTurnAvaiable);
                break;
            case ATLAS_BUILD_LIST:
                AtlasBuildList atlasBuildList = gson.fromJson(jsonObj, AtlasBuildList.class);
                viewUpdate.atlasBuildUpdate(atlasBuildList);
                break;
        }
    }

    /***
     * Send a message trough socket converted to json
     * Accepts all the objects that extends ServerAbstractMessage
     * @param msg the object to send
     */
    public void sendMessage(ServerAbstractMessage msg) {
        Gson gson = new Gson();
        output.println(gson.toJson(msg));
    }
}
