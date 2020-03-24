package it.polimi.ingsw.controller;

import java.util.ArrayList;

public class TurnManager {
    private ArrayList<PlayerManager> player = new ArrayList<PlayerManager>();

    public TurnManager(ArrayList<PlayerManager> player) {
        this.player = player;
    }
}
