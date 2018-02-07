package co.kartoo.app.rest.model;

/**
 * Created by MartinOenang on 10/16/2015.
 */
public class BankLocation {
    String id;
    String address;
    String dateCreated;

    public BankLocation() {}
    

    public BankLocation(String id, String address, String dateCreated) {
        this.id = id;
        this.address = address;
        this.dateCreated = dateCreated;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }
}
