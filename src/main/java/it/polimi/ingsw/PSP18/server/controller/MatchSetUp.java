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
    private ArrayList<String> divinitySelection = new ArrayList<>();
    private Integer divinitySelectionIndex = 0;
    private ArrayList<String> divinities;
    private MatchSocket matchSocket;
    private MatchRun matchRun;

    public MatchSetUp(MatchSocket matchSocket, int playerN){
        String[] divArray = {"Apollo", "Artemis", "Athena", "Atlas", "Demeter", "Hephaestus", "Minotaur", "Pan", "Prometheus"};
        divinities = new ArrayList<>(Arrays.asList(divArray));
        this.matchSocket = matchSocket;
        this.playerN = playerN;
    }

    /***
     * Wait for all the players to be ready and then start the divinity selection phase
     * @param socket the reference to the socket
     */
    public void readyManagement(SocketThread socket) {
        matchSocket.getSocketPlayerMap().get(socket).getPlayerData().setReady();
        for(PlayerManager player : matchSocket.getPlayerManagers()) {
            if(!player.getPlayerData().getReady() || matchSocket.getPlayerManagers().size() != playerN || matchSocket.getPlayerManagers().size() <= 1) {
                return;
            }
        }
        // Check if there is a match saved with these players
        matchRun = new MatchRun(matchSocket);
        // Set match run references to sockets
        for(SocketThread sock : matchSocket.getSockets()) {
            sock.setMatchRun(matchRun);
        }
        // Set match run references to playerManagers
        for(PlayerManager player : matchSocket.getPlayerManagers()) {
            player.setMatchRun(matchRun);
        }
        boolean hasBackup = BackupManager.backupCheck(matchSocket.getPlayerManagers());
        // If i manage to arrive here all the players are ready, i can start the divinity selection phase
        if(!hasBackup) {
            matchSocket.setMatchStatus(MatchStatus.DIVINITIES_SELECTION);
            matchSocket.getPlayerSocketMap().get(matchSocket.getPlayerManagers().get(matchSocket.getPlayerManagers().size()-1)).sendMessage(new DivinityPick(divinities, matchSocket.getPlayerManagers().size()));
        } else {
            BackupManager backupManager = new BackupManager(matchSocket, matchRun);
            backupManager.backupRestore();
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
            matchSocket.getPlayerSocketMap().get(matchSocket.getPlayerManagers().get(divinitySelectionIndex)).sendMessage(new DivinityList(divinitySelection));
        }
        matchSocket.getSocketPlayerMap().get(socket).divinityCreation(divinity, matchSocket); // use to change divinity
        if(divinitySelectionIndex == matchSocket.getPlayerManagers().size()) {
            // Set observers
            for(SocketThread sock : matchSocket.getSockets()) {
                matchRun.getGameMap().attach(new MapObserver(sock));
            }

            matchSocket.setMatchStatus(MatchStatus.WORKER_SETUP);
            matchSocket.getPlayerSocketMap().get(matchSocket.getPlayerManagers().get(0)).sendMessage(new PlaceReady());
        } else {
            divinitySelection.remove(divinity);
            matchSocket.getPlayerSocketMap().get(matchSocket.getPlayerManagers().get(divinitySelectionIndex)).sendMessage(new DivinityList(divinitySelection));
            divinitySelectionIndex++;
        }
    }

    public void divinitySelection(ArrayList<String> divinities) {
        for(String divinity : divinities) {
            if(!this.divinities.contains(divinity)) {
                matchSocket.getPlayerSocketMap().get(matchSocket.getPlayerManagers().get(matchSocket.getPlayerManagers().size()-1)).sendMessage(new DivinityPick(divinities, matchSocket.getPlayerManagers().size()));
            }
        }
        divinitySelection = divinities;
        matchSocket.getPlayerSocketMap().get(matchSocket.getPlayerManagers().get(divinitySelectionIndex)).sendMessage(new DivinityList(divinities));
        divinitySelectionIndex++;
    }
}
