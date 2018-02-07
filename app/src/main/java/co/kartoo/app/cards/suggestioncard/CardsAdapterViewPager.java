package co.kartoo.app.cards.suggestioncard;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import co.kartoo.app.R;

/**
 * Created by Luthfi Apriyanto on 2/25/2016.
 */

public class CardsAdapterViewPager extends PagerAdapter {

    Context context;
    String[] Id;
    String[] Name;
    String[] UrlImage, BankName, CardName;
    LayoutInflater inflater;
    Context mContext;

    public CardsAdapterViewPager(Context context, String[] Id, String[] Name, String[] UrlImage, String[] BankName, String[] CardName) {
        this.context = context;
        this.Id = Id;
        this.Name = Name;
        this.UrlImage = UrlImage;
        this.BankName = BankName;
        this.CardName = CardName;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return Name.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        // Declare Variables
        TextView cardName;
        ImageView cardImage;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View itemView = inflater.inflate(R.layout.card_view_pager, container, false);

        // Locate the TextViews in viewpager_item.xml
        cardName = (TextView) itemView.findViewById(R.id.cardName);
        cardImage = (ImageView) itemView.findViewById(R.id.cardImage);

        cardName.setText(Name[position]);

        Picasso.with(context)
                .load(UrlImage[position])
                .placeholder(R.color.placeholder)
                .fit()
                .into(cardImage);

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);

    }
}