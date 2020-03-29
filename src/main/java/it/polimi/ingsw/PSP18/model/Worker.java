package it.polimi.ingsw.PSP18.model;

public class Worker {
    private Integer x, y;

    public Worker(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }

    public void setPosition(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }

    public Integer getX() {
        return this.x;
    }

    public Integer getY() {
        return this.y;
    }
}
