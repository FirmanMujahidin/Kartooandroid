package co.kartoo.app;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.plus.Plus;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.microsoft.windowsazure.messaging.NotificationHub;
import com.microsoft.windowsazure.notifications.NotificationsManager;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.kartoo.app.drawer.DrawerViewActivity_;
import co.kartoo.app.forgotpassword.ChangeYourPassword_;
import co.kartoo.app.landing.LandingActivity_;
import co.kartoo.app.models.LoginPref_;
import co.kartoo.app.models.ProfilePref_;
import co.kartoo.app.nearby.GPSTracker;
import co.kartoo.app.notifications.GeofenceTransitionsIntentService;
import co.kartoo.app.rest.CardService;
import co.kartoo.app.rest.PromoService;
import co.kartoo.app.rest.model.ResponseDefault;
import co.kartoo.app.rest.model.newest.DiscoverPromotionCategory;
import co.kartoo.app.rest.model.newest.MallGeofence;
import co.kartoo.app.rest.model.newest.Notifications;
import co.kartoo.app.rest.model.newest.Tags;
import co.kartoo.app.search.SearchActivity_;
import co.kartoo.app.tabfragment.TabAdapter;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;


@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, ResultCallback<LocationSettingsResult> {
    public static final double Rad = 6372.8; // In kilometers
    private static final String BACKEND_ENDPOINT = "http://kartoo-dev.azure-mobile.net/";
    private static Boolean isVisible = false;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    public static final int REQUEST_CODE_FOR_SMS = 1;
    protected ArrayList<Geofence> mGeofenceList;
    protected LocationRequest locationRequest;
    @ViewById(R.id.mFLcontainer)
    FrameLayout mFLcontainer;
    @ViewById
    Toolbar mToolbar;
    @ViewById
    DrawerLayout mDLdrawer;
    @ViewById
    NavigationView mNVdrawer;
    @ViewById(R.id.mVPmain)
    ViewPager mVPmain;
    @ViewById
    TabLayout mTLtab;
    @ViewById
    FrameLayout mFLsearch;
    @ViewById
    EditText mETsearch;
    //    @ViewById
//    PagerTabStrip mPTStab;
    @ViewById
    LinearLayout mLLsearch;
    @ViewById
    ImageView toolbar_title;
    @ViewById
    TextView toolbar_title_text;
    @ViewById
    ImageView mIVsearchClear;
    @ViewById
    ImageView mIVsearch;
    @ViewById
    AppBarLayout tabanim_appbar;
    @ViewById
    LinearLayout All;
    @ViewById
    Button one;
    @ViewById
    Button two;
    @ViewById
    Button three;
    @ViewById
    Button four;
    TabAdapter tabAdapter;
    Retrofit retrofit, retrofit1;
    PromoService promoService;
    CardService cardService;
    PromoService mainService;
    String token;
    ArrayList<DiscoverPromotionCategory> listCategoryOutlet;
    View headerView;
    @Pref
    LoginPref_ loginPref;
    @Pref
    ProfilePref_ profilePref;
    Bitmap bitmap;
    GoogleApiClient mGoogleApiClient, mGoogleApiClientLocation;
    String authorization;
    PagerAdapter TabAdapter;
    Tags tag;
    ArrayList<String> mallId;
    ArrayList<String> mallName;
    ArrayList<String> mallLat;
    ArrayList<String> mallLong;
    ArrayList<String> mallImg;
    ArrayList<String> list;
    double[] distance;
    String[] mallLatFinal;
    String[] mallLongFinal;
    String[] mallNameFinal;
    GPSTracker gps;
    LatLng myLocation;
    int limitLoop;
    boolean popUpReview;
    int REQUEST_CHECK_SETTINGS = 100;
    private String SENDER_ID = "136945608372";
    private GoogleCloudMessaging gcm;
    private NotificationHub hub;
    private String HubName = "kartoo-devhub";
    private String HubListenConnectionString = "Endpoint=sb://kartoo-devhub-ns.servicebus.windows.net/;SharedAccessKeyName=DefaultListenSharedAccessSignature;SharedAccessKey=CZvooSmhFWdvyyEmSbVUk48j9HCHIMet/0StvPRbbTk=;EntityPath=kartoo-devhub";
    private RegisterClient registerClient;
    private PendingIntent mGeofencePendingIntent;
    private FirebaseAnalytics mFirebaseAnalytics;

    @AfterViews
    public void init() {

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        //check();
        mGoogleApiClientLocation = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        Log.e("TAG", "init: " + mGoogleApiClientLocation);
        mGoogleApiClientLocation.connect();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();
        Log.e("TAG", "init: " + mGoogleApiClient);
        mGoogleApiClient.connect();
        Log.e("TAG", "loginPref: " + loginPref.token().get());

        mVPmain.setOffscreenPageLimit(4);

        ArrayList<String> list = new ArrayList<String>();
        list.add("");

        View view = findViewById(R.id.mTLtab);
        //myLocation = getCurrentLocation();

        popUpReview = false;

        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(100); // half second between each showcase view
        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, "49");
        sequence.setConfig(config);

        sequence.addSequenceItem(new MaterialShowcaseView.Builder(this)
                .setTarget(one)
                .setDismissOnTouch(true)
                .setTitleText("Home")
                .setContentText(Html.fromHtml(this.getString(R.string.home)))
                .setMaskColour(getResources().getColor(R.color.transparent))
                .setContentTextColor(getResources().getColor(R.color.white))
                .build()
        );
        sequence.addSequenceItem(new MaterialShowcaseView.Builder(this)
                .setTarget(two)
                .setDismissOnTouch(true)
                .setTitleText("Categories")
                .setMaskColour(getResources().getColor(R.color.transparent))
                .setContentText("Browse through different promotion\ncategories")
                .build());
        sequence.addSequenceItem(new MaterialShowcaseView.Builder(this)
                .setTarget(three)
                .setDismissOnTouch(true)
                .setTitleText("Locator")
                .setMaskColour(getResources().getColor(R.color.transparent))
                .setContentText("Find promotions based on location")
                .build());
        sequence.addSequenceItem(new MaterialShowcaseView.Builder(this)
                .setTarget(four)
                .setDismissOnTouch(true)
                .setTitleText("Mall")
                .setMaskColour(getResources().getColor(R.color.transparent))
                .setContentText("Find promotions based on mall")
                .build());
        sequence.start();

        //GeofenceController.getInstance().init(this);

        MyHandler.mainActivity = this;
        NotificationsManager.handleNotifications(this, SENDER_ID, MyHandler.class);

        gcm = GoogleCloudMessaging.getInstance(this);
        hub = new NotificationHub(HubName, HubListenConnectionString, this);
        registerWithNotificationHubs();
        registerClient = new RegisterClient(this, BACKEND_ENDPOINT);

        authorization = loginPref.token().get();
        retrofit = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).addConverterFactory(new ToStringConverterFactory()).build();
        cardService = retrofit.create(CardService.class);

        retrofit1 = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
        mainService = retrofit1.create(PromoService.class);
