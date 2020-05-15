package it.polimi.ingsw.PSP18.client.view.gui.scenes;

import it.polimi.ingsw.PSP18.networking.messages.toclient.MatchLost;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class PopupController extends Controller {
    @FXML
    private ImageView defeatLogo;
    @FXML
    private ImageView spectateButton;
    @FXML
    private Label label2;
    @FXML
    private Label label;

    boolean finished = true;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        this.pageID = "LosePopUp";
    }

    @FXML
    public void confirmClick() {
        view.hidePopUp(finished);
    }

    @FXML
    public void setSpectate(){
        Platform.runLater(() -> {
            defeatLogo.setVisible(false);
            spectateButton.setVisible(true);
            label2.setVisible(true);
        });
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }
}
