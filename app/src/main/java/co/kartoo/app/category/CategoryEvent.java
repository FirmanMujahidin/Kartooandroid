package co.kartoo.app.category;

import java.util.ArrayList;

import co.kartoo.app.rest.model.newest.ListCategory;

public class CategoryEvent {
    ArrayList<ListCategory> listPromo;

    public CategoryEvent(ArrayList<ListCategory> listPromo) {
        this.listPromo = listPromo;
    }

    public ArrayList<ListCategory> getListPromo() {
        return listPromo;
    }

    public void setListPromo(ArrayList<ListCategory> listPromo) {
        this.listPromo = listPromo;
    }
}
