package co.kartoo.app.landing;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import co.kartoo.app.MainActivity_;
import co.kartoo.app.R;
import co.kartoo.app.models.LoginPref_;
import co.kartoo.app.rest.MainService;
import co.kartoo.app.rest.model.LoginCredentials;
import co.kartoo.app.rest.model.ResponseLogin;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;


@EActivity(R.layout.activity_landing) //artinya pakai layout ini

public class LandingActivity extends FragmentActivity
        //GoogleApiClient.ConnectionCallbacks,
        //GoogleApiClient.OnConnectionFailedListener
{

    //@ViewById(R.id.mBtnSigninFb)
    //ImageView mBtnSigninFb;

    //@ViewById
    //ImageView mBtnSigninGoogle;

    @ViewById
    ViewPager mVPlanding;

    @ViewById(R.id.mIVindicator1)
    View mVindicator1;

    @ViewById(R.id.mIVindicator2)
    View mVindicator2;

    @ViewById(R.id.mIVindicator3)
    View mVindicator3;

    @ViewById(R.id.mIVindicator4)
    View mVindicator4;

    @ViewById
    Button mButtonJoinNow;
    @ViewById
    RelativeLayout relativeLayout;
    @ViewById
    LinearLayout linearLayout4;
    @ViewById
    RelativeLayout relativeLayout2;
    @ViewById
    TextView mButtonLogin;
    @ViewById
    TextView mTVskiplogin;

    @Pref
    LoginPref_ loginPref;

    ProgressDialog loadingDialog;

    static final int RC_SIGN_IN = 0;
    GoogleApiClient mGoogleApiClient;
    boolean mIntentInProgress;
    boolean mSignInClicked;
    ConnectionResult mConnectionResult;

    CallbackManager callbackManager;
    LoginManager loginManager;
    Context mContext;
    String mixby;


    @Override
    protected void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
    }

    //@Click(R.id.mIVindicator1)
    //void mVindicator1() {
        //mVPlanding.setCurrentItem(0);
    //}
    @Click(R.id.mIVindicator2)
    void mVindicator2() {
        mVPlanding.setCurrentItem(1);
    }
    @Click(R.id.mIVindicator3)
    void mVindicator3() {
        mVPlanding.setCurrentItem(2);
    }
    @Click(R.id.mIVindicator4)
    void mVindicator4() {
        mVPlanding.setCurrentItem(3);
    }

    public void nextPage() {
        mVPlanding.setCurrentItem(mVPlanding.getCurrentItem() + 1);
    }

    @Override
    public void onBackPressed() {
//        if (mVPlanding.getCurrentItem() == 0) {
        super.onBackPressed();
//        } else {
//            mVPlanding.setCurrentItem(mVPlanding.getCurrentItem() - 1);
//        }
    }

    @AfterViews
    void init() {
        String projectToken = getString(R.string.mixpanel_token);
        MixpanelAPI mixpanel = MixpanelAPI.getInstance(this, projectToken);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTimeStamp = dateFormat.format(new Date());
        try {
            JSONObject props = new JSONObject();
            props.put("TimeStamp", currentTimeStamp);
            mixpanel.track("Landing Page", props);
            Log.e("MYAPP", "landing"+"landing");

        } catch (JSONException e) {
            Log.e("MYAPP", "Unable to add properties to JSONObject", e);
        }

        mVPlanding.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        mVPlanding.setOnPageChangeListener(new SplashPageChangeListener());
        updateIndicators(0);

        Log.e("landing", "landing" + loginPref.token().get());

        if (!(loginPref.token().get() == null || loginPref.token().get().equals(""))) {
            Intent intent = new Intent(LandingActivity.this, MainActivity_.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        loadingDialog = new ProgressDialog(this);
        loadingDialog.setMessage("Loading...");
        loadingDialog.setIndeterminate(true);
        loadingDialog.setCancelable(false);
        mContext = getApplicationContext();
    }

    @Click({R.id.mButtonLogin})
    public void setmButtonLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

    }


    @Click({R.id.mButtonJoinNow})
    public void setmButtonJoinNowClick() {
        Intent intent = new Intent(this, JoinNowActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    String APIkey = "exxAFFEyJOgPDZFAvPBFGcaIugSNhy49";
    String api = "api-key" + ":" + APIkey;
    final String basic = "Basic " + Base64.encodeToString(api.getBytes(), Base64.NO_WRAP);

    @Click({R.id.mTVskiplogin})
    public void setmTVskiplogin(){
        loadingDialog.show();
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://kartoo-dev.azure-mobile.net/").addConverterFactory(GsonConverterFactory.create()).build();
        final MainService service = retrofit.create(MainService.class);
        LoginCredentials credentials = new LoginCredentials("guest", "", "");
        Call<ResponseLogin> loginResponseCall = service.doLogin(basic, credentials);
        loginResponseCall.enqueue(new Callback<ResponseLogin>() {
            @Override
            public void onResponse(Response<ResponseLogin> response, Retrofit retrofit) {
                if (response.code()==200) {
                    mixby = "guest";

                    ResponseLogin responseServerBody = response.body();
                    String token = responseServerBody.getTokenValue();
                    String name = responseServerBody.getName();

                    Intent intent = new Intent(LandingActivity.this, MainActivity_.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    loginPref.type().put("guest");
                    intent.putExtra("name", name);
                    intent.putExtra("auth", token);
                    mixpanel();
                    loadingDialog.dismiss();
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(LandingActivity.this, "Trouble here, please try again later", Toast.LENGTH_LONG).show();
                loadingDialog.dismiss();
            }
        });
    }

    public void mixpanel(){
        String projectToken = getString(R.string.mixpanel_token);
        MixpanelAPI mixpanel = MixpanelAPI.getInstance(this, projectToken);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTimeStamp = dateFormat.format(new Date());
        try {
            JSONObject props = new JSONObject();
            props.put("TimeStamp", currentTimeStamp);
            if (mixby.equals("guest")){
                mixpanel.track("Log in as guest", props);
                Log.e("MYAPP", "Unable to add properties to JSONObject"+"guest");
            }

        } catch (JSONException e) {
            Log.e("MYAPP", "Unable to add properties to JSONObject", e);
        }
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private int mPageCount = 3;

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            LandingSplashFragment fragment = new LandingSplashFragment_();
            Bundle args = new Bundle();
            args.putInt(LandingSplashFragment.POSITION_KEY, position);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return mPageCount;
        }

    }


    private class SplashPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int position) {
        }

        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            updateIndicators(position);
        }

    }



    public void updateIndicators(int position) {
        switch (position) {
            case 0:
                mVindicator1.setBackgroundResource(R.drawable.ic_indicator_active);
                mVindicator1.requestLayout();

                mVindicator2.setBackgroundResource(R.drawable.ic_indicator_inactive);
                mVindicator2.requestLayout();

                mVindicator3.setBackgroundResource(R.drawable.ic_indicator_inactive);
                mVindicator3.requestLayout();

                //mVindicator4.setBackgroundResource(R.drawable.ic_indicator_inactive);
                //mVindicator4.requestLayout();
                break;

            case 1:
                mVindicator1.setBackgroundResource(R.drawable.ic_indicator_inactive);
                mVindicator1.requestLayout();

                mVindicator2.setBackgroundResource(R.drawable.ic_indicator_active);
                mVindicator2.requestLayout();

                mVindicator3.setBackgroundResource(R.drawable.ic_indicator_inactive);
                mVindicator3.requestLayout();

                //mVindicator4.setBackgroundResource(R.drawable.ic_indicator_inactive);
                //mVindicator4.requestLayout();
                break;

            case 2:
                mVindicator1.setBackgroundResource(R.drawable.ic_indicator_inactive);
                mVindicator1.requestLayout();

                mVindicator2.setBackgroundResource(R.drawable.ic_indicator_inactive);
                mVindicator2.requestLayout();
                mVindicator3.setBackgroundResource(R.drawable.ic_indicator_active);
                mVindicator3.requestLayout();

                //mVindicator4.setBackgroundResource(R.drawable.ic_indicator_inactive);
                //mVindicator4.requestLayout();
                break;
            /*
            case 3:
                mVindicator1.setBackgroundResource(R.drawable.ic_indicator_inactive);
                mVindicator1.requestLayout();

                mVindicator2.setBackgroundResource(R.drawable.ic_indicator_inactive);
                mVindicator2.requestLayout();
                mVindicator3.setBackgroundResource(R.drawable.ic_indicator_inactive);
                mVindicator3.requestLayout();
                mVindicator4.setBackgroundResource(R.drawable.ic_indicator_active);
                mVindicator4.requestLayout();
                break;
                */
        }


    }

}
