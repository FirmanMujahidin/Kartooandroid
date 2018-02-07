package co.kartoo.app.promo.AvailableOutlet;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import co.kartoo.app.R;
import co.kartoo.app.inappmaps.InAppMaps_;
import co.kartoo.app.rest.model.newest.Availableoutlets;

public class ApplicableOutletViewHolderDetail extends RecyclerView.ViewHolder implements View.OnClickListener {
    Context mContext;
    TextView list_item_available_outlet;
    Availableoutlets card;
    TextView address;
    ImageView Phone;
    LinearLayout onClick;
    String call,latlong, latitude, longitude, nameLocation;


    public ApplicableOutletViewHolderDetail(Context mContext, View itemView) {
        super(itemView);
        Phone = (ImageView) itemView.findViewById(R.id.Phone);
        list_item_available_outlet = (TextView) itemView.findViewById(R.id.list_item_available_outlet);
        address = (TextView) itemView.findViewById(R.id.address);
        onClick = (LinearLayout) itemView.findViewById(R.id.onClick);
        this.mContext = mContext;
        Phone.setOnClickListener(this);
        onClick.setOnClickListener(this);
        list_item_available_outlet.setOnClickListener(this);
    }

    public void bind(Availableoutlets card) {
        list_item_available_outlet.setText(card.getName());
        address.setText(card.getAddress());
        call = card.getTelephone();
        latitude = card.getLatitude();
        longitude = card.getLongitude();
        nameLocation = card.getName();
        latlong = card.getLatitude()+","+card.getLongitude();
        this.card = card;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == Phone.getId()){

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

            /*
            Uri gmmIntentUri = Uri.parse("geo:"+latlong+"?q="+latlong);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            v.getContext().startActivity(mapIntent);
            */
        }

    }
}
