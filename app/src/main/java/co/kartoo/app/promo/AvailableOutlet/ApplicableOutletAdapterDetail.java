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
public class ApplicableOutletAdapterDetail extends RecyclerView.Adapter<ApplicableOutletViewHolderDetail> {
    ArrayList<Availableoutlets> listApplicableCards;
    Context mContext;

    public ApplicableOutletAdapterDetail(ArrayList<Availableoutlets> listApplicableCards, Context mContext) {
        this.listApplicableCards = listApplicableCards;
        this.mContext = mContext;
    }

    @Override
    public ApplicableOutletViewHolderDetail onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ApplicableOutletViewHolderDetail(mContext, LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_available_outlet_detail, parent, false));
    }

    @Override
    public void onBindViewHolder(ApplicableOutletViewHolderDetail holder, int position) {
        holder.bind(listApplicableCards.get(position));
    }

    @Override
    public int getItemCount() {
        return listApplicableCards.size();
    }
}
