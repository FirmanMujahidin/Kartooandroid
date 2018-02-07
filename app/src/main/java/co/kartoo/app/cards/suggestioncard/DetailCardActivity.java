package co.kartoo.app.cards.suggestioncard;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.ArrayList;

import co.kartoo.app.R;
import co.kartoo.app.cards.suggestioncard.applycard.ApplyLandingActivity_;
import co.kartoo.app.models.ApplyCreditCardPref_;
import co.kartoo.app.models.LoginPref_;
import co.kartoo.app.models.ProfilePref_;
import co.kartoo.app.rest.CardService;
import co.kartoo.app.rest.model.ResponseDefault;
import co.kartoo.app.rest.model.newest.CardDetail;
import co.kartoo.app.rest.model.newest.Details;
import co.kartoo.app.rest.model.newest.Perks;
import de.greenrobot.event.EventBus;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

@EActivity(R.layout.activity_detail_card)
public class DetailCardActivity extends AppCompatActivity {

    @ViewById
    Toolbar mToolbar;
    @ViewById
    TextView mTVtitle;

    @ViewById
    TextView txtLeft;
    @ViewById
    TextView txtRight;
    @ViewById
    TextView txtCenter;

    @ViewById
    ImageView mVPcardTop;
    @ViewById
    TextView mTVcardName;

    @ViewById
    RecyclerView mRVperks;
    @ViewById
    RecyclerView mRVdetail;

    @ViewById
    RelativeLayout timeOut;

    @ViewById
    ProgressBar progressBar;
    @ViewById
    NestedScrollView scrollView;

    @Pref
    LoginPref_ loginPref;

    @Pref
    ProfilePref_ profilePref;

    @Pref
    ApplyCreditCardPref_ applyCreditCardPref;

    Retrofit retrofit;
    CardService service;

    PagerAdapter adapter;

    String authorization, cardID, cardName, bankName;

    ArrayList<Perks> listPerks;
    ArrayList<Details> listDetails;

    AdapterPerks perksAdapter;
    AdapterDetail detailAdapter;

    ProgressDialog loadingDialog;


    @AfterViews
    void init() {
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
        setUpNavDrawer();
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        loadingDialog = new ProgressDialog(this);
        loadingDialog.setMessage("Loading...");
        loadingDialog.setIndeterminate(true);
        loadingDialog.setCancelable(false);

        scrollView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        timeOut.setVisibility(View.GONE);

        Intent intent = getIntent();
        cardID = intent.getStringExtra("cardID");
        cardName = intent.getStringExtra("cardName");

        mTVtitle.setText(cardName);

        listPerks = new ArrayList<>();
        perksAdapter = new AdapterPerks(this, listPerks);

        listDetails = new ArrayList<>();
        detailAdapter = new AdapterDetail(this, listDetails);

        mRVperks.setNestedScrollingEnabled(false);
        mRVperks.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRVperks.setAdapter(perksAdapter);

        mRVdetail.setNestedScrollingEnabled(false);
        mRVdetail.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRVdetail.setAdapter(detailAdapter);

        retrofit = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
        service = retrofit.create(CardService.class);

        authorization = loginPref.token().get();

        reload();
    }

    public void reload(){
        Call<CardDetail> cardDetail = service.getCardDetail(loginPref.token().get(), cardID);
        Log.e("TAG", "init: "+cardDetail);
        cardDetail.enqueue(new Callback<CardDetail>() {
            @Override
            public void onResponse(final Response<CardDetail> response, Retrofit retrofit) {
                Log.e("TAG", "cardDetail: " + response.code());
                if (response.code() == 200) {

                    scrollView.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    timeOut.setVisibility(View.GONE);

                    txtLeft.setText(response.body().getMinimumIncome());
                    txtCenter.setText(response.body().getCardFee());
                    txtRight.setText(response.body().getMonthlyRateFee());

                    mTVcardName.setText(response.body().getCard().getCard_Edition());

                    bankName = response.body().getCard().getBank().getName();

                    Picasso.with(DetailCardActivity.this).load(response.body().getCard().getUrl_img())
                            .fit()
                            .placeholder(R.color.placeholder)
                            .into(mVPcardTop);

                    EventBus.getDefault().postSticky(new EventPerks(response.body().getPerks()));

                    EventBus.getDefault().postSticky(new EventDetail(response.body().getDetails()));

                    Log.e("TAG", "getCardDetail: "+response.body().getDetails().size() );
                }
            }
            @Override
            public void onFailure(Throwable t) {

                scrollView.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                timeOut.setVisibility(View.VISIBLE);

                Log.e("TAG", "cardDetail: "+t);
                //do Refresh
            }
        });
    }

    @Click(R.id.timeOut)
    public void timeOutClick() {
        reload();
    }

    @Click(R.id.mBtnApply)
    public void mBtnApplyClick() {

        loadingDialog.show();

        Log.e("TAG", "mBtnApplyClick: "+cardName );
        applyCreditCardPref.nameCardApplied().put(cardName);
        applyCreditCardPref.bankName().put(bankName);

        Log.e("TAG", "cardId: "+cardID );
        Call<ResponseDefault> doApply = service.doApply(loginPref.token().get(), cardID);
        Log.e("TAG", "init: "+doApply);
        doApply.enqueue(new Callback<ResponseDefault>() {
            @Override
            public void onResponse(final Response<ResponseDefault> response, Retrofit retrofit) {
                Log.e("TAG", "cardDetail: " + response.code());
                if (response.code() == 200) {
                    loadingDialog.dismiss();
                    applyCreditCardPref.cardID().put(cardID);
                    applyCreditCardPref.applicationID().put(response.body().getMessage());
                    Log.e("TAG", "onResponse: "+ response.body().getMessage());
                    Intent intent = new Intent(DetailCardActivity.this, ApplyLandingActivity_.class);
                    startActivity(intent);
                }
                else {
                    loadingDialog.dismiss();
                    Toast.makeText(DetailCardActivity.this, "Error has occured, please try again", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Throwable t) {
                loadingDialog.dismiss();
                Toast.makeText(DetailCardActivity.this, "Error has occured, please try again", Toast.LENGTH_SHORT).show();
                Log.e("TAG", "cardDetail: "+t);
            }
        });


    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().registerSticky(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    public void onEvent(EventPerks event) {
        listPerks.clear();
        listPerks.addAll(event.getListPerks());
        perksAdapter.notifyDataSetChanged();
    }

    public void onEvent(EventDetail event) {
        listDetails.clear();
        listDetails.addAll(event.getlistDetail());
        detailAdapter.notifyDataSetChanged();
    }

    private void setUpNavDrawer() {
        if (mToolbar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mToolbar.setNavigationIcon(R.drawable.ic_back_orange);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }
}
