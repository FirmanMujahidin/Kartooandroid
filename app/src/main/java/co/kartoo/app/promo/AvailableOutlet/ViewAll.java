package co.kartoo.app.promo.AvailableOutlet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;

import co.kartoo.app.R;
import co.kartoo.app.rest.PromoService;
import co.kartoo.app.rest.model.newest.Availableoutlets;
import co.kartoo.app.rest.model.newest.PopularDetail;
import co.kartoo.app.rest.model.newest.ViewAllOutlet;
import co.kartoo.app.views.NumberClusterRendererDetailPromo;
import co.kartoo.app.views.SpaceItemDecoration;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class ViewAll extends AppCompatActivity implements OnMapReadyCallback {

    Retrofit retrofit;
    SupportMapFragment mapFragment;
    GoogleMap googleMap;
    ArrayList<Availableoutlets> allNearbyOutlet;
    ArrayList<Availableoutlets> showedNearbyOutlet;
    ArrayList<Availableoutlets> listOutltet;
    ArrayList<Availableoutlets> listLatestPromo;
    LatLng myLocation;
    ProgressBar progressBar;
    Toolbar mToolbar;


    private boolean loading = false;
    int lastPage = 1;
    int maxPage = -1;

    ClusterManager<Availableoutlets> mClusterManager;

    RecyclerView mLVseeAll;
    ApplicableOutletAdapterDetail adapterDetail;

    private LatLngBounds.Builder bounds;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);

        mLVseeAll = (RecyclerView) findViewById(R.id.mLVseeAll);

        mToolbar = (Toolbar) findViewById(R.id.mToolbar);

        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }

        setUpNavDrawer();
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Intent intent = getIntent();
        final String idPromotion = intent.getStringExtra("Id");
        SharedPreferences bb = getSharedPreferences("my_prefs", 0);
        final String token = bb.getString("MID", null);

        retrofit = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
        final PromoService promoService = retrofit.create(PromoService.class);

        Call<ViewAllOutlet> discover = promoService.getPromoActivityViewAll(token, idPromotion, 1);
        myLocation = getCurrentLocation();
        allNearbyOutlet = new ArrayList<>();
        showedNearbyOutlet = new ArrayList<>();

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        discover.enqueue(new Callback<ViewAllOutlet>() {
            @Override
            public void onResponse(Response<ViewAllOutlet> response, Retrofit retrofit) {
                Log.e("TAG", "onResponsePopular: " + response.code());
                if (response.isSuccess()) {
                    if (response.code() == 200) {
                        Log.e("TAG", "jumlah: "+response.body().getOutletDTOs().size());
                        listOutltet = response.body().getOutletDTOs();
                        showListOutlet();
                        }
                    }
                }
            @Override
            public void onFailure(Throwable t) {
            }
        });

        //int spacingInPixels = 3;
        //mLVseeAll.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
        //mLVseeAll.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mLVseeAll.setLayoutManager(new LinearLayoutManager(ViewAll.this));

        //adapterRecent = new RecentAdapter(SearchActivity.this, filteredList);
        //mRVsuggestion.setAdapter(adapterRecent);

    }



    @Override
    public void onMapReady(final GoogleMap googleMap) {
        myLocation = getCurrentLocation();

        LatLng latlong = new LatLng(-6.227908, 106.828797);
        //Log.e("TAG", "onMapReady: "+myLocation.toString());
        CameraUpdate updateToMyLocation = CameraUpdateFactory.newLatLngZoom(myLocation, 10);
        if (myLocation != null) {
            googleMap.moveCamera(updateToMyLocation);
            Log.e("TAG", "oncallcluster: " + "idPromotion");

            mClusterManager = new ClusterManager<Availableoutlets>(this, googleMap);
            mClusterManager.setRenderer(new NumberClusterRendererDetailPromo(this, googleMap, mClusterManager));
            googleMap.setOnCameraChangeListener(mClusterManager);
            googleMap.setOnMarkerClickListener(mClusterManager);

            Intent intent = getIntent();
            String idPromotion = intent.getStringExtra("Id");

            SharedPreferences bb = getSharedPreferences("my_prefs", 0);
            String token = bb.getString("MID", null);

            bounds = new LatLngBounds.Builder();

            retrofit = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
            PromoService promoService = retrofit.create(PromoService.class);
            Log.e("TAG", "oncallcluster: " + idPromotion);
            Log.e("TAG", "oncallcluster: " + token);
            Call<PopularDetail> discover = promoService.getPromoActivityPopular(token, idPromotion);
            discover.enqueue(new Callback<PopularDetail>() {
                @Override
                public void onResponse(Response<PopularDetail> response, Retrofit retrofit) {
                    Log.e("TAG", "onResponseCluster: " + response.code());
                    if (response.isSuccess()) {
                        if (response.code() == 200) {
                            Log.e("TAG", "onResponseCluster: " + response.body().getName());
                            int a = response.body().getAvailableoutlets().size();
                            for (int i = 0; i < a; i++) {
                                Log.e("TAG", "onResponse: " + response.body().getAvailableoutlets().get(i).getLatitude());
                                allNearbyOutlet.clear();
                                allNearbyOutlet.addAll(response.body().getAvailableoutlets());
                                mClusterManager.addItems(allNearbyOutlet);
                                mClusterManager.cluster();


                                double lati = Double.parseDouble(response.body().getAvailableoutlets().get(i).getLatitude());
                                double longi = Double.parseDouble(response.body().getAvailableoutlets().get(i).getLongitude());

                                bounds.include(new LatLng(lati, longi));
                            }

                            googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 100));

                        }
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                }
            });
        }
    }


    public LatLng getCurrentLocation() {
        return new LatLng(-6.227908, 106.828797);
    }

    private void showListOutlet(){
        mLVseeAll.setAdapter(new ApplicableOutletAdapterDetail(listOutltet, this));
    }

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
}
