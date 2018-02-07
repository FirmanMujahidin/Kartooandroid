package co.kartoo.app.bank.detail;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import co.kartoo.app.R;
import co.kartoo.app.rest.model.newest.BankFeedItem;
import android.view.ViewGroup.LayoutParams;


/**
 * Created by MartinOenang on 10/12/2015.
 */
public class BankDetilViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    BankFeedItem feedItem;
    Context mContext;

    TextView TVbody, mTVtime, more;
    ImageView mIVicon,IVpicture;
    private boolean zoomOut =  false;
    String link, urlimage;


    public BankDetilViewHolder(Context mContext, View itemView) {
        super(itemView);
        Log.e("viewholder", "initated");
        mIVicon = (ImageView) itemView.findViewById(R.id.mIVicon);
        TVbody = (TextView) itemView.findViewById(R.id.TVbody);
        mTVtime = (TextView) itemView.findViewById(R.id.mTVtime);
        IVpicture = (ImageView) itemView.findViewById(R.id.IVpicture);
        more = (TextView) itemView.findViewById(R.id.more);
        itemView.setOnClickListener(this);
        IVpicture.setOnClickListener(this);
        more.setOnClickListener(this);
        mIVicon.setOnClickListener(this);
        this.mContext = mContext;
    }

    public void bind(BankFeedItem feedItem) {




        urlimage = feedItem.getPicture();
        mTVtime.setText(feedItem.getDatePosted());
        TVbody.setText(feedItem.getBody());

        TVbody.post(new Runnable() {
            @Override
            public void run() {
                Log.e("TA", "run: "+TVbody.getLineCount() );
                if (TVbody.getLineCount() < 4){
                    more.setVisibility(View.GONE);
                }
            }
        });

        TVbody.setMovementMethod(LinkMovementMethod.getInstance());

        if(feedItem.getSource().equals("Twitter")){
            mIVicon.setBackgroundResource(R.drawable.bank_tweet);
        }
        else if (feedItem.getSource().equals("Facebook")){
            mIVicon.setBackgroundResource(R.drawable.bank_fb);
        }
        else if (feedItem.getSource().equals("Kartoo")) {
            mIVicon.setBackgroundResource(R.drawable.bank_kartoo);
        }

        if (feedItem.getPicture()!=null){
            link = feedItem.getPermalink_URL();

            Picasso.with(mContext)
                    .load(feedItem.getPicture())
                    .fit()
                    .centerCrop()
                    .placeholder(R.color.placeholder)
                    .into(IVpicture);
        }
        else {
            IVpicture.setVisibility(View.GONE);
        }

        this.feedItem = feedItem;
    }

    @Override
    public void onClick(View v) {
        /*
        Log.d("Category", "item clicked");
        Intent intent = new Intent(v.getContext(), ActivityMall_.class);
        intent.putExtra("name", mallDetail.getName());
        intent.putExtra("id", mallDetail.getId());
        intent.putExtra("address", mallDetail.getAddress());
        intent.putExtra("latitude", mallDetail.getLatitude());
        intent.putExtra("longitude", mallDetail.getLongitude());

        v.getContext().startActivity(intent);
        */
        if(v==mIVicon){
            if (link != null) {

                String url = link;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                v.getContext().startActivity(i);
            }
        }

        if (v==more){
            TVbody.setMaxLines(100);
            more.setVisibility(View.GONE);
        }

        if (v==IVpicture) {
            if (urlimage!=null){
                Intent intent = new Intent(v.getContext(), ImagePopUp.class);
                intent.putExtra("url", urlimage);
                v.getContext().startActivity(intent);
            }
        }

    }
}
