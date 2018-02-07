package co.kartoo.app.cards.model;

/**
 * Created by MartinOenang on 10/17/2015.
 */
public class CardSpinnerItem {
    String id;
    String name;

    public CardSpinnerItem(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