/*
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addApi(LocationServices.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();
        Log.e("TAG", "init: "+mGoogleApiClient);
        mGoogleApiClient.connect();
*/
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);

        Intent intent = getIntent();
        String origin = intent.getStringExtra("FromEditProfile");

        if (origin == null || origin.equals("")) {
            if (loginPref.email().get() == null || loginPref.email().get().equals("")) {
                final String email = intent.getStringExtra("email");
                loginPref.email().put(email);
            }
            if (loginPref.name().get() == null || loginPref.name().get().equals("")) {
                final String name = intent.getStringExtra("name");
                loginPref.name().put(name);
            }
            if (loginPref.token().get() == null || loginPref.token().get().equals("")) {
                final String token = intent.getStringExtra("auth");
                loginPref.token().put(token);
            }
            if (loginPref.type().get() == null || loginPref.type().get().equals("")) {
                final String type = intent.getStringExtra("type");
                loginPref.type().put(type);
            }
            if (loginPref.urlPhoto().get() == null || loginPref.urlPhoto().get().equals("")) {
                final String urlPhoto = intent.getStringExtra("urlPhoto");
                loginPref.urlPhoto().put(urlPhoto);
            }

            if (loginPref.uid().get() == null || loginPref.uid().get().equals("")) {
                if ((intent.getStringExtra("uidGplus") == null || intent.getStringExtra("uidGplus").equals(""))) {
                    final String uidGplus = intent.getStringExtra("uidGplus");
                    loginPref.uid().put(uidGplus);
                }
                if ((intent.getStringExtra("uidFacebook") == null || intent.getStringExtra("uidFacebook").equals(""))) {
                    final String uidFacebook = intent.getStringExtra("uidFacebook");
                    loginPref.uid().put(uidFacebook);
                }
            }
        } else {
            final String name = intent.getStringExtra("name");
            loginPref.name().put(name);
            bitmap = intent.getParcelableExtra("BitmapImage");
            if (bitmap != null) {
                loginPref.urlPhoto().put(BitMapToString(bitmap));
                loginPref.fromBitmap().put(1);
                Log.e("TAG", "urlImage: " + loginPref.urlPhoto().get());
            }
        }

        if (!loginPref.type().get().equals("guest")) {
            if (!loginPref.interest().get()) {
                Intent intent5 = new Intent(MainActivity.this, InterestActivity_.class);
                intent5.putExtra("from", "MainActivity");
                startActivity(intent5);
            }
        }

        Call<String> notification = cardService.notifiication(authorization, loginPref.regid().get());
        notification.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                if (response.code() == 200) {
                    String notif = response.body();
                    notif = notif.substring(1, notif.length() - 1);
                    loginPref.notifID().put(notif);
                    putgcm();
                } else if (response.code() == 401) {
                    //doSignin();
                }
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });

        String projectToken = getString(R.string.mixpanel_token);
        MixpanelAPI mixpanel = MixpanelAPI.getInstance(this, projectToken);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTimeStamp = dateFormat.format(new Date());
        try {
            JSONObject props = new JSONObject();
            props.put("TimeStamp", currentTimeStamp);
            props.put("email", loginPref.email().get());
            if (popUpReview) {
                props.put("PopUpReview", popUpReview);
            }
            mixpanel.track("Home Page", props);
        } catch (JSONException e) {
        }
        Bundle params = new Bundle();
        params.putString("email", loginPref.email().get());
        if (popUpReview) {
            params.putString("start_pop_up_review", popUpReview + "");
        }
        mFirebaseAnalytics.logEvent("home_page", params);
        mFirebaseAnalytics.setUserProperty("email", loginPref.email().get());

        MixpanelAPI.People people = mixpanel.getPeople();
        people.identify(loginPref.email().get());
        mixpanel.getPeople().identify(loginPref.email().get());
        mixpanel.getPeople().set("$email", loginPref.email().get());
        people.initPushHandling("136945608372");

        boolean mustChangePassword = intent.getBooleanExtra("mustChangePassword", false);
        if (mustChangePassword) {
            Intent intent2 = new Intent(MainActivity.this, ChangeYourPassword_.class);
            intent2.putExtra("email", loginPref.email().get());
            intent2.putExtra("token", loginPref.token().get());
            startActivity(intent2);
        }

        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
        setUpNavDrawer();
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        headerView = LayoutInflater.from(this).inflate(R.layout.drawer_header, null);

        ImageView mIVprofilePicture = (ImageView) headerView.findViewById(R.id.mIVprofilePicture);
        TextView mTVname = (TextView) headerView.findViewById(R.id.mTVname);
        ProgressBar mPBlevel = (ProgressBar) headerView.findViewById(R.id.mPBlevel);
        mNVdrawer.addHeaderView(headerView);
        try {
            mTVname.setText("Hi, " + loginPref.name().get().split(" ")[0]);
        } catch (ArrayIndexOutOfBoundsException e) {
            mTVname.setText("Hi, " + loginPref.name().get());
        }
        if (!loginPref.poin().get().equals("")) {
            mPBlevel.setProgress(Integer.parseInt(loginPref.poin().get()));
        } else {
            mPBlevel.setProgress(0);
        }
        if (loginPref.urlPhoto().get() == null || loginPref.urlPhoto().get().equals("")) {
            //mIVprofilePicture.setImageBitmap(bitmap);
        } else {
            if (loginPref.fromBitmap().get() == 1) {
                mIVprofilePicture.setImageBitmap(StringToBitMap(loginPref.urlPhoto().get()));
            } else {
                mIVprofilePicture.setBackground(null);
                Picasso.with(this).load(loginPref.urlPhoto().get())
                        .resize(300, 300)
                        .centerInside()
                        .onlyScaleDown()
                        .into(mIVprofilePicture);
            }
        }

        mNVdrawer.setItemIconTintList(null);
        mNVdrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Bundle bundle = new Bundle();
                Intent intent = new Intent(MainActivity.this, DrawerViewActivity_.class);
                switch (menuItem.getItemId()) {
//                    case R.id.drawer_my_feed:
//                        User user = new User();
//                        user.setId(loginPref.uid().get() + "");
//                        intent = new Intent(MainActivity.this, ActivityDetailFriends_.class);
//                        bundle.putSerializable("selectedUser", user);
//                        intent.putExtras(bundle);
//                        startActivity(intent);
//                        break;

                    case R.id.bank_page:
                        bundle.putString("selectedPage", "bankPage");
                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;

                    case R.id.drawer_bookmark:
                        if (loginPref.type().get().equals("guest")) {
                            dialog();
                            break;
                        } else {
                            bundle.putString("selectedPage", "editprofile");
                            intent.putExtras(bundle);
                            startActivity(intent);
                            break;
                        }


//                    case R.id.drawer_banks:
//                        bundle.putString("selectedPage", "banks");
//                        intent.putExtras(bundle);
//                        startActivity(intent);
//                        break;
                    case R.id.drawer_liked_promo:
                        if (loginPref.type().get().equals("guest")) {
                            dialog();
                            break;
                        } else {
                            bundle.putString("selectedPage", "likedPromo");
                            intent.putExtras(bundle);
                            startActivity(intent);
                            break;
                        }

                    case R.id.drawer_notif:
                        if (loginPref.type().get().equals("guest")) {
                            dialog();
                            break;
                        } else {
                            bundle.putString("selectedPage", "notif");
                            intent.putExtras(bundle);
                            startActivity(intent);
                            break;
                        }
//                    case R.id.drawer_friends:
//                        bundle.putString("selectedPage", "friends");
//                        intent.putExtras(bundle);
//                        startActivity(intent);
//                        break;
//                    case R.id.drawer_credit_card:
//                        if (loginPref.type().get().equals("guest")) {
//                            dialog();
//                            break;
//                        } else {
//                            bundle.putString("selectedPage", "creditCard");
//                            intent.putExtras(bundle);
//                            startActivity(intent);
//                            break;
//                        }

                    case R.id.drawer_rate:
                        String url = "http://app.kartoo.co/";
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                        break;

                    case R.id.contact_us_title:
                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + "support@kartoo.co"));
                        startActivity(Intent.createChooser(emailIntent, "Chooser Title"));
                        break;

                    case R.id.drawer_my_cards:
                        if (loginPref.type().get().equals("guest")) {
                            dialog();
                            break;
                        } else {
                            bundle.putString("selectedPage", "my_cards");
                            intent.putExtras(bundle);
                            startActivity(intent);
                            break;
                        }

