package co.kartoo.app.rest.model.newest;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Luthfi Apriyanto on 3/2/2016.
 */
public class Home implements Serializable {
    String userID;
    String name;
    String url_photo;
    ArrayList<EditorsPick> editorsPickPromotionList;
    ArrayList<Popular> popularPromotionList;
    Personalized personalizedPromotionCategory;
    String currentTime;


    public ArrayList<EditorsPick> getEditorsPickPromotionList() {
        return editorsPickPromotionList;
    }

    public void setEditorsPickPromotionList(ArrayList<EditorsPick> editorsPickPromotionList) {
        this.editorsPickPromotionList = editorsPickPromotionList;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl_photo() {
        return url_photo;
    }

    public void setUrl_photo(String url_photo) {
        this.url_photo = url_photo;
    }


    public ArrayList<Popular> getPopularPromotionList() {
        return popularPromotionList;
    }

    public void setPopularPromotionList(ArrayList<Popular> popularPromotionList) {
        this.popularPromotionList = popularPromotionList;
    }

    public Personalized getPersonalizedPromotionCategory() {
        return personalizedPromotionCategory;
    }

    public void setPersonalizedPromotionCategory(Personalized personalizedPromotionCategory) {
        this.personalizedPromotionCategory = personalizedPromotionCategory;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }
}