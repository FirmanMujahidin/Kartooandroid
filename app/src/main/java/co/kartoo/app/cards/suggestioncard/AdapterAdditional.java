package co.kartoo.app.cards.suggestioncard;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import co.kartoo.app.R;
import co.kartoo.app.rest.model.newest.DetailItem;

/**
 * Created by Luthfi Apriyanto on 4/19/2016.
 */
public class AdapterAdditional extends RecyclerView.Adapter<AdapterAdditional.ViewHolderAdditional> {

    private List<DetailItem> item;

    public class ViewHolderAdditional extends RecyclerView.ViewHolder {
        public TextView mTVitemKey, mTVitemValue;

        public ViewHolderAdditional(View view) {
            super(view);
            mTVitemKey = (TextView) view.findViewById(R.id.mTVitemKey);
            mTVitemValue = (TextView) view.findViewById(R.id.mTVitemValue);
        }
    }


    public AdapterAdditional(List<DetailItem> item) {
        this.item = item;
    }

    @Override
    public ViewHolderAdditional onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_detail, parent, false);

        return new ViewHolderAdditional(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolderAdditional holder, int position) {
        DetailItem itemItem = item.get(position);

        holder.mTVitemKey.setText(itemItem.getItemKey());
        holder.mTVitemValue.setText(itemItem.getItemValue());
    }

    @Override
    public int getItemCount() {
        return item.size();
    }
}