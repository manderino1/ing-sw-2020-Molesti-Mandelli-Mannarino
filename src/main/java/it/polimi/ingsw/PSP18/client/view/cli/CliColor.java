package it.polimi.ingsw.PSP18.client.view.cli;

public enum CliColor {
    ANSI_RED("\u001B[31m"),
    ANSI_GREEN("\u001B[32m"),
    ANSI_YELLOW("\u001B[33m"),
    ANSI_BLUE("\u001B[34m"),
    ANSI_PURPLE("\u001B[35m");

    private String escape;

    static final String RESET = "\u001B[0m";

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
