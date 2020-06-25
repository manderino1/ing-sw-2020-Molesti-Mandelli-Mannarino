package it.polimi.ingsw.PSP18.client.view.gui.scenes;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

/***
 * Controller for win/lose/reconnect popup
 */
public class PopupController extends Controller {
    @FXML
    private ImageView defeatLogo;
    @FXML
    private ImageView spectateButton;
    @FXML
    private Label label2;

    boolean finished = true;

    /***
     * @param location unused
     * @param resources unused
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        this.pageID = "LosePopUp";
    }

    /***
     * On loss if the match is still running click spectate for staying in the match
     */
    @FXML
    private void spectateClick() {
        view.hidePopUp(false, false);
    }

    /***
     * On win/loss play again callback to go back into player number selection screen
     */
    @FXML
    private void againClick() {
        view.hidePopUp(true, false);
    }

    /***
     * On server crash or player disconnection go back reconnect for reloading backup
     */
    @FXML
    private void reconnectClick() {
        view.hidePopUp(false, true);
        view.reconnect();
    }

    /***
     * Enable spectate button on setup screen
     */
    @FXML
    public void setSpectate(){
        Platform.runLater(() -> {
            defeatLogo.setVisible(false);
            spectateButton.setVisible(true);
            label2.setVisible(true);
        });
    }

    /***
     * Set the match as finished if the game has ended
     * @param finished true if the match is ended
     */
    public void setFinished(boolean finished) {
        this.finished = finished;
    }
}
