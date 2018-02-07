package co.kartoo.app.rest.model;

/**
 * Created by MartinOenang on 10/11/2015.
 */
public class ResponseLogin {
    String userId;
    String mobileServiceAuthenticationToken;
    String name;
    Boolean mustChangePassword;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMobileServiceAuthenticationToken() {
        return mobileServiceAuthenticationToken;
    }

    public void setMobileServiceAuthenticationToken(String mobileServiceAuthenticationToken) {
        this.mobileServiceAuthenticationToken = mobileServiceAuthenticationToken;
    }

    public Boolean getMustChangePassword() {
        return mustChangePassword;
    }

    public void setMustChangePassword(Boolean mustChangePassword) {
        this.mustChangePassword = mustChangePassword;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    //Boolean

    public String getUid() {
        return userId;
    }

    public void setUid(String uid) {
        this.userId = uid;
    }

    public String getTokenValue() {
        return mobileServiceAuthenticationToken;
    }

    public void setTokenValue(String tokenValue) {
        this.mobileServiceAuthenticationToken = tokenValue;
    }
}
