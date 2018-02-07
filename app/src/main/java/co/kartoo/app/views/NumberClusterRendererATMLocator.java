package co.kartoo.app.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
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
import co.kartoo.app.rest.model.newest.AtmLocator;
import co.kartoo.app.rest.model.newest.Nearby;

/**
 * Created by MartinOenang on 10/24/2015.
 */
public class NumberClusterRendererATMLocator extends DefaultClusterRenderer<AtmLocator> {
    private IconGenerator mClusterIconGenerator;
    private ImageView mImageView;
    private ImageView mClusterImageView;
    private IconGenerator mIconGenerator;
    private Context mContext;

    public NumberClusterRendererATMLocator(Context context, GoogleMap map, ClusterManager<AtmLocator> clusterManager) {
        super(context, map, clusterManager);
        mIconGenerator = new IconGenerator(context.getApplicationContext());
        mClusterIconGenerator = new IconGenerator(context.getApplicationContext());
        mImageView = new ImageView(context.getApplicationContext());
        View customMarker = ((Activity) context).getLayoutInflater().inflate(R.layout.marker, null);
        mClusterIconGenerator.setContentView(customMarker);
        mClusterImageView = (ImageView) customMarker.findViewById(R.id.image);
        mIconGenerator.setContentView(mImageView);
        this.mContext = context;
    }

    @Override
    protected boolean shouldRenderAsCluster(Cluster<AtmLocator> cluster) {
        return cluster.getSize() > 999;
    }

    @Override
    protected void onBeforeClusterRendered(Cluster<AtmLocator> cluster, MarkerOptions markerOptions) {
        super.onBeforeClusterRendered(cluster, markerOptions);
        Drawable drawable = mContext.getResources().getDrawable(R.drawable.ic_pin_burger);
        mClusterImageView.setImageDrawable(drawable);

        Bitmap icon = mClusterIconGenerator.makeIcon("+"+String.valueOf(cluster.getSize()));
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
    }

    @Override
    protected void onBeforeClusterItemRendered(AtmLocator item, MarkerOptions markerOptions) {
        super.onBeforeClusterItemRendered(item, markerOptions);
        mImageView.setImageResource(R.drawable.ic_pin_burger);

        Bitmap icon = mIconGenerator.makeIcon();

        if (item.getCategory().equals("ATM")){
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_atm)).title(item.getName()).snippet(item.getAddress());
        }
        else if (item.getCategory().equals("BRANCH")) {
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_branch)).title(item.getName()).snippet(item.getAddress());
        }
        else if (item.getCategory().equals("CDM")){
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_atm)).title(item.getName()).snippet(item.getAddress());
        }


    }

    @Override
    public Cluster<AtmLocator> getCluster(Marker marker) {
        return super.getCluster(marker);
    }
}
