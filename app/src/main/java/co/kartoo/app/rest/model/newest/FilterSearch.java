package co.kartoo.app.rest.model.newest;

import java.util.ArrayList;

/**
 * Created by Luthfi Apriyanto on 12/19/2016.
 */

public class FilterSearch {
    String query;
    String isMyCard;
    String sortBy;
    String latitude;
    String longitude;
    ArrayList<String> cardTypes;
    ArrayList<String> categoryTypes;

    public FilterSearch(){

    }

    public FilterSearch(String query, String isMyCard, String sortBy, String latitude, String longitude, ArrayList<String> cardTypes, ArrayList<String> categoryTypes) {
        this.query = query;
        this.isMyCard = isMyCard;
        this.sortBy = sortBy;
        this.latitude = latitude;
        this.longitude = longitude;
        this.cardTypes = cardTypes;
        this.categoryTypes = categoryTypes;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getIsMyCard() {
        return isMyCard;
    }

    public void setIsMyCard(String isMyCard) {
        this.isMyCard = isMyCard;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
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

    public ArrayList<String> getCardTypes() {
        return cardTypes;
    }

    public void setCardTypes(ArrayList<String> cardTypes) {
        this.cardTypes = cardTypes;
    }

    public ArrayList<String> getCategoryTypes() {
        return categoryTypes;
    }

    public void setCategoryTypes(ArrayList<String> categoryTypes) {
        this.categoryTypes = categoryTypes;
    }
}