package co.kartoo.app.category;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import co.kartoo.app.R;
import co.kartoo.app.rest.model.newest.ListCategory;

/**
 * Created by MartinOenang on 10/10/2015.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder> {

    ArrayList<ListCategory> listCategory;
    Context mContext;

    public CategoryAdapter(Context mContext, ArrayList<ListCategory> listCategory) {
        this.mContext = mContext;
        this.listCategory = listCategory;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CategoryViewHolder(mContext,LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_categories, parent, false));
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
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
}
