package it.polimi.ingsw.PSP18.client.view.gui.scenes;

import it.polimi.ingsw.PSP18.networking.messages.toclient.DivinityList;
import it.polimi.ingsw.PSP18.networking.messages.toserver.DivinityReceiver;
import it.polimi.ingsw.PSP18.networking.messages.toserver.DivinitySelection;
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
 * Controller of fxml to display the choice between three divinities
 */
public class PickDivinity3Controller extends PickDivinityController {
    @FXML
    public ImageView divinitySelected1;
    @FXML
    public ImageView divinitySelected2;
    @FXML
    public ImageView divinitySelected3;
    @FXML
    public ImageView divinityBox1;
    @FXML
    public ImageView divinityBox2;
    @FXML
    public ImageView divinityBox3;
    @FXML
    public ImageView divinityPower1;
    @FXML
    public ImageView divinityPower2;
    @FXML
    public ImageView divinityPower3;


    /***
     * @param location url reference (unused)
     * @param resources class bundle (unused)
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        this.pageID = "PickDivinity3";
    }

    /***
     * Divinity click callback
     */
    @FXML
    private void divinityClick1() {
        socket.sendMessage(new DivinityReceiver(divinityList.getDivinities().get(0)));
        view.goToWait();
    }

    /***
     * Divinity click callback
     */
    @FXML
    private void divinityClick2() {
        socket.sendMessage(new DivinityReceiver(divinityList.getDivinities().get(1)));
        view.goToWait();
    }

    /***
     * Divinity click callback
     */
    @FXML
    private void divinityClick3() {
        socket.sendMessage(new DivinityReceiver(divinityList.getDivinities().get(2)));
        view.goToWait();
    }

    /***
     * Set the correct image of the divinity
     * @param divinityList list of the possible divinities
     */
    public void showChoices(DivinityList divinityList) {
        this.divinityList = divinityList;

        Platform.runLater(() -> {
            Image image = new Image("/2DGraphics/" + divinityList.getDivinities().get(0) + ".png");
            divinitySelected1.setImage(image);
            divinitySelected1.setVisible(true);

            Image image1 = new Image("/2DGraphics/Power" + divinityList.getDivinities().get(0) + ".png");
            divinityPower1.setImage(image1);
            divinityPower1.setVisible(true);

            divinityBox1.setVisible(true);

            Image image2 = new Image("/2DGraphics/" + divinityList.getDivinities().get(1) + ".png");
            divinitySelected2.setImage(image2);
            divinitySelected2.setVisible(true);

            Image image3 = new Image("/2DGraphics/Power" + divinityList.getDivinities().get(1) + ".png");
            divinityPower2.setImage(image3);
            divinityPower2.setVisible(true);

            divinityBox2.setVisible(true);

            Image image4 = new Image("/2DGraphics/" + divinityList.getDivinities().get(2) + ".png");
            divinitySelected3.setImage(image4);
            divinitySelected3.setVisible(true);

            Image image5 = new Image("/2DGraphics/Power" + divinityList.getDivinities().get(2) + ".png");
            divinityPower3.setImage(image5);
            divinityPower3.setVisible(true);

            divinityBox3.setVisible(true);
        });



    }
}