package co.kartoo.app.bank.detail;

import co.kartoo.app.rest.model.newest.BankFeed;

/**
 * Created by Luthfi Apriyanto on 5/2/2016.
 */
public class BankDetilEvent {
    BankFeed listFeed;

    public BankDetilEvent(BankFeed listFeed) {
        this.listFeed = listFeed;
    }

    public BankFeed getListPromo() {
        return listFeed;
    }

    public void setListPromo(BankFeed listFeed) {
        this.listFeed = listFeed;
    }
}
