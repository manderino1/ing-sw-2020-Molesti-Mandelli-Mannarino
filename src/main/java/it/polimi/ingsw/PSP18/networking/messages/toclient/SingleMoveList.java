package it.polimi.ingsw.PSP18.networking.messages.toclient;

import it.polimi.ingsw.PSP18.server.model.Direction;
import it.polimi.ingsw.PSP18.server.model.Worker;

import java.util.ArrayList;

/***
 * This message class is used to send the possible moves list
 * when there is the possibility to move only with one worker
 */
public class SingleMoveList extends ClientAbstractMessage {
    private ArrayList<Direction> moveList;
    private Integer workerID;
    private Worker worker;
    private boolean optional;

    /***
     * Constructor to initialize the available moves direction list
     * @param moveList the list of possible moves for worker #1
     * @param workerID the worker ID
     * @param optional true if the move is optional
     * @param worker worker that has to move
     */
    public SingleMoveList(ArrayList<Direction> moveList, Integer workerID, boolean optional, Worker worker) {
        this.type = ClientMessageType.SINGLE_MOVE_LIST;
        this.moveList = moveList;
        this.workerID = workerID;
        this.optional = optional;
        this.worker = worker;
    }

    /***
     * Returns the list of possible moves
     * @return the list of possible moves
     */
    public ArrayList<Direction> getMoveList() {
        return moveList;
    }

    /***
     * Returns the worker ID
     * @return the worker ID
     */
    public Integer getWorkerID() {
        return workerID;
    }

    /***
     * Return the worker position reference
     * @return the worker reference
     */
    public Worker getWorker() {
        return worker;
    }

    /***
     * True if the move is optional
     * @return true if optional
     */
    public boolean isOptional() {
        return optional;
    }
}