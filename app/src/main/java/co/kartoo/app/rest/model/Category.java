package co.kartoo.app.rest.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Category implements Serializable {
    String id;
    String promotion;
    String url_img;
    String merchant;
    String bank;
    ArrayList<String> outlets;
    String band;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPromotion() {
        return promotion;
    }

    public void setPromotion(String promotion) {
        this.promotion = promotion;
    }

    public String getUrl_img() {
        return url_img;
    }

    public void setUrl_img(String url_img) {
        this.url_img = url_img;
    }

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public ArrayList<String> getOutlets() {
        return outlets;
    }

    public void setOutlets(ArrayList<String> outlets) {
        this.outlets = outlets;
    }

    public String getBand() {
        return band;
    }

    public void setBand(String band) {
        this.band = band;
    }
}
