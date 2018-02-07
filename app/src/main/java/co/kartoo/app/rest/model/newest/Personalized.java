package co.kartoo.app.rest.model.newest;

import java.io.Serializable;

/**
 * Created by Luthfi Apriyanto on 3/2/2016.
 */
public class Personalized implements Serializable{
    String id;
    String caption;
    String name;
    String url_img;
    String promotions;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl_img() {
        return url_img;
    }

    public void setUrl_img(String url_img) {
        this.url_img = url_img;
    }

    public String getPromotions() {
        return promotions;
    }

    public void setPromotions(String promotions) {
        this.promotions = promotions;
    }
}
