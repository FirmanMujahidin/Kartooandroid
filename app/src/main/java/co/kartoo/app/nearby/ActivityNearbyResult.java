package co.kartoo.app.nearby;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.ArrayList;
import java.util.Map;

import co.kartoo.app.DetailPromoActivity;
import co.kartoo.app.R;
import co.kartoo.app.models.LoginPref_;
import co.kartoo.app.rest.PromoService;
import co.kartoo.app.rest.model.Card;
import co.kartoo.app.rest.model.newest.Nearby;
import co.kartoo.app.rest.model.newest.NearbyHeader;
import co.kartoo.app.views.NumberClusterRendererNearby;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;


@EActivity(R.layout.activity_nearby_result)
public class ActivityNearbyResult extends AppCompatActivity implements OnMapReadyCallback, CompoundButton.OnCheckedChangeListener, ClusterManager.OnClusterClickListener<Nearby> {

    @ViewById
    Switch mTBmyCards;

    @ViewById
    RecyclerView mRVpromo;
    @ViewById
    Toolbar mToolbar;
    @ViewById
    TextView mTVtitle;
    @ViewById
    RelativeLayout relativeLayout3;
    @ViewById
    ProgressBar progressBar;
    @ViewById
    LinearLayout GPSoff;
    @ViewById
    TextView Location;
    @ViewById
    ProgressBar progressHorizontal;


    PromoFromNearbyAdapter adapter;
    ArrayList<Nearby> listCategoryOutlet;
    ArrayList<Nearby> listCategoryOutletToShow;
    Map<String, Card> myCardsMap;
    GPSTracker gps;
    LatLng myLocation;
    SupportMapFragment mapFragment;
    ClusterManager<Nearby> mClusterManager;
    ArrayList<Nearby> allNearbyOutlet;
    ArrayList<Nearby> showedNearbyOutlet;
    boolean myCards;
    PromoService service;
    Retrofit retrofit;


    @Pref
    LoginPref_ loginPref;

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
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
        retrofit = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
        service = retrofit.create(PromoService.class);
        mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
        mapFragment.getMapAsync(this);
        myLocation = getCurrentLocation();

        progressHorizontal.setVisibility(View.VISIBLE);

        checkPromos();

