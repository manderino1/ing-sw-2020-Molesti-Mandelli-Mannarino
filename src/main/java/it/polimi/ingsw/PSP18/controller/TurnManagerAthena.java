package it.polimi.ingsw.PSP18.controller;

import java.util.ArrayList;

public class TurnManagerAthena extends TurnManager {
    public boolean bool;
    public TurnManagerAthena(ArrayList<PlayerManager> player) {
        super(player);
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
