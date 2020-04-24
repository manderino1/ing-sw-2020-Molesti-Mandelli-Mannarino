package it.polimi.ingsw.PSP18.client.view.cli;

/***
 * This is the class used to set string colors
 */
public enum CliColor {
    ANSI_RED("\u001B[31m"),
    ANSI_GREEN("\u001B[32m"),
    ANSI_YELLOW("\u001B[33m"),
    ANSI_BLUE("\u001B[34m"),
    ANSI_PURPLE("\u001B[35m");

    private String escape;

    static final String RESET = "\u001B[0m";

    /***
     * Method used to set the string color
     * @param escape
     */
    CliColor(String escape)
    {
        this.escape = escape;
    }
    public String getEscape()
    {
        return escape;
    }
    @Override
    public String toString()
    {
        return escape;
    }
}
