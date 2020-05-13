package it.polimi.ingsw.PSP18.client.view.gui;

import it.polimi.ingsw.PSP18.client.view.ViewUpdate;
import it.polimi.ingsw.PSP18.client.view.gui.scenes.*;
import it.polimi.ingsw.PSP18.networking.SocketClient;
import it.polimi.ingsw.PSP18.networking.messages.toclient.*;
import it.polimi.ingsw.PSP18.server.controller.Match;
import it.polimi.ingsw.PSP18.server.model.PlayerData;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;

public class GuiViewUpdate extends ViewUpdate {
    private Stage stage;
    private Scene scene;
    private Controller controller;
    private Parent parent;
    private SocketClient socket;
    private final int PORT = 9002;
    private InetAddress host;
    private String name;
    private Popup popup = new Popup();

    private ArrayList<PlayerData> playerDataArrayList = new ArrayList<>();

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
    }

    public void setSocket(SocketClient socket) {
        this.socket = socket;
    }

    /*
        From here we place the functions that are called when message of the selected types are read from socket
    */

    /***
     * If we currently are in the match scene, calls the moveUpdate function in MatchController
     * @param gameMapUpdate contains the new map, the last diretction, the last x and y coordinate and a boolean that signals if the move is a build or a move
     */
    @Override
    public void updateMap(GameMapUpdate gameMapUpdate) {
        if (controller.getPageID().equals("Match")) {
            ((MatchController)controller).mapUpdate(gameMapUpdate);
        } else {
            switchScene("Match");
            ((MatchController)controller).mapUpdate(gameMapUpdate);
            ((MatchController)controller).updatePlayers(playerDataArrayList);
        }
    }

    @Override
    public void moveUpdate(MoveList movelist) {
        if (controller.getPageID().equals("Match")) {
            ((MatchController)controller).showMoveList(movelist);
        }
    }

    @Override
    public void updatePlayerData(PlayerDataUpdate playerDataUpdate) {
        boolean present = false;
        if(playerDataUpdate != null) {
            for(PlayerData player : playerDataArrayList) {
                if (player.getPlayerID().equals(playerDataUpdate.getPlayerID())) {
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

        if(controller.getPageID().equals("Lobby")) {
            ((LobbyController)controller).updatePlayers(playerDataArrayList);
        } else if (controller.getPageID().equals("WaitingRoom")) {
            ((WaitingRoomController)controller).updatePlayers(playerDataArrayList);
        } else if (controller.getPageID().equals("Match")) {
            ((MatchController)controller).updatePlayers(playerDataArrayList);
        }
    }

    @Override
    public void selectNick() {
        if(controller.getPageID().equals("Lobby")) { // Do not invert the two ifs
            ((LobbyController)controller).insertNick();
        } else if(controller.getPageID().equals("Login")) {
            switchScene("Lobby");
        }
    }

    @Override
    public void selectDivinity(DivinityList divinityList) {
        ArrayList<String> divinities = divinityList.getDivinities();
        String nextScene;
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

    @Override
    public void buildUpdate(BuildList buildList) {
        if(controller.getPageID().equals("Match")) {
            ((MatchController)controller).standardBuildList(buildList);
        }
    }

    @Override
    public void matchLostUpdate(MatchLost matchLost) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/LosePopUp.fxml"));
        try {
            popup.getContent().add(loader.load());
            Controller controller = loader.getController();
            controller.setView(this);
            Platform.runLater(() -> popup.show(stage));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void matchWonUpdate(MatchWon matchWon) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/WinPopUp.fxml"));
        try {
            popup.getContent().add(loader.load());
            Controller controller = loader.getController();
            controller.setView(this);
            Platform.runLater(() -> popup.show(stage));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void startMatch(StartMatch startMatch) {
        if (controller.getPageID().equals("Match")) {
            ((MatchController)controller).setMatchStarted(true);
        }
    }

    @Override
    public void matchReadyUpdate(MatchReady matchReady) {
        ((LobbyController)controller).unlockReady();
    }

    @Override
    public void setWorker(PlaceReady placeReady) {
        if (!controller.getPageID().equals("Match")) {
            switchScene("Match");
        }
        ((MatchController)controller).placeWorkerInit();
    }

    @Override
    public void prometheusBuildListUpdate(PrometheusBuildList prometheusBuildList) {
        if (controller.getPageID().equals("Match")) {
            ((MatchController)controller).prometheusBuildShow(prometheusBuildList);
        }
    }

    @Override
    public void singleMoveUpdate(SingleMoveList singleMoveList) {
        if (controller.getPageID().equals("Match")) {
            ((MatchController)controller).singleMoveUpdate(singleMoveList);
        }
    }

    @Override
    public void buildListFlagUpdate(BuildListFlag buildListFlag) {
        if (controller.getPageID().equals("Match")) {
            ((MatchController)controller).optionalBuildUpdate(buildListFlag);
        }
    }

    @Override
    public void endTurn(EndTurnAvaiable endTurnAvaiable) {
        if (controller.getPageID().equals("Match")) {
            ((MatchController)controller).showEndTurn();
        }
    }

    @Override
    public void atlasBuildUpdate(AtlasBuildList atlasBuildList) {
        if (controller.getPageID().equals("Match")) {
            ((MatchController)controller).atlasBuild(atlasBuildList);
        }
    }

    @Override
    public void divinitySelection(DivinityPick divinityPick) {
        switchScene("PickDivinity9");
        ((PickDivinity9Controller)controller).setnPlayers(divinityPick.getnOfPlayers());
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

    public void hidePopUp() {
        Platform.runLater(()->popup.hide());
        popup.getContent().clear();
    }
}
