package it.polimi.ingsw.PSP18.networking.messages.toclient;

import it.polimi.ingsw.PSP18.networking.messages.toserver.ServerMessageType;

import java.util.ArrayList;

/***
 * This message is sent to the last player, he'll chose nOfPlayers divinities to pick from
 */
public class DivinityPick extends ClientAbstractMessage {
    ArrayList<String> divinities;
    int nOfPlayers;

    /***
     * Constructor of DivinityReceiver, message used to set the divinity name using a string in input
     * @param divinities the divinity chosen by the player
     */
    public DivinityPick(ArrayList<String> divinities, int nOfPlayers) {
        this.type = ClientMessageType.DIVINITY_PICK;
        this.divinities=divinities;
        this.nOfPlayers = nOfPlayers;
    }

    /***
     * Get the divinities list to pick from (all the divinities in the game)
     * @return the list of divinities
     */
    public ArrayList<String> getDivinities() {
        return divinities;
    }

    /***
     * Get the number of players in the game
     * @return the number of players into the game
     */
    public int getnOfPlayers() {
        return nOfPlayers;
    }
}
