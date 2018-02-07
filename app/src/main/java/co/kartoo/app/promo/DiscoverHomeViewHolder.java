package co.kartoo.app.promo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import co.kartoo.app.DetailPromoActivity;
import co.kartoo.app.R;
import co.kartoo.app.rest.model.newest.Discover;
import co.kartoo.app.rest.model.newest.DiscoverPromotion;
import co.kartoo.app.views.SquareRelativeLayout;

public class DiscoverHomeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView mTVdiscount;
    TextView mTVoutletName;
    TextView mTVbank, mTVband;
    DiscoverPromotion promo;
    RelativeLayout band;
    Context mContext;
    SquareRelativeLayout mSRLmain;
    ImageView mIVdiscover;
    String id, imageHeader, textHeader;

    public DiscoverHomeViewHolder(Context mContext, View itemView) {
        super(itemView);
        mTVdiscount = (TextView) itemView.findViewById(R.id.mTVdiscount);
        mTVoutletName = (TextView) itemView.findViewById(R.id.mTVoutletName);
        mTVbank = (TextView) itemView.findViewById(R.id.mTVbank);
        mSRLmain = (SquareRelativeLayout) itemView.findViewById(R.id.mSRLmain);
        mIVdiscover = (ImageView) itemView.findViewById(R.id.mIVdiscover);
        mTVband = (TextView) itemView.findViewById(R.id.mTVband);
        band = (RelativeLayout) itemView.findViewById(R.id.band);
        itemView.setOnClickListener(this);
        this.mContext = mContext;
    }

    public void bind(DiscoverPromotion promo) {
        final DiscoverPromotion prom = promo;
        imageHeader = promo.getUrl_img();
        textHeader = promo.getPromotion();

        this.mTVdiscount.setText(promo.getMerchant());
        try {
            this.mTVoutletName.setText(promo.getPromotion());
        }
        catch (Exception e) {
            this.mTVoutletName.setText("");
        }
        try {
            this.mTVbank.setText(promo.getBank());
        } catch (Exception e) {
            this.mTVbank.setText("");
        }

        if (promo.getBand()!=null){
            if (!promo.getBand().equals("")){
                mTVband.setText(promo.getBand());
            }
        }
        else {
            band.setVisibility(View.GONE);
        }

        band.setVisibility(View.GONE);
        id = promo.getId();

        Picasso.with(mContext).load(promo.getUrl_img())
                .placeholder(R.color.placeholder)
                .fit()
                .centerCrop()
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(mIVdiscover, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(mContext).load(prom.getUrl_img())
                                .placeholder(R.color.placeholder)
                                .fit()
                                .centerCrop()
                                .into(mIVdiscover);
                    }
                });
        this.promo = promo;
    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent(v.getContext(), DetailPromoActivity.class);
        intent.putExtra("Id", promo.getId());
        intent.putExtra("imageHeader", imageHeader);
        intent.putExtra("textHeader", textHeader);
        intent.putExtra("outletName", promo.getMerchant());
        Log.e("idEditor", "item clicked"+promo.getId());
        v.getContext().startActivity(intent);
    }
}