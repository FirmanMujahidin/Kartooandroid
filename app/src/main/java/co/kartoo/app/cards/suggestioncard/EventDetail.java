package co.kartoo.app.cards.suggestioncard;

import java.util.ArrayList;

import co.kartoo.app.rest.model.newest.Details;
import co.kartoo.app.rest.model.newest.Perks;

/**
 * Created by Luthfi Apriyanto on 4/19/2016.
 */
public class EventDetail {

    ArrayList<Details> listDetail;

    public EventDetail(ArrayList<Details> listDetail) {
        this.listDetail = listDetail;
    }

    public ArrayList<Details> getlistDetail() {
        return listDetail;
    }

    public void setListPromo(ArrayList<Details> listDetail) {
        this.listDetail = listDetail;
    }
}
