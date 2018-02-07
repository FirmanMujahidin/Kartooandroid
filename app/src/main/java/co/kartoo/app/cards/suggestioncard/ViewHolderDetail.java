package co.kartoo.app.cards.suggestioncard;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import co.kartoo.app.R;
import co.kartoo.app.rest.model.newest.DetailItem;
import co.kartoo.app.rest.model.newest.Details;
import co.kartoo.app.rest.model.newest.Perks;

public class ViewHolderDetail extends RecyclerView.ViewHolder implements View.OnClickListener {

    RecyclerView mRVitem;
    TextView mTVheader;
    Context mContext;
    Details details;



    public ViewHolderDetail(Context mContext, View itemView) {
        super(itemView);
        mTVheader = (TextView) itemView.findViewById(R.id.mTVheader) ;
        mRVitem = (RecyclerView) itemView.findViewById(R.id.item);

        itemView.setOnClickListener(this);

        this.mContext = mContext;
    }

    public void bind(Details details) {
        Log.e("TAG", "bind: "+details.getDetailItems().size());

        List<DetailItem> itemList = new ArrayList<>();

        for (int i = 0; i < details.getDetailItems().size(); i++) {
            DetailItem item = new DetailItem(details.getDetailItems().get(i).getItemKey(), details.getDetailItems().get(i).getItemValue());
            itemList.add(item);
        }

        AdapterAdditional adapter = new AdapterAdditional(itemList);
        mRVitem.setNestedScrollingEnabled(false);
        mRVitem.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRVitem.setAdapter(adapter);

        mTVheader.setText(details.getHeader());

        this.details = details;
    }

    @Override
    public void onClick(View v) {

    }
}