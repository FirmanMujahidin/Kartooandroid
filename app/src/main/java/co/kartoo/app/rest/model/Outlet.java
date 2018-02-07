package co.kartoo.app.rest.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by MartinOenang on 10/21/2015.
 */
public class Outlet implements Serializable, ClusterItem {
    int id;
    ArrayList<Promo> promotions;
    ArrayList<Photo> outletGalleries;
    Merchant merchant;
    Area area;
    String website;
    String telphone;
    String latitude;
    String longitude;

    public ArrayList<Photo> getOutletGalleries() {
        return outletGalleries;
    }

    public void setOutletGalleries(ArrayList<Photo> outletGalleries) {
        this.outletGalleries = outletGalleries;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Promo> getPromotions() {
        return promotions;
    }

    public void setPromotions(ArrayList<Promo> promotions) {
        this.promotions = promotions;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
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

    @Override
    public LatLng getPosition() {
        return new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
    }
}
