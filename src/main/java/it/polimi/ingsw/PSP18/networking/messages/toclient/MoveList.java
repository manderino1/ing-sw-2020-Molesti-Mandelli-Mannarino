package it.polimi.ingsw.PSP18.view.messages.toclient;

import it.polimi.ingsw.PSP18.model.Direction;

import java.util.ArrayList;

public class MoveList extends ClientAbstractMessage {
    private ArrayList<Direction> moveList;

    /***
     * Constructor to initialize the available moves direction list
     * @param moveList the list of possible moves
     */
    public MoveList(ArrayList<Direction> moveList) {
        this.type = ClientMessageType.MOVE_LIST;
        this.moveList = moveList;
    }

    /***
     * Returns the list of possible moves
     * @return the list of possible moves
     */
    public ArrayList<Direction> getMoveList() {
        return moveList;
    }
}
