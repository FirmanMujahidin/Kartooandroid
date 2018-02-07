package co.kartoo.app;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.androidannotations.annotations.EService;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.Calendar;

import co.kartoo.app.models.LoginPref_;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by MartinOenang on 11/13/2015.
 */
@EService
public class DinningAlarmService extends Service {
    @Pref
    LoginPref_ loginPref;
    LocationManager locationManager;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void getDiningPromo() {
        Calendar c = Calendar.getInstance();
        int time = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        Log.e("TAG", "AlarmService: " + time);
        Log.e("TAG", "AlarmService: " + minute);

        if (time == 23 && minute == 59){
            SharedPreferences prefs2 = getSharedPreferences("saving_mall", 0);
            SharedPreferences.Editor edit = prefs2.edit();
            edit.clear();
            edit.apply();

        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        getDiningPromo();
        return START_STICKY;
    }
}
