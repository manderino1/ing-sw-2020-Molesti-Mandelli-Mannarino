package it.polimi.ingsw.PSP18.server.controller;

import it.polimi.ingsw.PSP18.networking.SocketThread;
import it.polimi.ingsw.PSP18.networking.messages.toclient.DivinityList;
import it.polimi.ingsw.PSP18.networking.messages.toclient.DivinityPick;
import it.polimi.ingsw.PSP18.networking.messages.toclient.PlaceReady;
import it.polimi.ingsw.PSP18.server.model.MatchStatus;
import it.polimi.ingsw.PSP18.server.view.MapObserver;

import java.util.ArrayList;

public class MatchSetUp {

    private Match match;
    private ArrayList<String> divinitySelection = new ArrayList<>();
    private Integer divinitySelectionIndex = 0;

    public MatchSetUp(Match match){
        this.match = match;
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
            match.getMatchSocket().getPlayerSocketMap.get(match.getMatchSocket().getPlayerManagers.get(divinitySelectionIndex)).sendMessage(new DivinityList(divinitySelection));
        }
        match.getMatchSocket().getSocketPlayerMap.get(socket).divinityCreation(divinity); // use to change divinity
        if(divinitySelectionIndex == match.getMatchSocket().getPlayerManagers.size()) {
            // Set observers
            for(SocketThread sock : sockets) {
                gameMap.attach(new MapObserver(sock));
            }

            match.getMatchStatus = MatchStatus.WORKER_SETUP;
            playerSocketMap.get(playerManagers.get(workerPlacementIndex)).sendMessage(new PlaceReady());
            workerPlacementIndex++;
        } else {
            divinitySelection.remove(divinity);
            playerSocketMap.get(playerManagers.get(divinitySelectionIndex)).sendMessage(new DivinityList(divinitySelection));
            divinitySelectionIndex++;
        }
    }

    public void divinitySelection(ArrayList<String> divinities) {
        for(String divinity : divinities) {
            if(!this.divinities.contains(divinity)) {
                playerSocketMap.get(playerManagers.get(playerManagers.size()-1)).sendMessage(new DivinityPick(divinities, playerManagers.size()));
            }
        }
        divinitySelection = divinities;
        playerSocketMap.get(playerManagers.get(divinitySelectionIndex)).sendMessage(new DivinityList(divinities));
        divinitySelectionIndex++;
    }
    public ArrayList<String> getDivinitySelection(){
        return divinitySelection;
    }
}
