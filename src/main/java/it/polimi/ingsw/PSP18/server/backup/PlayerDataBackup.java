package it.polimi.ingsw.PSP18.server.backup;

import it.polimi.ingsw.PSP18.server.model.Color;
import it.polimi.ingsw.PSP18.server.model.Move;
import it.polimi.ingsw.PSP18.server.model.PlayerData;

public class PlayerDataBackup {
    private String playerID;
    private Color playerColor;
    private Integer playOrder;
    private Move lastMove;
    private String divinity;
    private Boolean isReady = false;
    private Boolean hasLost = false;

    public PlayerDataBackup(PlayerData playerData) {
        this.playerID = playerData.getPlayerID();
        this.playerColor = playerData.getPlayerColor();
        this.playOrder = playerData.getPlayOrder();
        this.lastMove = playerData.getLastMove();
        this.divinity = playerData.getDivinity();
        this.isReady = playerData.getReady();
        this.hasLost = playerData.getLost();
    }

    public String getPlayerID() {
        return playerID;
    }

    public Color getPlayerColor() {
        return playerColor;
    }

    public Integer getPlayOrder() {
        return playOrder;
    }

    public Move getLastMove() {
        return lastMove;
    }

    public String getDivinity() {
        return divinity;
    }

    public Boolean getReady() {
        return isReady;
    }

    public Boolean getHasLost() {
        return hasLost;
    }
}
