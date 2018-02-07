package co.kartoo.app.rest.model;

/**
 * Created by Luthfi Apriyanto on 3/18/2016.
 */
public class ChangePassword {

    String email;
    String password;
    String oldPassword;

    public ChangePassword(){
    }

    public ChangePassword(String email, String password , String oldPassword) {
        this.email = email;
        this.password = password;
        this.oldPassword = oldPassword;
    }
}
