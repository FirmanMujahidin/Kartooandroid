package co.kartoo.app.promo.AvailableOutlet;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import co.kartoo.app.R;
import co.kartoo.app.rest.model.newest.Availableoutlets;

/**
 * Created by MartinOenang on 11/19/2015.
 */
public class ApplicableOutletAdapter extends RecyclerView.Adapter<ApplicableOutletViewHolder> {
    ArrayList<Availableoutlets> listApplicableCards;
    Context mContext;

    public ApplicableOutletAdapter(ArrayList<Availableoutlets> listApplicableCards, Context mContext) {
        this.listApplicableCards = listApplicableCards;
        this.mContext = mContext;
    }

    @Override
    public ApplicableOutletViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ApplicableOutletViewHolder(mContext, LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_available_outlet, parent, false));
    }

    @Override
    public void onBindViewHolder(ApplicableOutletViewHolder holder, int position) {
        holder.bind(listApplicableCards.get(position));
    }

    @Override
    public int getItemCount() {
        return listApplicableCards.size();
    }
}
