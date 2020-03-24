package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Map;
import it.polimi.ingsw.model.PlayerData;
import it.polimi.ingsw.model.Worker;

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
