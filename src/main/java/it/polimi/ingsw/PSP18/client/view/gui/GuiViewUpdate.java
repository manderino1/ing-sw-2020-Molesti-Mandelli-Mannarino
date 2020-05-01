package it.polimi.ingsw.PSP18.client.view.gui;

import it.polimi.ingsw.PSP18.client.view.ViewUpdate;
import it.polimi.ingsw.PSP18.client.view.gui.scenes.Controller;
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
    private FXMLLoader loader;
    private Controller controller;

    public GuiViewUpdate() {
        loader = new FXMLLoader();
        Parent parent;
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

    }
}
