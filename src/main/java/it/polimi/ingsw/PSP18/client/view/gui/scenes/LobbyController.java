package it.polimi.ingsw.PSP18.client.view.gui.scenes;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class LobbyController extends Controller {

    public ImageView readyButton;
    public ImageView confirmButton;
    public TextField nicknameBar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
    }

    public void readyButtonClicked(MouseEvent mouseEvent) {
        Image image = new Image("/2DGraphics/GreenButton.png");
        readyButton.setImage(image);
    }

    public void confirmButtonClicked(MouseEvent mouseEvent) {
        Image image = new Image("/2DGraphics/GreenButton.png");
        confirmButton.setImage(image);

        if (nicknameBar.getCharacters() == ) {

        }
    }
}
