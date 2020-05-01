package it.polimi.ingsw.PSP18.client.view.gui.scenes;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class LobbyController extends Controller {
    @FXML
    ImageView confirmButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
    }

    @FXML
    public void confirmMouseEnter() {
        Image image= new Image(getClass().getResourceAsStream("/2DGraphics/RedButton.png"));
        confirmButton.setImage(image);
    }

}
