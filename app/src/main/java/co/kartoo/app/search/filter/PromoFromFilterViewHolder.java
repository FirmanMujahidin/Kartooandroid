package co.kartoo.app.search.filter;

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
import co.kartoo.app.rest.model.newest.SearchPromotion;

/**
 * Created by EricLaptop001 on 4/11/2017.
 */

public class PromoFromFilterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    ImageView mSRLmain, mIVshare, mIVfavorite, imageBlur;
    TextView mTVdiscount;
    TextView mTVoutletName, mTVband;
    TextView mTVbank;
    SearchPromotion editorsPick;
    Context mContext;
    TextView mTVLocation;
    RelativeLayout tag, band;
    boolean isFavorite;
    PromoService promoService;
    String authorization, imageHeader, textHeader;
    String outletName;
    boolean fuck;


    public PromoFromFilterViewHolder(Context mContext, View itemView, PromoService promoService, String authorization) {
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
        itemView.setOnLongClickListener(this);

        this.mContext = mContext;
        this.promoService = promoService;
        this.authorization = authorization;


    }

    public void bind(SearchPromotion editorsPick) {
        this.isFavorite = isFavorite;

        if (editorsPick.getBand() != null) {
            if (!editorsPick.getBand().equals("")) {
                try {
                    mTVband.setText(editorsPick.getBand());
                } catch (Exception e) {
                }
            }
        } else {
            try {
                band.setVisibility(View.GONE);
            } catch (Exception e) {
            }
        }

        try {

            mTVLocation.setText("");
//            if (editorsPick.getOutlets().size() > 1) {
//                mTVLocation.setText(editorsPick.getOutlets().get(0) + " and " + (editorsPick.getOutlets().size() - 1) + " more");
//            } else {
//
//            }
        } catch (Exception e) {
        }

        try {
            mTVoutletName.setText(editorsPick.getMerchant());
            //outletName = editorsPick.getOutlets().get(0);
            Log.e("TAG","outletName"+ outletName);
            Log.e("TAG","outletName"+ mTVoutletName);
        } catch (Exception e) {
        }

        try {
            mTVdiscount.setText(editorsPick.getPromotion());
            textHeader = editorsPick.getPromotion();
        } catch (Exception e) {
        }

        try {
            Picasso.with(mContext)
                    .load(editorsPick.getUrl_img())
                    .fit()
                    .centerCrop()
                    .placeholder(R.color.placeholder)
                    .into(mSRLmain);

            imageHeader = editorsPick.getUrl_img();
        } catch (Exception e) {
        }

        try {
            mTVbank.setText(editorsPick.getBank());
        } catch (Exception e) {

        }
        this.editorsPick = editorsPick;
    }


    @Override
    public void onClick(View v) {

        Intent intent = new Intent(v.getContext(), DetailPromoActivity.class);

        intent.putExtra("Id", editorsPick.getId());
        intent.putExtra("imageHeader", editorsPick.getUrl_img());
        intent.putExtra("outletName", editorsPick.getMerchant());
        intent.putExtra("textHeader", editorsPick.getPromotion());
        intent.putExtra("from", "Category list");

        //intent.putExtra("token", token);
        Log.e("TAG", "onClickViewPager: "+editorsPick.getPromotion());
        v.getContext().startActivity(intent);
    }

    @Override
    public boolean onLongClick(View v){
        Log.e("TAG", "onLongClick: " + "LONG");
        return true;
    }
}
