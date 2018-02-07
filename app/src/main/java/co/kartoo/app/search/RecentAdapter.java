package co.kartoo.app.search;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import co.kartoo.app.R;
import co.kartoo.app.promo.AvailableOutlet.TrendingPromoViewHolderCategory;
import co.kartoo.app.rest.model.newest.DiscoverPromotionCategory;
import co.kartoo.app.rest.model.newest.SearchRecent;
import co.kartoo.app.rest.model.newest.SearchResultSuggestion;
import co.kartoo.app.rest.model.newest.SearchSuggestion;

public class RecentAdapter extends RecyclerView.Adapter<RecentViewHolder> {

    ArrayList<SearchResultSuggestion> listRecent;
    Context mContext;

    public RecentAdapter(Context mContext, ArrayList<SearchResultSuggestion> listRecent) {
        Log.e("adapter", "initiated " + listRecent.size() + " items");
        this.listRecent = listRecent;
        this.mContext = mContext;
    }

    @Override
    public RecentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecentViewHolder(mContext, LayoutInflater.from(parent.getContext()).inflate(R.layout.list_search_recent, parent, false));
    }

    @Override
    public void onBindViewHolder(RecentViewHolder holder, int position) {
        Log.e("adapter", "bind "+position);
        holder.bind(listRecent.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return listRecent.size();
    }
}