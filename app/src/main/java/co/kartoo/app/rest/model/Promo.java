package co.kartoo.app.rest.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Created by MartinOenang on 10/8/2015.
 */
public class Promo implements Comparable<Promo>, Serializable, ClusterItem {
    int id;
    ArrayList<Card> cards;
    String name;
    String shortDesc;
    String longDesc;
    String startDate;
    String endDate;
    String urlImg;
    int likeCounter;
    String createdAt;
    Merchant merchant;
    Area area;
    boolean active;
    boolean myCard;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isMyCard() {
        return myCard;
    }

    public void setMyCard(boolean myCard) {
        this.myCard = myCard;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getLongDesc() {
        return longDesc;
    }

    public void setLongDesc(String longDesc) {
        this.longDesc = longDesc;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getUrlImg() {
        return urlImg;
    }

    public void setUrlImg(String urlImg) {
        this.urlImg = urlImg;
    }

    public int getLikeCounter() {
        return likeCounter;
    }

    public void setLikeCounter(int likeCounter) {
        this.likeCounter = likeCounter;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Promo(int id, ArrayList<Card> cards, String name, String shortDesc, String longDesc, String startDate, String endDate, String urlImg, int likeCounter, String createdAt) {
        this.id = id;
        this.cards = cards;
        this.name = name;
        this.shortDesc = shortDesc;
        this.longDesc = longDesc;
        this.startDate = startDate;
        this.endDate = endDate;
        this.urlImg = urlImg;
        this.likeCounter = likeCounter;
        this.createdAt = createdAt;
    }

    public Promo(){

    }

    @Override
    public int compareTo(Promo another) {
        return another.getId() - getId();
    }

    @Override
    public LatLng getPosition() {
        return null;
//        return new LatLng();
    }
}
