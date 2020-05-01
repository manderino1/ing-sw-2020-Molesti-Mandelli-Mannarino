package it.polimi.ingsw.PSP18.client.view.gui.scenes;

import it.polimi.ingsw.PSP18.networking.SocketClient;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class Controller implements Initializable {
    protected SocketClient socket;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setSocket(SocketClient socket) {
        this.socket = socket;
    }
}
