package co.kartoo.app.rest.model.newest;

/**
 * Created by Luthfi Apriyanto on 1/16/2017.
 */

public class CreditCard {
    private String name, cvv, valid;

    public CreditCard() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }

    public CreditCard(String name, String cvv, String valid) {
        this.name = name;
        this.cvv = cvv;
        this.valid = valid;

    }
}