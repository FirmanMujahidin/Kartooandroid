package co.kartoo.app.rest.model.newest;

/**
 * Created by Luthfi Apriyanto on 5/30/2016.
 */
public class BankFeedItem {
    String id;
    String bank;
    String post_Id;
    String body;
    String created_time;
    String picture;
    String permalink_URL;
    String source;
    String latitude;
    String longitude;
    String datePosted;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getPost_Id() {
        return post_Id;
    }

    public void setPost_Id(String post_Id) {
        this.post_Id = post_Id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCreated_time() {
        return created_time;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPermalink_URL() {
        return permalink_URL;
    }

    public void setPermalink_URL(String permalink_URL) {
        this.permalink_URL = permalink_URL;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(String datePosted) {
        this.datePosted = datePosted;
    }
}
