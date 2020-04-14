package it.polimi.ingsw.PSP18.networking.messages.toclient;

import it.polimi.ingsw.PSP18.server.controller.divinities.Divinity;

import java.util.ArrayList;

public class DivinityList extends ClientAbstractMessage{
    private ArrayList<String> divinities = new ArrayList<String>();

    /***
     * Constructor method of DivinityList class
     * @param divinities
     */
    public DivinityList(ArrayList<String> divinities) {
        this.type = ClientMessageType.DIVINITY_LIST;
        this.divinities = divinities;
    }

    /***
     * Return the divinities Arraylist
     * @return
     */
    public ArrayList<String> getDivinities() {
        return divinities;
    }
}
