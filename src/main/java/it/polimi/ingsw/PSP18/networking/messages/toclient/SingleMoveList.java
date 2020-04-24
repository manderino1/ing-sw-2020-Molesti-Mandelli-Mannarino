package it.polimi.ingsw.PSP18.networking.messages.toclient;

import it.polimi.ingsw.PSP18.server.model.Direction;

import java.util.ArrayList;

public class SingleMoveList extends ClientAbstractMessage {
    private ArrayList<Direction> moveList;
    private Integer workerID;
    private boolean optional;

    /***
     * Constructor to initialize the available moves direction list
     * @param moveList the list of possible moves for worker #1
     * @param workerID the worker ID
     * @param optional true if the move is optional
     */
    public SingleMoveList(ArrayList<Direction> moveList, Integer workerID, boolean optional) {
        this.type = ClientMessageType.SINGLE_MOVE_LIST;
        this.moveList = moveList;
        this.workerID = workerID;
        this.optional = optional;
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

    public boolean isOptional() {
        return optional;
    }
}