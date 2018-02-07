package co.kartoo.app.rest.model.newest;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by Luthfi Apriyanto on 3/11/2016.
 */
public class ClusterAvailableOutlet implements ClusterItem {
    private final LatLng mPosition;

    public ClusterAvailableOutlet(double lat, double lng) {
        mPosition = new LatLng(lat, lng);
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }
}