package it.polimi.ingsw.PSP18.client.view.gui;

import it.polimi.ingsw.PSP18.client.view.ViewUpdate;
import it.polimi.ingsw.PSP18.client.view.gui.scenes.LoginController;
import it.polimi.ingsw.PSP18.client.view.gui.scenes.Controller;
import it.polimi.ingsw.PSP18.client.view.gui.scenes.LobbyController;
import it.polimi.ingsw.PSP18.client.view.gui.scenes.PickDivinity9Controller;
import it.polimi.ingsw.PSP18.networking.SocketClient;
import it.polimi.ingsw.PSP18.networking.messages.toclient.*;
import it.polimi.ingsw.PSP18.server.model.PlayerData;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

    private ArrayList<PlayerData> playerDataArrayList = new ArrayList<>();

    public GuiViewUpdate() {
        FXMLLoader loader;
        loader = new FXMLLoader();
        try {
            loader.setLocation(getClass().getResource("/FXML/Login.fxml"));
            parent = loader.load();
            scene = new Scene(parent);
            controller = loader.getController();
            ((LoginController)controller).setView(this);
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
    @Override
    public void updateMap(GameMapUpdate gameMapUpdate) {

    }

    @Override
    public void moveUpdate(MoveList movelist) {

    }

    @Override
    public void updatePlayerData(PlayerDataUpdate playerDataUpdate) {
        boolean present = false;
        if(playerDataUpdate != null) {
            for(PlayerData player : playerDataArrayList) {
                if (player.getPlayerID().equals(playerDataUpdate.getPlayerID())) {
                    player.setDivinity(playerDataUpdate.getDivinity());
                    present = true;
                    break;
                }
            }
            if(!present) {
                PlayerData playerData = new PlayerData(playerDataUpdate.getPlayerID(), playerDataUpdate.getPlayerColor(), playerDataUpdate.getPlayOrder());
                playerData.setDivinity(playerDataUpdate.getDivinity());
                playerDataArrayList.add(playerData);
            }
        }

        if(controller.getPageID().equals("Lobby")) {
            ((LobbyController)controller).updatePlayers(playerDataArrayList);
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

    }

    @Override
    public void buildUpdate(BuildList buildList) {

    }

    @Override
    public void matchLostUpdate(MatchLost matchLost) {

    }

    @Override
    public void matchWonUpdate(MatchWon matchWon) {

    }

    @Override
    public void startMatch(StartMatch startMatch) {

    }

    @Override
    public void matchReadyUpdate(MatchReady matchReady) {
        ((LobbyController)controller).unlockReady();
    }

    @Override
    public void setWorker(PlaceReady placeReady) {

    }

    @Override
    public void prometheusBuildListUpdate(PrometheusBuildList prometheusBuildList) {

    }

    @Override
    public void singleMoveUpdate(SingleMoveList singleMoveList) {

    }

    @Override
    public void buildListFlagUpdate(BuildListFlag buildListFlag) {

    }

    @Override
    public void endTurn(EndTurnAvaiable endTurnAvaiable) {

    }

    @Override
    public void atlasBuildUpdate(AtlasBuildList atlasBuildList) {

    }

    @Override
    public void divinitySelection(DivinityPick divinityPick) {
        switchScene("PickDivinity9");
        ((PickDivinity9Controller)controller).setnPlayers(divinityPick.getnOfPlayers());
    }

    private void switchScene(String name) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/FXML/" + name + ".fxml"));
        try {
            parent = loader.load();
            controller = loader.getController();
            controller.setSocket(socket);
            scene.setRoot(parent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
