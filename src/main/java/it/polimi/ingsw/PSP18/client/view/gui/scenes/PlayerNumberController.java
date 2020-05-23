package it.polimi.ingsw.PSP18.client.view.gui.scenes;

import it.polimi.ingsw.PSP18.networking.messages.toserver.PlayerNumber;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

/***
 * Controller for player number selection, after login
 */
public class PlayerNumberController extends Controller {
    /***
     * @param location url reference (unused)
     * @param resources class bundle (unused)
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        this.pageID = "PlayerNumber";
    }

    @FXML
    private void selectedTwo() {
        socket.sendMessage(new PlayerNumber(2));
    }

    @FXML
    private void selectedThree() {
        socket.sendMessage(new PlayerNumber(3));
    }
}