//                    case R.id.drawer_search:
//                        bundle.putString("selectedPage", "search");
//                        intent.putExtras(bundle);
//                        startActivity(intent);
//                        break;
//                    case R.id.drawer_settings:
//                        bundle.putString("selectedPage", "settings");
//                        intent.putExtras(bundle);
//                        startActivity(intent);
//                        break;
                    case R.id.drawer_about:
                        bundle.putString("selectedPage", "about");
                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;
//                    case R.id.drawer_leaderboard:
//                        bundle.putString("selectedPage", "leaderboard");
//                        intent.putExtras(bundle);
//                        startActivity(intent);
//                        break;
                    case R.id.drawer_logout:
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                        alertDialogBuilder.setMessage("Are you sure?");
                        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.e("sebelum", "sebelum" + loginPref.name().get());
                                loginPref.name().remove();
                                loginPref.email().remove();
                                loginPref.token().remove();
                                loginPref.urlPhoto().remove();
                                loginPref.level().remove();
                                loginPref.uid().remove();
                                loginPref.type().remove();
                                loginPref.fromBitmap().remove();
                                loginPref.addcard().remove();
                                loginPref.regid().remove();
                                loginPref.notifID().remove();
                                loginPref.interest().remove();
                                // LoginManager.getInstance().logOut();

                                final SharedPreferences verifyCounterPref = getSharedPreferences("verifyCounterPref", Context.MODE_PRIVATE);
                                final SharedPreferences.Editor verifyEditCounter = verifyCounterPref.edit();

                                verifyEditCounter.putInt("counterKey", 0);
                                verifyEditCounter.apply();

                                FacebookSdk.sdkInitialize(getApplicationContext());
                                LoginManager.getInstance().logOut();
                                if (mGoogleApiClient.isConnected()) {
                                    Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                                    mGoogleApiClient.disconnect();
                                    Log.e("asdf", "logged out");
                                }
                                Log.e("sesudah", "sesudah" + loginPref.name().get());
                                Intent intent2 = new Intent(MainActivity.this, LandingActivity_.class);
                                startActivity(intent2);
                                finish();
                            }
                        });
                        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                }
                mDLdrawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        mETsearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Log.e("TAG", "onEditorAction: " + v.getText().toString());
                    search(v.getText().toString());
                    mETsearch.clearFocus();
                    return true;
                }
                return false;
            }
        });

        Log.e("TAG", "GeoFenceBefore: " + mGeofenceList);

        final int[] icons = new int[]{
                R.drawable.first_tab,
                R.drawable.second_tab,
                R.drawable.third_tab,
                R.drawable.fourth_tab,
        };

        final String[] title = new String[]{"", "Categories", "Nearby", "Malls"};

        for (int i = 0; i < 4; i++) {
            mTLtab.addTab(mTLtab.newTab().setIcon(icons[i]));
        }

        mTLtab.setTabGravity(TabLayout.GRAVITY_FILL);

        String category = intent.getStringExtra("category");
        Log.e("TAG", "find our category: " + category);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.mVPmain);
        TabAdapter = new TabAdapter(getSupportFragmentManager(), mTLtab.getTabCount());
        viewPager.setAdapter(TabAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTLtab));

        mTLtab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                invalidateOptionsMenu();
                viewPager.setCurrentItem(tab.getPosition());
                TabAdapter.notifyDataSetChanged();
                toolbar_title.setVisibility(View.VISIBLE);
                toolbar_title_text.setVisibility(View.GONE);

                mLLsearch.setVisibility(View.GONE);
                mFLsearch.setVisibility(View.VISIBLE);
                mETsearch.setText("");
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mETsearch.getWindowToken(), 0);

                if (tab.getPosition() != 0) {
                    toolbar_title.setVisibility(View.GONE);
                    toolbar_title_text.setVisibility(View.VISIBLE);
                    toolbar_title_text.setText(title[tab.getPosition()]);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        Intent intent1 = new Intent(this, DinningAlarmService_.class);
        this.startService(intent1);
    }

    public void callMallNotification() {
        //Log.e("TAG", "mGoogleApiClient: "+ mGoogleApiClient.isConnected());

        mGeofenceList = new ArrayList<Geofence>();
        mGeofencePendingIntent = null;
        retrofit1 = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
        mainService = retrofit1.create(PromoService.class);

        Call<ArrayList<MallGeofence>> getMall = mainService.mall(authorization);
        getMall.enqueue(new Callback<ArrayList<MallGeofence>>() {
            @Override
            public void onResponse(Response<ArrayList<MallGeofence>> response, Retrofit retrofit) {
                Log.e("TAG", "namaMall: " + response.code());
                if (response.code() == 200) {
                    int a = response.body().size();
                    mallId = new ArrayList<String>();
                    mallName = new ArrayList<String>();
                    mallLat = new ArrayList<String>();
                    mallLong = new ArrayList<String>();
                    mallImg = new ArrayList<String>();

                    Log.e("TAG", "Jumlah: " + a);

                    for (int i = 0; i < a; i++) {
                        mallId.add(response.body().get(i).getMallId());
                        mallName.add(response.body().get(i).getMallName());
                        mallLat.add(response.body().get(i).getLatitude());
                        mallLong.add(response.body().get(i).getLongitude());
                        mallImg.add(response.body().get(i).getUrl_img());
                    }

                    if (a != 0) {
                        if (a > 99) {
                            limitLoop = 100;
                        } else {
                            limitLoop = a;
                        }

                        Log.e("TAG", "limitLoop: " + limitLoop);

                        Float radius = Float.parseFloat("0.2") * 1000.0f;

                        for (int i = 0; i < limitLoop; i++) {
                            mGeofenceList.add(new Geofence.Builder()
                                    .setRequestId(mallLat.get(i))
                                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_DWELL)
                                    .setCircularRegion(Double.parseDouble(mallLat.get(i)), Double.parseDouble(mallLong.get(i)), radius)
                                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
                                    .setLoiteringDelay(180000)
                                    .build());
                        }
                    }
                    Log.e("TAG", "mGoogleApiClient: " + mGoogleApiClient.isConnected());
                    Log.e("TAG", "GeoFence: " + mGeofenceList);

                    if (mGoogleApiClientLocation.isConnected()) {
                        if (mGeofenceList.size() != 0) {
                            LocationServices.GeofencingApi.addGeofences(mGoogleApiClientLocation,
                                    getGeofencingRequest(),
                                    getGeofencePendingIntent());
                            Log.e("TAG", "LocationServices: " + LOCATION_SERVICE);
                        }
                    }

                    Log.e("TAG", "namaMall: " + mallName);
                    Log.e("TAG", "mallLat: " + mallLat);
                    Log.e("TAG", "mallLong: " + mallLong);
                    sharedClass();
                } else if (response.code() == 401) {
                    //doSignin();
                }

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_DWELL);
        builder.addGeofences(mGeofenceList);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public void putgcm() {
        cardService = retrofit1.create(CardService.class);
        Notifications credential = new Notifications("gcm", loginPref.regid().get(), list);
        Call<ResponseDefault> putnotification = cardService.putnotification(authorization, loginPref.notifID().get(), credential);
        putnotification.enqueue(new Callback<ResponseDefault>() {
            @Override
            public void onResponse(Response<ResponseDefault> response, Retrofit retrofit) {
                Log.e("TAG", "SUCESSGULL: " + response.code());
                if (response.code() == 200) {

                }
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }

    public void sharedClass() {

        SharedPreferences prefs = getSharedPreferences("mall_pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();

        Gson gson = new Gson();
        List<String> setmallName = new ArrayList<String>();
        List<String> setmallLat = new ArrayList<String>();
        List<String> setmallLong = new ArrayList<String>();
        List<String> setmallId = new ArrayList<String>();
        List<String> setmallImg = new ArrayList<String>();

        setmallName.addAll(mallName);
        setmallLat.addAll(mallLat);
        setmallLong.addAll(mallLong);
        setmallId.addAll(mallId);
        setmallImg.addAll(mallImg);

        String setName = gson.toJson(setmallName);
        String setLat = gson.toJson(setmallLat);
        String setLong = gson.toJson(setmallLong);
        String setId = gson.toJson(setmallId);
        String setImg = gson.toJson(setmallImg);

        edit.putString("mallName", setName);
        edit.putString("mallLat", setLat);
        edit.putString("mallLong", setLong);
        edit.putString("mallId", setId);
        edit.putString("mallImg", setImg);

        edit.apply();
    }

    @SuppressWarnings("unchecked")
    private void registerWithNotificationHubs() {
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object... params) {
                try {
                    String regid = gcm.register(SENDER_ID);
                    //ToastNotify("Registered Successfully - RegId : " + regid);
                    Log.e("TAG", "REGID: " + regid);
                    loginPref.regid().put(regid);

                } catch (Exception e) {
                    //ToastNotify("Registration Exception Message - " + e.getMessage());
                    return e;
                }
                return null;
            }
        }.execute(null, null, null);
    }


    @Override
    protected void onPause() {
        super.onPause();
        isVisible = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isVisible = true;

        /**
         * Update Dialog
         */
        //forceUpdate();

        /**
         * POPUP REVIEW
         */

        final SharedPreferences counterPref = getSharedPreferences("counterPref", Context.MODE_PRIVATE);
        final int counterKey = counterPref.getInt("counterKey", 0);
        final SharedPreferences.Editor editCounter = counterPref.edit();

        //editCounter.putInt("counterKey", 0);
        //editCounter.apply();

        Log.e("TAG", "counterPrefMain: " + counterKey);
        if (counterPref.getAll() != null) {
            if (counterKey == 8) {
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                alertDialogBuilder.setMessage("Show us some love by taking a moment to rate us in our play store page. It won't take more than a minute of your time. Thanks you for your support ! \uD83D\uDE01");
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        popUpReview = true;
                        String url = "http://app.kartoo.co/";
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                        dialog.cancel();
                        editCounter.putInt("counterKey", counterKey + 1);
                        editCounter.apply();
                    }
                });
                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MainActivity.this, PopupReviewActivity_.class);
                        startActivity(intent);
                        dialog.cancel();
                        editCounter.putInt("counterKey", counterKey + 1);
                        editCounter.apply();
                    }
                });
                alertDialogBuilder.setNeutralButton("Later", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editCounter.putInt("counterKey", 0);
                        editCounter.apply();
                        //loginPref.counterReview().put(0);
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        }

        final SharedPreferences verifyCounterPref = getSharedPreferences("verifyCounterPref", Context.MODE_PRIVATE);
        final int verifyCounterKey = verifyCounterPref.getInt("counterKey", 0);
        final SharedPreferences.Editor verifyEditCounter = verifyCounterPref.edit();

        verifyEditCounter.putInt("counterKey", verifyCounterKey + 1);
        verifyEditCounter.apply();
        Log.e("TAG", "verifyCounter: " + verifyCounterKey);

        if (verifyCounterPref.getAll() != null) {
            if (verifyCounterKey == 3) {
                Log.e("TAG", "verifiedphonenumber: " + profilePref.isVerified().get());
                if (profilePref.isVerified().get() != null && !profilePref.isVerified().get() && !loginPref.type().get().equals("guest")) {
                    final Dialog d = new Dialog(MainActivity.this, R.style.CustomDialogTheme);
                    d.setContentView(R.layout.activity_popup_number);
                    d.show();
                    Button verify_btn = (Button) d.findViewById(R.id.BtnVerify);
                    verify_btn.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, EditProfile_.class);
                            intent.putExtra("token", loginPref.token().get());
                            intent.putExtra("email", loginPref.email().get());

                            if (loginPref.urlPhoto().get() != null) {
                                intent.putExtra("image", loginPref.urlPhoto().get());
                            }

                            if (loginPref.name().get() != null) {
                                intent.putExtra("name", loginPref.name().get());
                            }
                            startActivity(intent);
                            d.dismiss();

                        }
                    });

                    Button close_btn = (Button) d.findViewById(R.id.BtnSkip);
                    close_btn.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            d.dismiss();
                        }
                    });
                }
            }
            if (verifyCounterKey > 3) {
                verifyEditCounter.putInt("counterKey", 0);
                verifyEditCounter.apply();
            }
        }

    }

    public void ToastNotify(final String notificationMessage) {
        if (isVisible == true)
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, notificationMessage, Toast.LENGTH_LONG).show();
                }
            });
    }

    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    // onclik search images
    @Click(R.id.mFLsearch)
    public void mIVsearchClick() {

        Intent i = new Intent(MainActivity.this, SearchActivity_.class);
        startActivity(i);

        /*
        mLLsearch.setVisibility(View.VISIBLE);
        if (toolbar_title.getVisibility() == View.VISIBLE){
            toolbar_title.setVisibility(View.GONE);
        }
        if (toolbar_title_text.getVisibility() == View.VISIBLE){
            toolbar_title_text.setVisibility(View.GONE);
        }


        mFLsearch.setVisibility(View.GONE);
        mETsearch.requestFocus();
        mETsearch.setText("");
        mETsearch.setFocusable(true);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,0);
        */
    }

    @Click(R.id.mIVsearchClear)
    public void mIVsearchClearClick() {
        mLLsearch.setVisibility(View.GONE);

        int position = mTLtab.getSelectedTabPosition();

        if (position > 0) {
            toolbar_title.setVisibility(View.GONE);
            toolbar_title_text.setVisibility(View.VISIBLE);
        } else {
            toolbar_title_text.setVisibility(View.GONE);
            toolbar_title.setVisibility(View.VISIBLE);
        }

        mFLsearch.setVisibility(View.VISIBLE);
        mETsearch.setText("");
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mETsearch.getWindowToken(), 0);
    }

    public void dialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setTitle("Oops!");
        alertDialogBuilder.setMessage("You need to login first");
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.e("sebelum", "sebelum" + loginPref.name().get());
                loginPref.name().remove();
                loginPref.email().remove();
                loginPref.token().remove();
                loginPref.urlPhoto().remove();
                loginPref.level().remove();
                loginPref.uid().remove();
                loginPref.type().remove();
                loginPref.fromBitmap().remove();
                loginPref.addcard().remove();
                loginPref.regid().remove();
                loginPref.notifID().remove();
                loginPref.interest().remove();

                profilePref.isVerified().remove();

                final SharedPreferences verifyCounterPref = getSharedPreferences("verifyCounterPref", Context.MODE_PRIVATE);
                final SharedPreferences.Editor verifyEditCounter = verifyCounterPref.edit();

                verifyEditCounter.putInt("counterKey", 0);
                verifyEditCounter.apply();

                // LoginManager.getInstance().logOut();
                FacebookSdk.sdkInitialize(getApplicationContext());
                LoginManager.getInstance().logOut();
                if (mGoogleApiClient.isConnected()) {
                    Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                    mGoogleApiClient.disconnect();
                    Log.e("asdf", "logged out");
                }
                Log.e("sesudah", "sesudah" + loginPref.name().get());
                Intent intent2 = new Intent(MainActivity.this, LandingActivity_.class);
                startActivity(intent2);
                finish();
            }
        });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("TAG", "onStart: " + mGoogleApiClientLocation.isConnected());
        //PermissionManager.check(this, Manifest.permission.RECEIVE_SMS, REQUEST_CODE_FOR_SMS);
        mGoogleApiClient.connect();
        mGoogleApiClientLocation.connect();
        isVisible = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        if (mGoogleApiClientLocation.isConnected()) {
            mGoogleApiClientLocation.disconnect();
        }

        isVisible = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }

        if (mGoogleApiClientLocation.isConnected()) {
            mGoogleApiClientLocation.disconnect();
        }
    }

    private void setUpNavDrawer() {
        if (mToolbar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mToolbar.setNavigationIcon(R.drawable.ic_menu_orange);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDLdrawer.openDrawer(GravityCompat.START);
                }
            });
        }
    }

    void search(final String query) {
        Log.e("TAG", "onEditorAction:query " + query);
        Intent intent = new Intent(MainActivity.this, SearchActivity_.class);
        intent.putExtra("query", query);
        startActivity(intent);
        mETsearch.setText("");
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    public void doSignin() {
        Toast.makeText(this, "Your session has expired, please login again", Toast.LENGTH_LONG).show();
        loginPref.name().remove();
        loginPref.email().remove();
        loginPref.token().remove();
        loginPref.urlPhoto().remove();
        loginPref.level().remove();
        loginPref.uid().remove();
        loginPref.type().remove();
        loginPref.fromBitmap().remove();
        loginPref.addcard().remove();
        loginPref.regid().remove();
        loginPref.notifID().remove();
        loginPref.interest().remove();

        // LoginManager.getInstance().logOut();
        FacebookSdk.sdkInitialize(getApplicationContext());
        LoginManager.getInstance().logOut();
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            Log.e("asdf", "logged out");
        }
        Log.e("sesudah", "sesudah" + loginPref.name().get());
        Intent intent2 = new Intent(MainActivity.this, LandingActivity_.class);
        startActivity(intent2);
        finish();
    }


    @Override
    public void onBackPressed() {
        if (mDLdrawer.isDrawerOpen(GravityCompat.START))
            mDLdrawer.closeDrawer(GravityCompat.START);
        else {
            FragmentManager fm = getSupportFragmentManager();
            if (fm.getBackStackEntryCount() == 0)
                super.onBackPressed();
            else
                fm.popBackStack();
        }
    }

    public void onConnected(Bundle bundle) {
        Log.e("TAG", "onConnected" + "apa");
        check();
        //callMallNotification();

    }

    public void locationRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClientLocation, builder.build());
        result.setResultCallback(this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e("TAG", "onConnectionFailed: " + connectionResult.hasResolution());
        Log.e("TAG", "onConnectionFailed: " + connectionResult.toString());
    }

    private void check() {
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissionsNeeded = new ArrayList<String>();
            final List<String> permissionsList = new ArrayList<String>();
            if (!addPermission(permissionsList, android.Manifest.permission.ACCESS_FINE_LOCATION))
                permissionsNeeded.add("GPS");
            if (!addPermission(permissionsList, android.Manifest.permission.ACCESS_COARSE_LOCATION))
                permissionsNeeded.add("Location");
            if (!addPermission(permissionsList, android.Manifest.permission.RECEIVE_SMS))
                permissionsNeeded.add("SMS");
            if (!addPermission(permissionsList, android.Manifest.permission.CALL_PHONE))
                permissionsNeeded.add("and Call");

            if (permissionsList.size() > 0) {
                if (permissionsNeeded.size() > 0) {
                    // Need Rationale
                    String message = "You need to grant access to " + permissionsNeeded.get(0);
                    for (int i = 1; i < permissionsNeeded.size(); i++)
                        message = message + ", " + permissionsNeeded.get(i);
                    showMessageOKCancel(message,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                            REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                                }
                            });
                    return;
                }
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                return;
            }
            callMallNotification();
            locationRequest();

        } else {
            callMallNotification();
            locationRequest();
        }

    }

    private boolean addPermission(List<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!shouldShowRequestPermissionRationale(permission))
                return false;
        }
        return true;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new android.support.v7.app.AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(android.Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.CALL_PHONE, PackageManager.PERMISSION_GRANTED);


                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    callMallNotification();
                    locationRequest();


                    // All Permissions Granted
                } else {
                    // Permission Denied
                    Toast.makeText(MainActivity.this, "Some Permission is Denied", Toast.LENGTH_SHORT).show();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                // NO need to show the dialog;
                break;

            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                //  GPS disabled show the user a dialog to turn it on
                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().
                    status.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);

                } catch (IntentSender.SendIntentException e) {
                    //failed to show dialog
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                // Location settings are unavailable so not possible to show any dialog now
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {

                Toast.makeText(getApplicationContext(), "GPS enabled", Toast.LENGTH_LONG).show();
            } else {

                Toast.makeText(getApplicationContext(), "GPS is not enabled", Toast.LENGTH_LONG).show();
            }

        }
    }

    // check version on play store and force update
    public void forceUpdate() {
        PackageManager packageManager = this.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String currentVersion = packageInfo.versionName;
        new ForceUpdateAsync(currentVersion, MainActivity.this).execute();
    }
}