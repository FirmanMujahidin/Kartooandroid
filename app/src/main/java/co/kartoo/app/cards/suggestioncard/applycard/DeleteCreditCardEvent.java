package co.kartoo.app.cards.suggestioncard.applycard;

import co.kartoo.app.rest.model.newest.Availablecards;
import co.kartoo.app.rest.model.newest.CreditCard;

public class DeleteCreditCardEvent {
    CreditCard deletedCard;

    public DeleteCreditCardEvent(CreditCard deletedCard) {
        this.deletedCard = deletedCard;
    }

    public CreditCard getDeletedCard() {
        return deletedCard;
    }

    public void setDeletedCard(CreditCard deletedCard) {
        this.deletedCard = deletedCard;
    }
}
