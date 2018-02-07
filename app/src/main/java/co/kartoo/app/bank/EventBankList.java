package co.kartoo.app.bank;

import java.util.ArrayList;

import co.kartoo.app.rest.model.newest.BankPage;

/**
 * Created by Luthfi Apriyanto on 4/19/2016.
 */
public class EventBankList {

    ArrayList<BankPage> listBank;

    public EventBankList(ArrayList<BankPage> listBank) {
        this.listBank = listBank;
    }

    public ArrayList<BankPage> getListPromo() {
        return listBank;
    }

    public void setListPromo(ArrayList<BankPage> listBank) {
        this.listBank = listBank;
    }
}
