package it.polimi.ingsw.PSP18.client.view.gui;

import it.polimi.ingsw.PSP18.client.view.ViewUpdate;
import it.polimi.ingsw.PSP18.client.view.gui.scenes.*;
import it.polimi.ingsw.PSP18.networking.SocketClient;
import it.polimi.ingsw.PSP18.networking.messages.toclient.*;
import it.polimi.ingsw.PSP18.networking.messages.toserver.Replay;
import it.polimi.ingsw.PSP18.server.model.PlayerData;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

/***
 * Class used for the management of the gui
 */
public class GuiViewUpdate extends ViewUpdate {
    private Stage stage;
    private Scene scene;
    private Controller controller;
    private Parent parent;
    private SocketClient socket;
    private String name;
    private Popup popup = new Popup();

    private ArrayList<PlayerData> playerDataArrayList = new ArrayList<>();

    /***
     * Constructor that init the javafx scene
     */
    public GuiViewUpdate() {
        FXMLLoader loader;
        loader = new FXMLLoader();
        try {
            loader.setLocation(getClass().getResource("/FXML/Login.fxml"));
            parent = loader.load();
            scene = new Scene(parent);
            controller = loader.getController();
            controller.setView(this);
            stage = new Stage();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle("Santorini");
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest( event -> {
            Platform.exit();
            System.exit(0);
        } );
    }

    /***
     * Set the socket value
     * @param socket socket reference
     */
    public void setSocket(SocketClient socket) {
        this.socket = socket;
    }

    /*
        From here we place the functions that are called when message of the selected types are read from socket
    */

    /***
     * If we currently are in the match scene, calls the moveUpdate function in MatchController
     * @param gameMapUpdate contains the new map, the last direction, the last x and y coordinate and a boolean that signals if the move is a build or a move
     */
    @Override
    public void updateMap(GameMapUpdate gameMapUpdate) {
        if (controller.getPageID().equals("Match")) {
            ((MatchController)controller).mapUpdate(gameMapUpdate);
        } else {
            switchScene("Match");
            ((MatchController)controller).fullMapUpdate(gameMapUpdate);
            ((MatchController)controller).updatePlayers(playerDataArrayList);
        }
    }

    /***
     * Ask to the player which worker needs to be move and where
     * @param movelist the message that says that the player needs to move
     */
    @Override
    public void moveUpdate(MoveList movelist) {
        if (controller.getPageID().equals("Match")) {
            ((MatchController)controller).showMoveList(movelist);
        } else {
            switchScene("Match");
        }
    }

    /***
     * Update the player data of the connected players and show them to the User
     * @param playerDataUpdate the message that asks for the player's data
     */
    @Override
    public void updatePlayerData(PlayerDataUpdate playerDataUpdate) {
        boolean present = false;
        if(playerDataUpdate != null) {
            for(PlayerData player : playerDataArrayList) {
                if (player.getPlayerID().equals(playerDataUpdate.getPlayerID())) {
                    player.setPlayerColor(playerDataUpdate.getPlayerColor());
                    player.setPlayOrder(playerDataUpdate.getPlayOrder());
                    player.setDivinity(playerDataUpdate.getDivinity());
                    if (playerDataUpdate.getReady()) {
                        player.setReady();
                    }
                    present = true;
                    break;
                }
            }
            if(!present) {
                PlayerData playerData = new PlayerData(playerDataUpdate.getPlayerID(), playerDataUpdate.getPlayerColor(), playerDataUpdate.getPlayOrder());
                playerData.setDivinity(playerDataUpdate.getDivinity());
                if (playerDataUpdate.getReady()) {
                    playerData.setReady();
                }
                playerDataArrayList.add(playerData);
            }
        }

        switch (controller.getPageID()) {
            case "Lobby":
                ((LobbyController) controller).updatePlayers(playerDataArrayList);
                break;
            case "WaitingRoom":
                ((WaitingRoomController) controller).updatePlayers(playerDataArrayList);
                break;
            case "Match":
                ((MatchController) controller).updatePlayers(playerDataArrayList);
                break;
        }
    }

    /***
     * Ask the player to insert his nick
     */
    @Override
    public void selectNick() {
        if(controller.getPageID().equals("Lobby")) { // Do not invert the two ifs
            ((LobbyController)controller).insertNick();
        } else if(controller.getPageID().equals("PlayerNumber")) {
            switchScene("Lobby");
        }
    }

    /***
     * Asks the player to chose his divinity
     * @param divinityList the message that asks for the player divinity
     */
    @Override
    public void selectDivinity(DivinityList divinityList) {
        ArrayList<String> divinities = divinityList.getDivinities();
        if(divinities.size() == 3) {
            switchScene("PickDivinity3");
            ((PickDivinity3Controller)controller).showChoices(divinityList);
        } else if (divinities.size() == 2) {
            switchScene("PickDivinity2");
            ((PickDivinity2Controller)controller).showChoices(divinityList);
        } else {
            switchScene("PickDivinity1");
            ((PickDivinity1Controller)controller).showChoices(divinityList);
        }

    }

    /***
     * Aks the player where he wants to build
     * @param buildList the message that asks for the player build
     */
    @Override
    public void buildUpdate(BuildList buildList) {
        if(controller.getPageID().equals("Match")) {
            ((MatchController)controller).standardBuildList(buildList);
        }
    }

    /***
     * Notify the player that he has lost
     * @param matchLost the message that notify the player that he has lost
     */
    @Override
    public void matchLostUpdate(MatchLost matchLost) {
        for(PlayerData playerData : playerDataArrayList){
            if (matchLost.getMatchLost().equals(playerData.getPlayerID())) {
                playerData.setLost();
                updatePlayerData(null);
            }
        }

        if(matchLost.isMe()) {
            Platform.runLater(() -> {
                ((MatchController)controller).setLabelOnLost();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/LosePopUp.fxml"));
                try {
                    popup.getContent().add(loader.load());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Controller controller = loader.getController();
                controller.setView(this);
                parent.setDisable(true); // Disable user input
                popup.show(stage);
                if(!matchLost.isFinished()) {
                    ((PopupController) controller).setSpectate();
                    ((PopupController) controller).setFinished(false);
                }
            });
        }
    }

    /***
     * Notify the player that he has won
     * @param matchWon the message that notify the player that he has won
     */
    @Override
    public void matchWonUpdate(MatchWon matchWon) {
        if(matchWon.isMe()) {
            Platform.runLater(() -> {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/WinPopUp.fxml"));
                try {
                    popup.getContent().add(loader.load());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Controller controller = loader.getController();
                controller.setView(this);
                parent.setDisable(true); // Disable user input
                popup.show(stage);
            });
        }
    }

    /***
     * Notify the player that the match has started
     * @param startMatch empty message
     */
    @Override
    public void startMatch(StartMatch startMatch) {
        if (controller.getPageID().equals("Match")) {
            ((MatchController)controller).setMatchStarted(true);
        }
    }

    /***
     * Asks the player when they are ready
     * @param matchReady the message that asks if players are ready
     */
    @Override
    public void matchReadyUpdate(MatchReady matchReady) {
        ((LobbyController)controller).unlockReady();
    }

    /***
     * Set the worker reference
     * @param placeReady empty message
     */
    @Override
    public void setWorker(PlaceReady placeReady) {
        if (!controller.getPageID().equals("Match")) {
            switchScene("Match");
        }
        ((MatchController)controller).placeWorkerInit();
    }

    /***
     * Method used in case prometheus wants to build before the movement
     * @param prometheusBuildList contains two sets of possible moves one for each worker
     */
    @Override
    public void prometheusBuildListUpdate(PrometheusBuildList prometheusBuildList) {
        if (controller.getPageID().equals("Match")) {
            ((MatchController)controller).prometheusBuildShow(prometheusBuildList);
        } else {
            switchScene("Match");
        }
    }

    /***
     * In case the player already moved and his hero can move again
     * @param singleMoveList a set of possible moves
     */
    @Override
    public void singleMoveUpdate(SingleMoveList singleMoveList) {
        if (controller.getPageID().equals("Match")) {
            ((MatchController)controller).singleMoveUpdate(singleMoveList);
        }
    }

    /***
     * In case a divinity wants to move again using his special ability
     * @param buildListFlag a set of possible building moves
     */
    @Override
    public void buildListFlagUpdate(BuildListFlag buildListFlag) {
        if (controller.getPageID().equals("Match")) {
            ((MatchController)controller).optionalBuildUpdate(buildListFlag);
        }
    }

    /***
     * Method used to end the current turn
     * @param endTurnAvaiable message used to end the turn
     */
    @Override
    public void endTurn(EndTurnAvaiable endTurnAvaiable) {
        if (controller.getPageID().equals("Match")) {
            ((MatchController)controller).showEndTurn();
        }
    }

    /***
     * Method used in case atlas wants to build using his power
     * @param atlasBuildList message containing a list of possible building moves
     */
    @Override
    public void atlasBuildUpdate(AtlasBuildList atlasBuildList) {
        if (controller.getPageID().equals("Match")) {
            ((MatchController)controller).atlasBuild(atlasBuildList);
        }
    }

    /***
     * The user has to select n divinities from this list
     * @param divinityPick the list of divinities to pick from
     */
    @Override
    public void divinitySelection(DivinityPick divinityPick) {
        switchScene("PickDivinity9");
        ((PickDivinity9Controller)controller).setnPlayers(divinityPick.getnOfPlayers());
    }

    /***
     * Tell the server the number of players
     */
    @Override
    public void playerNumber() {
        if (!controller.getPageID().equals("PlayerNumber")) {
            switchScene("PlayerNumber");
        }
    }

    public void switchScene(String name) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/FXML/" + name + ".fxml"));
        try {
            parent = loader.load();
            controller = loader.getController();
            controller.setSocket(socket);
            controller.setView(this);
            Platform.runLater(() -> scene.setRoot(parent));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void goToWait(){
        switchScene("WaitingRoom");
        ((WaitingRoomController)controller).updatePlayers(playerDataArrayList);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void hidePopUp(boolean finished, boolean reconnect) {
        Platform.runLater(()->popup.hide());
        popup.getContent().clear();

        // If the connection has ended just go back to the player number selection screen
        if(reconnect) {
            return;
        }

        // If the match is finished go back to the player selection screen
        if(finished) {
            playerDataArrayList.clear();
            socket.sendMessage(new Replay());
        } else {
            parent.setDisable(false); // Re-enable the user input
        }
    }

    @Override
    public void serverDisconnected(){
        Platform.runLater(() -> {
            parent.setDisable(true);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/ReconnectPopUp.fxml"));
            try {
                popup.getContent().add(loader.load());
            } catch (IOException e) {
                e.printStackTrace();
            }
            Controller controller = loader.getController();
            controller.setView(this);
            popup.show(stage);
        });
    }

    public void reconnect() {
        playerDataArrayList.clear();
        try {
            Socket sock = new Socket(socket.getIP().getHostName(), socket.getIP().getPort());
            socket = new SocketClient(sock, this);
            socket.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
