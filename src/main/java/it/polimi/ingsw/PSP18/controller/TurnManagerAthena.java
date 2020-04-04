package it.polimi.ingsw.PSP18.controller;

import it.polimi.ingsw.PSP18.model.Match;

import java.util.ArrayList;

public class TurnManagerAthena extends TurnManager {
    public boolean bool;
    public TurnManagerAthena(Match match) {
        super(match);
    }
    //public void getLastMove(player) {
    //}
    @Override
    public void ManageTurn(){

        //to do: ricezione segnale dalla view


        if (this.player.get(0).getPlayerData().getLastMove().getLevel() == 1) bool = true;
        else bool = false;
        this.player.get(super.indexCurrentPlayer).manageTurn(bool);
    }
}
