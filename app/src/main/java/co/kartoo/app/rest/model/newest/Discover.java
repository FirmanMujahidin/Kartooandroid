package co.kartoo.app.rest.model.newest;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Luthfi Apriyanto on 3/4/2016.
 */
public class Discover implements Serializable {

    String maxPage;
    String currentPage;
    ArrayList<DiscoverPromotion> promotions;


    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }


    public ArrayList<DiscoverPromotion> getPromotions() {
        return promotions;
    }

    public void setPromotions(ArrayList<DiscoverPromotion> promotions) {
        this.promotions = promotions;
    }


    public String getMaxPage() {
        return maxPage;
    }

    public void setMaxPage(String maxPage) {
        this.maxPage = maxPage;
    }


}
