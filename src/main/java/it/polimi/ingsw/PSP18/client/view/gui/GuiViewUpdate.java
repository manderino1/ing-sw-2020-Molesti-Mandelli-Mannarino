package it.polimi.ingsw.PSP18.client.view.gui;

import it.polimi.ingsw.PSP18.client.view.ViewUpdate;
import it.polimi.ingsw.PSP18.client.view.gui.scenes.Controller;
import it.polimi.ingsw.PSP18.client.view.gui.scenes.LobbyController;
import it.polimi.ingsw.PSP18.networking.SocketClient;
import it.polimi.ingsw.PSP18.networking.messages.toclient.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class GuiViewUpdate extends ViewUpdate {
    private Stage stage;
    private Scene scene;
    private Controller controller;
    private Parent parent;
    private SocketClient socket;

    public GuiViewUpdate() {
        FXMLLoader loader;
        loader = new FXMLLoader();
        try {
            loader.setLocation(getClass().getResource("/FXML/Lobby.fxml"));
            parent = loader.load();
            scene = new Scene(parent);
            controller = loader.getController();
            stage = new Stage();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle("Santorini");
        stage.setScene(scene);
        stage.show();
    }

    // TODO: Non ci serve dopo
    public void setSocket(SocketClient socket) {
        this.socket = socket;
        controller.setSocket(socket);
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

    }

    @Override
    public void selectNick() {
        ((LobbyController)controller).insertNick();
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
    }

    private void switchScene(String name) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/FXML/" + name + ".fxml"));
        try {
            parent = loader.load();
            controller = loader.getController();
            //controller.setSocket(socket);
            scene.setRoot(parent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
