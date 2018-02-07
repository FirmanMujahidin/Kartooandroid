package co.kartoo.app.models;

import org.androidannotations.annotations.sharedpreferences.SharedPref;

import java.util.List;


@SharedPref(value=SharedPref.Scope.UNIQUE)
public interface ApplyCreditCardPref {
    String name();
    String gender();
    String dob();
    String city();
    String address();
    String phone();
    String interest();

    boolean isVerified();
    String nameCardApplied();

    String applicationID();
    String cardID();
    String bankName();
}
