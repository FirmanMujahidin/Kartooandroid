package co.kartoo.app.rest.model;

/**
 * Created by MartinOenang on 10/9/2015.
 */
public class LoginCredentials {

    private String type;
    private String principal;
    private String credential;

    public LoginCredentials(){
    }

    public LoginCredentials(String type, String principal, String credential) {
        this.type = type;
        this.principal = principal;
        this.credential = credential;
    }
}
