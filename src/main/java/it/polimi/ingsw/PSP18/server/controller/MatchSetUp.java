package it.polimi.ingsw.PSP18.server.controller;

import it.polimi.ingsw.PSP18.networking.SocketThread;
import it.polimi.ingsw.PSP18.networking.messages.toclient.DivinityList;
import it.polimi.ingsw.PSP18.networking.messages.toclient.DivinityPick;
import it.polimi.ingsw.PSP18.networking.messages.toclient.PlaceReady;
import it.polimi.ingsw.PSP18.server.model.MatchStatus;
import it.polimi.ingsw.PSP18.server.view.MapObserver;

import java.util.ArrayList;
import java.util.Arrays;

public class MatchSetUp {
    private int playerN;
    private Match match;
    private ArrayList<String> divinitySelection = new ArrayList<>();
    private Integer divinitySelectionIndex = 0;
    private ArrayList<String> divinities;

    public MatchSetUp(Match match){
        String[] divArray = {"Apollo", "Artemis", "Athena", "Atlas", "Demeter", "Hephaestus", "Minotaur", "Pan", "Prometheus"};
        divinities = new ArrayList<>(Arrays.asList(divArray));
        this.match = match;
    }

    public MatchSetUp(Match match, int playerN){
        this(match);
        this.playerN = playerN;
    }

    /***
     * Wait for all the players to be ready and then start the divinity selection phase
     * @param socket the reference to the socket
     */
    public void readyManagement(SocketThread socket) {
        match.getMatchSocket().getSocketPlayerMap().get(socket).getPlayerData().setReady();
        for(PlayerManager player : match.getMatchSocket().getPlayerManagers()) {
            if(!player.getPlayerData().getReady() || match.getMatchSocket().getPlayerManagers().size() != playerN || match.getMatchSocket().getPlayerManagers().size() <= 1) {
                return;
            }
        }
        // Check if there is a match saved with these players
        boolean hasBackup = match.getBackupManager().backupCheck();
        // If i manage to arrive here all the players are ready, i can start the divinity selection phase
        if(!hasBackup) {
            match.setMatchStatus(MatchStatus.DIVINITIES_SELECTION);
            match.getMatchSocket().getPlayerSocketMap().get(match.getMatchSocket().getPlayerManagers().get(match.getMatchSocket().getPlayerManagers().size()-1)).sendMessage(new DivinityPick(divinities, match.getMatchSocket().getPlayerManagers().size()));
        } else {
            match.getBackupManager().backupRestore();
        }
    }

    /***
     * Create the divinity of the player that decided which divinity to use
     * If there are no more players that have to choose the divinity start the match
     * If there are other players, ask the next to choose the divinity
     * @param socket the socket reference, used to get the correct player
     * @param divinity string that represent the divinity to be created
     */
    public void divinityCreation(SocketThread socket, String divinity) {
        // Check that the divinity selection is correct
        if(!divinitySelection.contains(divinity)) {
            match.getMatchSocket().getPlayerSocketMap().get(match.getMatchSocket().getPlayerManagers().get(divinitySelectionIndex)).sendMessage(new DivinityList(divinitySelection));
        }
        match.getMatchSocket().getSocketPlayerMap().get(socket).divinityCreation(divinity); // use to change divinity
        if(divinitySelectionIndex == match.getMatchSocket().getPlayerManagers().size()) {
            // Set observers
            for(SocketThread sock : match.getMatchSocket().getSockets()) {
                match.getMatchRun().getGameMap().attach(new MapObserver(sock));
            }

            match.setMatchStatus(MatchStatus.WORKER_SETUP);
            match.getMatchSocket().getPlayerSocketMap().get(match.getMatchSocket().getPlayerManagers().get(0)).sendMessage(new PlaceReady());
        } else {
            divinitySelection.remove(divinity);
            match.getMatchSocket().getPlayerSocketMap().get(match.getMatchSocket().getPlayerManagers().get(divinitySelectionIndex)).sendMessage(new DivinityList(divinitySelection));
            divinitySelectionIndex++;
        }
    }

    public void divinitySelection(ArrayList<String> divinities) {
        for(String divinity : divinities) {
            if(!this.divinities.contains(divinity)) {
                match.getMatchSocket().getPlayerSocketMap().get(match.getMatchSocket().getPlayerManagers().get(match.getMatchSocket().getPlayerManagers().size()-1)).sendMessage(new DivinityPick(divinities, match.getMatchSocket().getPlayerManagers().size()));
            }
        }
        divinitySelection = divinities;
        match.getMatchSocket().getPlayerSocketMap().get(match.getMatchSocket().getPlayerManagers().get(divinitySelectionIndex)).sendMessage(new DivinityList(divinities));
        divinitySelectionIndex++;
    }
}
