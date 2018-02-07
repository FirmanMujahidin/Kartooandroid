package co.kartoo.app.notifications;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import co.kartoo.app.MainActivity;
import co.kartoo.app.MainActivity_;
import co.kartoo.app.R;
import co.kartoo.app.mall.ActivityMall_;
import co.kartoo.app.rest.PromoService;
import co.kartoo.app.rest.model.newest.DiscoverPromotionCategory;
import co.kartoo.app.search.SearchActivity_;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;


/**
 * Created by Luthfi Apriyanto on 5/13/2016.
 */
public class GeofenceTransitionsIntentService extends IntentService {

    protected static final String TAG = "GeofenceTransitionsIS";

    NotificationManager mNotificationManager;
    Intent resultIntent;
    String subject, id, latit, longit, address, contain, img;
    TaskStackBuilder stackBuilder;

    ArrayList<String> saved;

    String[] savingValue;

    Boolean found;
    ArrayList<String> candidate;
    Bitmap image;

    String errorMessage;

    private FirebaseAnalytics mFirebaseAnalytics;
    /**
     * This constructor is required, and calls the super IntentService(String)
     * constructor with the name for a worker thread.
     */
    public GeofenceTransitionsIntentService() {
        // Use the TAG to name the worker thread.
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }

    /**
     * Handles incoming intents.
     * @param intent sent by Location Services. This Intent is provided to Location
     *               Services (inside a PendingIntent) when addGeofences() is called.
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);


        if (geofencingEvent.hasError()) {

            errorMessage = "Error"+geofencingEvent.getErrorCode();//GeofenceErrorMessages.getErrorString(this, geofencingEvent.getErrorCode());
            Log.e(TAG, errorMessage);
            return;
        }

        saved = new ArrayList<String>();

        SharedPreferences prefs = getSharedPreferences("mall_pref", 0);
        SharedPreferences prefs2 = getSharedPreferences("saving_mall", 0);

        Gson gson = new Gson();
        String nameMall = prefs.getString("mallName", null);
        String Lat = prefs.getString("mallLat", null);
        String Long = prefs.getString("mallLong", null);
        String Id = prefs.getString("mallId", null);
        String Img = prefs.getString("mallImg", null);

        String save = prefs2.getString("mallSaved", null);

        String[] listName = gson.fromJson(nameMall, String[].class);
        String[] listLat = gson.fromJson(Lat, String[].class);
        String[] listLong = gson.fromJson(Long, String[].class);
        String[] listId = gson.fromJson(Id, String[].class);
        String[] listImg = gson.fromJson(Img, String[].class);

        String[] listSave = gson.fromJson(save, String[].class);

        if (listSave != null){
            savingValue = new String[listSave.length];
            for (int i = 0; i < listSave.length; i++) {
                savingValue[i] = listSave[i];
            }
        }

        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_DWELL ) {
            List<String> geofenceIds = new ArrayList<>();
            for (Geofence geofence : geofencingEvent.getTriggeringGeofences()) {
                geofenceIds.add(geofence.getRequestId());
            }

            //to log saved geofence used to be
            if(listSave!=null){
                Log.e(TAG, "saved: "+listSave.length);
                for (int i = 0; i < listSave.length; i++) {
                    Log.e(TAG, "saved: "+listSave[i]);
                }
            }

            if (listSave == null){
                mainLoop:
                for (int i = 0; i < listLat.length; i++) {
                    for (int j = 0; j < geofencingEvent.getTriggeringGeofences().size(); j++) {
                        if (listLat[i].equals(geofencingEvent.getTriggeringGeofences().get(j).getRequestId())){
                            subject = listName[i];
                            id = listId[i];
                            latit = listLat[i];
                            longit = listLong[i];
                            img = listImg[i];

                            shared();

                            try {
                                URL url = new URL(img);
                                image = BitmapFactory.decodeStream(url.openStream());

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            sendNotification(subject);
                            break mainLoop;
                        }
                    }
                }

            }

            else {
                found = false;
                candidate = new ArrayList<String>();
                outerLoop:
                for (int i = 0; i < listLat.length; i++) {
                    for (int j = 0; j < geofencingEvent.getTriggeringGeofences().size(); j++) {
                        if (listLat[i].equals(geofencingEvent.getTriggeringGeofences().get(j).getRequestId())){
                            for (int k = 0; k < listSave.length; k++) {
                                if (listSave[k].equals(listName[i])){
                                    found = true;
                                    break;
                                }
                            }
                            if (!found){
                                subject = listName[i];
                                id = listId[i];
                                latit = listLat[i];
                                longit = listLong[i];
                                img = listImg[i];
                                shared();

                                try {
                                    URL url = new URL(img);
                                    image = BitmapFactory.decodeStream(url.openStream());

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                sendNotification(subject);
                                break outerLoop;
                            }
                            found = false;

                        }
                    }
                }
            }

            // Get the geofences that were triggered. A single event can trigger multiple geofences.
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();

            // Get the transition details as a String.
            String geofenceTransitionDetails = getGeofenceTransitionDetails(
                    this,
                    geofenceTransition,
                    triggeringGeofences
            );

            // Send notification and log the transition details.
            //sendNotification(subject);
            Log.i(TAG, geofenceTransitionDetails);
        } else {
            // Log the error.
            //Log.e(TAG, getString(R.string.geofence_transition_invalid_type, geofenceTransition));
        }
    }

    public void shared(){
        SharedPreferences prefs = getSharedPreferences("saving_mall", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();

        //saved.add(subject);
        Gson gson = new Gson();
        List<String> savedMall = new ArrayList<String>();

        if (savingValue!=null){
            for (int i = 0; i < savingValue.length; i++) {
                savedMall.add(savingValue[i]);
            }
        }

        savedMall.add(subject);

        String setSave = gson.toJson(savedMall);
        edit.putString("mallSaved", setSave);

        edit.apply();
    }

    /**
     * Gets transition details and returns them as a formatted string.
     *
     * @param context               The app context.
     * @param geofenceTransition    The ID of the geofence transition.
     * @param triggeringGeofences   The geofence(s) triggered.
     * @return                      The transition details formatted as String.
     */
    private String getGeofenceTransitionDetails(
            Context context,
            int geofenceTransition,
            List<Geofence> triggeringGeofences) {

        String geofenceTransitionString = getTransitionString(geofenceTransition);

        // Get the Ids of each geofence that was triggered.
        ArrayList triggeringGeofencesIdsList = new ArrayList();
        for (Geofence geofence : triggeringGeofences) {
            triggeringGeofencesIdsList.add(geofence.getRequestId());
        }
        String triggeringGeofencesIdsString = TextUtils.join(", ",  triggeringGeofencesIdsList);

        return geofenceTransitionString + ": " + triggeringGeofencesIdsString;
    }

