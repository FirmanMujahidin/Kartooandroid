package co.kartoo.app.search.filter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import co.kartoo.app.R;
import co.kartoo.app.rest.model.newest.FilterCategories;

public class FilterCategoriesAdapter extends RecyclerView.Adapter<FilterCategoriesViewHolder> {

    ArrayList<FilterCategories> listCategory;
    Context mContext;

    public FilterCategoriesAdapter(Context mContext, ArrayList<FilterCategories> listCategory) {
        this.mContext = mContext;
        this.listCategory = listCategory;

    }

    @Override
    public FilterCategoriesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FilterCategoriesViewHolder(mContext,LayoutInflater.from(parent.getContext()).inflate(R.layout.list_filter_categories, parent, false));
    }

    @Override
    public void onBindViewHolder(FilterCategoriesViewHolder holder, int position) {

        holder.bind(listCategory.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return listCategory.size();
    }

    public interface RecyclerViewClickListener
    {
        public void recyclerViewListClicked(View v, int position);
    }

}
