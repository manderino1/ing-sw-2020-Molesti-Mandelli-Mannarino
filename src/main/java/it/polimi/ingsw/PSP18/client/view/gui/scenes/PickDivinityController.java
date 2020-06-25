package it.polimi.ingsw.PSP18.client.view.gui.scenes;

import it.polimi.ingsw.PSP18.networking.messages.toclient.DivinityList;

/***
 * Controller of fxml to display the choice between any number of divinities
 */
public abstract class PickDivinityController extends Controller {
    protected DivinityList divinityList;

    public abstract void showChoices(DivinityList divinityList);
}
