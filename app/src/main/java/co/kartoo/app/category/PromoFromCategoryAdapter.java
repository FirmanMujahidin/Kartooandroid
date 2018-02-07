package co.kartoo.app.category;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import co.kartoo.app.R;
import co.kartoo.app.rest.PromoService;
import co.kartoo.app.rest.model.Category;
import co.kartoo.app.rest.model.newest.DiscoverPromotionCategory;

public class PromoFromCategoryAdapter extends RecyclerView.Adapter<PromoFromCategoryViewHolder>  {
    ArrayList<DiscoverPromotionCategory> bookmarkedOutlet;
    ArrayList<Category> listOutlet;
    Context mContext;
    String authorization;
    PromoService promoService;
    RecyclerView recycleView;
    ArrayList<Category> listOutletNew;
    int diff;


    public PromoFromCategoryAdapter(Context mContext, ArrayList<Category> listOutlet, ArrayList<Category> listOutletNew, PromoService promoService, String authorization, RecyclerView recycleView) {
        this.mContext = mContext;
        this.listOutlet = listOutlet;
        this.bookmarkedOutlet = new ArrayList<>();
        this.authorization = authorization;
        this.promoService = promoService;
        this.recycleView = recycleView;
        this.listOutletNew = listOutletNew;
    }

    public boolean isPromotionExist(Category current, ArrayList<Category> newList)
    {
        for(Category n: newList)
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
    public PromoFromCategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PromoFromCategoryViewHolder(mContext, LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_promo_category, parent, false), promoService, authorization);
    }

    @Override
    public void onBindViewHolder(PromoFromCategoryViewHolder holder, int position) {
        Log.e("TAG", "position: " + position);
        //filterSimilarPromotions();
        holder.bind(listOutlet.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        listOutletNew = new ArrayList<>();
//        for(int i = 0; i < listOutlet.size(); i++){
//            Log.e("checkListOutlet", "List Item: " + listOutlet.get(i).getMerchant());
//        }
        //filterSimilarPromotions();
        return listOutlet.size();
    }
}
