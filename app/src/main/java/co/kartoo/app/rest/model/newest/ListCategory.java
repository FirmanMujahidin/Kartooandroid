package co.kartoo.app.rest.model.newest;

/**
 * Created by firma on 3/25/2017.
 */

public class ListCategory {
    String id;
    String caption;
    String name;
    String url_img;

    public ListCategory(String id, String caption, String name, String url_img) {
        this.id = id;
        this.caption = caption;
        this.name = name;
        this.url_img = url_img;
    }

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
}
