package co.kartoo.app.mall;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import co.kartoo.app.R;
import co.kartoo.app.nearby.PromoFromNearbyViewHolder;
import co.kartoo.app.rest.model.newest.MallDetail;
import co.kartoo.app.rest.model.newest.Nearby;


public class MallAdapter extends RecyclerView.Adapter<MallViewHolder> {

    ArrayList<MallDetail> listPromo;
    Context mContext;

    public MallAdapter(Context mContext, ArrayList<MallDetail> listPromo) {
        Log.e("adapter", "initiated " + listPromo.size() + " items");
        this.listPromo = listPromo;
        this.mContext = mContext;
    }

    @Override
    public MallViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MallViewHolder(mContext, LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mall, parent, false));
    }

    @Override
    public void onBindViewHolder(MallViewHolder holder, int position) {
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