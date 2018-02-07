package co.kartoo.app.rest.model;

/**
 * Created by MartinOenang on 10/9/2015.
 */
public class UpdateProfile {

    String id;
    String email;
    String date_created;
    String userlevel;
    String name;
    String point;
    String uid_facebook;
    String address;
    String uid_gplus;
    String url_photo;
    String phonenumber;
    String isVerified;
    String dateofbirth;
    String gender;
    String timezone;
    String city;

    public UpdateProfile(){

    }

    public UpdateProfile(String id, String email , String date_created, String userlevel, String name, String point, String uid_facebook,
                         String address, String uid_gplus, String url_photo, String phonenumber, String isVerified, String dateofbirth,
                         String gender, String timezone, String city) {
        this.id = id;
        this.email = email;
        this.date_created = date_created;
        this.userlevel = userlevel;
        this.name = name;
        this.point = point;
        this.uid_facebook = uid_facebook;
        this.address = address;
        this.uid_gplus = uid_gplus;
        this.url_photo = url_photo;
        this.phonenumber = phonenumber;
        this.isVerified = isVerified;
        this.dateofbirth = dateofbirth;
        this.gender = gender;
        this.timezone = timezone;
        this.city = city;
    }
}