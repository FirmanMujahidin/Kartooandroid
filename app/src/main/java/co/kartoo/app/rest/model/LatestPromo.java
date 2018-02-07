package co.kartoo.app.rest.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by MartinOenang on 10/22/2015.
 */
public class LatestPromo implements Serializable {
    String page;
    String totalPage;
    ArrayList<Promo> content;

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(String totalPage) {
        this.totalPage = totalPage;
    }

    public ArrayList<Promo> getContent() {
        return content;
    }

    public void setContent(ArrayList<Promo> content) {
        this.content = content;
    }
}
