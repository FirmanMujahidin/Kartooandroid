package co.kartoo.app.models;

import org.androidannotations.annotations.sharedpreferences.SharedPref;

import java.util.ArrayList;


@SharedPref(value=SharedPref.Scope.UNIQUE)
public interface LoginPref {
    String email();
    String token();
    String name();
    String poin();
    String urlPhoto();
    int level();
    String uid();
    String type();
    boolean isFirstLogin();
    boolean isNotifOn();
    boolean isNotifStart();
    int fromBitmap();
    boolean addcard();
    String regid();
    String notifID();
    String savedBookmark();
    boolean noCard();
    String geofence();
    String status();
    boolean interest();

    int counterReview();
}
