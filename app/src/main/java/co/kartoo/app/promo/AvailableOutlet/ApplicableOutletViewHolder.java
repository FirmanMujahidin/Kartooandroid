package co.kartoo.app.promo.AvailableOutlet;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import co.kartoo.app.R;
import co.kartoo.app.inappmaps.InAppMaps_;
import co.kartoo.app.rest.model.newest.Availableoutlets;

/**
 * Created by MartinOenang on 11/19/2015.
 */
public class ApplicableOutletViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    Context mContext;
    TextView list_item_available_outlet;
    ImageView imageView7;
    String call;
    String latlong, nameLocation, latitude, longitude;

    Availableoutlets card;

    public ApplicableOutletViewHolder(Context mContext, View itemView) {
        super(itemView);

        list_item_available_outlet = (TextView) itemView.findViewById(R.id.list_item_available_outlet);
        imageView7 = (ImageView) itemView.findViewById(R.id.imageView7);
        this.mContext = mContext;
        list_item_available_outlet.setOnClickListener(this);
        imageView7.setOnClickListener(this);

    }

    public void bind(Availableoutlets card) {
        list_item_available_outlet.setText(card.getName());
        call = card.getTelephone();
        latlong = card.getLatitude()+","+card.getLongitude();
        latitude = card.getLatitude();
        longitude = card.getLongitude();
        nameLocation = card.getName();

        this.card = card;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == imageView7.getId()){
            if (call==null){
                Toast.makeText(v.getContext(), "There is no Phone Number", Toast.LENGTH_LONG).show();
            }
            else {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:"+call));
                v.getContext().startActivity(callIntent);
            }
        }
        else {
            Intent newIntent = new Intent(v.getContext(), InAppMaps_.class);
            newIntent.putExtra("latitude", latitude);
            newIntent.putExtra("longitude", longitude);
            newIntent.putExtra("mallName", nameLocation);
            v.getContext().startActivity(newIntent);
        }
    }
}
