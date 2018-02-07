package co.kartoo.app.promo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import co.kartoo.app.R;
import co.kartoo.app.rest.model.newest.EditorsPick;

public class HighlightPromoAdapter extends RecyclerView.Adapter<HighlightPromoViewHolder> {

    ArrayList<EditorsPick> listPromo;
    Context mContext;

    public HighlightPromoAdapter(Context mContext, ArrayList<EditorsPick> listPromo) {
        Log.e("adapter", "initiated " + listPromo.size() + " items");
        this.listPromo = listPromo;
        this.mContext = mContext;
    }

    @Override
    public HighlightPromoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HighlightPromoViewHolder(mContext, LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_home_outlet, parent, false));
    }

    @Override
    public void onBindViewHolder(HighlightPromoViewHolder holder, int position) {
        Log.e("adapter", "bind "+position);
        int pos = position % 5;
        holder.bind(listPromo.get(pos));
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        //Log.e("TAG", " Highlights getItemCount: " + listPromo.size());
        return listPromo.size() * 2000;
        //return Integer.MAX_VALUE;
    }
}