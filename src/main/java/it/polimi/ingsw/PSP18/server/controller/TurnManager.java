package it.polimi.ingsw.PSP18.server.controller;

import it.polimi.ingsw.PSP18.server.backup.MatchBackup;

/***
 * class that articulates the players' turns
 */
public class TurnManager {
    protected MatchSocket matchSocket;
    protected BackupManager backupManager;
    protected Integer indexCurrentPlayer;

    /***
     * constructor of the class, start managing the turn of the players in the current match
     * @param matchSocket reference to the class that manages sockets and players
     * @param backupManager  reference to the class that manages backup, to backup at the end of turn
     */
    public TurnManager(MatchSocket matchSocket, BackupManager backupManager) {
        this.matchSocket = matchSocket;
        this.backupManager = backupManager;
        setupTurn();
    }

    /***
     * constructor of the class, start managing the turn of the players in the current match
     * @param matchSocket reference to the class that manages sockets and players
     * @param backupManager  reference to the class that manages backup, to backup at the end of turn
     * @param indexCurrentPlayer the index of the player to start the match from
     */
    public TurnManager(MatchSocket matchSocket, BackupManager backupManager, int indexCurrentPlayer) {
        this.matchSocket = matchSocket;
        this.backupManager = backupManager;
        setupTurn(indexCurrentPlayer);
    }

    /***
     * Called in the constructor, initializes the turn order
     */
    private void setupTurn() {
        indexCurrentPlayer = 0;
        matchSocket.setCurrentPlayer(matchSocket.getPlayerManagers().get(indexCurrentPlayer));
        manageTurn(); // Start the match
    }

    /***
     * Called in the constructor, initializes the turn order
     * Can accept the actual current player to restore backup
     * @param indexCurrentPlayer the player that has to play index
     */
    private void setupTurn(int indexCurrentPlayer) {
        this.indexCurrentPlayer = indexCurrentPlayer;
        matchSocket.setCurrentPlayer(matchSocket.getPlayerManagers().get(indexCurrentPlayer));
        manageTurn(); // Start the match
    }

    /***
     * Called form the parser, when a signal is received to end turn switch the turn to the following player
     */
    public void passTurn() {
        if(matchSocket.getPlayerManagers().contains(matchSocket.getCurrentPlayer())) {
            indexCurrentPlayer = indexCurrentPlayer + 1;
        }
        if(indexCurrentPlayer == matchSocket.getPlayerManagers().size()) {
            indexCurrentPlayer = 0; // If I reached the end of the array go back
        }
        matchSocket.setCurrentPlayer(matchSocket.getPlayerManagers().get(indexCurrentPlayer));
        backupManager.updateFile();
        manageTurn();
    }

    /***
     * When the following turn starts call the manage turn function
     * from the correct player manager
     */
    public void manageTurn(){
        this.matchSocket.getPlayerManagers().get(indexCurrentPlayer).manageTurn(false);
    }

    /***
     * Get the index of the current player
     * @return the index of the current player
     */
    public Integer getIndexCurrentPlayer() {
        return indexCurrentPlayer;
    }
}
