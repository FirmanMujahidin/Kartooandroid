package co.kartoo.app.events;

import java.util.ArrayList;

import co.kartoo.app.rest.model.newest.EditorsPick;

/**
 * Created by MartinOenang on 10/21/2015.
 */
public class TrendingFinishedEvent {
    ArrayList<EditorsPick> listPromo;

    public TrendingFinishedEvent(ArrayList<EditorsPick> listPromo) {
        this.listPromo = listPromo;
    }

    public ArrayList<EditorsPick> getListPromo() {
        return listPromo;
    }

    public void setListPromo(ArrayList<EditorsPick> listPromo) {
        this.listPromo = listPromo;
    }
}
