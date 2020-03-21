package it.polimi.ingsw.model;

public class PlayerData {
    private String playerID;
    private Color playerColor;
    private Integer playOrder;
    private Divinity playerDivinity;
    private Move lastMove;

    public PlayerData(String playerID, Color playerColor, Integer playOrder, Divinity playerDivinity) {
        this.playerID = playerID;
        this.playerColor = playerColor;
        this.playOrder = playOrder;
        this.playerDivinity = playerDivinity;
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

    public Divinity getPlayerDivinity() {
        return playerDivinity;
    }

    public Move getLastMove() {
        return lastMove;
    }
}
