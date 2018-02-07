package co.kartoo.app.promo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import co.kartoo.app.R;
import co.kartoo.app.rest.model.newest.Availablecards;

/**
 * Created by MartinOenang on 11/19/2015.
 */
public class ApplicableCardsAdapter extends RecyclerView.Adapter<ApplicableCardsViewHolder> {
    ArrayList<Availablecards> listApplicableCards;
    Context mContext;

    public ApplicableCardsAdapter(ArrayList<Availablecards> listApplicableCards, Context mContext) {
        this.listApplicableCards = listApplicableCards;
        this.mContext = mContext;
    }

    @Override
    public ApplicableCardsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ApplicableCardsViewHolder(mContext, LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_applicable_card, parent, false));
    }

    @Override
    public void onBindViewHolder(ApplicableCardsViewHolder holder, int position) {
        holder.bind(listApplicableCards.get(position));
    }

    @Override
    public int getItemCount() {
        return listApplicableCards.size();
        }
}
