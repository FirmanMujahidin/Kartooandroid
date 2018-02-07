package co.kartoo.app.tabfragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.plus.Plus;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import co.kartoo.app.R;
import co.kartoo.app.landing.LandingActivity_;
import co.kartoo.app.mall.MallAdapter;
import co.kartoo.app.mall.MallEvent;
import co.kartoo.app.models.LoginPref_;
import co.kartoo.app.nearby.GPSTracker;
import co.kartoo.app.promo.ActivityFilter_;
import co.kartoo.app.rest.PromoService;
import co.kartoo.app.rest.model.newest.MallDetail;
import co.kartoo.app.rest.model.newest.Malls;
import de.greenrobot.event.EventBus;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

@EFragment(R.layout.fragment_malls)
public class FragmentMalls extends Fragment{

    @ViewById
    RelativeLayout relativeLayout3;
    @ViewById
    LinearLayout GPSoff;
    @ViewById
    TextView Location;
    @ViewById
    RecyclerView mRVmall;
    @ViewById
    ProgressBar progressBarScroll;
    @ViewById
    RelativeLayout oops;
    @ViewById
    Spinner sortbySpinner;
    @ViewById
    RelativeLayout timeOut;
    @ViewById
    Button reload;
    MallAdapter adapter;
    ArrayList<MallDetail> listCategoryOutletToShow;
    Retrofit retrofit;
    PromoService service;
    String[] sortBy = {
            "Distance",
            "A to Z",
    };
    int lastPage=  1;
    int maxPage = -1;
    int diff;
    @Pref
    LoginPref_ loginPref;
    LatLng myLocation;
    GPSTracker gps;
    String sortValue;
    int a;
    GoogleApiClient mGoogleApiClient;
    boolean isVisible;
    private boolean loading = false;
    private FirebaseAnalytics mFirebaseAnalytics;


    @AfterViews
    public void init() {
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addApi(Plus.API)
                .addApi(LocationServices.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();
        mGoogleApiClient.connect();

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());

        retrofit = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
        service = retrofit.create(PromoService.class);

        progressBarScroll.setVisibility(View.GONE);
        mRVmall.setVisibility(View.GONE);

        mixpanel();
        firebase();
        myLocation = getCurrentLocation();

        listCategoryOutletToShow =  new ArrayList<>();
        adapter = new MallAdapter(getContext(),listCategoryOutletToShow);
        mRVmall.setLayoutManager(new LinearLayoutManager(getContext()));
        mRVmall.setAdapter(adapter);

        //spinner SortBy
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item, sortBy);
        sortbySpinner.setAdapter(adapter);

        if (myLocation.longitude==0){
            a=1;
            sortbySpinner.setSelection(a);
        }

        sortbySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                if (pos==0){
                    if (myLocation.longitude==0){
                        Toast.makeText(getActivity(), "Are you lost? Please turn on your location", Toast.LENGTH_LONG).show();
                        sortbySpinner.setSelection(a);
                    }
                    else {
                        sortValue = "distance";
                        mRVmall.setVisibility(View.GONE);

                    }

                } else if (pos==1){
                    sortValue = "alphabet";
                    mRVmall.setVisibility(View.GONE);

                }
                Log.e("TAG", "onItemSelected: "+pos);
                Log.e("TAG", "onItemSelected: "+sortbySpinner.getSelectedItem().toString().toLowerCase());
                a = sortbySpinner.getSelectedItemPosition();
                loadData();

            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    @Click(R.id.mIVfilter)
    public void mIVfilterClick() {
        Intent intent = new Intent(getActivity(), ActivityFilter_.class);
        startActivityForResult(intent, 1);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.e("TAG", "setUserVisibleHint: "+ isVisibleToUser);
        if (isVisibleToUser && !isVisible ) {
            getCurrentLocation();
            myLocation = getCurrentLocation();
            //loadData();
            isVisible = true;
        }
    }

