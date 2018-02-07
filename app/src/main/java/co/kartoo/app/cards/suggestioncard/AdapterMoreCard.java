package co.kartoo.app.cards.suggestioncard;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import co.kartoo.app.R;
import co.kartoo.app.rest.model.newest.Availablecards;
import co.kartoo.app.rest.model.newest.Details;

/**
 * Created by Luthfi Apriyanto on 4/19/2016.
 */
public class AdapterMoreCard extends RecyclerView.Adapter<ViewHolderMoreCards> {

    ArrayList<Availablecards> listDetail;
    Context mContext;


    public AdapterMoreCard(Context mContext, ArrayList<Availablecards> listDetail) {
        this.mContext = mContext;
        this.listDetail = listDetail;
    }

    @Override
    public ViewHolderMoreCards onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.e("TAG", "onCreateViewHolder: "+viewType);
        return new ViewHolderMoreCards(mContext, LayoutInflater.from(parent.getContext()).inflate(R.layout.list_more_cards, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolderMoreCards holder, int position) {
        holder.bind(listDetail.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        Log.e("TAG", "getItemCount: "+listDetail.size() );
        return listDetail.size();
    }
}