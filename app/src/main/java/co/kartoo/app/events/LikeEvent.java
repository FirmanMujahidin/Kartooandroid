package co.kartoo.app.events;

import java.util.ArrayList;

import co.kartoo.app.rest.model.newest.DiscoverPromotionCategory;

/**
 * Created by MartinOenang on 10/21/2015.
 */
public class LikeEvent {
    ArrayList<DiscoverPromotionCategory> listPromo;

    public LikeEvent(ArrayList<DiscoverPromotionCategory> listPromo) {
        this.listPromo = listPromo;
    }

    public ArrayList<DiscoverPromotionCategory> getListPromo() {
        return listPromo;
    }

    public void setListPromo(ArrayList<DiscoverPromotionCategory> listPromo) {
        this.listPromo = listPromo;
    }
}
