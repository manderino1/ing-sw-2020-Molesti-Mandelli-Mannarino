package it.polimi.ingsw.PSP18.client.view.gui.scenes;

import it.polimi.ingsw.PSP18.client.view.gui.GuiViewUpdate;
import it.polimi.ingsw.PSP18.networking.SocketClient;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class Controller implements Initializable {
    protected SocketClient socket;
    protected String pageID;
    protected GuiViewUpdate view;

    public void setView(GuiViewUpdate view) {
        this.view = view;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void setSocket(SocketClient socket) {
        this.socket = socket;
    }

    public String getPageID() {
        return pageID;
    }
}
