package co.kartoo.app.cards.suggestioncard;

import java.util.ArrayList;

import co.kartoo.app.rest.model.newest.BankPage;
import co.kartoo.app.rest.model.newest.Perks;

/**
 * Created by Luthfi Apriyanto on 4/19/2016.
 */
public class EventPerks {

    ArrayList<Perks> listPerks;

    public EventPerks(ArrayList<Perks> listPerks) {
        this.listPerks = listPerks;
    }

    public ArrayList<Perks> getListPerks() {
        return listPerks;
    }

    public void setListPromo(ArrayList<Perks> listPerks) {
        this.listPerks = listPerks;
    }
}
