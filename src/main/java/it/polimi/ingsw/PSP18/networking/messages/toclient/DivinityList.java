package it.polimi.ingsw.PSP18.networking.messages.toclient;

import it.polimi.ingsw.PSP18.server.controller.divinities.Divinity;

import java.util.ArrayList;

/***
 * This message class is used to send the list of possible divinity to choose from
 */

public class DivinityList extends ClientAbstractMessage{
    private ArrayList<String> divinities = new ArrayList<String>();

    /***
     * Constructor method of DivinityList class
     * @param divinities the list of possible divinities to be chosen
     */
    public DivinityList(ArrayList<String> divinities) {
        this.type = ClientMessageType.DIVINITY_LIST;
        this.divinities = divinities;
    }

    /***
     * Return the divinities ArrayList
     * @return the list of possible selectable divinities
     */
    public ArrayList<String> getDivinities() {
        return divinities;
    }
}
