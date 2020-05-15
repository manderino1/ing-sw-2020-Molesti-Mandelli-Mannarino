package it.polimi.ingsw.PSP18.client.view.gui.scenes;

import it.polimi.ingsw.PSP18.networking.SocketClient;
import it.polimi.ingsw.PSP18.networking.messages.toserver.PlayerNumber;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.CacheHint;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController extends Controller {
    @FXML
    private ImageView confirmButton;
    @FXML
    private ImageView background1;
    @FXML
    private ImageView background2;
    @FXML
    private Label labelText;
    @FXML
    private ImageView confirmButton2;
    @FXML
    private ImageView confirmButton3;
    @FXML
    private Label selectLabel2;
    @FXML
    private Label selectLabel3;
    @FXML
    private TextField ipAddress;

    private final int PORT = 9002;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        this.pageID = "Login";
    }

    @FXML
    private void inputIP() {
        try {
            InetAddress host = InetAddress.getByName(ipAddress.getText());
            Socket sock = new Socket(host, PORT);
            socket = new SocketClient(sock, view);
            socket.start();
            view.setSocket(socket);
        } catch (IOException e) {
            ipAddress.setText("Unknown IP, Retry");
        }
    }

    @FXML
    public void selectPlayerNumber(){
        Platform.runLater(() -> {
            confirmButton.setDisable(true);
            ipAddress.setDisable(true);

            confirmButton2.setDisable(false);
            confirmButton3.setDisable(false);
            labelText.setDisable(false);
            selectLabel2.setDisable(false);
            selectLabel3.setDisable(false);
            background1.setDisable(false);
            background2.setDisable(false);

            confirmButton2.setVisible(true);
            confirmButton3.setVisible(true);
            labelText.setVisible(true);
            selectLabel2.setVisible(true);
            selectLabel3.setVisible(true);
            background1.setVisible(true);
            background2.setVisible(true);
        });
    }

    @FXML
    private void selectedTwo(MouseEvent mouseEvent) {
        socket.sendMessage(new PlayerNumber(2));
    }

    @FXML
    private void selectedThree(MouseEvent mouseEvent) {
        socket.sendMessage(new PlayerNumber(3));
    }
}
