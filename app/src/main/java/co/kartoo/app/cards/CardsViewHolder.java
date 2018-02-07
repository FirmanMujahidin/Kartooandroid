package co.kartoo.app.cards;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import de.greenrobot.event.EventBus;
import co.kartoo.app.R;
import co.kartoo.app.events.DeleteCardEvent;
import co.kartoo.app.rest.CardService;
import co.kartoo.app.rest.model.ResponseDefault;
import co.kartoo.app.rest.model.newest.Availablecards;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class CardsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    ImageView mIVcardImg;
    ImageView mIVdelete;
    TextView mTVcardName;
    TextView mTVlastCheckinTime;
    TextView mTVlastCheckinPlace;
    Context mContext;
    Availablecards card;
    String token, userId;

    public CardsViewHolder(View itemView, Context context, String token) {
        super(itemView);
        Log.e("viewholder", "initated");
        mIVcardImg = (ImageView) itemView.findViewById(R.id.mIVcardImg);
        mIVdelete = (ImageView) itemView.findViewById(R.id.mIVdelete);
        mTVcardName = (TextView) itemView.findViewById(R.id.mTVcardName);
        mTVlastCheckinTime = (TextView) itemView.findViewById(R.id.mTVlastCheckinTime);
        mTVlastCheckinPlace = (TextView) itemView.findViewById(R.id.mTVlastCheckinPlace);
        this.mContext = context;
        this.token = token;
        this.userId = userId;
        mIVdelete.setOnClickListener(this);
    }

    public void bind(Availablecards card) {
        Picasso.with(mContext).load(card.getUrl_img()).into(mIVcardImg);
        mTVcardName.setText(card.getCard_Edition());
        this.card = card;
    }

    @Override
    public void onClick(View v) {
        if (v == mIVdelete) {
            new AlertDialog.Builder(mContext)
                    .setTitle("Delete card?")
                    .setMessage("Do you really want to delete " +card.getCard_Edition() + "?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Log.e("TAG", "CardsViewHolder: "+token);
                            Retrofit retrofit = new Retrofit.Builder().baseUrl(mContext.getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
                            CardService service = retrofit.create(CardService.class);
                            Call<ResponseDefault> deleteUserCardCall = service.deleteUserCard(token, card.getId());
                            Log.e("TAG", "onResponsedelete " + token+card.getId());
                            deleteUserCardCall.enqueue(new Callback<ResponseDefault>() {
                                @Override
                                public void onResponse(Response<ResponseDefault> response, Retrofit retrofit) {
                                    Log.e("TAG", "onResponsedelete " + response.code());
                                    if (response.isSuccess()) {
                                        if (response.code() == 200) {
                                            EventBus.getDefault().postSticky(new DeleteCardEvent(card));
                                            Toast.makeText(mContext, "Delete card success", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(mContext, "Sorry, failed to delete card. Please try again later", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Throwable t) {
                                    Toast.makeText(mContext, "Sorry, failed to delete card. Please try again later", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    })
                    .setNegativeButton(android.R.string.no, null).show();
        }
    }
}
