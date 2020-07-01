package it.polimi.ingsw.PSP18.networking;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.PSP18.networking.messages.toclient.ClientPing;
import it.polimi.ingsw.PSP18.networking.messages.toclient.PlayerNumberReady;
import it.polimi.ingsw.PSP18.server.controller.MatchManager;
import it.polimi.ingsw.PSP18.server.controller.*;
import it.polimi.ingsw.PSP18.server.controller.divinities.Atlas;
import it.polimi.ingsw.PSP18.server.controller.divinities.Prometheus;
import it.polimi.ingsw.PSP18.server.model.Color;
import it.polimi.ingsw.PSP18.server.model.MatchStatus;
import it.polimi.ingsw.PSP18.server.model.PlayerData;
import it.polimi.ingsw.PSP18.networking.messages.toclient.ClientAbstractMessage;
import it.polimi.ingsw.PSP18.networking.messages.toserver.*;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

/***
 * The class implements an instance of the socket on the server side
 */
public class SocketThread extends Thread {
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;
    private MatchSetUp matchSetup;
    private MatchRun matchRun;
    private MatchSocket matchSocket;
    private MatchManager manager;

    /***
     * Constructor for the server side socket
     * Init the buffers
     * @param clientSocket the socket reference
     * @param manager the match in which the socket will play reference
     */
    public SocketThread(Socket clientSocket, MatchManager manager) {
        this.socket = clientSocket;
        this.manager = manager;
        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*
         * Ping every 5 seconds the connected socket
         */
        new Thread(() -> {
            while (true) {
                sendMessage(new ClientPing());

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
     * Constructor for the server side socket
     * Init the buffers
     * @param clientSocket the socket reference
     * @param manager the match in which the socket will play reference
     * @param debug true if debug mode is on, no timeout
     */
    public SocketThread(Socket clientSocket, MatchManager manager, boolean debug) {
        this.socket = clientSocket;
        this.manager = manager;
        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*
         * Ping every 5 seconds the connected socket
         */
        if(!debug) {
            new Thread(() -> {
                while (true) {
                    sendMessage(new ClientPing());

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
            } catch (SocketException | SocketTimeoutException e) {
                if(matchSocket != null) {
                    for(SocketThread sock : matchSocket.getSockets()) {
                        sock.closeConnection();
                    }
                    matchSocket.setMatchStatus(MatchStatus.MATCH_ENDED);
                }
                System.out.println("Socket disconnected");
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sends a message through the socket, testing only
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

    /***
     * Parse the input message from the server and call the correct function into the client view
     * Has a switch case for all the different message types that extends ServerAbstractMessage
     * @param msg the input message to parse
     */
    private void messageParse(String msg) {
        Gson gson = new Gson();
        JsonObject jsonObj = JsonParser.parseString(msg).getAsJsonObject();
        String msgTopicString = jsonObj.get("type").getAsString();

        ServerMessageType type = ServerMessageType.valueOf(msgTopicString);

        switch(type) {
            case MOVE_RECEIVER:
                MoveReceiver moveReceiver = gson.fromJson(jsonObj, MoveReceiver.class);
                if(matchSocket.getCurrentSocket() == this) {
                    matchSocket.getCurrentPlayer().getDivinity().moveReceiver(moveReceiver.getDirection(), moveReceiver.getWorkerID());
                }
                break;
            case BUILD_RECEIVER:
                BuildReceiver buildReceiver = gson.fromJson(jsonObj, BuildReceiver.class);
                if(matchSocket.getCurrentSocket() == this) {
                    matchSocket.getCurrentPlayer().getDivinity().buildReceiver(buildReceiver.getDirection());
                }
                break;
            case PLAYER_DATA_RECEIVER:
                PlayerDataReceiver playerDataReceiver = gson.fromJson(jsonObj, PlayerDataReceiver.class);
                Color playerColor = Color.RED;
                if(matchSocket.getPlayerManagers().size() == 0) {
                    playerColor = Color.RED;
                } else if (matchSocket.getPlayerManagers().size() == 1) {
                    playerColor = Color.BLUE;
                } else if(matchSocket.getPlayerManagers().size() == 2) {
                    playerColor = Color.GREEN;
                }
                PlayerData playerData = new PlayerData(playerDataReceiver.getPlayerID(), playerColor, matchSocket.getPlayerManagers().size());
                matchSocket.addPlayer(new PlayerManager(playerData), this);
                break;
            case DIVINITY_RECEIVER:
                DivinityReceiver divinityReceiver = gson.fromJson(jsonObj, DivinityReceiver.class);
                matchSetup.divinityCreation(this, divinityReceiver.getDivinity());
                break;
            case ENDTURN_RECEIVER:
                EndTurnReceiver endTurnReceiver = gson.fromJson(jsonObj, EndTurnReceiver.class);
                if(matchSocket.getCurrentSocket() == this) {
                    matchRun.getTurnManager().passTurn();
                }
                break;
            case READY_RECEIVER:
                ReadyReceiver readyReceiver = gson.fromJson(jsonObj, ReadyReceiver.class);
                matchSetup.readyManagement(this);
                break;
            case WORKER_RECEIVER:
                WorkerReceiver workerReceiver = gson.fromJson(jsonObj, WorkerReceiver.class);
                matchRun.workerPlacement(this, workerReceiver);
                break;
            case PROMETHEUS_BUILD_RECEIVER:
                PrometheusBuildReceiver prometheusBuildReceiver = gson.fromJson(jsonObj, PrometheusBuildReceiver.class);
                if(matchSocket.getCurrentSocket() == this) {
                    if(matchSocket.getCurrentPlayer().getDivinity() instanceof Prometheus) {
                        ((Prometheus) matchSocket.getCurrentPlayer().getDivinity()).receiveWorker(prometheusBuildReceiver);
                    }
                }
                break;
            case ATLAS_BUILD_RECEIVER:
                AtlasBuildReceiver atlasBuildReceiver = gson.fromJson(jsonObj, AtlasBuildReceiver.class);
                if(matchSocket.getCurrentSocket() == this) {
                    if(matchSocket.getCurrentPlayer().getDivinity() instanceof Atlas) {
                        ((Atlas) matchSocket.getCurrentPlayer().getDivinity()).buildReceiver(atlasBuildReceiver.getDirection(), atlasBuildReceiver.isDome());
                    }
                }
                break;
            case DIVINITY_SELECTION:
                DivinitySelection divinitySelection = gson.fromJson(jsonObj, DivinitySelection.class);
                matchSetup.divinitySelection(divinitySelection.getDivinities());
                break;
            case PLAYER_NUMBER:
                PlayerNumber playerNumber = gson.fromJson(jsonObj, PlayerNumber.class);
                setMatchSocket(manager.getMatchSocket(playerNumber.getN()));
                break;
            case REPLAY:
                matchRun.detachSocket(this);
                sendMessage(new PlayerNumberReady());
        }
    }

    /***
     * Send a message trough socket converted to json
     * Accepts all the objects that extends ClientAbstractMessage
     * @param msg the object to send
     */
    public void sendMessage(ClientAbstractMessage msg) {
        Gson gson = new Gson();
        output.println(gson.toJson(msg));
    }

    /***
     * Set the match reference after the number of player choice and add my reference to the match
     * @param matchSocket the match reference to be set
     */
    public void setMatchSocket(MatchSocket matchSocket) {
        this.matchSocket = matchSocket;
        matchSocket.addSocket(this);
    }

    /***
     * Set the match setup reference at the start of the match
     * @param matchSetup object used during the setup phase of the match
     */
    public void setMatchSetup(MatchSetUp matchSetup) {
        this.matchSetup = matchSetup;
    }

    /***
     * Set the match reference on the game phase of the match
     * @param matchRun object used during the game phase of the match
     */
    public void setMatchRun(MatchRun matchRun) {
        this.matchRun = matchRun;
    }
}
