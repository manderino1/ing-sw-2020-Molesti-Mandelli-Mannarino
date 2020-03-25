package it.polimi.ingsw.PSP18.controller;

import it.polimi.ingsw.PSP18.controller.divinities.Divinity;
import it.polimi.ingsw.PSP18.model.PlayerData;
import it.polimi.ingsw.PSP18.model.Map;
import it.polimi.ingsw.PSP18.model.Worker;

public class PlayerManager {
    private Map map;
    private Worker[] worker = new Worker[2];
    private PlayerData playerData;
    private Divinity divinity;

    public PlayerManager() {
    }

    public Worker[] getWorker() {
        return worker;
    }

    public void setWorker(Worker[] worker) {
        this.worker = worker;
    }
}
