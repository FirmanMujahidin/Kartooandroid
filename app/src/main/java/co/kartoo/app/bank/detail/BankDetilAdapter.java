package co.kartoo.app.bank.detail;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import co.kartoo.app.R;
import co.kartoo.app.rest.model.newest.BankFeedItem;

/**
 * Created by MartinOenang on 10/12/2015.
 */
public class BankDetilAdapter extends RecyclerView.Adapter<BankDetilViewHolder> {

    ArrayList<BankFeedItem> listBank;
    Context mContext;

    public BankDetilAdapter(Context mContext, ArrayList<BankFeedItem> listBank) {
        Log.e("adapter", "initiated " + listBank.size() + " items");
        this.listBank = listBank;
        this.mContext = mContext;
    }

    @Override
    public BankDetilViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BankDetilViewHolder(mContext, LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bank_feed, parent, false));
    }

    @Override
    public void onBindViewHolder(BankDetilViewHolder holder, int position) {
        Log.e("adapter", "bind "+position);
        holder.bind(listBank.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return listBank.size();
    }
}