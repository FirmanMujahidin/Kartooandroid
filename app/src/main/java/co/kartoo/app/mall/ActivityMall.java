package co.kartoo.app.mall;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import co.kartoo.app.R;
import co.kartoo.app.category.PromoFromCategoryAdapter;
import co.kartoo.app.events.TrendingFinishedEventCategory;
import co.kartoo.app.inappmaps.InAppMaps_;
import co.kartoo.app.models.LoginPref_;
import co.kartoo.app.nearby.GPSTracker;
import co.kartoo.app.promo.ActivityFilter_;
import co.kartoo.app.rest.PromoService;
import co.kartoo.app.rest.model.Category;
import co.kartoo.app.rest.model.CategoryHeader;
import co.kartoo.app.rest.model.newest.DiscoverPromotionCategory;
import co.kartoo.app.views.MeasuredStaggeredGridLayoutManagerViewAll;
import co.kartoo.app.views.SpaceItemDecoration;
import de.greenrobot.event.EventBus;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;


@EActivity(R.layout.activity_mall)
public class ActivityMall extends AppCompatActivity
        implements CompoundButton.OnCheckedChangeListener , OnMapReadyCallback {

    private static LatLng STARTING_MARKER_POSITION;
    @ViewById
    Switch mTBmyCards;
    @ViewById
    ImageView mIVfilter;
    @ViewById
    RecyclerView mRVpromo;
    @ViewById
    Toolbar mToolbar;
    @ViewById
    TextView mTVtitle;
    @ViewById
    RelativeLayout relativeLayout3;
    @ViewById
    RelativeLayout oops;
    @ViewById
    Spinner sortbySpinner;
    @ViewById
    RelativeLayout timeOut;
    @ViewById
    Button reload;
    @ViewById
    TextView TVaddress;
    @ViewById
    NestedScrollView SVmain;
    @ViewById
    ImageView pin;
    @ViewById
    ProgressBar progressBar;
    @ViewById
    ProgressBar progressBarScroll;
    ArrayList<DiscoverPromotionCategory> listCategoryOutlet;
    ArrayList<Category> listTrendingPromo;
    ArrayList<Category> filteredListTrendingPromo;
    PromoFromCategoryAdapter adapterTrendingPromo;

    String TAG = "ActivityListPromoCategory";

    int lastPage=  1;
    int maxPage = -1;
    int diff;

    @Pref
    LoginPref_ loginPref;

    boolean myCards;
    String idPromo, name;

    String[] sortBy = {
            "A to Z",
            "Popular",
            "Ending Soon",
    };

    String sortValue, address, latitude, longitude;
    LatLng myLocation;
    GPSTracker gps;
    int a;
    SupportMapFragment mapFragment;
    MeasuredStaggeredGridLayoutManagerViewAll layoutManagerLatestPromo;
    private boolean loading = false;
    private GoogleMap mMap;
    private FirebaseAnalytics mFirebaseAnalytics;

    private void setUpNavDrawer() {
        if (mToolbar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mToolbar.setNavigationIcon(R.drawable.ic_back_orange);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }
    @AfterViews
    public void init() {
        mRVpromo.setFocusable(false);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(100); // half second between each showcase view
        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, "52");
        sequence.setConfig(config);
        sequence.addSequenceItem(new MaterialShowcaseView.Builder(this)
                .setTarget(mTBmyCards)
                .setDismissOnTouch(true)
                .setTitleText("My Cards")
                .setMaskColour(getResources().getColor(R.color.transparent))

                .setContentText("Filter the list of promotions\nbased on your cards")
                .build()
        );
        sequence.addSequenceItem(new MaterialShowcaseView.Builder(this)
                .setTarget(sortbySpinner)
                .setDismissOnTouch(true)
                .setTitleText("Sort By")
                .setMaskColour(getResources().getColor(R.color.transparent))

                .setContentText("Arrange the promotions \nbased on different filters")
                .build()
        );
        sequence.start();

        progressBar.setVisibility(View.VISIBLE);


        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
        mRVpromo.setVisibility(View.GONE);
        oops.setVisibility(View.GONE);
        mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
        if(mapFragment.getView()!=null){
            mapFragment.getView().setVisibility(View.GONE);
        }
        setUpNavDrawer();
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getCurrentLocation();
        myLocation = getCurrentLocation();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
        final PromoService promoService = retrofit.create(PromoService.class);
        final String token = loginPref.token().get();

        listCategoryOutlet = new ArrayList<>();

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        idPromo = intent.getStringExtra("id");
        address = intent.getStringExtra("address");
        latitude = intent.getStringExtra("latitude");
        longitude = intent.getStringExtra("longitude");

        if (latitude!=null||latitude!=""){
            TVaddress.setText(address);
            STARTING_MARKER_POSITION =new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
            SupportMapFragment mapFragment  = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
            mapFragment.getMapAsync(this);

            //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(STARTING_MARKER_POSITION, 15));

        } else {
            mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
            if(mapFragment.getView()!=null){
                mapFragment.getView().setVisibility(View.GONE);
            }
        }

        //mMap.getUiSettings().setScrollGesturesEnabled(false);

        mTVtitle.setText(name);

        mTBmyCards.setChecked(true);


        if (loginPref.type().get().equals("guest")){
            mTBmyCards.setChecked(false);
        }

        listTrendingPromo = new ArrayList<>();
        filteredListTrendingPromo = new ArrayList<>();
        adapterTrendingPromo = new PromoFromCategoryAdapter(this, listTrendingPromo, filteredListTrendingPromo, promoService, token, mRVpromo);

        mRVpromo.setNestedScrollingEnabled(false);
        //mRVpromo.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mRVpromo.addItemDecoration(new SpaceItemDecoration(3));
        mRVpromo.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mRVpromo.setAdapter(adapterTrendingPromo);

        //spinner SortBy
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, sortBy);
        sortbySpinner.setAdapter(adapter);


        sortbySpinner.setSelection(1);
        sortValue = "endingsoon";
        checkPromos();

        sortbySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                if (pos==0){
                    sortValue = "popular";
                    mRVpromo.setVisibility(View.GONE);

                } else if (pos==1){
                    sortValue = "endingsoon";
                    mRVpromo.setVisibility(View.GONE);

                } else if (pos==2){
                    sortValue = "alphabet";
                    mRVpromo.setVisibility(View.GONE);

                }
                Log.e("TAG", "onItemSelected: "+pos);
                Log.e("TAG", "onItemSelected: "+sortbySpinner.getSelectedItem().toString().toLowerCase());
                a = sortbySpinner.getSelectedItemPosition();
                loadData();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        mTBmyCards.setOnCheckedChangeListener(ActivityMall.this);
    }

    public LatLng getCurrentLocation() {
        gps = new GPSTracker(this);
        double latitude;
        double longitude;
        // check if GPS enabled
        if(gps.canGetLocation()){
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            Log.e("TAG", "getCurrentLocation: "+latitude+longitude );
            return new LatLng(latitude, longitude);
        }
        else{
            return new LatLng(0,0);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setScrollGesturesEnabled(false);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(STARTING_MARKER_POSITION, 15));


    }

    public void checkPromos(){
        progressBar.setVisibility(View.VISIBLE);
        oops.setVisibility(View.GONE);
        mRVpromo.setVisibility(View.GONE);
        timeOut.setVisibility(View.GONE);

        if(mapFragment.getView()!=null){
            mapFragment.getView().setVisibility(View.GONE);
        }

        Retrofit retrofit = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
        final PromoService promoService = retrofit.create(PromoService.class);
        final String token = loginPref.token().get();
        Call<CategoryHeader> favoriteOutletCall = promoService.getMallPromotions(token, idPromo, 1, myCards + "", sortValue);
        favoriteOutletCall.enqueue(new Callback<CategoryHeader>() {
            @Override
            public void onResponse(Response<CategoryHeader> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    if (response.code() == 200) {
                        Log.e("TAG", "PERTAMA: "+response.body().getPromotions().size());
                        if (response.body().getPromotions().size()==0){
                            mTBmyCards.setChecked(false);
                            //Log.e("eventTrending", getString(response.body().getMaxPage()));

                            oops.setVisibility(View.GONE);
                            loadData();
                        }
                        else {
                            mTBmyCards.setChecked(true);
                            loadData();

                        }
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                progressBar.setVisibility(View.GONE);
                oops.setVisibility(View.GONE);
                mRVpromo.setVisibility(View.GONE);
                timeOut.setVisibility(View.VISIBLE);
            }
        });
    }

    public void loadData() {
        getCurrentLocation();
        listTrendingPromo.clear();

        progressBar.setVisibility(View.VISIBLE);
        oops.setVisibility(View.GONE);
        mRVpromo.setVisibility(View.GONE);
        timeOut.setVisibility(View.GONE);

        if(mapFragment.getView()!=null){
            mapFragment.getView().setVisibility(View.GONE);
        }


        Retrofit retrofit = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
        final PromoService promoService = retrofit.create(PromoService.class);
        final String token = loginPref.token().get();

        Intent intent = getIntent();
        final String id = intent.getStringExtra("id");

        if (mTBmyCards.isChecked()){
            myCards = true;
        }
        else {
            myCards = false;
        }
        int sortBy = 0;

        Log.e("TAG", "masukScrollAwal: "+myCards);

        //if (myCards){
        Call<CategoryHeader> promoByCategoryCall = promoService.getMallPromotions(token, idPromo, 1, myCards + "", sortValue);
        promoByCategoryCall.enqueue(new Callback<CategoryHeader>() {
                @Override
                public void onResponse(Response<CategoryHeader> response, Retrofit retrofit) {
                    if (response.isSuccess()) {
                        Log.e("TAG", "LoadDataMyCard"+ response.code());
                        Log.e("TAG", "KEDUA"+ response.code());
                        if (response.code() == 200) {
                            if(mapFragment.getView()!=null){
                                mapFragment.getView().setVisibility(View.VISIBLE);
                            }
                            if (response.body().getPromotions().size()==0){
                                progressBar.setVisibility(View.GONE);
                                mRVpromo.setVisibility(View.GONE);
                                oops.setVisibility(View.VISIBLE);
                                timeOut.setVisibility(View.GONE);
                                if(mapFragment.getView()!=null){
                                    mapFragment.getView().setVisibility(View.GONE);
                                }
                            }
                            else {
                                Log.e("TAG", "KEDUA"+ response.code());

                                progressBar.setVisibility(View.GONE);
                                mRVpromo.setVisibility(View.VISIBLE);
                                oops.setVisibility(View.GONE);
                                timeOut.setVisibility(View.GONE);

                            }
                            EventBus.getDefault().postSticky(new TrendingFinishedEventCategory(response.body()));
                            //scroll();
                            mTBmyCards.setOnCheckedChangeListener(ActivityMall.this);

                            mixpanel();
                        }
                    }
                }
                @Override
                public void onFailure(Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    oops.setVisibility(View.GONE);
                    mRVpromo.setVisibility(View.GONE);
                    timeOut.setVisibility(View.VISIBLE);
                }
            });
        //}
//        else {
//            Call<CategoryOutlet> promoByCategoryCall = promoService.getPromo(token, id, 1, sortValue, String.valueOf(myLocation.longitude), String.valueOf(myLocation.latitude));
//            promoByCategoryCall.enqueue(new Callback<CategoryOutlet>() {
//                @Override
//                public void onResponse(Response<CategoryOutlet> response, Retrofit retrofit) {
//                    if (response.isSuccess()) {
//                        Log.e("TAG", "LoadDataNotMyCard"+ response.body().getMaxPage());
//                        Log.e("TAG", "LoadDataNotMyCardSize"+ response.body().getPromotions().size());
//
//
//                        Log.e("TAG", "Category: NotmyCard"+ response.code());
//                        if (response.code() == 200) {
//
//                            if(mapFragment.getView()!=null){
//                                mapFragment.getView().setVisibility(View.VISIBLE);
//                            }
//
//                            if (response.body().getPromotions().size()==0){
//                                Log.e("TAG", "KEDUA"+ response.code());
//
//                                progressBar.setVisibility(View.GONE);
//                                mRVpromo.setVisibility(View.GONE);
//                                oops.setVisibility(View.VISIBLE);
//                                timeOut.setVisibility(View.GONE);
//                                if(mapFragment.getView()!=null){
//                                    mapFragment.getView().setVisibility(View.GONE);
//                                }
//
//
//                            }
//                            else {
//                                Log.e("TAG", "KEDUA"+ response.code());
//
//                                progressBar.setVisibility(View.GONE);
//                                mRVpromo.setVisibility(View.VISIBLE);
//                                oops.setVisibility(View.GONE);
//                                timeOut.setVisibility(View.GONE);
//
//                            }
//                            EventBus.getDefault().postSticky(new TrendingFinishedEventCategory(response.body()));
//                            mTBmyCards.setOnCheckedChangeListener(ActivityMall.this);
//
//                        }
//                    }
//                }
//                @Override
//                public void onFailure(Throwable t) {
//                    progressBar.setVisibility(View.GONE);
//                    oops.setVisibility(View.GONE);
//                    mRVpromo.setVisibility(View.GONE);
//                    timeOut.setVisibility(View.VISIBLE);
//                }
//            });
//        }
        Log.e("TAG", "loadData: "+myCards);

        SVmain.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                View view = SVmain.getChildAt(SVmain.getChildCount() - 1);
                if (listTrendingPromo.size()!=0){
                    diff = (view.getBottom() - (SVmain.getHeight() + SVmain.getScrollY()));
                }
                Log.e("TAG", "diff: "+diff);

                if (diff == 0) {
                    if (!loading && lastPage < maxPage) {
                        loading = true;
                        Log.e("TAG", "masukScroll: "+myCards);
                        progressBarScroll.setVisibility(View.VISIBLE);
                        //if (myCards){
                            Log.e("TAG", "onScrollChanged: "+lastPage+1);
                        Call<CategoryHeader> promoByCategoryCall = promoService.getMallPromotions(token, idPromo, 1, myCards + "", sortValue);
                        promoByCategoryCall.enqueue(new Callback<CategoryHeader>() {
                                @Override
                                public void onResponse(Response<CategoryHeader> response, Retrofit retrofit) {
                                    Log.e("TAG", "onResponseMallScroll: "+response.code());
                                    loading = false;
                                    if (response.isSuccess()) {
                                        if (response.code() == 200) {
                                            progressBarScroll.setVisibility(View.GONE);
                                            listTrendingPromo.addAll(response.body().getPromotions());
                                            adapterTrendingPromo.notifyDataSetChanged();
                                            maxPage = Integer.valueOf(response.body().getMaxPage());
                                            lastPage++;
                                        }
                                    }
                                }
                                @Override
                                public void onFailure(Throwable t) {
                                    //progressBar.setVisibility(View.GONE);
                                    loading = false;
                                }
                            });
                        //}
//                        else {
//                            Log.e("TAG", "onScrollChanged: "+"masuk+NotMyCard");
//                            Call<CategoryOutlet> promoByCategoryCall = promoService.getPromo(token, id, lastPage+1, sortValue, String.valueOf(myLocation.longitude), String.valueOf(myLocation.latitude));
//                            promoByCategoryCall.enqueue(new Callback<CategoryOutlet>() {
//                                @Override
//                                public void onResponse(Response<CategoryHeader> response, Retrofit retrofit) {
//                                    Log.e("TAG", "onScrollChanged: "+response.code() );
//                                    loading = false;
//                                    if (response.isSuccess()) {
//                                        if (response.code() == 200) {
//                                            progressBarScroll.setVisibility(View.GONE);
//
//                                            listTrendingPromo.addAll(response.body().getPromotions());
//                                            adapterTrendingPromo.notifyDataSetChanged();
//                                            maxPage = response.body().getMaxPage();
//                                            lastPage++;
//                                        }
//                                    }
//                                }
//                                @Override
//                                public void onFailure(Throwable t) {
//                                    //progressBar.setVisibility(View.GONE);
//                                    loading = false;
//                                }
//                            });
//                        }
                    }
                }
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
            props.put("email", loginPref.email().get());
            props.put("category", name);
            if (name.equals(address)){
                props.put("Mall Notification", name);
            }
            mixpanel.track("View Promos by category", props);
        } catch (JSONException e) {
        }
    }
    public void firebase(){
        Bundle params = new Bundle();
        params.putString("email", loginPref.email().get());
        params.putString("category", name);
        if (name.equals(address)){
            params.putString("mall_notification", name);
        }
        mFirebaseAnalytics.logEvent("view_promo_by_category", params);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        listTrendingPromo.clear();
        oops.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        loadData();
    }


    @Click(R.id.mIVfilter)
    public void mIVfilterClick() {
        Intent intent = new Intent(this, ActivityFilter_.class);
        startActivityForResult(intent,1);
    }

    @Click(R.id.reload)
    public void reloadClick() {
        loadData();
    }

    @Click(R.id.pin)
    public void pinClick() {
        Intent newIntent = new Intent(this, InAppMaps_.class);
        newIntent.putExtra("latitude", latitude);
        newIntent.putExtra("longitude", longitude);
        newIntent.putExtra("mallName", name);
        startActivity(newIntent);

        /*
        Uri gmmIntentUri = Uri.parse("google.navigation:q="+latitude+","+longitude);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
        */
    }

    @Override
    public void onStart() {
        super.onStart();
        listTrendingPromo.clear();
        EventBus.getDefault().registerSticky(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        /*
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        mRVpromo.scrollToPosition(preferences.getInt("position", 0));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mRVpromo.scrollBy(0, - preferences.getInt("offset", 0));
            }
        }, 500);
        */
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    public void onEvent(TrendingFinishedEventCategory event) {
        //Log.e("eventTrending", getString(event.getListPromo().getMaxPage()));
        listTrendingPromo.clear();
        maxPage = Integer.valueOf(event.getListPromo().getMaxPage());
        listTrendingPromo.addAll(event.getListPromo().getPromotions());
        adapterTrendingPromo.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        super.onPause();
        /*
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        View firstChild = mRVpromo.getChildAt(0);
        int firstVisiblePosition = mRVpromo.getChildAdapterPosition(firstChild);
        int offset = firstChild.getTop();

        Log.d(TAG, "Postition: " + firstVisiblePosition);
        Log.d(TAG, "Offset: " + offset);

        preferences.edit()
                .putInt("position", firstVisiblePosition)
                .putInt("offset", offset)
                .apply();

               */
    }
}
