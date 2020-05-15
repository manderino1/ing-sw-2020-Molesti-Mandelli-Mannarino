package it.polimi.ingsw.PSP18.networking.messages.toserver;

/***
 * This message class sends is used to send the coordinates of the placed workers to the server
 */
public class WorkerReceiver extends ServerAbstractMessage {
    private Integer x1, y1, x2, y2;

    /***
     * Constructor of the WorkReceiver class, a message used to set the workers coordinates for a player
     * @param x1 x coordinate of the first worker
     * @param y1 y coordinate of the first worker
     * @param x2 x coordinate of the second worker
     * @param y2 y coordinate of the second worker
     */
    public WorkerReceiver(Integer x1, Integer y1, Integer x2, Integer y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.type = ServerMessageType.WORKER_RECEIVER;
    }

    /***
     * Returns x coordinate of the first worker
     * @return x coordinate of the first worker
     */
    public Integer getX1() {
        return x1;
    }

    /***
     * Returns y coordinate of the first worker
     * @return y coordinate of the first worker
     */
    public Integer getY1() {
        return y1;
    }

    /***
     * Returns x coordinate of the second worker
     * @return x coordinate of the second worker
     */
    public Integer getX2() {
        return x2;
    }

    /***
     * Returns y coordinate of the second worker
     * @return y coordinate of the second worker
     */
    public Integer getY2() {
        return y2;
    }
}
