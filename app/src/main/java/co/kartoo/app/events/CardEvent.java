package co.kartoo.app.events;

import java.util.ArrayList;

import co.kartoo.app.rest.model.newest.Availablecards;

/**
 * Created by MartinOenang on 10/21/2015.
 */
public class CardEvent {
    ArrayList<Availablecards> listPromo;

    public CardEvent(ArrayList<Availablecards> listPromo) {
        this.listPromo = listPromo;
    }

    public ArrayList<Availablecards> getListPromo() {
        return listPromo;
    }

    public void setListPromo(ArrayList<Availablecards> listPromo) {
        this.listPromo = listPromo;
    }
}
