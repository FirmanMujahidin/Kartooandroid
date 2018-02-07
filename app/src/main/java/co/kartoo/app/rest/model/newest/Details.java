package co.kartoo.app.rest.model.newest;

import java.util.ArrayList;

/**
 * Created by Luthfi Apriyanto on 12/7/2016.
 */

public class Details {
    String header;
    ArrayList<DetailItem> detailItems;

    public ArrayList<DetailItem> getDetailItems() {
        return detailItems;
    }

    public void setDetailItems(ArrayList<DetailItem> detailItems) {
        this.detailItems = detailItems;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

}
