package co.kartoo.app.interest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import co.kartoo.app.R;
import co.kartoo.app.rest.PromoService;
import co.kartoo.app.rest.model.newest.Interest;
import co.kartoo.app.rest.model.newest.ListCategory;

public class InterestAdapter extends RecyclerView.Adapter<InterestViewHolder> {
    ArrayList<Interest> interestChoosen;

    ArrayList<ListCategory> listCategory;
    Context mContext;
    String authorization;
    PromoService promoService;

    public InterestAdapter(Context mContext, ArrayList<ListCategory> listCategory, PromoService promoService, String authorization) {
        this.interestChoosen = new ArrayList<>();
        this.mContext = mContext;
        this.listCategory = listCategory;
        this.promoService = promoService;
        this.authorization = authorization;
    }

    @Override
    public InterestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new InterestViewHolder(mContext,LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_interest, parent, false), promoService, authorization);
    }

    @Override
    public void onBindViewHolder(InterestViewHolder holder, int position) {
        boolean isChoosen = false;

        for (int i =0; i < interestChoosen.size(); i++) {
            if (listCategory.get(position).getId().equals(interestChoosen.get(i).getCategory().getId())) {
                isChoosen = true;
            }
        }

        holder.bind(listCategory.get(position), isChoosen);
    }

    public void setInterest(ArrayList<Interest> interestChoosen) {
        this.interestChoosen.clear();
        this.interestChoosen.addAll(interestChoosen);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return listCategory.size()-1;
    }

    public interface RecyclerViewClickListener
    {
        public void recyclerViewListClicked(View v, int position);
    }

}
