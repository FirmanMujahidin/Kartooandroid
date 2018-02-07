package co.kartoo.app.bank;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import co.kartoo.app.R;
import co.kartoo.app.rest.PromoService;
import co.kartoo.app.rest.model.newest.BankPage;

/**
 * Created by Luthfi Apriyanto on 4/19/2016.
 */
public class AdapterBankList extends RecyclerView.Adapter<ViewHolderBankList> {

    ArrayList<BankPage> followingBank;
    ArrayList<BankPage> listBank;
    Context mContext;
    String authorization;
    PromoService promoService;

    public AdapterBankList(Context mContext, ArrayList<BankPage> listOutlet, PromoService promoService, String authorization) {
        this.mContext = mContext;
        this.listBank = listOutlet;
        this.followingBank = new ArrayList<>();
        this.authorization = authorization;
        this.promoService = promoService;
    }

    @Override
    public ViewHolderBankList onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolderBankList(mContext, LayoutInflater.from(parent.getContext()).inflate(R.layout.list_bank, parent, false), promoService, authorization);
    }

    @Override
    public void onBindViewHolder(ViewHolderBankList holder, int position) {
        boolean isFollowing = false;

        for (int i = 0; i < followingBank.size(); i++) {
            if (listBank.get(position).getId() == followingBank.get(i).getId()) {
                isFollowing = true;
            }
        }

        holder.bind(listBank.get(position), isFollowing);
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