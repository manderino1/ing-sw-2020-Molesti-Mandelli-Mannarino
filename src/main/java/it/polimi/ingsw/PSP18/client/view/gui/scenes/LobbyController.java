package it.polimi.ingsw.PSP18.client.view.gui.scenes;

import it.polimi.ingsw.PSP18.networking.messages.toserver.PlayerDataReceiver;
import it.polimi.ingsw.PSP18.networking.messages.toserver.ReadyReceiver;
import it.polimi.ingsw.PSP18.server.model.PlayerData;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/***
 * Controller for LobbyController.fxml
 * Manages the lobby, nickname insert and ready management
 */
public class LobbyController extends Controller {
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
    private ImageView blueBox2;
    @FXML
    private Text nick1;
    @FXML
    private ImageView yellowBox1;
    @FXML
    private ImageView blueBox1;
    @FXML
    private Text nick2;
    @FXML
    private Text firstReadyStatus;
    @FXML
    private Text secondReadyStatus;
    @FXML
    private ImageView readyButton;
    @FXML
    private ImageView confirmButton;
    @FXML
    private TextField nicknameBar;
    private boolean nickOK = false;
    private boolean isReady = false;
    private boolean firstNick = true;

    /***
     * Init function that sets up the page id
     * @param location unused
     * @param resources unused
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        this.pageID = "Lobby";
    }

    /***
     * Ready button callback, tell the server that the user is ready
     */
    @FXML
    private void readyButtonClicked() {
        if(!isReady && nickOK) {
            Image image = new Image("/2DGraphics/GreenButton.png");
            readyButton.setImage(image);

            socket.sendMessage(new ReadyReceiver());
            isReady = true;
        }
    }

    /***
     * Confirm button callback, send the server the selected nick
     */
    @FXML
    private void confirmButtonClicked() {
        if(!nickOK) {
            socket.sendMessage(new PlayerDataReceiver(nicknameBar.getText()));
            view.setName(nicknameBar.getText());
            firstNick = false;
        }
    }

    /***
     * Called on insert nick request from the server
     * If called more than once is because the nickname is already used
     */
    public void insertNick() {
        Image image = new Image("/2DGraphics/RedButton.png");
        confirmButton.setImage(image);

        if(!firstNick) {
            nicknameBar.setText("Nick already used, try again");
        }

        nickOK = false;
    }

    /***
     * Unlock the ready button after correct nick insertion
     */
    public void unlockReady() {
        Image image = new Image("/2DGraphics/GreenButton.png");
        confirmButton.setImage(image);

        nickOK = true;
    }

    /***
     * Update other players data when other users connect and select their nick
     * @param players array of connected players
     */
    public void updatePlayers(ArrayList<PlayerData> players) {
        boolean firstNickInput = true;
        for (PlayerData playerData : players){
            if (!playerData.getPlayerID().equals(nicknameBar.getText()) && firstNickInput) {
                updateSinglePlayer(playerData, blueBox1, nick1, firstPlayer, firstPlayerReady, firstPlayerDivinity, yellowBox1, firstReadyStatus);
                firstNickInput = false;

            } else if (!playerData.getPlayerID().equals(nicknameBar.getText()) && !firstNickInput && !playerData.getPlayerID().equals(firstPlayer.getText()) ) {
                updateSinglePlayer(playerData, blueBox2, nick2, secondPlayer, secondPlayerReady, secondPlayerDivinity, yellowBox2, secondReadyStatus);
            }
        }
    }

    /***
     * Called from the updatePlayers function to update a single player
     * @param playerData data of the player to insert
     * @param blueBox2 blue box surrounding image
     * @param nick2 nick of the player
     * @param secondPlayer text under player
     * @param secondPlayerReady text under player, tells play order
     * @param secondPlayerDivinity text under player, shows divinity name if selected
     * @param yellowBox2 yellow box surrounding image
     * @param secondReadyStatus text under player, present if ready
     */
    private void updateSinglePlayer(PlayerData playerData, ImageView blueBox2, Text nick2, Text secondPlayer, Text secondPlayerReady, ImageView secondPlayerDivinity, ImageView yellowBox2, Text secondReadyStatus) {
        Platform.runLater(() -> {
            blueBox2.setVisible(true);
            nick2.setVisible(true);
            secondPlayer.setText(playerData.getPlayerID());
            secondPlayer.setVisible(true);
            int playOrder = playerData.getPlayOrder() + 1;
            secondPlayerReady.setText("Play Order: " + playOrder);
            secondPlayerReady.setVisible(true);
        });

        Platform.runLater(() -> {
            if(playerData.getReady()){
                secondReadyStatus.setText("Ready!");
            } else {
                secondReadyStatus.setText("Not ready!");
            }
            secondReadyStatus.setVisible(true);
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
