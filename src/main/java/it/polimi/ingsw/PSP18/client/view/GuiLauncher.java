package it.polimi.ingsw.PSP18.client.view;

import it.polimi.ingsw.PSP18.client.view.gui.GuiViewUpdate;
import javafx.application.Application;
import javafx.stage.Stage;

/***
 * Launch gui application, called from the launcher
 */
public class GuiLauncher extends Application {
    /***
     * Start method for the application
     * @param stage root stage reference
     * @throws Exception if stage open is failed throw exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        GuiViewUpdate guiViewUpdate = new GuiViewUpdate();
    }
}
