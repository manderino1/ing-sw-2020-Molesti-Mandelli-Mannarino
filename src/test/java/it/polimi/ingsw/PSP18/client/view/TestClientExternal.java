package it.polimi.ingsw.PSP18.client.view;

import it.polimi.ingsw.PSP18.client.view.Launcher;
import it.polimi.ingsw.PSP18.server.controller.Match;
import org.junit.Test;

public class TestClientExternal {
    @Test
    public void launcherTest() {
        Match match = new Match();
        Launcher launcher = new Launcher("127.0.0.1");
    }
}
