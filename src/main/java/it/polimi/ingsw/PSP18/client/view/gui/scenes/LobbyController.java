package it.polimi.ingsw.PSP18.client.view.gui.scenes;

import it.polimi.ingsw.PSP18.networking.messages.toserver.PlayerDataReceiver;
import it.polimi.ingsw.PSP18.networking.messages.toserver.ReadyReceiver;
import it.polimi.ingsw.PSP18.server.controller.divinities.Divinity;
import it.polimi.ingsw.PSP18.server.model.PlayerData;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class LobbyController extends Controller {
    @FXML
    public Text firstPlayerReady;
    @FXML
    public Text secondPlayerReady;
    @FXML
    public Text secondPlayer;
    @FXML
    public Text firstPlayer;
    @FXML
    public ImageView firstPlayerDivinity;
    @FXML
    public ImageView secondPlayerDivinity;
    @FXML
    public ImageView yellowBox2;
    @FXML
    public ImageView blueBox2;
    @FXML
    public Text nick1;
    @FXML
    public ImageView yellowBox1;
    @FXML
    public ImageView blueBox1;
    @FXML
    public Text nick2;
    @FXML
    private ImageView readyButton;
    @FXML
    private ImageView confirmButton;
    @FXML
    private TextField nicknameBar;
    private boolean nickOK = false;
    private boolean isReady = false;
    private boolean firstNick = true;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        this.pageID = "Lobby";
    }

    @FXML
    private void readyButtonClicked(MouseEvent mouseEvent) {
        if(!isReady && nickOK) {
            Image image = new Image("/2DGraphics/GreenButton.png");
            readyButton.setImage(image);

            socket.sendMessage(new ReadyReceiver());
            isReady = true;
        }
    }

    @FXML
    private void confirmButtonClicked(MouseEvent mouseEvent) {
        if(!nickOK) {
            socket.sendMessage(new PlayerDataReceiver(nicknameBar.getText()));
            view.setName(nicknameBar.getText());
            firstNick = false;
        }
    }

    public void insertNick() {
        Image image = new Image("/2DGraphics/RedButton.png");
        confirmButton.setImage(image);

        if(!firstNick) {
            nicknameBar.setText("Nick already used, try again");
        }

        nickOK = false;
    }

    public void unlockReady() {
        Image image = new Image("/2DGraphics/GreenButton.png");
        confirmButton.setImage(image);

        nickOK = true;
    }

    public void updatePlayers(ArrayList<PlayerData> players) {
        boolean firstNickInput = true;
        for (PlayerData playerData : players){
            if (!playerData.getPlayerID().equals(nicknameBar.getText()) && firstNickInput) {
                updateSinglePlayer(playerData, blueBox1, nick1, firstPlayer, firstPlayerReady, firstPlayerDivinity, yellowBox1);
                firstNickInput = false;

            } else if (!playerData.getPlayerID().equals(nicknameBar.getText()) && !firstNickInput && !playerData.getPlayerID().equals(firstPlayer.getText()) ) {
                updateSinglePlayer(playerData, blueBox2, nick2, secondPlayer, secondPlayerReady, secondPlayerDivinity, yellowBox2);
            }
        }
    }

    private void updateSinglePlayer(PlayerData playerData, ImageView blueBox2, Text nick2, Text secondPlayer, Text secondPlayerReady, ImageView secondPlayerDivinity, ImageView yellowBox2) {
        Platform.runLater(() -> {
            blueBox2.setVisible(true);
            nick2.setVisible(true);
            secondPlayer.setText(playerData.getPlayerID());
            secondPlayer.setVisible(true);
            int playOrder = playerData.getPlayOrder() + 1;
            secondPlayerReady.setText("Play Order: " + playOrder);
            secondPlayerReady.setVisible(true);
        });

        if(playerData.getDivinity() == null){
            Platform.runLater(() -> {
                Image image = new Image("/2DGraphics/Random.png");
                secondPlayerDivinity.setImage(image);
                secondPlayerDivinity.setVisible(true);
                yellowBox2.setVisible(true);
            });
        } else {
            Platform.runLater(() -> {
                Image image = new Image("/2DGraphics/" + playerData.getDivinity() + ".png");
                secondPlayerDivinity.setImage(image);
                secondPlayerDivinity.setVisible(true);
                yellowBox2.setVisible(true);
            });
        }
    }
}
