package co.kartoo.app.category;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import co.kartoo.app.R;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {
    private ImageItem[] itemsData;

    public RecycleViewAdapter(ImageItem[] itemsData) {
        this.itemsData = itemsData;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecycleViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_categories, null);

        // create ViewHolder

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // - get data from your itemsData at this position
        // - replace the contents of the view with that itemsData

        viewHolder.txtViewTitle.setText(itemsData[position].getTitle());
        viewHolder.imgViewIcon.setImageResource(itemsData[position].getImage());
        viewHolder.txtViewCaption.setText(itemsData[position].getCaption());

        viewHolder.imgViewIcon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.e("TAG", "onclickCategory: " + position);
                Intent intent = new Intent(v.getContext(), ActivityListPromoCategory_.class);
                intent.putExtra("id", itemsData[position].getId());
                intent.putExtra("name", itemsData[position].getTitle());
                v.getContext().startActivity(intent);


            }
        });
    }
    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtViewTitle;
        public ImageView imgViewIcon;
        public TextView txtViewCaption;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            txtViewTitle = (TextView) itemLayoutView.findViewById(R.id.mTVcatName);
            txtViewCaption = (TextView) itemLayoutView.findViewById(R.id.mTVcatCaption);
            imgViewIcon = (ImageView) itemLayoutView.findViewById(R.id.mLLcontainer);
        }
    }


    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return itemsData.length;
    }
}