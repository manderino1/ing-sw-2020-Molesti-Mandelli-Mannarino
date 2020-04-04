package it.polimi.ingsw.PSP18.controller;

import it.polimi.ingsw.PSP18.model.Match;

import java.util.ArrayList;

public class TurnManager {
    protected ArrayList<PlayerManager> player = new ArrayList<PlayerManager>();
    protected Integer indexCurrentPlayer;

    public TurnManager(Match match) {
        this.player = player;
        setupTurn();
    }

    /***
     * Called in the constructor, initializes the turn order
     */
    private void setupTurn() {
        indexCurrentPlayer = 0;
    }

    /***
     * Called form the parser, when a signal is received to end turn switch the turn to the following player
     */
    public void passTurn() {
        indexCurrentPlayer = indexCurrentPlayer + 1;
        ManageTurn();
    }

    public void ManageTurn(){

        //to do: ricezione segnale dalla view

        this.player.get(indexCurrentPlayer).manageTurn(false);
    }
}
