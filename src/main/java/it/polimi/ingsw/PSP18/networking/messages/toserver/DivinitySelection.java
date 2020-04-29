package it.polimi.ingsw.PSP18.networking.messages.toserver;

import java.util.ArrayList;

/***
 * This message is sent from the last player
 * It contains the list of selected divinities
 */
public class DivinitySelection extends ServerAbstractMessage {
    private ArrayList<String> divinities;

    /***
     * Constructor of DivinityReceiver, message used to set the divinity name using a string in input
     * @param divinities the divinity chosen by the player
     */
    public DivinitySelection(ArrayList<String> divinities) {
        this.type = ServerMessageType.DIVINITY_SELECTION;
        this.divinities=divinities;
    }

    /***
     * Get the list of divinities
     * @return the list of divinities
     */
    public ArrayList<String> getDivinities() {
        return divinities;
    }
}
