package co.kartoo.app.rest.model;

public class RegisterUser {
    String name;
    String password;
    String email;
    String urlphoto;
    String uidFacebook;
    String uidGplus;

    public RegisterUser() {

    }

    public RegisterUser(String name, String email, String password, String urlphoto, String uidFacebook, String uidGplus) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.urlphoto = urlphoto;
        this.uidFacebook = uidFacebook;
        this.uidGplus = uidGplus;
    }
}
