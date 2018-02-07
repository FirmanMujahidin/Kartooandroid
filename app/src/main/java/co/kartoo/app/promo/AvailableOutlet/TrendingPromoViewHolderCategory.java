package co.kartoo.app.promo.AvailableOutlet;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import co.kartoo.app.DetailPromoActivity;
import co.kartoo.app.R;
import co.kartoo.app.rest.PromoService;
import co.kartoo.app.rest.model.newest.DiscoverPromotionCategory;

public class TrendingPromoViewHolderCategory extends RecyclerView.ViewHolder implements View.OnClickListener {
    ImageView mSRLmain, mIVshare, mIVfavorite, imageBlur;
    TextView mTVdiscount;
    TextView mTVoutletName;
    TextView mTVbank;
    DiscoverPromotionCategory editorsPick;
    Context mContext;
    TextView mTVLocation, mTVband;
    RelativeLayout band;
    boolean isFavorite;
    PromoService promoService;
    String authorization;

    public TrendingPromoViewHolderCategory(Context mContext, View itemView) {
        super(itemView);
        Log.e("viewholder", "initated");
        mIVshare = (ImageView) itemView.findViewById(R.id.mIVshare);
        mIVfavorite = (ImageView) itemView.findViewById(R.id.mIVfavorite);
        mSRLmain = (ImageView) itemView.findViewById(R.id.mLLcontainer);
        mTVoutletName = (TextView) itemView.findViewById(R.id.mTVoutletName);
        mTVdiscount = (TextView) itemView.findViewById(R.id.mTVoutletPromo);
        mTVbank = (TextView) itemView.findViewById(R.id.mTVbank);
        mSRLmain = (ImageView) itemView.findViewById(R.id.mLLcontainer);
        mTVLocation = (TextView) itemView.findViewById(R.id.mTVLocation);
        imageBlur = (ImageView) itemView.findViewById(R.id.imageBlur);
        mTVband = (TextView) itemView.findViewById(R.id.mTVband);
        band = (RelativeLayout) itemView.findViewById(R.id.band);
        itemView.setOnClickListener(this);
        this.mContext = mContext;
        this.promoService = promoService;
        this.authorization = authorization;

        //Blurry.with(mContext).capture(itemView).into(imageBlur);
    }

    public void bind(DiscoverPromotionCategory editorsPick) {
        //Blurry.with(mContext).capture(itemView).into(imageBlur);
        //Blurry.with(mContext).radius(25).sampling(2).onto((ViewGroup) itemView);

        this.isFavorite = isFavorite;

        if (editorsPick.getBand()!=null){
            if (!editorsPick.getBand().equals("")){
                mTVband.setText(editorsPick.getBand());
            }
        }
        else {
            band.setVisibility(View.GONE);
        }

        mTVLocation.setText(editorsPick.getOutlet());

        if(isFavorite){
            Picasso.with(mContext).load(R.drawable.icon_favorite_active).into(mIVfavorite);
        }

        mTVoutletName.setText(editorsPick.getMerchant().getName());
        mTVdiscount.setText(editorsPick.getName());
        try {
            mTVbank.setText(editorsPick.getBank().getName());
        } catch(Exception e) {

        }
        //mTVLocation.setText(editorsPick.getMerchant().g);

        Picasso.with(mContext)
                .load(editorsPick.getUrl_img())
                .fit()
                .centerCrop()
                .placeholder(R.color.placeholder)
                .into(mSRLmain);

        this.editorsPick = editorsPick;
    }

    @Override
    public void onClick(View v) {
        //Log.e("Promo", "item clicked");
        //Bundle bundle = new Bundle();
        //bundle.putSerializable("selectedPromo", editorsPick);
        //Intent intent = new Intent(v.getContext(), ActivityPromo_.class);
        //intent.putExtras(bundle);
        //v.getContext().startActivity(intent);


        Intent intent = new Intent(v.getContext(), DetailPromoActivity.class);
        intent.putExtra("Id", editorsPick.getId());
        //intent.putExtra("token", token);
        Log.e("TAG", "onClickViewPager: "+editorsPick.getId() );
        v.getContext().startActivity(intent);
    }
}
