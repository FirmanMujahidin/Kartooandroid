package co.kartoo.app.category;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import co.kartoo.app.R;
import co.kartoo.app.rest.model.newest.ListCategory;

/**
 * Created by MartinOenang on 10/12/2015.
 */
public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    ImageView mLLcontainer;
    TextView mTVcatName;
    TextView mTVcatCaption;
    ListCategory category;
    Boolean hasPromoForMyCard;
    Context mContext;
    private Target target;

    public CategoryViewHolder(Context mContext, View itemView) {
        super(itemView);
        this.mContext = mContext;
        mLLcontainer = (ImageView) itemView.findViewById(R.id.mLLcontainer);
        mTVcatName = (TextView) itemView.findViewById(R.id.mTVcatName);
        mTVcatCaption = (TextView) itemView.findViewById(R.id.mTVcatCaption);
        itemView.setOnClickListener(this);
    }

    public void bind(ListCategory category) {
        mTVcatName.setText(category.getName());
        mTVcatCaption.setText(category.getCaption());
        mLLcontainer.setBackground(null);

        Picasso.with(mContext).load(category.getUrl_img())
                .placeholder(R.drawable.ph_category)
                .resize(720,360)
                .onlyScaleDown()
                .centerInside()
                .into(mLLcontainer);
        this.category = category;
    }

    @Override
    public void onClick(View v) {
        Log.d("Category", "item clicked");
        Intent intent = new Intent(v.getContext(), ActivityListPromoCategory_.class);
        intent.putExtra("name", category.getName());
        intent.putExtra("id", category.getId());
        //intent.putExtra("hasPromo", hasPromoForMyCard);
        v.getContext().startActivity(intent);
    }
}
