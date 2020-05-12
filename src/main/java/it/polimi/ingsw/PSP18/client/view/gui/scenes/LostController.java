package it.polimi.ingsw.PSP18.client.view.gui.scenes;

import javafx.fxml.FXML;

import java.net.URL;
import java.util.ResourceBundle;

public class LostController extends Controller {
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        this.pageID = "LosePopUp";
    }

    @FXML
    public void confirmClick() {
        view.hidePopUp();
    }
}
