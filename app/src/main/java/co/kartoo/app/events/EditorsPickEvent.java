package co.kartoo.app.events;

import java.util.ArrayList;

import co.kartoo.app.rest.model.newest.DiscoverPromotion;

/**
 * Created by MartinOenang on 10/21/2015.
 */
public class EditorsPickEvent {
    ArrayList<DiscoverPromotion> listPromo;

    public EditorsPickEvent(ArrayList<DiscoverPromotion> listPromo) {
        this.listPromo = listPromo;
    }

    public ArrayList<DiscoverPromotion> getListPromo() {
        return listPromo;
    }

    public void setListPromo(ArrayList<DiscoverPromotion> listPromo) {
        this.listPromo = listPromo;
    }
}
