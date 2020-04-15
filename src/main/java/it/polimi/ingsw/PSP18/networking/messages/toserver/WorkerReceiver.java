package it.polimi.ingsw.PSP18.networking.messages.toserver;

public class WorkerReceiver extends ServerAbstractMessage {
    private Integer x1, y1, x2, y2;

    public WorkerReceiver(Integer x1, Integer y1, Integer x2, Integer y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.type = ServerMessageType.WORKER_RECEIVER;
    }

    public Integer getX1() {
        return x1;
    }

    public Integer getY1() {
        return y1;
    }

    public Integer getX2() {
        return x2;
    }

    public Integer getY2() {
        return y2;
    }
}
