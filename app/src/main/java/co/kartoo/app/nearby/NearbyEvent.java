package co.kartoo.app.nearby;

import co.kartoo.app.rest.model.newest.NearbyHeader;

/**
 * Created by Luthfi Apriyanto on 5/2/2016.
 */
public class NearbyEvent {
    NearbyHeader listPromo;

    public NearbyEvent(NearbyHeader listPromo) {
        this.listPromo = listPromo;
    }

    public NearbyHeader getListPromo() {
        return listPromo;
    }

    public void setListPromo(NearbyHeader listPromo) {
        this.listPromo = listPromo;
    }
}
