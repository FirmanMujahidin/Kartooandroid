package co.kartoo.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.sharedpreferences.Pref;

import co.kartoo.app.models.LoginPref_;

/**
 * Created by firma on 27-Apr-17.
 */

@EActivity(R.layout.activity_popup_number)
public class PopupNumberActivity extends AppCompatActivity {

    @Pref
    LoginPref_ loginPref;

    public void init() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int widht = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(widht*.7),(int)(height*.4));

    }

    @Click(R.id.BtnSkip)
    public void onClikSkip(View view){
        final SharedPreferences counterPref = getSharedPreferences("verifyCounterPref", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editCounter = counterPref.edit();
        editCounter.putInt("counterKey", 0);
        editCounter.apply();

        onBackPressed();
    }

    @Click(R.id.BtnVerify)
    public void onClikVerify(View view){

        Intent intent = new Intent(PopupNumberActivity.this, EditProfile_.class);
        intent.putExtra("token", loginPref.token().get());
        intent.putExtra("email", loginPref.email().get());

        if (loginPref.urlPhoto().get() != null) {
            intent.putExtra("image", loginPref.urlPhoto().get());
        }

        if (loginPref.name().get() != null) {
            intent.putExtra("name", loginPref.name().get());
        }

        startActivity(intent);
        finish();
    }
}
