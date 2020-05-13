package it.polimi.ingsw.PSP18.client.view.gui.scenes;

import it.polimi.ingsw.PSP18.networking.messages.toclient.MatchLost;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class PopupController extends Controller {
    @FXML
    private Label label;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        this.pageID = "LosePopUp";
    }

    @FXML
    public void confirmClick() {
        view.hidePopUp();
    }

    @FXML
    public void setSpectate(){
        Platform.runLater(() -> label.setText("SPECTATE"));
    }
}
