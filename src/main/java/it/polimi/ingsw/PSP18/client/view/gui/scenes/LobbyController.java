package it.polimi.ingsw.PSP18.client.view.gui.scenes;

import it.polimi.ingsw.PSP18.networking.messages.toserver.PlayerDataReceiver;
import it.polimi.ingsw.PSP18.networking.messages.toserver.ReadyReceiver;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class LobbyController extends Controller {
    @FXML
    private ImageView readyButton;
    @FXML
    private ImageView confirmButton;
    @FXML
    private TextField nicknameBar;
    private boolean nickOK = false;
    private boolean isReady = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
    }

    @FXML
    private void readyButtonClicked(MouseEvent mouseEvent) {
        if(!isReady) {
            Image image = new Image("/2DGraphics/GreenButton.png");
            readyButton.setImage(image);

            socket.sendMessage(new ReadyReceiver());
            isReady = true;
        }
    }

    @FXML
    private void confirmButtonClicked(MouseEvent mouseEvent) {
        if(!nickOK) {
            socket.sendMessage(new PlayerDataReceiver(nicknameBar.getText()));
        }
    }

    public void insertNick() {
        Image image = new Image("/2DGraphics/RedButton.png");
        confirmButton.setImage(image);

        nickOK = false;
    }

    public void unlockReady() {
        Image image = new Image("/2DGraphics/GreenButton.png");
        confirmButton.setImage(image);

        nickOK = true;
    }
}
