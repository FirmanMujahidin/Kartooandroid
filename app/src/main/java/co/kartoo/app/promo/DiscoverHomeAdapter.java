package co.kartoo.app.promo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import co.kartoo.app.R;
import co.kartoo.app.rest.model.newest.DiscoverPromotion;

public class DiscoverHomeAdapter extends RecyclerView.Adapter<DiscoverHomeViewHolder> {

    ArrayList<DiscoverPromotion> listPromo;
    Context mContext;
    public DiscoverHomeAdapter(Context mContext, ArrayList<DiscoverPromotion> listPromo) {
        this.listPromo = listPromo;
        this.mContext = mContext;
    }

    @Override
    public DiscoverHomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DiscoverHomeViewHolder(mContext, LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_home_promo, parent, false));
        /*
        if (viewType == 4) {
            return new DiscoverHomeViewHolder(mContext, LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_home_promo_big, null));
        } else {
            return new DiscoverHomeViewHolder(mContext, LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_home_promo, parent, false));
        }
        */
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(DiscoverHomeViewHolder holder, int position) {

        if (position == 4) {
            //StaggeredGridLayoutManager.LayoutParams layoutParams = new StaggeredGridLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            //layoutParams.setMargins(1,1,1,1);
            //layoutParams.setFullSpan(true);
            //holder.itemView.setLayoutParams(layoutParams);
        }
        holder.bind(listPromo.get(position));
    }

    @Override
    public int getItemCount() {
        return listPromo.size();
    }

    public void addItem(DiscoverPromotion promo) {
        listPromo.add(promo);
    }
}
