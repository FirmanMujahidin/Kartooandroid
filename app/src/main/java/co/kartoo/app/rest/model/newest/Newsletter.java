package co.kartoo.app.rest.model.newest;

/**
 * Created by EricLaptop002 on 7/6/2017.
 */

public class Newsletter {
    private String id;
    private String url_img;
    private String header;
    private String body;
    private String timeSent;

    public Newsletter(String id, String url_img, String header, String body, String timeSent) {
        this.id = id;
        this.url_img = url_img;
        this.header = header;
        this.body = body;
        this.timeSent = timeSent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl_img() {
        return url_img;
    }

    public void setUrl_img(String url_img) {
        this.url_img = url_img;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(String timeSent) {
        this.timeSent = timeSent;
    }
}
