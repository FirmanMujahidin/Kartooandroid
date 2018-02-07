package co.kartoo.app.mall;

import java.util.ArrayList;

import co.kartoo.app.rest.model.newest.Malls;
import co.kartoo.app.rest.model.newest.Nearby;

/**
 * Created by Luthfi Apriyanto on 5/2/2016.
 */
public class MallEvent {
    Malls listPromo;

    public MallEvent(Malls listPromo) {
        this.listPromo = listPromo;
    }

    public Malls getListPromo() {
        return listPromo;
    }

    public void setListPromo(Malls listPromo) {
        this.listPromo = listPromo;
    }
}
