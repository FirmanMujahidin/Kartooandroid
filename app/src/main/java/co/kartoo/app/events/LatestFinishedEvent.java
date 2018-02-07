package co.kartoo.app.events;

import co.kartoo.app.rest.model.newest.Discover;

/**
 * Created by MartinOenang on 10/21/2015.
 */
public class LatestFinishedEvent {
    Discover listPromo;

    public LatestFinishedEvent(Discover listPromo) {
        this.listPromo = listPromo;
    }

    public Discover getListPromo() {
        return listPromo;
    }

    public void setListPromo(Discover listPromo) {
        this.listPromo = listPromo;
    }
}
