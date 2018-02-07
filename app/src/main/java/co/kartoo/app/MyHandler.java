package co.kartoo.app;

/**
 * Created by Luthfi Apriyanto on 4/4/2016.
 */

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.microsoft.windowsazure.notifications.NotificationsHandler;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import co.kartoo.app.category.ActivityListPromoCategory_;
import co.kartoo.app.newsletter.NewsNotificationActivity;
import co.kartoo.app.newsletter.ReadNotificationActivity;
import co.kartoo.app.newsletter.ReadNotificationActivity_;
import co.kartoo.app.rest.MainService;
import co.kartoo.app.rest.PromoService;
import co.kartoo.app.rest.model.ResponseDefault;
import co.kartoo.app.rest.model.newest.DiscoverPromotionCategory;
import co.kartoo.app.search.SearchActivity_;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class MyHandler extends NotificationsHandler {
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    Context ctx;
    Bitmap image;

    String urlImage, keyvalue, keyword, nhMessage, token, query, notificationid;
    Intent resultIntent;
    TaskStackBuilder stackBuilder;

    Retrofit retrofit;
    PromoService promoService;
    static public MainActivity mainActivity;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    public void onReceive(Context context, Bundle bundle) {
        ctx = context;
        nhMessage = bundle.getString("message");
        urlImage = bundle.getString("url_img");
        keyvalue = bundle.getString("keyvalue");
        keyword = bundle.getString("keyword");
        notificationid = bundle.getString("notificationid");

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(ctx);

        new DownloadWebPageTask().execute(urlImage);

        mixpanel();

        retrofit = new Retrofit.Builder().baseUrl(ctx.getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
        promoService = retrofit.create(PromoService.class);

        SharedPreferences bb = ctx.getSharedPreferences("my_prefs", 0);
        token = bb.getString("MID", null);

        MainService mainService = retrofit.create(MainService.class);
        Call<ResponseDefault> sendNotifId = mainService.sendNotifId(token, notificationid);
        sendNotifId.enqueue(new Callback<ResponseDefault>() {
            @Override
            public void onResponse(Response<ResponseDefault> response, Retrofit retrofit) {
                if(response.code() == 200) {
                    Log.e("TAG", "notificationid: "+response.body());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("TAG", "responselenght: "+"FAIL");
            }
        });
    }

    private class DownloadWebPageTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {
            try {
                URL url = new URL(urlImage);
                image = BitmapFactory.decodeStream(url.openStream());

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("TAG", "doInBackground: "+"FAIL" );
            }

            Log.e("TAG", "keyword: "+ keyword );
            if (keyword.equals("New Promotions")){
                Log.e("TAG", "token: "+ token );
                Call<ArrayList<DiscoverPromotionCategory>> searchCall = promoService.searchAll(token,keyword,"","0","0");
                searchCall.enqueue(new Callback<ArrayList<DiscoverPromotionCategory>>() {
                    @Override
                    public void onResponse(Response<ArrayList<DiscoverPromotionCategory>> response, Retrofit retrofit) {
                        if (response.isSuccess()) {
                            if(response.code() == 200) {
                                Log.e("TAG", "responselenght: "+response.body().size() );
                                if(response.body().size()!=0){
                                    sendNotification(nhMessage);
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Log.e("TAG", "responselenght: "+"FAIL");
                    }
                });
            }
            else {
                sendNotification(nhMessage);
                Log.e("TAG", "responselenght: "+"SEND");

            }
            return image;
        }
        protected void onPostExecute(String result) {
        }
    }

    private void sendNotification(String msg) {
        mNotificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        Bitmap icon = BitmapFactory.decodeResource(ctx.getResources(), R.mipmap.ic_launcher);

        if (keyvalue.equals("search")){
            resultIntent = new Intent(ctx, SearchActivity_.class);
            stackBuilder = TaskStackBuilder.create(ctx);
            stackBuilder.addParentStack(SearchActivity_.class);
            stackBuilder.addNextIntent(resultIntent);
            resultIntent.putExtra("query", keyword);
        }
        else if(keyvalue.equals("category")){
            resultIntent = new Intent(ctx, ActivityListPromoCategory_.class);
            stackBuilder = TaskStackBuilder.create(ctx);
            stackBuilder.addParentStack(ActivityListPromoCategory_.class);
            stackBuilder.addNextIntent(resultIntent);
            resultIntent.putExtra("id", keyword);
            resultIntent.putExtra("name", "Promotions");
        }
        else if(keyvalue.equals("promo")){
            resultIntent = new Intent(ctx, DetailPromoActivity.class);
            stackBuilder = TaskStackBuilder.create(ctx);
            stackBuilder.addParentStack(DetailPromoActivity.class);
            stackBuilder.addNextIntent(resultIntent);
            resultIntent.putExtra("Id", keyword);
            resultIntent.putExtra("from", "notification");
        }
        else if(keyvalue.equals("newsletter")) {
            resultIntent = new Intent(ctx, ReadNotificationActivity_.class);
            stackBuilder = TaskStackBuilder.create(ctx);
            stackBuilder.addParentStack(ReadNotificationActivity_.class);
            stackBuilder.addNextIntent(resultIntent);
            resultIntent.putExtra("id", keyword);
            //resultIntent.putExtra("from", "notification");
        }
        else {
            resultIntent = new Intent(ctx, MainActivity_.class);
            stackBuilder = TaskStackBuilder.create(ctx);
            stackBuilder.addParentStack(MainActivity_.class);
            stackBuilder.addNextIntent(resultIntent);
        }

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Log.e("TAG", "bigPicture1: "+image);
        NotificationCompat.Builder mBuilder;

        if (image == null){
            mBuilder =
                    new NotificationCompat.Builder(ctx)
                            .setSmallIcon(R.drawable.icon_notif)
                            .setContentTitle("Kartoo")
                            .setStyle(new NotificationCompat.BigTextStyle())
                            .setContentText(msg)
                            .setLargeIcon(icon);
        }
        else {
            mBuilder =
                    new NotificationCompat.Builder(ctx)
                            .setSmallIcon(R.drawable.icon_notif)
                            .setContentTitle("Kartoo")
                            .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(image).setSummaryText(msg))
                            .setContentText(msg)
                            .setLargeIcon(icon);
        }

        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.setAutoCancel(true);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    public void mixpanel(){
        String projectToken = ctx.getString(R.string.mixpanel_token);
        MixpanelAPI mixpanel = MixpanelAPI.getInstance(ctx, projectToken);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTimeStamp = dateFormat.format(new Date());
        try {
            JSONObject props = new JSONObject();
            props.put("TimeStamp", currentTimeStamp);
            props.put("KeyValue", keyvalue);
            props.put("KeyWord", keyword);
            mixpanel.track("Notification Received", props);
        } catch (JSONException e) {
        }
    }

    public void firebase(){
        Bundle params = new Bundle();
        params.putString("key_value", keyvalue);
        params.putString("key_word", keyword);
        mFirebaseAnalytics.logEvent("notification_received", params);
    }
}