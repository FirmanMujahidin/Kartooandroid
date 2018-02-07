package co.kartoo.app.nearby;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import co.kartoo.app.DetailPromoActivity;
import co.kartoo.app.R;
import co.kartoo.app.rest.model.newest.Nearby;

public class PromoFromNearbyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    ImageView mSRLmain;
    TextView mTVdiscount;
    TextView mTVoutletName;
    TextView mTVbank, mTVband;
    RelativeLayout band;
    TextView mTVLocation;
    Nearby editorsPick;
    Context mContext;
    String imageHeader, textHeader, outletName;

    public PromoFromNearbyViewHolder(Context mContext, View itemView) {
        super(itemView);
        Log.e("viewholder", "initated");
        mSRLmain = (ImageView) itemView.findViewById(R.id.mLLcontainer);
        mTVoutletName = (TextView) itemView.findViewById(R.id.mTVoutletName);
        mTVdiscount = (TextView) itemView.findViewById(R.id.mTVoutletPromo);
        mTVbank = (TextView) itemView.findViewById(R.id.mTVbank);
        mSRLmain = (ImageView) itemView.findViewById(R.id.mLLcontainer);
        mTVLocation = (TextView) itemView.findViewById(R.id.mTVLocation);
        mTVband = (TextView) itemView.findViewById(R.id.mTVband);
        band = (RelativeLayout) itemView.findViewById(R.id.band);
        itemView.setOnClickListener(this);
        this.mContext = mContext;
    }

    public void bind(Nearby editorsPick) {
        mTVLocation.setText("");
        mTVoutletName.setText(editorsPick.getMerchant());
        mTVdiscount.setText(editorsPick.getPromotion());
        textHeader = editorsPick.getBank();
        imageHeader = editorsPick.getPromotion();
        outletName = editorsPick.getMerchant();

        if (editorsPick.getBand() != null) {
            if (!editorsPick.getBand().equals("")) {
                mTVband.setText(editorsPick.getBand());
            }
        }
        else {
            band.setVisibility(View.GONE);
        }
        try {
            mTVbank.setText(editorsPick.getBank());
        } catch(Exception e) {

        }

        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                BitmapDrawable background = new BitmapDrawable(bitmap);
                mSRLmain.setBackground(background);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

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
        intent.putExtra("from", "Open Nearby Promotion");

        intent.putExtra("imageHeader", editorsPick.getUrl_img());
        intent.putExtra("outletName", editorsPick.getMerchant());
        intent.putExtra("textHeader", editorsPick.getPromotion());

        //intent.putExtra("token", token);
        Log.e("TAG", "onClickViewPager: " + editorsPick.getPromotion());
        v.getContext().startActivity(intent);
    }
}
