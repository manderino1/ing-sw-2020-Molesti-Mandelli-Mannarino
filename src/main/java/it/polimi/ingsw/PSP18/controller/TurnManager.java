package it.polimi.ingsw.PSP18.controller;

import java.util.ArrayList;

public class TurnManager {
    protected ArrayList<PlayerManager> player = new ArrayList<PlayerManager>();
    protected Integer indexCurrentPlayer;

    public TurnManager(ArrayList<PlayerManager> player) {
        this.player = player;
        setupTurn();
    }

    /***
     * Called in the constructor, initializes the turn order
     */
    private void setupTurn() {
        indexCurrentPlayer = 0;
    }

    public void ManageTurn(){

        //to do: ricezione segnale dalla view

        this.player.get(indexCurrentPlayer).manageTurn(false);
    }
}
