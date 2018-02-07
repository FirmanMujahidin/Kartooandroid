package co.kartoo.app.nearby;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.ArrayList;
import java.util.Map;

import co.kartoo.app.R;
import co.kartoo.app.models.LoginPref_;
import co.kartoo.app.rest.PromoService;
import co.kartoo.app.rest.model.Card;
import co.kartoo.app.rest.model.newest.Nearby;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by MartinOenang on 10/26/2015.
 */
@EActivity(R.layout.activity_cluster_list)
public class ClusterListActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    @ViewById
    ToggleButton mTBactiveNow;
    @ViewById
    ToggleButton mTBmyCards;
    @ViewById
    ImageView mIVfilter;
    @ViewById
    RecyclerView mRVpromo;
    @ViewById
    Toolbar mToolbar;
    @ViewById
    TextView mTVtitle;
    @ViewById
    RelativeLayout relativeLayout3;
    @ViewById
    ProgressBar progressBar;


    PromoFromNearbyAdapter adapter;
    ArrayList<Nearby> listCategoryOutlet;
    ArrayList<Nearby> listCategoryOutletToShow;
    Map<String,Card> myCardsMap;

    @Pref
    LoginPref_ loginPref;

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
    @AfterViews
    public void init() {
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
        setUpNavDrawer();
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
        PromoService promoService = retrofit.create(PromoService.class);
        String token = loginPref.token().get();

        listCategoryOutletToShow = new ArrayList<>();
        Bundle bundle = getIntent().getExtras();

        listCategoryOutlet = (ArrayList<Nearby>) bundle.getSerializable("result");
        listCategoryOutletToShow.addAll(listCategoryOutlet);

        adapter = new PromoFromNearbyAdapter(getApplicationContext(),listCategoryOutletToShow);
        mRVpromo.setLayoutManager(new LinearLayoutManager(this));
        mRVpromo.setAdapter(adapter);

        mTBactiveNow.setOnCheckedChangeListener(ClusterListActivity.this);
        mTBmyCards.setOnCheckedChangeListener(ClusterListActivity.this);
    }
    /*
        public void loadData() {

            boolean myCards = mTBmyCards.isChecked();
            int sortBy = 0;
            listCategoryOutletToShow.clear();

            ArrayList<Promo> listPromoCategory = listCategoryOutlet.get(i).getPromotions();
            if (myCards) {
                listCategoryOutletToShow.add(listCategoryOutlet.get(i));


            } else {
            listCategoryOutletToShow.add(listCategoryOutlet.get(i));
            }




            adapter.notifyDataSetChanged();
        }
    */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        //loadData();
    }
}

