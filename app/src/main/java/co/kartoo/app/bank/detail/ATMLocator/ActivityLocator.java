package co.kartoo.app.bank.detail.ATMLocator;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.ArrayList;
import java.util.Map;

import co.kartoo.app.R;
import co.kartoo.app.models.LoginPref_;
import co.kartoo.app.nearby.GPSTracker;
import co.kartoo.app.nearby.PromoFromNearbyAdapter;
import co.kartoo.app.rest.BankService;
import co.kartoo.app.rest.model.Card;
import co.kartoo.app.rest.model.newest.AtmLocator;
import co.kartoo.app.rest.model.newest.Nearby;
import co.kartoo.app.views.NumberClusterRendererATMLocator;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

@EActivity(R.layout.activity_locator)
public class ActivityLocator extends AppCompatActivity implements OnMapReadyCallback, CompoundButton.OnCheckedChangeListener {
    @ViewById
    ToggleButton mTBatm;
    @ViewById
    ToggleButton mTBbranch;
    @ViewById
    ToggleButton mTBcdm;

    @ViewById
    Toolbar mToolbar;
    @ViewById
    TextView mTVtitle;
    @ViewById
    RelativeLayout relativeLayout3;
    @ViewById
    ProgressBar progressBar;

    @ViewById
    ProgressBar progressHorizontal;

    @ViewById
    Switch switchPositions;

    @ViewById
    ImageView pin;

    @ViewById
    LinearLayout GPSoff;
    @ViewById
    TextView Location;


    PromoFromNearbyAdapter adapter;
    ArrayList<Nearby> listCategoryOutlet;
    ArrayList<Nearby> listCategoryOutletToShow;
    Map<String,Card> myCardsMap;
    GPSTracker gps;
    LatLng myLocation;
    SupportMapFragment mapFragment;
    ClusterManager<AtmLocator> mClusterManager;
    ArrayList<AtmLocator> allNearbyOutlet;
    ArrayList<AtmLocator> showedNearbyOutlet;
    boolean myCards;
    BankService service;
    Retrofit retrofit;

    String Id;
    Boolean atm, branch, cdm;
    LatLng lat;

    GoogleMap maps;
    Boolean BtnSwitch;


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

        Intent intent = getIntent();
        Id = intent.getStringExtra("Id");

        progressHorizontal.setVisibility(View.VISIBLE);


        retrofit = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
        service = retrofit.create(BankService.class);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        myLocation = getCurrentLocation();



        setUpNavDrawer();
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mapFragment.getMapAsync(this);
        allNearbyOutlet = new ArrayList<>();
        showedNearbyOutlet = new ArrayList<>();
        getCurrentLocation();

        BtnSwitch = false;
        switchPositions.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    BtnSwitch = true;
                    pin.setVisibility(View.VISIBLE);
                } else {
                    BtnSwitch = false;
                    pin.setVisibility(View.GONE);
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            showedNearbyOutlet.clear();
            showedNearbyOutlet.addAll((ArrayList<AtmLocator>) data.getExtras().getSerializable("result"));

            allNearbyOutlet.clear();
            allNearbyOutlet.addAll((ArrayList<AtmLocator>)data.getExtras().getSerializable("result"));

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


    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(false);

        //lat  = googleMap.getCameraPosition().target;
        //Log.e("TAG", "target: "+lat );

        maps = googleMap;

        googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener(){
            @Override
            public boolean onMyLocationButtonClick()
            {
                getCurrentLocation();
                myLocation = getCurrentLocation();

                loadData(myLocation);
                //TODO: Any custom actions
                return false;
            }
        });

        myLocation = getCurrentLocation();
        CameraUpdate updateToMyLocation = CameraUpdateFactory.newLatLngZoom(myLocation, 16);

        if (myLocation!= null) {
            googleMap.moveCamera(updateToMyLocation);

            mClusterManager = new ClusterManager<AtmLocator>(this, googleMap);
            mClusterManager.setRenderer(new NumberClusterRendererATMLocator(this, googleMap, mClusterManager));
            googleMap.setOnCameraChangeListener(mClusterManager);
            googleMap.setOnMarkerClickListener(mClusterManager);
            googleMap.setOnInfoWindowClickListener(mClusterManager);
            //googleMap.se(mClusterManager);
            //mClusterManager.setOnClusterClickListener(this);

            mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<AtmLocator>() {
                @Override
                public boolean onClusterItemClick(AtmLocator atmLocator) {
                    Log.e("TAG", "onClusterItemClick: "+"click" );

                    return false;
                }
            });

            loadData(myLocation);
        }

        mClusterManager.setOnClusterItemInfoWindowClickListener(new ClusterManager.OnClusterItemInfoWindowClickListener<AtmLocator>() {
            @Override
            public void onClusterItemInfoWindowClick(AtmLocator atmLocator) {
                Log.e("TAG", "onClusterItemInfoWindowClick: "+atmLocator.getLatitude() );
                Uri gmmIntentUri = Uri.parse("google.navigation:q="+atmLocator.getLatitude()+","+atmLocator.getLongitude());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
                // TODO Auto-generated method stub

            }
        });


        googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition arg0) {

                lat  = maps.getCameraPosition().target;
                Log.e("TAG", "onCameraChange: "+lat );

                if(BtnSwitch){
                    loadData(lat);
                }
                //CameraUpdate updateToMyLocation = CameraUpdateFactory.newLatLngZoom(lat, 16);
            }
        });


    }



    public void loadData(LatLng latlong) {
        showedNearbyOutlet.clear();


        progressHorizontal.setVisibility(View.VISIBLE);
        if(mTBatm.isChecked()){
            atm = true;
        } else {
            atm = false;
        }

        if(mTBbranch.isChecked()){
            branch = true;
        }else {
            branch = false;
        }

        if(mTBcdm.isChecked()){
            cdm = true;
        }else{
            cdm = false;
        }

        Log.e("TAG", "loadData: "+atm);
        Log.e("TAG", "loadData: "+branch);
        Log.e("TAG", "loadData: "+cdm);


        Call<ArrayList<AtmLocator>> AtmLocator = service.getAtmLocator(loginPref.token().get(), Id, latlong.latitude+"", latlong.longitude+"",atm+"",branch+"",cdm+"");
        AtmLocator.enqueue(new Callback<ArrayList<AtmLocator>>() {
                @Override
                public void onResponse(Response<ArrayList<AtmLocator>> response, Retrofit retrofit) {

                    Log.e("TAG", "NearbyResponse: "+response.code());
                    if (response.code()==200) {
                        progressHorizontal.setVisibility(View.INVISIBLE);

                        allNearbyOutlet.clear();
                        allNearbyOutlet.addAll(response.body());

                        mClusterManager.addItems(allNearbyOutlet);
                        mClusterManager.cluster();

                        mTBatm.setOnCheckedChangeListener(ActivityLocator.this);
                        mTBbranch.setOnCheckedChangeListener(ActivityLocator.this);
                        mTBcdm.setOnCheckedChangeListener(ActivityLocator.this);



                    } else if (response.code() == 401) {

                    } else if (response.code() == 404) {

                    } else if (response.code() == 500) {

                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    progressHorizontal.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Something went wrong. Please try again later", Toast.LENGTH_LONG).show();
                }
            });

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

    /*
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
*/

    @Click(R.id.Location)
    public void LocationClick(){
        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        loadData(lat);
    }

}