    /**
     * Posts a notification in the notification bar when a transition is detected.
     * If the user clicks the notification, control goes to the MainActivity.
     */
    private void sendNotification(String notificationDetails) {
        //String contextText = String.format("Welcome to ", notificationDetails);
        Bitmap icon = BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher);

        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        resultIntent = new Intent(this, ActivityMall_.class);
        stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(ActivityMall_.class);
        stackBuilder.addNextIntent(resultIntent);
        resultIntent.putExtra("id", id);
        resultIntent.putExtra("address", subject);
        resultIntent.putExtra("latitude", latit);
        resultIntent.putExtra("longitude", longit);
        resultIntent.putExtra("name", subject);

        //Mixpanel
        String projectToken = getString(R.string.mixpanel_token);
        MixpanelAPI mixpanel = MixpanelAPI.getInstance(this, projectToken);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTimeStamp = dateFormat.format(new Date());
        try {
            if (errorMessage!=null){
                JSONObject prop = new JSONObject();
                prop.put("errorMessage", errorMessage);
                mixpanel.track("Error Geofencing", prop);
            }
            else {
                JSONObject props = new JSONObject();
                props.put("TimeStamp", currentTimeStamp);
                props.put("mall notif name", notificationDetails);
                mixpanel.track("Mall Notif Received", props);
            }
        } catch (JSONException e) {
        }

        //Firebase
        Bundle params = new Bundle();
        if (errorMessage!=null){
            params.putString("error_message", errorMessage);
            mFirebaseAnalytics.logEvent("error_geofencing", params);
        }else {
            params.putString("mall_notif_name", notificationDetails);
            mFirebaseAnalytics.logEvent("mall_notif_received", params);
        }

        NotificationCompat.Builder mBuilder;

        Intent intent = new Intent(this, SearchActivity_.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        Log.e(TAG, "sendNotification: "+notificationDetails );
        mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.icon_notif)
                        .setContentTitle("Welcome to "+notificationDetails)
                        .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(image).setSummaryText("Here are the promos for you!"))
                        .setContentText("Here are the promos for you!")
                        .setLargeIcon(icon);

        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.setAutoCancel(true);
        mNotificationManager.notify(0, mBuilder.build());
    }

    /**
     * Maps geofence transition types to their human-readable equivalents.
     *
     * @param transitionType    A transition type constant defined in Geofence
     * @return                  A String indicating the type of transition
     */
    private String getTransitionString(int transitionType) {
        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                return "getString(R.string.geofence_transition_entered)";
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                return "getString(R.string.geofence_transition_exited)";
            default:
                return "getString(R.string.unknown_geofence_transition)";
        }
    }
}