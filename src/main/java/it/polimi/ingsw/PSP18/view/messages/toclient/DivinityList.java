package it.polimi.ingsw.PSP18.view.messages.toclient;

import it.polimi.ingsw.PSP18.controller.divinities.Divinity;

import java.util.ArrayList;

public class DivinityList extends ClientAbstractMessage{
    private ArrayList<Divinity> divinities = new ArrayList<Divinity>();

    /***
     * Constructor method of DivinityList class
     * @param divinities
     */
    public DivinityList(ArrayList<Divinity> divinities) {
        this.type = ClientMessageType.DIVINITY_LIST;
        this.divinities = divinities;
    }

    /***
     * Return the divinities Arraylist
     * @return
     */
    public ArrayList<Divinity> getDivinities() {
        return divinities;
    }
}
