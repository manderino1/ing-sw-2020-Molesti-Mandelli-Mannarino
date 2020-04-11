package it.polimi.ingsw.PSP18.networking.messages.toserver;

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
