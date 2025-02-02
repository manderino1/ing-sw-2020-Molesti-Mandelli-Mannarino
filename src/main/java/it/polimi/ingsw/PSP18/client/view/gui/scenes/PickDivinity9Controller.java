package it.polimi.ingsw.PSP18.client.view.gui.scenes;

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
import java.util.HashMap;
import java.util.ResourceBundle;

/***
 * Controller for fxml to prompt to the last user the selection between all divinities
 */
public class PickDivinity9Controller extends Controller {
    @FXML
    private CheckBox athenaCheckbox, minotaurCheckbox, hephaestusCheckbox, panCheckbox, atlasCheckbox;
    @FXML
    private CheckBox demeterCheckbox, artemisCheckbox, apolloCheckbox, prometheusCheckbox;
    @FXML
    private ImageView athena, minotaur, hephaestus, pan, atlas, demeter, artemis, apollo, prometheus;
    @FXML
    private ImageView nextButton;
    @FXML
    private Label topText;

    private int nPlayers;
    private boolean sendOK = false, sent = false;
    private HashMap<ImageView, CheckBox> checkBoxHashMap = new HashMap<>();

    /***
     * Init the map between images and checkboxes to sync clicks
     * @param location unused
     * @param resources unused
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        this.pageID = "PickDivinity9";

        checkBoxHashMap.put(athena, athenaCheckbox);
        checkBoxHashMap.put(minotaur, minotaurCheckbox);
        checkBoxHashMap.put(hephaestus, hephaestusCheckbox);
        checkBoxHashMap.put(pan, panCheckbox);
        checkBoxHashMap.put(atlas, atlasCheckbox);
        checkBoxHashMap.put(demeter, demeterCheckbox);
        checkBoxHashMap.put(artemis, artemisCheckbox);
        checkBoxHashMap.put(apollo, apolloCheckbox);
        checkBoxHashMap.put(prometheus, prometheusCheckbox);
    }

    /***
     * Confirm click callback, if green send the divinities list to the server
     */
    @FXML
    private void confirmClick() {
        if(sendOK && !sent) {
            ArrayList<String> divinities = new ArrayList<>();
            if(athenaCheckbox.isSelected()) {
                divinities.add("Athena");
            }
            if(minotaurCheckbox.isSelected()) {
                divinities.add("Minotaur");
            }
            if(hephaestusCheckbox.isSelected()) {
                divinities.add("Hephaestus");
            }
            if(panCheckbox.isSelected()) {
                divinities.add("Pan");
            }
            if(atlasCheckbox.isSelected()) {
                divinities.add("Atlas");
            }
            if(demeterCheckbox.isSelected()) {
                divinities.add("Demeter");
            }
            if(artemisCheckbox.isSelected()) {
                divinities.add("Artemis");
            }
            if(apolloCheckbox.isSelected()) {
                divinities.add("Apollo");
            }
            if(prometheusCheckbox.isSelected()) {
                divinities.add("Prometheus");
            }
            socket.sendMessage(new DivinitySelection(divinities));
            sent = true;
            view.goToWait();
        }
    }

    /***
     * Checkbox click callback, update the confirm button if necessary
     */
    @FXML
    private void checkboxClick() {
        int counter = 0;
        if(athenaCheckbox.isSelected()) {
            counter++;
        }
        if(minotaurCheckbox.isSelected()) {
            counter++;
        }
        if(hephaestusCheckbox.isSelected()) {
            counter++;
        }
        if(panCheckbox.isSelected()) {
            counter++;
        }
        if(atlasCheckbox.isSelected()) {
            counter++;
        }
        if(demeterCheckbox.isSelected()) {
            counter++;
        }
        if(artemisCheckbox.isSelected()) {
            counter++;
        }
        if(apolloCheckbox.isSelected()) {
            counter++;
        }
        if(prometheusCheckbox.isSelected()) {
            counter++;
        }

        if(counter == nPlayers) {
            Image image = new Image("/2DGraphics/GreenButton.png");
            nextButton.setImage(image);
            sendOK = true;
        } else {
            Image image = new Image("/2DGraphics/RedButton.png");
            nextButton.setImage(image);
            sendOK = false;
        }
    }

    /***
     * Set the number of players in the game for correct functionality of confirm button
     * @param nPlayers the number of players in the game
     */
    public void setnPlayers(int nPlayers) {
        this.nPlayers = nPlayers;
        Platform.runLater(() -> topText.setText("Pick " + nPlayers + " divinities for the game"));
    }

    /***
     * Divinity callback click, update hashMap and fire checbox click
     * @param mouseEvent the mouse event reference for getting divinity source
     */
    @FXML
    private void divinityClick(MouseEvent mouseEvent) {
        checkBoxHashMap.get((ImageView)mouseEvent.getSource()).fire();
        checkboxClick();
    }
}
