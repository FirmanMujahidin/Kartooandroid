package co.kartoo.app.rest.model.newest;

import java.util.ArrayList;

/**
 * Created by firma on 3/25/2017.
 */

public class NearbyHeader {
    int maxPage;
    int currentPage;
    ArrayList<Nearby> promotions;

    public int getMaxPage() {
        return maxPage;
    }

    public void setMaxPage(int maxPage) {
        this.maxPage = maxPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public ArrayList<Nearby> getPromotions() {
        return promotions;
    }

    public void setPromotions(ArrayList<Nearby> promotions) {
        this.promotions = promotions;
    }
}
