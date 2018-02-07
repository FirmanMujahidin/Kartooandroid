package co.kartoo.app.search.result;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.ArrayList;

import co.kartoo.app.R;
import co.kartoo.app.models.LoginPref_;
import co.kartoo.app.promo.AvailableOutlet.TrendingPromoAdapterCategory;
import co.kartoo.app.rest.PromoService;
import co.kartoo.app.rest.model.newest.DiscoverPromotionCategory;
import co.kartoo.app.rest.model.newest.SearchResultSuggestion;
import co.kartoo.app.search.RecentAdapter;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

@EActivity(R.layout.activity_search_result)
public class SearchResultActivity extends AppCompatActivity
        implements CompoundButton.OnCheckedChangeListener {

    @ViewById
    ImageView mIVsearchClear, mIVsearch;
    @ViewById
    ImageView mIVfilter;
    @ViewById
    EditText mETsearch;
    @ViewById
    TextView toCategory;
    @ViewById
    RecyclerView mRVpromo, mRVresult;
    @ViewById
    Toolbar mToolbar;
    @ViewById
    RelativeLayout oops;

    @ViewById
    RelativeLayout timeOut;
    @ViewById
    Button reload;
    @ViewById
    ProgressBar progressBar;
    @Pref
    LoginPref_ loginPref;

    Retrofit retrofit;
    PromoService promoService;
    String token, sort, myCards;
    String query;

    TrendingPromoAdapterCategory adapter;
    RecentAdapter adapterRecent;
    ArrayList<DiscoverPromotionCategory> listCategoryOutlet;
    ArrayList cardType;
    ArrayList categoryType;
    private ArrayList<SearchResultSuggestion> recentList = new ArrayList<>();

    @AfterViews
    void init() {
        SharedPreferences prefs = getSharedPreferences("filter", 0);

        Gson gson = new Gson();
        String categoryListKey = prefs.getString("categoryList", null);
        String cardTypeKey = prefs.getString("cardType", null);

        sort = prefs.getString("sort", "");
        categoryType = gson.fromJson(categoryListKey, ArrayList.class);
        cardType = gson.fromJson(cardTypeKey, ArrayList.class);

        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }

        prefs = getSharedPreferences("recentKey", 0);

        retrofit = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
        promoService = retrofit.create(PromoService.class);
        token = loginPref.token().get();

        listCategoryOutlet = new ArrayList<>();

        Intent intent = getIntent();
        query = intent.getStringExtra("query");

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        adapter = new TrendingPromoAdapterCategory(getApplicationContext(), listCategoryOutlet);
        mRVresult.setLayoutManager(new LinearLayoutManager(this));
        mRVresult.setAdapter(adapter);


        Retrofit retrofit = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
        final PromoService promoService = retrofit.create(PromoService.class);
        final String token = loginPref.token().get();


    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

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
