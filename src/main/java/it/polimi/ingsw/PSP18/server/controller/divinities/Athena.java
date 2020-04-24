package it.polimi.ingsw.PSP18.server.controller.divinities;

import it.polimi.ingsw.PSP18.server.controller.DirectionManagement;
import it.polimi.ingsw.PSP18.server.controller.PlayerManager;
import it.polimi.ingsw.PSP18.server.model.Direction;

/***
 * Constructor of the class, initialize divinity parameters name and playerManager
 */
public class Athena extends Divinity {
    public Athena(String name, PlayerManager playerManager) {
        super(name, playerManager);
    }
}

