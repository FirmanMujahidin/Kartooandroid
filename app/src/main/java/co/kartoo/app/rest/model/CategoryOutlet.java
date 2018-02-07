package co.kartoo.app.rest.model;

import java.io.Serializable;
import java.util.ArrayList;

import co.kartoo.app.rest.model.newest.DiscoverPromotionCategory;

/**
 * Created by MartinOenang on 10/23/2015.
 */
public class CategoryOutlet implements Serializable{
    String id;
    String name;
    String caption;
    String url_img;
    int currentPage;
    int maxPage;
    ArrayList<DiscoverPromotionCategory> promotions;

    public ArrayList<DiscoverPromotionCategory> getPromotions() {
        return promotions;
    }

    public void setPromotions(ArrayList<DiscoverPromotionCategory> promotions) {
        this.promotions = promotions;
    }

    public String getUrl_img() {
        return url_img;
    }

    public void setUrl_img(String url_img) {
        this.url_img = url_img;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getMaxPage() {
        return maxPage;
    }

    public void setMaxPage(int maxPage) {
        this.maxPage = maxPage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

}