        setUpNavDrawer();
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mapFragment.getMapAsync(this);
        allNearbyOutlet = new ArrayList<>();
        showedNearbyOutlet = new ArrayList<>();
        getCurrentLocation();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            showedNearbyOutlet.clear();
            showedNearbyOutlet.addAll((ArrayList<Nearby>) data.getExtras().getSerializable("result"));
            allNearbyOutlet.clear();
            allNearbyOutlet.addAll((ArrayList<Nearby>) data.getExtras().getSerializable("result"));
            mClusterManager.clearItems();
            mClusterManager.addItems(showedNearbyOutlet);
//            mClusterManager.cluster();
        }
    }

    public LatLng getCurrentLocation() {
        gps = new GPSTracker(this);
        double latitude;
        double longitude;
        // check if GPS enabled
        if (gps.canGetLocation()) {
            GPSoff.setVisibility(View.GONE);

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            Log.e("TAG", "getCurrentLocation: " + latitude + longitude);
            return new LatLng(latitude, longitude);
        } else {
            GPSoff.setVisibility(View.VISIBLE);
            mapFragment.getView().setVisibility(View.GONE);

            return new LatLng(0, 0);
        }
    }

    public void checkPromos() {
        Call<NearbyHeader> searchOutletCall = service.searchCoordinateMyCard(loginPref.token().get(), "1", myCards + "", myLocation.latitude + "", myLocation.longitude + "", "distance");
        searchOutletCall.enqueue(new Callback<NearbyHeader>() {
            @Override
            public void onResponse(Response<NearbyHeader> response, Retrofit retrofit) {
                Log.e("TAG", "checkPromosResponse: " + response.code());
                if (response.isSuccess()) {
                    if (response.body().getPromotions().size() == 0) {
                        mTBmyCards.setChecked(false);
                    } else {
                        mTBmyCards.setChecked(true);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(false);

        googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener(){
            @Override
            public boolean onMyLocationButtonClick()
            {
                getCurrentLocation();
                myLocation = getCurrentLocation();

                loadData();
                //TODO: Any custom actions
                return false;
            }
        });

        myLocation = getCurrentLocation();
        CameraUpdate updateToMyLocation = CameraUpdateFactory.newLatLngZoom(myLocation, 16);

        if (loginPref.type().get().equals("guest")){
            mTBmyCards.setChecked(false);
        }

        if (myLocation!= null) {
            googleMap.moveCamera(updateToMyLocation);

            mClusterManager = new ClusterManager<Nearby>(this, googleMap);
            mClusterManager.setRenderer(new NumberClusterRendererNearby(this, googleMap, mClusterManager));
            googleMap.setOnCameraChangeListener(mClusterManager);
            googleMap.setOnMarkerClickListener(mClusterManager);

            mClusterManager.setOnClusterClickListener(this);
            mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<Nearby>() {
                @Override
                public boolean onClusterItemClick(Nearby outlet) {
                    Intent intent = new Intent(getApplicationContext(), DetailPromoActivity.class);
                    intent.putExtra("Id", outlet.getId());
                    intent.putExtra("outletName", outlet.getMerchant());
                    intent.putExtra("imageHeader", outlet.getUrl_img());
                    intent.putExtra("textHeader", outlet.getPromotion());
                    intent.putExtra("from", "Open Nearby Promotion");
                    startActivity(intent);
                    return false;
                }
            });
            loadData();

        } else {

        }
    }
    public void loadData() {
        progressHorizontal.setVisibility(View.VISIBLE);

        if (mTBmyCards.isChecked()){
            myCards = true;
        }
        else {
            myCards = false;
        }
        Log.e("TAG", "loadData: "+myCards);


        int sortBy = 0;
        showedNearbyOutlet.clear();

        //if (myCards) {
        Call<NearbyHeader> searchOutletCall = service.searchCoordinateMyCard(loginPref.token().get(), "1", myCards + "", myLocation.latitude + "", myLocation.longitude + "", "distance");
        searchOutletCall.enqueue(new Callback<NearbyHeader>() {
                @Override
                public void onResponse(Response<NearbyHeader> response, Retrofit retrofit) {

                    Log.e("TAG", "NearbyResponse: " + response.code());
                    if (response.code() == 200) {

                        Log.e("TAG", "Nearby: " + response.body().getPromotions().size());
                        progressHorizontal.setVisibility(View.INVISIBLE);

                        allNearbyOutlet.clear();
                        allNearbyOutlet.addAll(response.body().getPromotions());
                        mClusterManager.addItems(allNearbyOutlet);
                        mClusterManager.cluster();
                        mTBmyCards.setOnCheckedChangeListener(ActivityNearbyResult.this);

                    } else if (response.code() == 401) {

                    } else if (response.code() == 404) {

                    } else if (response.code() == 500) {

                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    progressHorizontal.setVisibility(View.INVISIBLE);
                    Toast.makeText(ActivityNearbyResult.this, "We cannot give you nearby promotions. Please try again later", Toast.LENGTH_LONG).show();
                }
            });
        /*
        }
        else {
            Call<ArrayList<Nearby>> searchOutletCall = service.searchCoordinate(loginPref.token().get(), myLocation.latitude + "", myLocation.longitude + "", "distance");
            searchOutletCall.enqueue(new Callback<ArrayList<Nearby>>() {
                @Override
                public void onResponse(Response<ArrayList<Nearby>> response, Retrofit retrofit) {
                    Log.e("TAG", "NearbyResponseNOT: "+response.code());

                    if (response.isSuccess()) {
                        Log.e("TAG", "NearbyNOT: "+response.body().size());
                        progressHorizontal.setVisibility(View.INVISIBLE);

                        allNearbyOutlet.clear();
                        allNearbyOutlet.addAll(response.body());
                        mClusterManager.addItems(allNearbyOutlet);
                        mClusterManager.cluster();
                        mTBmyCards.setOnCheckedChangeListener(ActivityNearbyResult.this);
                    } else if (response.code() == 401) {

                    } else if (response.code() == 404) {

                    } else if (response.code() == 500) {

                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    progressHorizontal.setVisibility(View.INVISIBLE);
                    Toast.makeText(ActivityNearbyResult.this, "We cannot give you nearby promotions. Please try again later", Toast.LENGTH_LONG).show();

                }
            });

        }
        */

        if(mClusterManager==null){
            mClusterManager.addItems(showedNearbyOutlet);
            mClusterManager.cluster();
        }
        else {
            mClusterManager.clearItems();
            mClusterManager.addItems(showedNearbyOutlet);
            mClusterManager.cluster();
        }
    }


    @Override
    public boolean onClusterClick(Cluster<Nearby> cluster) {
        ArrayList<Nearby> selectedOutlet = (ArrayList<Nearby>) cluster.getItems();
        Log.e("clusterClick", selectedOutlet.size() + "");
        Bundle bundle = new Bundle();
        bundle.putSerializable("result", selectedOutlet);
        Intent intent = new Intent(this, ClusterListActivity_.class);
        intent.putExtras(bundle);
        startActivity(intent);
        return false;
    }


    @Click(R.id.Location)
    public void LocationClick(){
        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        loadData();
    }

}

