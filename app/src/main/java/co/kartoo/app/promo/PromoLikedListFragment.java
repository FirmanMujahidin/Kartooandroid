package co.kartoo.app.promo;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.ArrayList;

import co.kartoo.app.R;
import co.kartoo.app.events.LikeEvent;
import co.kartoo.app.models.LoginPref_;
import co.kartoo.app.promo.AvailableOutlet.TrendingPromoAdapterCategory;
import co.kartoo.app.rest.PromoService;
import co.kartoo.app.rest.model.newest.DiscoverPromotion;
import co.kartoo.app.rest.model.newest.DiscoverPromotionCategory;
import co.kartoo.app.views.MeasuredStaggeredGridLayoutManager;
import de.greenrobot.event.EventBus;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

@EFragment(R.layout.fragment_liked_promo)
public class PromoLikedListFragment extends Fragment {
    @ViewById(R.id.mRVbanks)
    RecyclerView mRVlikedPromo;
    @ViewById
    RelativeLayout timeOut, oops, mRVall;
    @ViewById
    ProgressBar progressBar;
    @ViewById
    Button reload;


    MeasuredStaggeredGridLayoutManager layoutManager;
    ArrayList<DiscoverPromotion> listLikedPromo;
    String authorization;
    LikedPromoAdapter adapter;

    ArrayList<DiscoverPromotionCategory> listCategoryOutlet;
    ArrayList<DiscoverPromotionCategory> listCategoryOutletToShow;
    ArrayList<DiscoverPromotionCategory> listTrendingPromo;
    TrendingPromoAdapterCategory adapterTrendingPromo;

    @Pref
    LoginPref_ loginPref;

    Retrofit retrofit;
    PromoService promoService;

    @AfterViews
    void init() {

        progressBar.setVisibility(View.VISIBLE);
        timeOut.setVisibility(View.GONE);
        oops.setVisibility(View.GONE);
        mRVlikedPromo.setVisibility(View.GONE);

        //layoutManager = new MeasuredStaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        //mRVlikedPromo.setLayoutManager(layoutManager);

        retrofit = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
        promoService = retrofit.create(PromoService.class);
/*
        listLikedPromo = new ArrayList<>();
        adapter = new LikedPromoAdapter(getActivity(), listLikedPromo);
        mRVlikedPromo.addItemDecoration(new SpaceItemDecoration(3));
        mRVlikedPromo.setAdapter(adapter);
*/
        listTrendingPromo = new ArrayList<>();
        adapterTrendingPromo = new TrendingPromoAdapterCategory(getActivity(),listTrendingPromo);
        //mRVlikedPromo.addItemDecoration(new SpaceItemDecoration(3));
        mRVlikedPromo.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mRVlikedPromo.setAdapter(adapterTrendingPromo);

        authorization = loginPref.token().get();
        String userId = loginPref.uid().get()+"";

        loadData();


    }

    public void loadData(){

        progressBar.setVisibility(View.VISIBLE);
        timeOut.setVisibility(View.GONE);
        oops.setVisibility(View.GONE);
        mRVlikedPromo.setVisibility(View.GONE);

        Call<ArrayList<DiscoverPromotionCategory>> getFavoritePromoCall = promoService.getAllFavoritePromotions(authorization, "", "0", "0");
        getFavoritePromoCall.enqueue(new Callback<ArrayList<DiscoverPromotionCategory>>() {
            @Override
            public void onResponse(Response<ArrayList<DiscoverPromotionCategory>> response, Retrofit retrofit) {
                Log.e("TAG", "onResponse: "+response.code() );
                if (response.isSuccess()) {
                    if (response.code() == 200) {
                        if(response.body().size() == 0){
                            progressBar.setVisibility(View.GONE);
                            timeOut.setVisibility(View.GONE);
                            oops.setVisibility(View.VISIBLE);
                            mRVlikedPromo.setVisibility(View.GONE);
                        }
                        else {
                            progressBar.setVisibility(View.GONE);
                            timeOut.setVisibility(View.GONE);
                            oops.setVisibility(View.GONE);
                            mRVlikedPromo.setVisibility(View.VISIBLE);
                            EventBus.getDefault().postSticky(new LikeEvent(response.body()));
                        }
                    }
                }

            }

            @Override
            public void onFailure(Throwable t) {
                progressBar.setVisibility(View.GONE);
                timeOut.setVisibility(View.VISIBLE);
                oops.setVisibility(View.GONE);
                mRVlikedPromo.setVisibility(View.GONE);
            }
        });
    }

    @Click(R.id.reload)
    public void setReload() {
        loadData();
    }

    @Click(R.id.mRVall)
    public void mRVallClick(){
        loadData();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().registerSticky(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        //lastPage = 1;
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    public void onEvent(LikeEvent event) {
        Log.e("eventTrending", "caught");
        listTrendingPromo.clear();
        listTrendingPromo.addAll(event.getListPromo());
        adapterTrendingPromo.notifyDataSetChanged();

    }

}
