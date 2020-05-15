package it.polimi.ingsw.PSP18.networking.messages.toserver;

/***
 * This is the class that represents a message used to set the divinity name
 */
public class DivinityReceiver extends ServerAbstractMessage {

    private String divinity;

    /***
     * Constructor of DivinityReceiver, message used to set the divinity name using a string in input
     * @param divinity the divinity chosen by the player
     */
    public DivinityReceiver(String divinity) {
        this.type = ServerMessageType.DIVINITY_RECEIVER;
        this.divinity=divinity;
    }

    /***
     * Returns divinity
     * @return divinity
     */
    public String getDivinity(){return divinity;}
}
