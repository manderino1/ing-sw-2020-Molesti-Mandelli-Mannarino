package it.polimi.ingsw.PSP18.server.backup;

import it.polimi.ingsw.PSP18.server.controller.divinities.Divinity;
import it.polimi.ingsw.PSP18.server.model.PlayerData;
import it.polimi.ingsw.PSP18.server.model.Worker;

/***
 * Class that saves the player managers information
 */
public class PlayerManagerBackup {
    private Worker[] workers = new Worker[2];
    private PlayerDataBackup playerData;

    /***
     * Saves the player manager information
     * @param worker1 first worker of the player
     * @param worker2 second worker of the player
     * @param playerData player's information
     * @param divinity divinity of the player
     */
    public PlayerManagerBackup(Worker worker1, Worker worker2, PlayerData playerData, Divinity divinity) {
        this.workers[0] = worker1;
        this.workers[1] = worker2;
        this.playerData = new PlayerDataBackup(playerData);
    }

    /***
     * returns the workers of the player
     * @return the workers of the player
     */
    public Worker[] getWorkers() {
        return workers;
    }

    /***
     * returns the player's information
     * @return the player's information
     */
    public PlayerDataBackup getPlayerData() {
        return playerData;
    }
}
