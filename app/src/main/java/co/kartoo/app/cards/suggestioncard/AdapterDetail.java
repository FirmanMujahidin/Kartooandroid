package co.kartoo.app.cards.suggestioncard;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import co.kartoo.app.R;
import co.kartoo.app.cards.suggestioncard.ViewHolderPerks;
import co.kartoo.app.rest.model.newest.Details;
import co.kartoo.app.rest.model.newest.Perks;

/**
 * Created by Luthfi Apriyanto on 4/19/2016.
 */
public class AdapterDetail extends RecyclerView.Adapter<ViewHolderDetail> {

    ArrayList<Details> listDetail;
    Context mContext;

    private static final int LIST_ITEM_VIEW = 1;
    private static final int LIST_HEADER_VIEW = 2;

    public AdapterDetail(Context mContext, ArrayList<Details> listDetail) {
        this.mContext = mContext;
        this.listDetail = listDetail;
    }

    @Override
    public ViewHolderDetail onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.e("TAG", "onCreateViewHolder: "+viewType);
        return new ViewHolderDetail(mContext, LayoutInflater.from(parent.getContext()).inflate(R.layout.list_detail_header, parent, false));
/*
        if (viewType == LIST_ITEM_VIEW) {
            return new ViewHolderDetail(mContext, LayoutInflater.from(parent.getContext()).inflate(R.layout.list_detail, parent, false));
        }
        else {
            return new ViewHolderDetail(mContext, LayoutInflater.from(parent.getContext()).inflate(R.layout.list_detail, parent, false));
        }
        */
    }

    @Override
    public void onBindViewHolder(ViewHolderDetail holder, int position) {
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