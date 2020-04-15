package it.polimi.ingsw.PSP18.networking;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.PSP18.client.view.ViewUpdate;
import it.polimi.ingsw.PSP18.networking.messages.toclient.*;
import it.polimi.ingsw.PSP18.networking.messages.toserver.ServerAbstractMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketClient extends Thread {
    Socket socket;
    BufferedReader input;
    PrintWriter output;
    ViewUpdate viewUpdate;

    public SocketClient(Socket clientSocket, ViewUpdate viewUpdate) {
        this.socket = clientSocket;
        this.viewUpdate = viewUpdate;
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

        ClientMessageType type = ClientMessageType.valueOf(msgTopicString);

        //TODO: add function calls
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
            case PLACE_READY:
                PlaceReady placeReady = gson.fromJson(jsonObj, PlaceReady.class);
        }
    }

    public void sendMessage(ServerAbstractMessage msg) {
        Gson gson = new Gson();
        output.println(gson.toJson(msg));
    }
}
