package co.kartoo.app.inappmaps;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import co.kartoo.app.R;
import co.kartoo.app.models.LoginPref_;
import co.kartoo.app.rest.PromoService;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;


@EActivity(R.layout.in_app_maps)
public class InAppMaps extends AppCompatActivity implements OnMapReadyCallback {

    @ViewById
    Toolbar mToolbar;
    @ViewById
    TextView mTVtitle;

    LatLng location;
    SupportMapFragment mapFragment;

    String latitude, longitude, mallName;


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
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        setUpNavDrawer();
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        latitude = intent.getStringExtra("latitude");
        longitude = intent.getStringExtra("longitude");
        longitude = intent.getStringExtra("longitude");
        mallName = intent.getStringExtra("mallName");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        location = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));

        googleMap.getUiSettings().setMapToolbarEnabled(false);

        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(location.latitude, location.longitude))
                .title(mallName)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.bank_locate)));

        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q="+latitude+","+longitude);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        CameraUpdate updateToMyLocation = CameraUpdateFactory.newLatLngZoom(location, 16);
        googleMap.moveCamera(updateToMyLocation);

    }


    @Click(R.id.Location)
    public void LocationClick(){
        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }

}

