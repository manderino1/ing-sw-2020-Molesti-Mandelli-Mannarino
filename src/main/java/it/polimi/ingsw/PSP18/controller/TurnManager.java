package it.polimi.ingsw.PSP18.controller;

import java.util.ArrayList;

public class TurnManager {
    private ArrayList<PlayerManager> player = new ArrayList<PlayerManager>();

    public TurnManager(ArrayList<PlayerManager> player) {
        this.player = player;
        setupTurn();
    }

    /***
     * Called in the constructor, initializes the turn order
     */
    private void setupTurn() {

    }
}
