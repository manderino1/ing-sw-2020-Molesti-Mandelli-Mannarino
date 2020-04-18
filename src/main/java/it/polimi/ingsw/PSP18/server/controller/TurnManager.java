package it.polimi.ingsw.PSP18.server.controller;

public class TurnManager {
    protected Match match;
    protected Integer indexCurrentPlayer;

    public TurnManager(Match match) {
        this.match = match;
        setupTurn();
    }

    /***
     * Called in the constructor, initializes the turn order
     */
    private void setupTurn() {
        indexCurrentPlayer = 0;
        match.setCurrentPlayer(match.getPlayerManagers().get(indexCurrentPlayer));
        manageTurn(); // Start the match
    }

    /***
     * Called form the parser, when a signal is received to end turn switch the turn to the following player
     */
    public void passTurn() {
        if(match.getPlayerManagers().contains(match.getCurrentPlayer())) {
            indexCurrentPlayer = indexCurrentPlayer + 1;
        }
        if(indexCurrentPlayer == match.getPlayerManagers().size()) {
            indexCurrentPlayer = 0;
        }
        match.setCurrentPlayer(match.getPlayerManagers().get(indexCurrentPlayer));
        manageTurn();
    }

    /***
     * When the following turn starts call the manage turn function
     * from the correct player manager
     */
    public void manageTurn(){
        this.match.getPlayerManagers().get(indexCurrentPlayer).manageTurn(false);
    }
}
