package co.kartoo.app.newsletter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import co.kartoo.app.R;
import co.kartoo.app.rest.model.newest.Newsletter;
/**
 * Created by EricLaptop002 on 7/6/2017.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    private Context mContext;
    private List<Newsletter> mNewsList;

    public NewsAdapter(Context mContext, List<Newsletter> mNewsList) {
        this.mContext = mContext;
        this.mNewsList = mNewsList;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_list_row, parent, false);
        return new NewsViewHolder(mContext, itemView);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        holder.bind(mNewsList.get(position));
    }

    @Override
    public int getItemCount() {
        if (mNewsList != null) {
            return mNewsList.size();
        }
        return 0;
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Context mContext;
        public ImageView newsImageIcon;
        public TextView newsHeader, newsTimeSent;
        public Newsletter news;

        public NewsViewHolder(Context context, View itemView) {
            super(itemView);
            this.mContext = context;
            newsImageIcon = (ImageView) itemView.findViewById(R.id.mIVNewsImageIcon);
            newsHeader = (TextView) itemView.findViewById(R.id.mTvNewsHeader);
            newsTimeSent = (TextView) itemView.findViewById(R.id.mTvTimeSent);
            itemView.setOnClickListener(this);
        }

        private void bind(Newsletter news) {
            String date = news.getTimeSent().substring(0, 10);
            String hour = news.getTimeSent().substring(11, 13);
            int newHour = Integer.parseInt(hour) + 7;
            if (newHour > 23) {
                newHour = newHour - 24;
            }
            String newHourString = Integer.toString(newHour);
            String dateTime = date + " " + newHourString + news.getTimeSent().substring(13, 19);

            Picasso.with(mContext).load(news.getUrl_img()).into(newsImageIcon);
            this.newsHeader.setText(news.getHeader());
            this.newsTimeSent.setText(dateTime);
            this.news = news;
        }

        @Override
        public void onClick(View v) {
            Intent i = new Intent(v.getContext(), ReadNotificationActivity_.class);
            i.putExtra("id", this.news.getId());
            v.getContext().startActivity(i);
        }
    }
}
