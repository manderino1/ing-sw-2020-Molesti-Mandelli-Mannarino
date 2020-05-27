package it.polimi.ingsw.PSP18.client.view;

import it.polimi.ingsw.PSP18.server.controller.MatchManager;
import org.junit.Test;

public class TestClientExternal {
    @Test
    public void launcherTest() {
        MatchManager matchManager = new MatchManager();
        Launcher launcher = new Launcher("127.0.0.1");
    }
}
