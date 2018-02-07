package co.kartoo.app.rest.model;

/**
 * Created by MartinOenang on 10/12/2015.
 */
public class ResponseGetUserInfo {
    int id;
    long dateCreated;
    String username;
    String email;
    String name;
    String uidFacebook;
    String uidGplus;
    String urlPhoto;
    int bgCode;
    int level;
    int point;
    int cardCounter;
    int friendCounter;

    public int getTotalCards() {
        return cardCounter;
    }

    public void setTotalCards(int totalCards) {
        this.cardCounter = totalCards;
    }

    public int getTotalFriends() {
        return friendCounter;
    }

    public void setTotalFriends(int totalFriends) {
        this.friendCounter = totalFriends;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(long dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUidFacebook() {
        return uidFacebook;
    }

    public void setUidFacebook(String uidFacebook) {
        this.uidFacebook = uidFacebook;
    }

    public String getUidGplus() {
        return uidGplus;
    }

    public void setUidGplus(String uidGplus) {
        this.uidGplus = uidGplus;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public int getBgCode() {
        return bgCode;
    }

    public void setBgCode(int bgCode) {
        this.bgCode = bgCode;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }
}
