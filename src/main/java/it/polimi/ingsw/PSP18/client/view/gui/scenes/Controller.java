package it.polimi.ingsw.PSP18.client.view.gui.scenes;

import it.polimi.ingsw.PSP18.client.view.gui.GuiViewUpdate;
import it.polimi.ingsw.PSP18.networking.SocketClient;
import javafx.fxml.Initializable;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.input.MouseEvent;

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

    public void hoverEnter(javafx.scene.input.MouseEvent mouseEvent) {
        ColorAdjust blackout = new ColorAdjust();
        blackout.setBrightness(-0.2);

        ((Node)mouseEvent.getSource()).setEffect(blackout);
        ((Node)mouseEvent.getSource()).setCache(true);
        ((Node)mouseEvent.getSource()).setCacheHint(CacheHint.SPEED);
    }

    public void hoverExit(MouseEvent mouseEvent) {
        ((Node)mouseEvent.getSource()).setEffect(null);
    }
}
