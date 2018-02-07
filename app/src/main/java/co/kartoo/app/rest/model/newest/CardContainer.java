package co.kartoo.app.rest.model.newest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Luthfi Apriyanto on 1/19/2017.
 */

public class CardContainer {

    private List<CreditCard> content = new ArrayList<CreditCard>();

    public List<CreditCard> getContent() {
        return content;
    }

    public void setContent(List<CreditCard> content) {
        this.content = content;
    }
}
