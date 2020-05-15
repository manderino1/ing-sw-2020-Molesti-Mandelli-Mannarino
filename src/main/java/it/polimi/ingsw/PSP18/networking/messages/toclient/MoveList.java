package it.polimi.ingsw.PSP18.networking.messages.toclient;

import it.polimi.ingsw.PSP18.server.model.Direction;
import it.polimi.ingsw.PSP18.server.model.Worker;

import java.util.ArrayList;

/***
 * This message class is used to send to the client a list of the possible moves
 */
public class MoveList extends ClientAbstractMessage {
    private ArrayList<Direction> moveList1;
    private ArrayList<Direction> moveList2;
    private Worker worker1, worker2;

    /***
     * Constructor to initialize the available moves direction list
     * @param moveList1 the list of possible moves for worker #1
     * @param moveList2 the list of possible moves for worker #2
     * @param worker1 the first worker reference
     * @param worker2 the second worker reference
     */
    public MoveList(ArrayList<Direction> moveList1, ArrayList<Direction> moveList2, Worker worker1, Worker worker2) {
        this.type = ClientMessageType.MOVE_LIST;
        this.moveList1 = moveList1;
        this.moveList2 = moveList2;
        this.worker1 = worker1;
        this.worker2 = worker2;
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

    /***
     * Return the reference of the first worker
     * @return the first worker
     */
    public Worker getWorker1() {
        return worker1;
    }
    /***
     * Return the reference of the second worker
     * @return the second worker
     */
    public Worker getWorker2() {
        return worker2;
    }
}
