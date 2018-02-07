package co.kartoo.app.cards.suggestioncard.applycard;

import java.util.ArrayList;
import java.util.List;

import co.kartoo.app.rest.model.Category;
import co.kartoo.app.rest.model.newest.CreditCard;

public class CreditCardEvent {
    List<CreditCard> listCard;

    public CreditCardEvent(List<CreditCard> listCard) {
        this.listCard = listCard;
    }

    public List<CreditCard> getListCard() {
        return listCard;
    }

    public void setListPromo(List<CreditCard> listCard) {
        this.listCard = listCard;
    }
}
