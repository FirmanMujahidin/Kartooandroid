package co.kartoo.app.bank;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import co.kartoo.app.R;
import co.kartoo.app.bank.detail.ActivityBankDetail_;
import co.kartoo.app.rest.PromoService;
import co.kartoo.app.rest.model.newest.BankPage;

public class ViewHolderBankList extends RecyclerView.ViewHolder implements View.OnClickListener {
    ImageView IVbank;
    TextView mTVdiscount, TVbankName, TVtaglineBank;
    Button BtnFollow;
    Context mContext;
    PromoService promoService;
    String authorization ;
    Boolean isFollowing, follow;
    BankPage bankPage;
    Button btnMorph;
    String id;

    public ViewHolderBankList(Context mContext, View itemView, PromoService promoService, String authorization) {
        super(itemView);
        IVbank = (ImageView) itemView.findViewById(R.id.IVbank);
        TVbankName = (TextView) itemView.findViewById(R.id.TVbankName) ;
        TVtaglineBank = (TextView) itemView.findViewById(R.id.TVtaglineBank);

        itemView.setOnClickListener(this);


        this.mContext = mContext;
        this.promoService = promoService;
        this.authorization = authorization;

    }

    public void bind(BankPage bankPage, boolean isFollowing) {

        this.isFollowing = isFollowing;

        id = bankPage.getId();

        isFollowing = bankPage.getFollowing();

        follow = bankPage.getFollowing();

        Log.e("TAG", "follow BIND: "+follow );

        TVbankName.setText(bankPage.getName());
        TVtaglineBank.setText(bankPage.getDescription());

        Picasso.with(mContext)
                .load(bankPage.getUrl_img())
                .fit()
                .centerCrop()
                .placeholder(R.color.placeholder)
                .into(IVbank);

        this.bankPage = bankPage;
    }

    @Override
    public void onClick(View v) {

            Intent intent = new Intent(v.getContext(), ActivityBankDetail_.class);
            intent.putExtra("Id", bankPage.getId());
            intent.putExtra("name", bankPage.getName());
            intent.putExtra("image", bankPage.getUrl_img());
            intent.putExtra("desc", bankPage.getDescription());

            Log.e("TAG", "onClickViewPager: "+bankPage.getId() );
            v.getContext().startActivity(intent);
    }
}