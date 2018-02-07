package co.kartoo.app.cards.suggestioncard;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import co.kartoo.app.R;
import co.kartoo.app.cards.suggestioncard.applycard.ApplyLandingActivity_;
import co.kartoo.app.rest.model.newest.Availablecards;
import co.kartoo.app.rest.model.newest.CardDetail;
import co.kartoo.app.rest.model.newest.DetailItem;
import co.kartoo.app.rest.model.newest.Details;

public class ViewHolderMoreCards extends RecyclerView.ViewHolder implements View.OnClickListener {

    ImageView mIVcard;
    TextView mTVname, mTVincome, mTVperks;
    Context mContext;
    Availablecards details;

    String cardID, cardName;
    String shortPerks;




    public ViewHolderMoreCards(Context mContext, View itemView) {
        super(itemView);
        mTVname = (TextView) itemView.findViewById(R.id.mTVname) ;
        mTVincome = (TextView) itemView.findViewById(R.id.mTVincome) ;
        mTVperks = (TextView) itemView.findViewById(R.id.mTVperks) ;

        mIVcard = (ImageView) itemView.findViewById(R.id.mIVcard);

        itemView.setOnClickListener(this);

        this.mContext = mContext;
    }

    public void bind(Availablecards details) {

        cardID = details.getId();
        cardName = details.getCard_Edition();
        mTVname.setText(details.getCard_Edition());
        mTVincome.setText(details.getMinimumIncome());
        //mTVperks.setText(details.getShortPerks());
        shortPerks = "";

        if (details.getShortPerks().size()!=0){
            for (int i = 0; i < details.getShortPerks().size(); i++) {
                shortPerks += "- "+details.getShortPerks().get(i)+"\n";
            }
        }

        mTVperks.setText(shortPerks);


        Log.e("TAG", "shortPerks: "+details.getShortPerks().size());

        Picasso.with(mContext).load(details.getUrl_img())
                .fit()
                .placeholder(R.color.placeholder)
                .into(mIVcard);

        this.details = details;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(mContext, DetailCardActivity_.class);
        intent.putExtra("cardID", cardID);
        intent.putExtra("cardName", cardName);
        mContext.startActivity(intent);
    }
}