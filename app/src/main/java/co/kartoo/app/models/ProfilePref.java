package co.kartoo.app.models;

import org.androidannotations.annotations.sharedpreferences.SharedPref;


@SharedPref(value=SharedPref.Scope.UNIQUE)
public interface ProfilePref {
    String name();
    String gender();
    String dob();
    String city();
    String address();
    String phone();
    String interest();
    String cardName();

    boolean isVerified();
}
