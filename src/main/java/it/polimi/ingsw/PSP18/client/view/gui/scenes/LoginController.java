package it.polimi.ingsw.PSP18.client.view.gui.scenes;

import it.polimi.ingsw.PSP18.client.view.gui.GuiViewUpdate;
import it.polimi.ingsw.PSP18.networking.SocketClient;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

public class LoginController extends Controller {
    @FXML
    private TextField ipAddress;

    private GuiViewUpdate view;
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

    public void setView(GuiViewUpdate view) {
        this.view = view;
    }
}
