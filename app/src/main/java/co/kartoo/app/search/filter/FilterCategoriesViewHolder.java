package co.kartoo.app.search.filter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import co.kartoo.app.InterestActivity;
import co.kartoo.app.R;
import co.kartoo.app.rest.PromoService;
import co.kartoo.app.rest.model.Category;
import co.kartoo.app.rest.model.newest.FilterCategories;

public class FilterCategoriesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    ImageView icon;
    RelativeLayout mRLcategory;
    TextView label;
    CheckBox checkbox;
    FilterCategories filterCategories;
    Context mContext;
    Boolean click;

    public FilterCategoriesViewHolder(Context mContext, View itemView) {
        super(itemView);
        this.mContext = mContext;
        icon = (ImageView) itemView.findViewById(R.id.icon);
        label = (TextView) itemView.findViewById(R.id.label);
        mRLcategory = (RelativeLayout) itemView.findViewById(R.id.mRLcategory);
        checkbox = (CheckBox) itemView.findViewById(R.id.checkbox);
        click = false;
        itemView.setOnClickListener(this);
    }

    public void bind(FilterCategories filterCategories) {

        label.setText(filterCategories.getLabel());
        icon.setImageResource(filterCategories.getIcon());

        this.filterCategories = filterCategories;
    }


    @Override
    public void onClick(View v) {
        Log.d("Interest", "item clicked");
        if(!click){
            checkbox.setChecked(true);
            click = true;
        }
        else {
            checkbox.setChecked(false);
            click = false;
        }
        ((SearchFilterActivity) v.getContext()).userItemClick(getAdapterPosition());

    }

    private Target target;
}
