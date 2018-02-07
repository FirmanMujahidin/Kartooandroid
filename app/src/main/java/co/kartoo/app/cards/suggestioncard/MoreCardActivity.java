package co.kartoo.app.cards.suggestioncard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.ArrayList;

import co.kartoo.app.R;
import co.kartoo.app.models.LoginPref_;
import co.kartoo.app.rest.CardService;
import co.kartoo.app.rest.model.newest.Availablecards;
import co.kartoo.app.rest.model.newest.CardDetail;
import de.greenrobot.event.EventBus;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;


@EActivity(R.layout.activity_more_card)
public class MoreCardActivity extends AppCompatActivity {

    @ViewById
    Toolbar mToolbar;
    @ViewById
    TextView mTVtitle;

    @ViewById
    RecyclerView mRVcards;

    @ViewById
    Spinner sortbySpinner;

    @ViewById
    ProgressBar progressBar, progressBarScroll;

    @ViewById
    RelativeLayout timeOut, all;

    @Pref
    LoginPref_ loginPref;

    AdapterMoreCard adapterMoreCard;

    String authorization;
    Retrofit retrofit;
    CardService service;

    private boolean loading = false;

    int lastPage=  1;
    int maxPage = -1;
    int diff;

    ArrayList<Availablecards> listAvailableCards;

    String[] sortBy = {
            "Distance",
            "Popular",
            "Ending Soon",
            "A to Z",
    };

    @AfterViews
    void init() {
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
        setUpNavDrawer();
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        progressBar.setVisibility(View.VISIBLE);
        timeOut.setVisibility(View.GONE);
        all.setVisibility(View.GONE);

        listAvailableCards = new ArrayList<>();

        adapterMoreCard = new AdapterMoreCard(this, listAvailableCards);
        mRVcards.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRVcards.setAdapter(adapterMoreCard);

        retrofit = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
        service = retrofit.create(CardService.class);
        authorization = loginPref.token().get();

        loadData();
    }

    public void loadData(){
        progressBar.setVisibility(View.VISIBLE);
        timeOut.setVisibility(View.GONE);
        all.setVisibility(View.GONE);

        Log.e("TAG", "loadData: "+"loaddata");
        Call<ArrayList<Availablecards>> getMoreCards = service.getMoreCards(loginPref.token().get(), "sting kosong");
        getMoreCards.enqueue(new Callback<ArrayList<Availablecards>>() {
            @Override
            public void onResponse(final Response<ArrayList<Availablecards>> response, Retrofit retrofit) {
                Log.e("TAG", "onResponse: "+response.code() );
                if (response.code() == 200) {
                    progressBar.setVisibility(View.GONE);
                    timeOut.setVisibility(View.GONE);
                    all.setVisibility(View.VISIBLE);
                    Log.e("TAG", "onResponse: "+response.body());
                    EventBus.getDefault().postSticky(new EventMoreCards(response.body()));
                }
            }
            @Override
            public void onFailure(Throwable t) {
                progressBar.setVisibility(View.GONE);
                timeOut.setVisibility(View.VISIBLE);
                all.setVisibility(View.GONE);

                Log.e("TAG", "onFailure: "+t );
            }
        });

        /*
        mRVcards.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                progressBarScroll.setVisibility(View.VISIBLE);

                View view = mRVcards.getChildAt(mRVcards.getChildCount() - 1);
                if (listAvailableCards.size()!=0){
                    diff = (view.getBottom() - (mRVcards.getHeight() + mRVcards.getScrollY()));
                }
                Log.e("TAG", "diff: "+diff);

                if (diff < 0) {
                    if (!loading && lastPage < maxPage) {
                        progressBarScroll.setVisibility(View.VISIBLE);
                        loading = true;
                        Log.e("TAG", "onScrollChanged: "+lastPage+1);
                        Call<ArrayList<Availablecards>> getMoreCards = service.getMoreCards(loginPref.token().get(), lastPage+1);
                        getMoreCards.enqueue(new Callback<ArrayList<Availablecards>>() {
                                @Override
                                public void onResponse(Response<ArrayList<Availablecards>> response, Retrofit retrofit) {
                                    loading = false;
                                    if (response.code() == 200) {
                                        listAvailableCards.addAll(response.body());
                                            adapterMoreCard.notifyDataSetChanged();
                                            maxPage = 2;
                                            lastPage++;

                                    }
                                }
                                @Override
                                public void onFailure(Throwable t) {
                                    loading = false;
                                }
                            });
                    }
                }
            }
        });
        */
    }

    @Click(R.id.timeOut)
    public void timeOutClick() {
        loadData();
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

    @Override
    public void onStart() {
        super.onStart();
        listAvailableCards.clear();
        EventBus.getDefault().registerSticky(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    public void onEvent(EventMoreCards event) {
        Log.e("eventTrending", "caught");
        listAvailableCards.clear();
        //maxPage = 2;
        listAvailableCards.addAll(event.getlistAvailableCards());
        adapterMoreCard.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
