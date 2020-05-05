package it.polimi.ingsw.PSP18.client.view.gui.scenes;

import it.polimi.ingsw.PSP18.server.model.PlayerData;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class WaitingRoomController extends Controller {
    @FXML
    private Text firstPlayerReady;
    @FXML
    private Text secondPlayerReady;
    @FXML
    private Text secondPlayer;
    @FXML
    private Text firstPlayer;
    @FXML
    private ImageView firstPlayerDivinity;
    @FXML
    private ImageView secondPlayerDivinity;
    @FXML
    private ImageView yellowBox2;
    @FXML
    public ImageView blueBox2;
    @FXML
    private Text nick1;
    @FXML
    private ImageView yellowBox1;
    @FXML
    private ImageView blueBox1;
    @FXML
    private Text nick2;

    private boolean firstNickInput = true;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        this.pageID = "WaitingRoom";
    }


    public void updatePlayers(ArrayList<PlayerData> players) {
        boolean firstNickInput = true;
        for (PlayerData playerData : players){
            if (!playerData.getPlayerID().equals(view.getName()) && firstNickInput) {
                firstPlayer.setText(playerData.getPlayerID());
                int playOrder = playerData.getPlayOrder() + 1;
                firstPlayerReady.setText("Play Order: " + Integer.toString(playOrder));

                if(playerData.getDivinity() == null){
                    Image image = new Image("/2DGraphics/Random.png");
                    firstPlayerDivinity.setImage(image);
                } else {
                    Image image = new Image("/2DGraphics/" + playerData.getDivinity() + ".png");
                    firstPlayerDivinity.setImage(image);
                }
                firstNickInput = false;

            } else if (!playerData.getPlayerID().equals(view.getName()) && !firstNickInput && !playerData.getPlayerID().equals(firstPlayer.getText()) ) {
                blueBox2.setVisible(true);
                nick2.setVisible(true);
                secondPlayer.setText(playerData.getPlayerID());
                secondPlayer.setVisible(true);
                int playOrder = playerData.getPlayOrder() + 1;
                secondPlayerReady.setText("Play Order: " + Integer.toString(playOrder));
                secondPlayerReady.setVisible(true);

                if(playerData.getDivinity() == null){
                    Image image = new Image("/2DGraphics/Random.png");
                    secondPlayerDivinity.setImage(image);
                    secondPlayerDivinity.setVisible(true);
                    yellowBox2.setVisible(true);
                } else {
                    Image image = new Image("/2DGraphics/" + playerData.getDivinity() + ".png");
                    secondPlayerDivinity.setImage(image);
                    secondPlayerDivinity.setVisible(true);
                    yellowBox2.setVisible(true);
                }
            }
        }
    }
}
