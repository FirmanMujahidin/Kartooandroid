package co.kartoo.app.search.filter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import co.kartoo.app.R;
import co.kartoo.app.rest.PromoService;
//import co.kartoo.app.rest.model.Category;
import co.kartoo.app.rest.model.newest.DiscoverPromotionCategory;
import co.kartoo.app.rest.model.newest.SearchPromotion;

/**
 * Created by EricLaptop001 on 4/11/2017.
 */

public class PromoFromFilterAdapter extends RecyclerView.Adapter<PromoFromFilterViewHolder> {
    ArrayList<DiscoverPromotionCategory> bookmarkedOutlet;
    ArrayList<SearchPromotion> listOutlet;
    ArrayList<SearchPromotion> listOutletNew;
    Context mContext;
    String authorization;
    PromoService promoService;
    RecyclerView recycleView;
    int diff;


    public PromoFromFilterAdapter(Context mContext, ArrayList<SearchPromotion> listOutlet, PromoService promoService, String authorization, RecyclerView recycleView) {
        this.mContext = mContext;
        this.listOutlet = listOutlet;
        this.bookmarkedOutlet = new ArrayList<>();
        this.authorization = authorization;
        this.promoService = promoService;
        this.recycleView = recycleView;
        this.listOutletNew = new ArrayList<>();
    }

    public boolean isPromotionExist(SearchPromotion current, ArrayList<SearchPromotion> newList)
    {
        for(SearchPromotion n: newList)
        {
            if(n.getId().equals(current.getId()))
            {
                return true;
            }
        }
        return false;
    }

    public void filterSimilarPromotions(){
        for (int i = 0; i < listOutlet.size(); i++)
        {
            if (!isPromotionExist(listOutlet.get(i), listOutletNew))
            {
                listOutletNew.add(listOutlet.get(i));
            }
        }
    }
    @Override
    public PromoFromFilterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PromoFromFilterViewHolder(mContext, LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_promo_category, parent, false), promoService, authorization);
    }

    @Override
    public void onBindViewHolder(PromoFromFilterViewHolder holder, int position) {
        holder.bind(listOutletNew.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return position;

    }

    @Override
    public int getItemCount() {
        listOutletNew = new ArrayList<>();
        filterSimilarPromotions();
        return listOutletNew.size();
    }
}
