package co.kartoo.app.search;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import co.kartoo.app.DetailPromoActivity;
import co.kartoo.app.R;
import co.kartoo.app.cards.suggestioncard.AdapterAdditional;
import co.kartoo.app.rest.PromoService;
import co.kartoo.app.rest.model.newest.DetailItem;
import co.kartoo.app.rest.model.newest.DiscoverPromotionCategory;
import co.kartoo.app.rest.model.newest.SearchRecent;
import co.kartoo.app.rest.model.newest.SearchResultSuggestion;
import co.kartoo.app.rest.model.newest.SearchSuggestion;

public class RecentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView mTVheader;
    RecyclerView item;
    Context mContext;
    SearchResultSuggestion searchRecent;

    public RecentViewHolder(Context mContext, View itemView) {
        super(itemView);
        Log.e("viewholder", "initated");
        mTVheader = (TextView) itemView.findViewById(R.id.mTVheader);
        item = (RecyclerView) itemView.findViewById(R.id.item);
        itemView.setOnClickListener(this);
        this.mContext = mContext;
    }

    public void bind(SearchResultSuggestion searchRecent) {
        List<SearchRecent> itemList = new ArrayList<>();

        for (int i = 0; i < searchRecent.getResultList().size(); i++) {
            SearchRecent item = new SearchRecent(searchRecent.getResultList().get(i).getResult());
            itemList.add(item);
        }

        AdapterList adapter = new AdapterList(itemList);
        item.setNestedScrollingEnabled(false);
        item.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        item.setAdapter(adapter);

        Log.e("TAG", "searchRecent: "+searchRecent.getHeader());
        mTVheader.setText(searchRecent.getHeader());

        this.searchRecent = searchRecent;
    }

    @Override
    public void onClick(View v) {
        //((RecentInterface) v.getContext()).userItemClick(getAdapterPosition());
    }
}
