package co.kartoo.app.promo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import co.kartoo.app.R;
import co.kartoo.app.rest.model.newest.DiscoverPromotion;

/**
 * Created by MartinOenang on 11/14/2015.
 */
public class LikedPromoAdapter extends RecyclerView.Adapter<DiscoverHomeViewHolder>  {

    ArrayList<DiscoverPromotion> listPromo;
    Context mContext;

    public LikedPromoAdapter(Context mContext, ArrayList<DiscoverPromotion> listPromo) {
        this.listPromo = listPromo;
        this.mContext = mContext;
    }

    @Override
    public DiscoverHomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DiscoverHomeViewHolder(mContext, LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_home_promo, parent, false));
    }

    @Override
    public void onBindViewHolder(DiscoverHomeViewHolder holder, int position) {
        holder.bind(listPromo.get(position));
    }

    @Override
    public int getItemCount() {
        return listPromo.size();
    }
}
