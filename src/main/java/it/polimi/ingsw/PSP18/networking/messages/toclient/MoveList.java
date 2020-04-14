package it.polimi.ingsw.PSP18.networking.messages.toclient;

import it.polimi.ingsw.PSP18.server.model.Direction;

import java.util.ArrayList;

public class MoveList extends ClientAbstractMessage {
    private ArrayList<Direction> moveList1;
    private ArrayList<Direction> moveList2;
    /***
     * Constructor to initialize the available moves direction list
     * @param moveList1 the list of possible moves for worker #1
     * @param moveList2 the list of possible moves for worker #2
     */
    public MoveList(ArrayList<Direction> moveList1, ArrayList<Direction> moveList2 ) {
        this.type = ClientMessageType.MOVE_LIST;
        this.moveList1 = moveList1;
        this.moveList2 = moveList2;
    }

    /***
     * Returns the list of possible moves
     * @return the list of possible moves
     */
    public ArrayList<Direction> getMoveList1() {
        return moveList1;
    }
    public ArrayList<Direction> getMoveList2() {
        return moveList2;
    }
}
