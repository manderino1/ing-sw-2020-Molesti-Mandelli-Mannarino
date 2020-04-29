package it.polimi.ingsw.PSP18.server.backup;

import it.polimi.ingsw.PSP18.server.controller.divinities.Divinity;
import it.polimi.ingsw.PSP18.server.model.PlayerData;
import it.polimi.ingsw.PSP18.server.model.Worker;

public class PlayerManagerBackup {
    private Worker[] workers = new Worker[2];
    private PlayerDataBackup playerData;

    public PlayerManagerBackup(Worker worker1, Worker worker2, PlayerData playerData, Divinity divinity) {
        this.workers[0] = worker1;
        this.workers[1] = worker2;
        this.playerData = new PlayerDataBackup(playerData);
    }

    public Worker[] getWorkers() {
        return workers;
    }

    public PlayerDataBackup getPlayerData() {
        return playerData;
    }
}
