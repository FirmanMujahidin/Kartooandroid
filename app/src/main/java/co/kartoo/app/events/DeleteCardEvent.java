package co.kartoo.app.events;

import co.kartoo.app.rest.model.newest.Availablecards;

/**
 * Created by MartinOenang on 11/19/2015.
 */
public class DeleteCardEvent {
    Availablecards deletedCard;

    public DeleteCardEvent(Availablecards deletedCard) {
        this.deletedCard = deletedCard;
    }

    public Availablecards getDeletedCard() {
        return deletedCard;
    }

    public void setDeletedCard(Availablecards deletedCard) {
        this.deletedCard = deletedCard;
    }
}
