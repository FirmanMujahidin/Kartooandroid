package co.kartoo.app.rest.model.newest;

import java.io.Serializable;

import co.kartoo.app.rest.model.Bank;

/**
 * Created by Luthfi Apriyanto on 4/19/2016.
 */
public class BankPage implements Serializable {

    String id;
    String name;
    String description;
    String website;
    String facebook;
    String twitter;
    String call_center;
    String url_img;
    Boolean isFollowing;
    String url_bgimg;

    public BankPage() {
    }

    public BankPage(String id, String name, String description, String website, String facebook, String twitter, String call_center, String url_img, Boolean isFollowing) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.website = website;
        this.facebook = facebook;
        this.twitter = twitter;
        this.call_center = call_center;
        this.url_img = url_img;
        this.isFollowing = isFollowing;
    }


    public String getUrl_bgimg() {
        return url_bgimg;
    }

    public void setUrl_bgimg(String url_bgimg) {
        this.url_bgimg = url_bgimg;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getCall_center() {
        return call_center;
    }

    public void setCall_center(String call_center) {
        this.call_center = call_center;
    }

    public String getUrl_img() {
        return url_img;
    }

    public void setUrl_img(String url_img) {
        this.url_img = url_img;
    }

    public Boolean getFollowing() {
        return isFollowing;
    }

    public void setFollowing(Boolean following) {
        isFollowing = following;
    }

}