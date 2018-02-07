package co.kartoo.app.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

import co.kartoo.app.R;
import co.kartoo.app.rest.model.newest.Availableoutlets;
import co.kartoo.app.rest.model.newest.Nearby;

/**
 * Created by MartinOenang on 10/24/2015.
 */
public class NumberClusterRendererDetailPromo extends DefaultClusterRenderer<Availableoutlets> {
    private Context mContext;
    public NumberClusterRendererDetailPromo(Context context, GoogleMap map, ClusterManager<Availableoutlets> clusterManager) {
        super(context, map, clusterManager);
        this.mContext = context;
    }

    @Override
    protected boolean shouldRenderAsCluster(Cluster<Availableoutlets> cluster) {
        return cluster.getSize() > 3;
    }

    @Override
    protected void onBeforeClusterRendered(Cluster<Availableoutlets> cluster, MarkerOptions markerOptions) {
        super.onBeforeClusterRendered(cluster, markerOptions);

    }

    @Override
    protected void onBeforeClusterItemRendered(Availableoutlets item, MarkerOptions markerOptions) {
        super.onBeforeClusterItemRendered(item, markerOptions);
    }

    @Override
    public Cluster<Availableoutlets> getCluster(Marker marker) {
        return super.getCluster(marker);
    }
}
