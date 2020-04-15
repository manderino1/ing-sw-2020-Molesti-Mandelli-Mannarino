package it.polimi.ingsw.PSP18.networking.messages.toclient;

import it.polimi.ingsw.PSP18.server.model.Direction;

import java.util.ArrayList;

public class BuildListFlag extends ClientAbstractMessage{

    private Boolean flag;
    private ArrayList<Direction> buildlist;

    /***
     * constructor of BuildList
     * @param buildlist
     * @param flag
     */
    public BuildListFlag(ArrayList<Direction> buildlist, Boolean flag){
        this.type = ClientMessageType.BUILD_LIST;
        this.buildlist= buildlist;
        this.flag=flag;
    }

    /***
     * returns the flag
     * @return flag
     */
    public Boolean getFlag() {
        return flag;
    }

    /***
     * returns buildlist
     * @return the array of possibles moves
     */
    public ArrayList<Direction> getBuildlist() {
            return buildlist;
        }

}