    public LatLng getCurrentLocation() {
        gps = new GPSTracker(getContext());
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
            return new LatLng(0, 0);
        }
    }

    public void loadData() {
        timeOut.setVisibility(View.GONE);
        progressBarScroll.setVisibility(View.GONE);
        mRVmall.setVisibility(View.GONE);
        oops.setVisibility(View.GONE);
            Call<Malls> searchOutletCall = service.getMalls(loginPref.token().get(), 1, sortValue, myLocation.latitude+"", myLocation.longitude+"");
            searchOutletCall.enqueue(new Callback<Malls>() {
                @Override
                public void onResponse(Response<Malls> response, Retrofit retrofit) {
                    if (response.isSuccess()) {
                        Log.e("TAG", "onResponseNearby: "+response.body().getMalls().size());
                        if (response.body().getMalls().size()==0){
                            progressBarScroll.setVisibility(View.GONE);
                            mRVmall.setVisibility(View.GONE);
                            oops.setVisibility(View.VISIBLE);
                            timeOut.setVisibility(View.GONE);
                        }
                        else {
                            progressBarScroll.setVisibility(View.GONE);
                            mRVmall.setVisibility(View.VISIBLE);
                            oops.setVisibility(View.GONE);
                            timeOut.setVisibility(View.GONE);

                            EventBus.getDefault().postSticky(new MallEvent(response.body()));

                        }

                    } else if (response.code() == 401) {
                        Toast.makeText(getContext(), "Your session has expired, please login again", Toast.LENGTH_LONG).show();
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
                        //LoginManager.getInstance().logOut();
                        FacebookSdk.sdkInitialize(getContext());
                        LoginManager.getInstance().logOut();
                        if (mGoogleApiClient.isConnected()) {
                            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                            mGoogleApiClient.disconnect();
                            Log.e("asdf", "logged out");
                        }

                        Log.e("sesudah","sesudah"+loginPref.name().get());
                        Intent intent2 = new Intent(getActivity(), LandingActivity_.class);
                        startActivity(intent2);
                        getActivity().finish();

                    } else if (response.code() == 404) {

                    } else if (response.code() == 500) {

                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    timeOut.setVisibility(View.VISIBLE);
                    progressBarScroll.setVisibility(View.GONE);
                    mRVmall.setVisibility(View.GONE);
                    oops.setVisibility(View.GONE);
                }
            });

        mRVmall.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                View view = mRVmall.getChildAt(mRVmall.getChildCount() - 1);
                if (listCategoryOutletToShow.size() != 0) {
                    if(view!=null){
                        diff = (view.getBottom() - (mRVmall.getHeight() + mRVmall.getScrollY()));
                    }
                }
                Log.e("TAG", "onScrollChanged: "+diff);
                if (diff <0) {
                    if (!loading && lastPage < maxPage) {
                        loading = true;
                        progressBarScroll.setVisibility(View.GONE);
                        Log.e("TAG", "onScrollChangedlastPage: " + lastPage + 1);
                        Call<Malls> promoByCategoryCall = service.getMalls(loginPref.token().get(), lastPage+1, sortValue, myLocation.latitude+"", myLocation.longitude+"");
                        promoByCategoryCall.enqueue(new Callback<Malls>() {
                            @Override
                            public void onResponse(Response<Malls> response, Retrofit retrofit) {
                                loading = false;
                                if (response.isSuccess()) {
                                    if (response.code() == 200) {
                                        progressBarScroll.setVisibility(View.GONE);
                                        listCategoryOutletToShow.addAll(response.body().getMalls());
                                        adapter.notifyDataSetChanged();
                                        maxPage = response.body().getMaxPage();
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
                    }
                }
            }
        });
    }

    public void mixpanel(){
        String projectToken = getString(R.string.mixpanel_token);
        MixpanelAPI mixpanel = MixpanelAPI.getInstance(getContext(), projectToken);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTimeStamp = dateFormat.format(new Date());
        try {
            JSONObject props = new JSONObject();
            props.put("TimeStamp", currentTimeStamp);
            props.put("email", loginPref.email().get());
            if (myLocation!=null){
                props.put("LatLong", myLocation.latitude+", "+myLocation.longitude);
            }
            else {
                props.put("LatLong", "0"+", "+"0");
            }
            mixpanel.track("Open Nearby tab", props);
        } catch (JSONException e) {
            Log.e("MYAPP", "Unable to add properties to JSONObject", e);
        }
    }
    public void firebase(){
        Bundle params = new Bundle();
        params.putString("email", loginPref.email().get());
        if (myLocation!=null){
            params.putString("LatLong", myLocation.latitude+", "+myLocation.longitude);
        }
        else {
            params.putString("LatLong", "0"+", "+"0");
        }
        mFirebaseAnalytics.logEvent("fragment_mall", params);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().registerSticky(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("TAG", "onResume: "+ "resume");
        getCurrentLocation();

        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity().getApplicationContext());
        if (resultCode == ConnectionResult.SUCCESS) {

        } else {
            Toast.makeText(getActivity(),"Google Play Service unavailable. Please install it first", Toast.LENGTH_LONG).show();
        }
    }


    public void onEvent(MallEvent event) {
        Log.d("eventNearby", "caught");
        listCategoryOutletToShow.clear();
        listCategoryOutletToShow.addAll(event.getListPromo().getMalls());
        adapter.notifyDataSetChanged();
        maxPage = event.getListPromo().getMaxPage();

    }

    @Click(R.id.Location)
    public void LocationClick(){
        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }
    @Click(R.id.reload)
    public void reloadClick(){
        loadData();
    }
}
