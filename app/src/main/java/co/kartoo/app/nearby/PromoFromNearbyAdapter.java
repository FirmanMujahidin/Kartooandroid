package co.kartoo.app.nearby;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import co.kartoo.app.R;
import co.kartoo.app.rest.model.newest.Nearby;

public class PromoFromNearbyAdapter extends RecyclerView.Adapter<PromoFromNearbyViewHolder> {

    ArrayList<Nearby> listPromo;
    Context mContext;

    public PromoFromNearbyAdapter(Context mContext, ArrayList<Nearby> listPromo) {
        Log.e("adapter", "initiated " + listPromo.size() + " items");
        this.listPromo = listPromo;
        this.mContext = mContext;
    }

    @Override
    public PromoFromNearbyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PromoFromNearbyViewHolder(mContext, LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_promo_category, parent, false));
    }

    @Override
    public void onBindViewHolder(PromoFromNearbyViewHolder holder, int position) {
        Log.e("adapter", "bind "+position);
        holder.bind(listPromo.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return listPromo.size();
    }
}