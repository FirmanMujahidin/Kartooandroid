package co.kartoo.app.rest.model.newest;

/**
 * Created by Luthfi Apriyanto on 12/8/2016.
 */

public class DetailItem {
    String id;
    String header;
    String itemKey;
    String itemValue;

    public DetailItem() {
    }

    public DetailItem(String itemKey, String itemValue) {
        this.itemKey = itemKey;
        this.itemValue = itemValue;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getItemKey() {
        return itemKey;
    }

    public void setItemKey(String itemKey) {
        this.itemKey = itemKey;
    }

    public String getItemValue() {
        return itemValue;
    }

    public void setItemValue(String itemValue) {
        this.itemValue = itemValue;
    }
}
