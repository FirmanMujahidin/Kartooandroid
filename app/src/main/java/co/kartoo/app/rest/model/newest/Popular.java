package co.kartoo.app.rest.model.newest;

import java.io.Serializable;

import co.kartoo.app.rest.model.Bank;
import co.kartoo.app.rest.model.Merchant;

/**
 * Created by Luthfi Apriyanto on 3/2/2016.
 */
public class Popular implements Serializable {
    String id;
    String name;
    String subtitle;
    String url_img;
    String terms_and_condition;
    String start_date;
    String end_date;
    Boolean isFavorite;
    String typeofpromo;
    String promo_url;
    String availablecards;
    String availableoutlets;
    Bank bank;
    Merchant merchant;
    String similarpromotions;
    String band;

    public String getBand() {
        return band;
    }

    public void setBand(String band) {
        this.band = band;
    }

    public Bank getBank() {
        return bank;
    }

    public Merchant getMerchant() {
        return merchant;
    }


    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
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

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getUrl_img() {
        return url_img;
    }

    public void setUrl_img(String url_img) {
        this.url_img = url_img;
    }

    public String getTerms_and_condition() {
        return terms_and_condition;
    }

    public void setTerms_and_condition(String terms_and_condition) {
        this.terms_and_condition = terms_and_condition;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public Boolean getFavorite() {
        return isFavorite;
    }

    public void setFavorite(Boolean favorite) {
        isFavorite = favorite;
    }

    public String getTypeofpromo() {
        return typeofpromo;
    }

    public void setTypeofpromo(String typeofpromo) {
        this.typeofpromo = typeofpromo;
    }

    public String getPromo_url() {
        return promo_url;
    }

    public void setPromo_url(String promo_url) {
        this.promo_url = promo_url;
    }

    public String getAvailablecards() {
        return availablecards;
    }

    public void setAvailablecards(String availablecards) {
        this.availablecards = availablecards;
    }

    public String getAvailableoutlets() {
        return availableoutlets;
    }

    public void setAvailableoutlets(String availableoutlets) {
        this.availableoutlets = availableoutlets;
    }
    public String getSimilarpromotions() {
        return similarpromotions;
    }

    public void setSimilarpromotions(String similarpromotions) {
        this.similarpromotions = similarpromotions;
    }

}
