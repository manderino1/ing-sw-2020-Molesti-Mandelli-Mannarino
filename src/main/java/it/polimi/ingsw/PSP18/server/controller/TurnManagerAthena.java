package it.polimi.ingsw.PSP18.server.controller;
/***
 * class that articulates the players' turns when Athena is in the game
 */
public class TurnManagerAthena extends TurnManager {
    public boolean bool = false;

    /***
     * constructor of the class, start managing the turn of the players in the current match
     * @param matchSocket reference to the class that manages sockets and players
     * @param backupManager  reference to the class that manages backup, to backup at the end of turn
     */
    public TurnManagerAthena(MatchSocket matchSocket, BackupManager backupManager) {
        super(matchSocket, backupManager);
    }

    /***
     * constructor of the class, start managing the turn of the players in the current match
     * @param matchSocket reference to the class that manages sockets and players
     * @param backupManager  reference to the class that manages backup, to backup at the end of turn
     * @param indexCurrentPlayer the index of the player that has to play, for restoring
     */
    public TurnManagerAthena(MatchSocket matchSocket, BackupManager backupManager, int indexCurrentPlayer) {
        super(matchSocket, backupManager, indexCurrentPlayer);
    }

    /***
     * When the following turn starts call the manage turn function
     * bool is true if athena moved up last turn
     */
    @Override
    public void manageTurn(){
        for(PlayerManager player : matchSocket.getPlayerManagers()) {
            if(player.getDivinityName().equals("Athena")) { // Update Athena movement
                if(player.getPlayerData().getLastMove() != null) {
                    bool = (player.getPlayerData().getLastMove().getLevel() == 1);
                }
            }
        }

        if(matchSocket.getPlayerManagers().get(indexCurrentPlayer).getDivinityName().equals("Athena")) { // Athena can go up
            matchSocket.getPlayerManagers().get(indexCurrentPlayer).manageTurn(false);
        } else { // Call the manager with the correct raiseForbidden parameter
            matchSocket.getPlayerManagers().get(indexCurrentPlayer).manageTurn(bool);
        }
    }
}
