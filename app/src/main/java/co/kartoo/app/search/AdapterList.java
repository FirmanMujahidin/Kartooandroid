package co.kartoo.app.search;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import co.kartoo.app.R;
import co.kartoo.app.rest.model.newest.SearchRecent;

/**
 * Created by Luthfi Apriyanto on 2/1/2017.
 */

public class AdapterList extends RecyclerView.Adapter<AdapterList.ViewHolderList> implements View.OnClickListener {

    private List<SearchRecent> item;

    public class ViewHolderList extends RecyclerView.ViewHolder {
        public TextView mTVitemValue;

        public ViewHolderList(View view) {
            super(view);
            mTVitemValue = (TextView) view.findViewById(R.id.mTVitemValue);
        }
    }
    public AdapterList(List<SearchRecent> item) {
        this.item = item;
    }

    @Override
    public ViewHolderList onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_search_suggestion, parent, false);

        return new ViewHolderList(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolderList holder, final int position) {
        final SearchRecent itemItem = item.get(position);
        holder.mTVitemValue.setText(itemItem.getResult());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                ((RecentInterface) v.getContext()).userItemClick(itemItem.getResult());
                Log.e("TAG", "onClick: "+"apa");
            }
        });
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    @Override
    public void onClick(View v) {
        Log.e("TAG", "onClick: "+"apa" );
        //((RecentInterface) v.getContext()).userItemClick(get);
    }
}