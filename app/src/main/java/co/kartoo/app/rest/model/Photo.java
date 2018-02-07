package co.kartoo.app.rest.model;

import java.io.Serializable;

/**
 * Created by MartinOenang on 10/23/2015.
 */
public class Photo implements Serializable {
    String id;
    String urlImg;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrlImg() {
        return urlImg;
    }

    public void setUrlImg(String urlImg) {
        this.urlImg = urlImg;
    }
}
