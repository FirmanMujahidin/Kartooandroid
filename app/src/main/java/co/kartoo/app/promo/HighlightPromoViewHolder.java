package co.kartoo.app.promo;

import android.content.Context;
import android.content.Intent;
import android.support.v4.util.Pair;
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
import co.kartoo.app.rest.model.newest.EditorsPick;

public class HighlightPromoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    ImageView mSRLmain;
    TextView mTVdiscount;
    TextView mTVoutletName;
    TextView mTVbank, mTVband;
    RelativeLayout band;
    EditorsPick editorsPick;
    Context mContext;

    String imageHeader, textHeader;

    public HighlightPromoViewHolder(Context mContext, View itemView) {
        super(itemView);
        Log.e("viewholder", "initated");
        mSRLmain = (ImageView) itemView.findViewById(R.id.mLLcontainer);
        mTVoutletName = (TextView) itemView.findViewById(R.id.mTVoutletName);
        mTVdiscount = (TextView) itemView.findViewById(R.id.mTVoutletPromo);
        mTVbank = (TextView) itemView.findViewById(R.id.mTVbank);
        mTVband = (TextView) itemView.findViewById(R.id.mTVband);
        mSRLmain = (ImageView) itemView.findViewById(R.id.mLLcontainer);
        band = (RelativeLayout) itemView.findViewById(R.id.band);
        itemView.setOnClickListener(this);
        this.mContext = mContext;
    }

    public void bind(EditorsPick editorsPick) {
        final EditorsPick edPick = editorsPick;
        final String id = editorsPick.getId();

        imageHeader = editorsPick.getUrl_img();
        textHeader = editorsPick.getName();

        if (editorsPick.getBand()!=null ){
            if (!editorsPick.getBand().equals("")){
                mTVband.setText(editorsPick.getBand());
            }
        }
        else {
            band.setVisibility(View.GONE);
        }

        mTVoutletName.setText(editorsPick.getMerchant().getName());
        mTVdiscount.setText(editorsPick.getName());
        try {
            mTVbank.setText(editorsPick.getBank().getName());
        } catch(Exception e) {

        }
        editorsPick.getAvailablecards();
        editorsPick.getTerms_and_condition();

        Picasso.with(mContext)
                .load(editorsPick.getUrl_img())
                .fit()
                .centerCrop()
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(mSRLmain, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(mContext)
                                .load(edPick.getUrl_img())
                                .fit()
                                .centerCrop()
                                .into(mSRLmain);
                    }
                });

        this.editorsPick = editorsPick;
    }

    @Override
    public void onClick(View v) {

        //Bundle bundle = new Bundle();
        //bundle.putSerializable("selectedPromo", editorsPick);
        //Intent intent = new Intent(v.getContext(), DetailPromoActivity.class);
        //intent.putExtras(bundle);
        //v.getContext().startActivity(intent);

        Intent intent = new Intent(v.getContext(), DetailPromoActivity.class);
        intent.putExtra("Id", editorsPick.getId());
        intent.putExtra("from", "Open Highlight Promotion/Similar Promotions");
        intent.putExtra("imageHeader", imageHeader);
        intent.putExtra("textHeader", textHeader);
        intent.putExtra("outletName", editorsPick.getMerchant());
        Log.e("idEditor", "item clicked"+editorsPick.getId());

        Pair<View, String> p1 = Pair.create((View)mSRLmain, "mainImage");
        Pair<View, String> p2 = Pair.create((View)mTVdiscount, "mainText");

        //ActivityOptionsCompat options = ActivityOptionsCompat.
        //       makeSceneTransitionAnimation((Activity) v.getContext(), p1, p2);

        //v.getContext().startActivity(intent, options.toBundle());
        v.getContext().startActivity(intent);

    }
}