package co.kartoo.app.rest.model;

import java.util.ArrayList;

/**
 * Created by firma on 3/25/2017.
 */

public class CategoryHeader {
    String maxPage;
    String currentPage;
    ArrayList<Category> promotions;

    public String getMaxPage() {
        return maxPage;
    }

    public void setMaxPage(String maxPage) {
        this.maxPage = maxPage;
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    public ArrayList<Category> getPromotions() {
        return promotions;
    }

    public void setPromotions(ArrayList<Category> promotions) {
        this.promotions = promotions;
    }
}
