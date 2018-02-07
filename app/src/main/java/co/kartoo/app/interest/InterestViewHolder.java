package co.kartoo.app.interest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import co.kartoo.app.InterestActivity;
import co.kartoo.app.R;
import co.kartoo.app.rest.PromoService;
import co.kartoo.app.rest.model.newest.ListCategory;

public class InterestViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    ImageView mLLcontainer, mIVoverlay;
    TextView mTVcatName;
    ListCategory category;
    Context mContext;
    Boolean click;
    String authorization;
    PromoService promoService;
    boolean isChoosen;
    private Target target;

    public InterestViewHolder(Context mContext, View itemView, PromoService promoService , String authorization) {
        super(itemView);
        this.mContext = mContext;
        this.authorization = authorization;
        this.promoService = promoService;
        mLLcontainer = (ImageView) itemView.findViewById(R.id.mLLcontainer);
        mTVcatName = (TextView) itemView.findViewById(R.id.mTVcatName);
        mIVoverlay = (ImageView) itemView.findViewById(R.id.mIVoverlay);
        itemView.setOnClickListener(this);
    }

    public void bind(ListCategory category, boolean isChoosen) {

        mTVcatName.setText(category.getName());
        mLLcontainer.setBackground(null);
        Picasso.with(mContext).load(category.getUrl_img())
                .fit()
                .centerCrop()
                .placeholder(R.drawable.ph_category)
                .into(mLLcontainer);
        this.category = category;
        this.isChoosen = isChoosen;

        if(isChoosen){
            mIVoverlay.setBackgroundColor(mContext.getResources().getColor(R.color.ColorPrimaryYellow));
            click = true;
        } else {
            mIVoverlay.setBackgroundColor(mContext.getResources().getColor(android.R.color.transparent));
            click = false;
        }

    }

    @Override
    public void onClick(View v) {
        Log.d("Interest", "item clicked");
        if(!click){
            mIVoverlay.setBackgroundColor(v.getResources().getColor(R.color.ColorPrimaryYellow));
            click = true;
        }
        else if(click) {
            mIVoverlay.setBackgroundColor(v.getResources().getColor(android.R.color.transparent));
            click = false;
        }

        ((InterestActivity) v.getContext()).userItemClick(getAdapterPosition());

    }
}
