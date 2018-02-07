package co.kartoo.app.cards.suggestioncard.applycard;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.ArrayList;
import java.util.List;

import co.kartoo.app.R;
import co.kartoo.app.models.ApplyCreditCardPref;
import co.kartoo.app.models.ApplyCreditCardPref_;
import co.kartoo.app.rest.model.newest.CreditCard;

import static co.kartoo.app.R.id.mIVdelete;

/**
 * Created by Luthfi Apriyanto on 1/16/2017.
 */

public class AdapterCreditCard extends RecyclerView.Adapter<CreditCardViewHolder> {

    List<CreditCard> listCreditCard;
    Context mContext;

    public AdapterCreditCard(Context mContext, List<CreditCard> listCreditCard) {
        this.mContext = mContext;
        this.listCreditCard = listCreditCard;
    }

    @Override
    public CreditCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CreditCardViewHolder(mContext,LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_credit_card, parent, false));
    }

    @Override
    public void onBindViewHolder(CreditCardViewHolder holder, int position) {

        holder.bind(listCreditCard.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return listCreditCard.size();
    }

    public interface RecyclerViewClickListener
    {
        public void recyclerViewListClicked(View v, int position);
    }

}