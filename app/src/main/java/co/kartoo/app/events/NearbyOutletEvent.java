package co.kartoo.app.events;

import java.util.ArrayList;

import co.kartoo.app.rest.model.newest.Nearby;

/**
 * Created by MartinOenang on 10/22/2015.
 */
public class NearbyOutletEvent {
    ArrayList<Nearby> listOutlet;

    public NearbyOutletEvent(ArrayList<Nearby> listOutlet) {
        this.listOutlet = listOutlet;
    }

    public ArrayList<Nearby> getListOutlet() {
        return listOutlet;
    }

    public void setListOutlet(ArrayList<Nearby> listOutlet) {
        this.listOutlet = listOutlet;
    }
}
