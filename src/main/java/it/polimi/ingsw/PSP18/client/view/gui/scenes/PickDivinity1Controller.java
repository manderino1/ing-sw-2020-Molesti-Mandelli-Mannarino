package it.polimi.ingsw.PSP18.client.view.gui.scenes;

import it.polimi.ingsw.PSP18.networking.messages.toclient.DivinityList;
import it.polimi.ingsw.PSP18.networking.messages.toserver.DivinityReceiver;
import it.polimi.ingsw.PSP18.networking.messages.toserver.DivinitySelection;
import it.polimi.ingsw.PSP18.server.model.PlayerData;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/***
 * Controller of fxml to display the choice between a single divinity
 */
public class PickDivinity1Controller extends PickDivinityController {
    @FXML
    public ImageView divinitySelected;
    @FXML
    public ImageView divinityBox;
    @FXML
    public ImageView divinityPower;


    /***
     * @param location url reference (unused)
     * @param resources class bundle (unused)
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        this.pageID = "PickDivinity1";
    }

    /***
     * Divinity click callback
     */
    @FXML
    public void divinityClick() {
        socket.sendMessage(new DivinityReceiver(divinityList.getDivinities().get(0)));
        view.goToWait();
    }

    /***
     * Set the correct image of the divinity
     * @param divinityList list of the possible divinities
     */
    @FXML
    public void showChoices(DivinityList divinityList) {
        this.divinityList = divinityList;
        Platform.runLater(() -> {
            Image image = new Image("/2DGraphics/" + divinityList.getDivinities().get(0) + ".png");
            divinitySelected.setImage(image);
            divinitySelected.setVisible(true);

            Image image1 = new Image("/2DGraphics/Power" + divinityList.getDivinities().get(0) + ".png");
            divinityPower.setImage(image1);
            divinityPower.setVisible(true);

            divinityBox.setVisible(true);
        });
    }
}