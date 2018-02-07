package co.kartoo.app.cards.suggestioncard;

import java.util.ArrayList;

import co.kartoo.app.rest.model.newest.Availablecards;
import co.kartoo.app.rest.model.newest.CardDetail;
import co.kartoo.app.rest.model.newest.Details;

/**
 * Created by Luthfi Apriyanto on 4/19/2016.
 */
public class EventMoreCards {

    ArrayList<Availablecards> listAvailableCards;

    public EventMoreCards(ArrayList<Availablecards> listAvailableCards) {
        this.listAvailableCards = listAvailableCards;
    }

    public ArrayList<Availablecards> getlistAvailableCards() {
        return listAvailableCards;
    }

    public void setListPromo(ArrayList<Availablecards> listAvailableCards) {
        this.listAvailableCards = listAvailableCards;
    }
}
