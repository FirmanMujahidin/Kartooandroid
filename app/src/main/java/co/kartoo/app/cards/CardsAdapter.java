package co.kartoo.app.cards;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import co.kartoo.app.R;
import co.kartoo.app.rest.model.newest.Availablecards;

public class CardsAdapter extends RecyclerView.Adapter<CardsViewHolder> {
//    67:12:ED:55:D0:09:F3:E8:E1:8B:65:55:55:1E:57:0D:83:50:16:0F
    ArrayList<Availablecards> listCard;
    Context mContext;
    String userId;
    String token;

    public CardsAdapter(ArrayList<Availablecards> listCard, Context mContext, String token) {
        Log.e("TAG", "CardsAdapter: "+token);
        Log.e("CardAdapter", "initiated " + listCard.size() + " items");
        this.listCard = listCard;
        this.mContext = mContext;
        this.userId = userId;
        this.token = token;
    }

    @Override
    public CardsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CardsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_my_cards, parent, false), mContext, token);
    }

    @Override
    public void onBindViewHolder(CardsViewHolder holder, int position) {
        holder.bind(listCard.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return listCard.size();
    }
}
