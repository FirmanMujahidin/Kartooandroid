package co.kartoo.app.rest.model.newest;

import java.util.ArrayList;

/**
 * Created by firma on 3/29/2017.
 */

public class SearchPromotionHeader {

    String maxPage;
    String currentPage;
    ArrayList<SearchPromotion> promotions;

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

    public ArrayList<SearchPromotion> getPromotions() {
        return promotions;
    }

    public void setPromotions(ArrayList<SearchPromotion> promotions) {
        this.promotions = promotions;
    }
}
