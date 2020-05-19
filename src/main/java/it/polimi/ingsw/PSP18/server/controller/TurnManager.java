package it.polimi.ingsw.PSP18.server.controller;

/***
 * class that articulates the players' turns
 */
public class TurnManager {
    protected Match match;
    protected Integer indexCurrentPlayer;

    /***
     * constructor of the class, start managing the turn of the players in the current match
     * @param match the object that deals with the current match
     */
    public TurnManager(Match match) {
        this.match = match;
        setupTurn();
    }

    /***
     * constructor of the class, start managing the turn of the players in the current match
     * @param match the object that deals with the current match
     */
    public TurnManager(Match match, int indexCurrentPlayer) {
        this.match = match;
        setupTurn(indexCurrentPlayer);
    }

    /***
     * Called in the constructor, initializes the turn order
     */
    private void setupTurn() {
        indexCurrentPlayer = 0;
        match.setCurrentPlayer(match.getMatchSocket().getPlayerManagers().get(indexCurrentPlayer));
        manageTurn(); // Start the match
    }

    /***
     * Called in the constructor, initializes the turn order
     * Can accept the actual current player to restore backup
     * @param indexCurrentPlayer the player that has to play index
     */
    private void setupTurn(int indexCurrentPlayer) {
        this.indexCurrentPlayer = indexCurrentPlayer;
        match.setCurrentPlayer(match.getMatchSocket().getPlayerManagers().get(indexCurrentPlayer));
        manageTurn(); // Start the match
    }

    /***
     * Called form the parser, when a signal is received to end turn switch the turn to the following player
     */
    public void passTurn() {
        if(match.getMatchSocket().getPlayerManagers().contains(match.getCurrentPlayer())) {
            indexCurrentPlayer = indexCurrentPlayer + 1;
        }
        if(indexCurrentPlayer == match.getMatchSocket().getPlayerManagers().size()) {
            indexCurrentPlayer = 0; // If I reached the end of the array go back
        }
        match.setCurrentPlayer(match.getMatchSocket().getPlayerManagers().get(indexCurrentPlayer));
        match.updateFile();
        manageTurn();
    }

    /***
     * When the following turn starts call the manage turn function
     * from the correct player manager
     */
    public void manageTurn(){
        this.match.getMatchSocket().getPlayerManagers().get(indexCurrentPlayer).manageTurn(false);
    }

    /***
     * Get the index of the current player
     * @return the index of the current player
     */
    public Integer getIndexCurrentPlayer() {
        return indexCurrentPlayer;
    }
}
