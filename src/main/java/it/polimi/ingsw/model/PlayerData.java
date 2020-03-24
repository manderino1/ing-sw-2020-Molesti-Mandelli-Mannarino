package it.polimi.ingsw.model;

public class PlayerData {
    private String playerID;
    private Color playerColor;
    private Integer playOrder;
    private Move lastMove;

    public PlayerData(String playerID, Color playerColor, Integer playOrder) {
        this.playerID = playerID;
        this.playerColor = playerColor;
        this.playOrder = playOrder;
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
}
