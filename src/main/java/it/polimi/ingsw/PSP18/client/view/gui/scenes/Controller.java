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

/***
 * Generic controller class
 * Implements default function for setting socket reference and page ID to all sockets on scene switch
 */
public abstract class Controller implements Initializable {
    protected SocketClient socket;
    protected String pageID;
    protected GuiViewUpdate view;

    /***
     * Empty default initialize
     * @param url unused
     * @param resourceBundle unused
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    /***
     * Set the view reference to switch scene from the controller if necessary
     * @param view guiViewUpdate reference
     */
    public void setView(GuiViewUpdate view) {
        this.view = view;
    }

    /***
     * Set the socket reference for sending messages directly from the scene
     * @param socket connected socket reference
     */
    public void setSocket(SocketClient socket) {
        this.socket = socket;
    }

    /***
     * Get the page ID from its attribute, set in init function for all inherited classes
     * @return the controller page ID
     */
    public String getPageID() {
        return pageID;
    }

    /***
     * Standard function used from inherited classes for adding button effect on hovering enter
     * @param mouseEvent event reference
     */
    public void hoverEnter(javafx.scene.input.MouseEvent mouseEvent) {
        ColorAdjust blackout = new ColorAdjust();
        blackout.setBrightness(-0.2);

        ((Node)mouseEvent.getSource()).setEffect(blackout);
        ((Node)mouseEvent.getSource()).setCache(true);
        ((Node)mouseEvent.getSource()).setCacheHint(CacheHint.SPEED);
    }

    /***
     * tandard function used from inherited classes for removing effect on hovering exit
     * @param mouseEvent
     */
    public void hoverExit(MouseEvent mouseEvent) {
        ((Node)mouseEvent.getSource()).setEffect(null);
    }
}
