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

/***
 * Controller for login.fxml, used for IP input and socket connection
 */
public class LoginController extends Controller {
    @FXML
    private TextField ipAddress;
    private final int PORT = 9002;

    /***
     * @param location url reference (unused)
     * @param resources class bundle (unused)
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        this.pageID = "Login";
    }

    /***
     * Connect button callback, try to connect to the chosen ip and eventually prompt retry message
     */
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
}
