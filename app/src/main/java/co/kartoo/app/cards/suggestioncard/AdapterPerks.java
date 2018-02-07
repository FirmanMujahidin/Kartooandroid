package co.kartoo.app.cards.suggestioncard;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import co.kartoo.app.R;
import co.kartoo.app.bank.ViewHolderBankList;
import co.kartoo.app.rest.PromoService;
import co.kartoo.app.rest.model.newest.BankPage;
import co.kartoo.app.rest.model.newest.Perks;

/**
 * Created by Luthfi Apriyanto on 4/19/2016.
 */
public class AdapterPerks extends RecyclerView.Adapter<ViewHolderPerks> {

    ArrayList<Perks> listPerks;
    Context mContext;

    public AdapterPerks(Context mContext, ArrayList<Perks> listPerks) {
        this.mContext = mContext;
        this.listPerks = listPerks;
    }

    @Override
    public ViewHolderPerks onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolderPerks(mContext, LayoutInflater.from(parent.getContext()).inflate(R.layout.list_perks, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolderPerks holder, int position) {
        holder.bind(listPerks.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        Log.e("TAG", "getItemCount: "+listPerks.size() );
        return listPerks.size();
    }
}