package co.kartoo.app.bank;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import co.kartoo.app.R;
import co.kartoo.app.rest.PromoService;
import co.kartoo.app.rest.model.newest.BankPage;
import co.kartoo.app.views.SpaceItemDecoration;
import de.greenrobot.event.EventBus;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class ActivityBankList extends AppCompatActivity {

    Toolbar mToolbar;
    RecyclerView mRVbank;
    String token;
    ProgressBar progressBar;
    RelativeLayout timeOut;
    Button reload;

    AdapterBankList adapter;
    ArrayList<BankPage> listTrendingPromo;

    Retrofit retrofit;
    PromoService promoService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_list);
        mToolbar = (Toolbar) findViewById(R.id.mToolbar);
        mRVbank = (RecyclerView) findViewById(R.id.mRVbank);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        timeOut = (RelativeLayout) findViewById(R.id.timeOut);
        reload = (Button) findViewById(R.id.reload);

        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
        setUpNavDrawer();
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        SharedPreferences bb = getSharedPreferences("my_prefs", 0);
        token = bb.getString("MID", null);

        retrofit = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
        promoService = retrofit.create(PromoService.class);

        listTrendingPromo = new ArrayList<>();
        adapter = new AdapterBankList(this, listTrendingPromo, promoService, token);
        mRVbank.addItemDecoration(new SpaceItemDecoration(3));
        mRVbank.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRVbank.setAdapter(adapter);

        reload();

        timeOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reload();
            }
        });
    }

    public void reload(){
        progressBar.setVisibility(View.VISIBLE);
        mRVbank.setVisibility(View.INVISIBLE);
        timeOut.setVisibility(View.GONE);
        Call<ArrayList<BankPage>> bankCall = promoService.getBankList(token);
        bankCall.enqueue(new Callback<ArrayList<BankPage>>() {
            @Override
            public void onResponse(Response<ArrayList<BankPage>> response, Retrofit retrofit) {
                Log.e("TAG", "listFavoriteOutlet: "+response.body() );
                if (response.code() == 200) {
                    progressBar.setVisibility(View.GONE);
                    mRVbank.setVisibility(View.VISIBLE);
                    timeOut.setVisibility(View.GONE);
                    EventBus.getDefault().postSticky(new EventBankList(response.body()));
                }
            }
            @Override
            public void onFailure(Throwable t) {
                progressBar.setVisibility(View.GONE);
                mRVbank.setVisibility(View.GONE);
                timeOut.setVisibility(View.VISIBLE);
            }
        });
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
        listTrendingPromo.clear();
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

    public void onEvent(EventBankList event) {
        Log.e("eventTrending", "caught");
        listTrendingPromo.clear();
        listTrendingPromo.addAll(event.getListPromo());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
