package co.kartoo.app.mall;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import co.kartoo.app.R;
import co.kartoo.app.rest.model.newest.MallDetail;

/**
 * Created by MartinOenang on 10/12/2015.
 */
public class MallViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    ImageView mLLcontainer;
    TextView TVmallname;
    TextView TVpromosize;
    MallDetail mallDetail;
    Context mContext;

    public MallViewHolder(Context mContext, View itemView) {
        super(itemView);
        Log.e("viewholder", "initated");
        mLLcontainer = (ImageView) itemView.findViewById(R.id.mLLcontainer);
        TVmallname = (TextView) itemView.findViewById(R.id.TVmallname);
        TVpromosize = (TextView) itemView.findViewById(R.id.TVpromosize);
        mLLcontainer = (ImageView) itemView.findViewById(R.id.mLLcontainer);
        itemView.setOnClickListener(this);
        this.mContext = mContext;
    }

    public void bind(MallDetail mallDetail) {
        TVmallname.setText(mallDetail.getName());

        if (mallDetail.getTotalPromotions()<=1){
            TVpromosize.setText(mallDetail.getTotalPromotions()+" Promo");
        }
        else if (mallDetail.getTotalPromotions()>1){
            TVpromosize.setText(mallDetail.getTotalPromotions()+" Promos");
        }

        if (mallDetail.getUrl_img()!=null){
            Picasso.with(mContext)
                    .load(mallDetail.getUrl_img())
                    .fit()
                    .centerCrop()
                    .placeholder(R.color.placeholder)
                    .into(mLLcontainer);
        }
        this.mallDetail = mallDetail;
    }

    @Override
    public void onClick(View v) {
        Log.d("Category", "item clicked");
        Intent intent = new Intent(v.getContext(), ActivityMall_.class);
        intent.putExtra("name", mallDetail.getName());
        intent.putExtra("id", mallDetail.getId());
        intent.putExtra("address", mallDetail.getAddress());
        intent.putExtra("latitude", mallDetail.getLatitude());
        intent.putExtra("longitude", mallDetail.getLongitude());

        v.getContext().startActivity(intent);
    }
}
