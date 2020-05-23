package it.polimi.ingsw.PSP18.networking.messages.toclient;

import it.polimi.ingsw.PSP18.server.model.Direction;
import it.polimi.ingsw.PSP18.server.model.Worker;

import java.util.ArrayList;

/***
 * This message class is used from Prometheus to give the possibility to build with two workers
 */
public class PrometheusBuildList extends ClientAbstractMessage {

    private ArrayList<Direction> buildlist1;
    private ArrayList<Direction> buildlist2;
    protected Worker worker1, worker2;

    /***
     * Constructor of BuildList
     * @param buildlist1 the array of possibles moves of the first worker
     * @param buildlist2 the array of possibles moves of the second worker
     * @param worker1 first worker reference for special build
     * @param worker2 second worker reference for special build
     */
    public PrometheusBuildList(ArrayList<Direction> buildlist1, ArrayList<Direction> buildlist2, Worker worker1, Worker worker2){
        this.type=ClientMessageType.PROMETHEUS_BUILD_LIST;
        this.buildlist1= buildlist1;
        this.buildlist2= buildlist2;
        this.worker1 = worker1;
        this.worker2 = worker2;
    }

    /***
     * Returns buildlist1
     * @return the array of possibles moves
     */
    public ArrayList<Direction> getBuildlist1() {
        return buildlist1;
    }

    /***
     * Returns buildlist2
     * @return the array of possibles moves
     */
    public ArrayList<Direction> getBuildlist2() {
        return buildlist2;
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
