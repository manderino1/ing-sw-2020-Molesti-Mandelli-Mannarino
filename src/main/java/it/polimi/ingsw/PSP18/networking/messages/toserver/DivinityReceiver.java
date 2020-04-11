package it.polimi.ingsw.PSP18.view.messages.toserver;

import it.polimi.ingsw.PSP18.model.Direction;

public class DivinityReceiver extends ServerAbstractMessage {

    private String divinity;

    /***
     * constructor of DivinityReceiver
     * @param divinity the divinity chosen by the player
     */
    public DivinityReceiver(String divinity) {
        this.type = ServerMessageType.DIVINITY_RECEIVER;
        this.divinity=divinity;
    }

    /***
     * returns divinity
     * @return divinity
     */
    public String getDivinity(){return divinity;}
}
