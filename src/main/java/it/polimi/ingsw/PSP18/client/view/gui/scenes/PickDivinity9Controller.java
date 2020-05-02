package it.polimi.ingsw.PSP18.client.view.gui.scenes;

import it.polimi.ingsw.PSP18.networking.messages.toserver.DivinitySelection;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class PickDivinity9Controller extends Controller {
    @FXML
    private CheckBox athenaCheckbox, minotaurCheckbox, hephaestusCheckbox, panCheckbox, atlasCheckbox;
    @FXML
    private CheckBox demeterCheckbox, artemisCheckbox, apolloCheckbox, prometheusCheckbox;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        this.pageID = "PickDivinity9";
    }

    @FXML
    private void confirmClick() {
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
    }
}
