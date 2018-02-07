package co.kartoo.app.search;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.plus.Plus;
import com.google.gson.Gson;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.ArrayList;

import co.kartoo.app.R;
import co.kartoo.app.events.TrendingFinishedEventFilterResult;
import co.kartoo.app.landing.LandingActivity_;
import co.kartoo.app.models.LoginPref_;
import co.kartoo.app.nearby.GPSTracker;
import co.kartoo.app.promo.AvailableOutlet.TrendingPromoAdapterCategory;
import co.kartoo.app.rest.PromoService;
import co.kartoo.app.rest.model.newest.DiscoverPromotionCategory;
import co.kartoo.app.rest.model.newest.FilterSearch;
import co.kartoo.app.rest.model.newest.SearchPromotion;
import co.kartoo.app.rest.model.newest.SearchPromotionHeader;
import co.kartoo.app.rest.model.newest.SearchRecent;
import co.kartoo.app.rest.model.newest.SearchResultSuggestion;
import co.kartoo.app.rest.model.newest.SearchSuggestion;
import co.kartoo.app.search.filter.PromoFromFilterAdapter;
import co.kartoo.app.search.filter.SearchFilterActivity_;
import de.greenrobot.event.EventBus;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

@EActivity(R.layout.activity_search)
public class SearchActivity extends AppCompatActivity implements RecentInterface{
    @ViewById
    RecyclerView mRVresult, mRVsuggestion;
    @ViewById
    EditText mETsearch;
    @ViewById
    Toolbar mToolbar;
    @ViewById
    LinearLayout mLLsearch, mLLdata;

    @ViewById
    ImageView mIVsearchClear;
    @ViewById
    ImageView mIVsearch, mIVfilter;

    @ViewById
    ProgressBar mPBloading;
    @ViewById
    RelativeLayout oops;
    @ViewById
    TextView toCategory;

    @ViewById
    RelativeLayout timeOut;
    @ViewById
    Button reload;

    @ViewById
    Spinner sortbySpinner;

    @Pref
    LoginPref_ loginPref;

    Retrofit retrofit;
    PromoService promoService;
    String token, sort, myCards;
    String query;

    LatLng myLocation;
    String[] sortBy = {
            "A to Z",
            "Popular",
            "Distance",
            "Ending Soon",

    };
    String sortValue;
    GPSTracker gps;
    int a;
    int n;
    GoogleApiClient mGoogleApiClient;

    TrendingPromoAdapterCategory adapter;
    RecentAdapter adapterRecent;
    ArrayList<DiscoverPromotionCategory> listCategoryOutlet;

    SharedPreferences prefs;
    private ArrayList<SearchResultSuggestion> recentList = new ArrayList<>();
    SearchResultSuggestion recent;
    ArrayList<SearchResultSuggestion> filteredList;
    ArrayList<SearchRecent> preFilter;
    ArrayList cardType;
    ArrayList categoryType;

    ArrayList<SearchPromotion> listFilteredPromo;
    PromoFromFilterAdapter adapterFilteredPromo;

    int lastClick;
    int lastPage = 1;
    int maxPage = 1;
    int diff;
    int scrollY;
    int heightY;
    private boolean loading = false;

    @AfterViews
    void init() {

        SharedPreferences prefs = getSharedPreferences("filter", 0);

        Gson gson = new Gson();
        String categoryListKey = prefs.getString("categoryList", null);
        String cardTypeKey = prefs.getString("cardType", null);

        sort = prefs.getString("sort", "");
//        myCards = prefs.getString("myCards", "");
        categoryType = gson.fromJson(categoryListKey, ArrayList.class);
        cardType = gson.fromJson(cardTypeKey, ArrayList.class);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Plus.API)
                .addApi(LocationServices.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();
        mGoogleApiClient.connect();

        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }

        setUpNavDrawer();

        //mPBloading.setVisibility(View.VISIBLE);
        //oops.setVisibility(View.GONE);

        myLocation = getCurrentLocation();

        prefs = getSharedPreferences("recentKey", 0);

        retrofit = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
        promoService = retrofit.create(PromoService.class);
        token = loginPref.token().get();

        listCategoryOutlet = new ArrayList<>();

        Intent intent = getIntent();
        query = intent.getStringExtra("query");

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        adapter = new TrendingPromoAdapterCategory(getApplicationContext(),listCategoryOutlet);
        mRVresult.setLayoutManager(new LinearLayoutManager(this));
        mRVresult.setAdapter(adapter);

        //recent Adapter Set
        adapterRecent = new RecentAdapter(getApplicationContext(), recentList);
        mRVsuggestion.setLayoutManager(new LinearLayoutManager(this));
        mRVsuggestion.setAdapter(adapterRecent);

        //listFilteredPromo = new ArrayList<>();
        //adapterFilteredPromo = new PromoFromFilterAdapter(this, listFilteredPromo, promoService, token, mRVresult);
        //mRVresult.addItemDecoration(new SpaceItemDecoration(3));
        //mRVresult.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //mRVresult.setAdapter(adapterFilteredPromo);

        //spinner SortBy
        ArrayAdapter<String> adapterspinner= new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, sortBy);
        sortbySpinner.setAdapter(adapterspinner);

        if (myLocation.longitude==0){
            a=2;
            sortbySpinner.setSelection(a);
            sortValue = "expiring";
        }
        else {
            sortbySpinner.setSelection(0);
            sortValue = "distance";
        }

        sortbySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (pos==0){
                    if (myLocation.longitude==0){
                        Toast.makeText(SearchActivity.this, "Are you lost? Please turn on your location", Toast.LENGTH_LONG).show();
                        sortbySpinner.setSelection(a);
                    }
                    else {
                        sortValue = "distance";
                        //mRVresult.setVisibility(View.GONE);
                        //mPBloading.setVisibility(View.GONE);
                    }
                } else if (pos==1){
                    sortValue = "popular";
                    //mRVresult.setVisibility(View.GONE);
                    //mPBloading.setVisibility(View.GONE);


                } else if (pos==2){
                    sortValue = "expiring";
                    //mRVresult.setVisibility(View.GONE);
                    //mPBloading.setVisibility(View.GONE);


                } else if (pos==3){
                    sortValue = "alphabet";
                    //mRVresult.setVisibility(View.GONE);
                }
                Log.e("TAG", "onItemSelected: "+pos);
                Log.e("TAG", "onItemSelected: "+sortbySpinner.getSelectedItem().toString().toLowerCase());
                a = sortbySpinner.getSelectedItemPosition();
                mPBloading.setVisibility(View.VISIBLE);
                search(mETsearch.getText().toString(), sortValue);
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        if (query != null){
            mETsearch.setText(query);
            search(query,sortValue);
        }

        mETsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String str = mETsearch.getText().toString();
                if (str.length() == 0){
//                    if (filteredList!=null){
//                        filteredList.clear();
//                    }
                    recentList = new ArrayList<>();
                    adapterRecent = new RecentAdapter(getApplicationContext(), recentList);
                    mRVsuggestion.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
                    mRVsuggestion.setAdapter(adapterRecent);
                    mRVsuggestion.setVisibility(View.GONE);
                }
                else
                {
                    suggestion(str);
                }
//                if (str.length() >= 3){
//                    Log.e("TAG", "afterTextChanged: "+"LOAD");
//                    suggestion(mETsearch.getText().toString());
//                    mRVsuggestion.setVisibility(View.VISIBLE);
//                }
            }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //s = s.toString().toLowerCase();
                //filteredList = new ArrayList<>();
                //preFilter = new ArrayList<>();

                /*
                Log.e("TAG", "onTextChanged: "+ s );
                if (recentList.size()!=0){
                    for (int i = 0; i < recentList.size(); i++) {
                        Log.e("TAG", "onTextChanged: "+i);
                        preFilter = new ArrayList<>();
                        for (int j = 0; j < recentList.get(i).getResultList().size(); j++) {
                            //if (j < 6){
                                final String text = recentList.get(i).getResultList().get(j).getResult().toLowerCase();
                                if (text.contains(s)){
                                    Log.e("TAG", "contains: "+text);
                                    preFilter.add(new SearchRecent(text));
                                }
                            //}
                        }
                        if (preFilter.size()!=0){
                            recent = new SearchResultSuggestion(recentList.get(i).getHeader(), preFilter);
                            filteredList.add(recent);
                            for (int m = 0; m < filteredList.size() ; m++) {
                                for (int n = 0; n < filteredList.get(m).getResultList().size(); n++){
                                    Log.e("TAG", m + " " + n + "filteredListAfterAdd: " + filteredList.get(m).getResultList().get(n).getResult().toString());
                                }
                            }
                        }
                    }
                    for (int i = 0; i < filteredList.size() ; i++) {
                            for (int j = 0; j < filteredList.get(i).getResultList().size(); j++){
                                Log.e("TAG", i + " " + j + "filteredListBeforeSet: " + filteredList.get(i).getResultList().get(j).getResult().toString());
                            }
                    }

                    mRVsuggestion.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
                    adapterRecent = new RecentAdapter(SearchActivity.this, filteredList);
                    mRVsuggestion.setAdapter(adapterRecent);
                    adapterRecent.notifyDataSetChanged();
                }
                */
            }
        });

        Log.e("TAG", "query: "+query+sortValue);
        mETsearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Log.e("TAG", "onEditorAction: "+sortValue);
                    Log.e("TAG", "onEditorAction: "+mETsearch.getText().toString() );
                    search(v.getText().toString(), sortValue);

                    mETsearch.clearFocus();
                    mRVsuggestion.setVisibility(View.GONE);
                    mPBloading.setVisibility(View.VISIBLE);

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY,0);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void userItemClick(String name) {
        mETsearch.setText(name);
        mETsearch.setSelection(mETsearch.length());
        search(name, sortValue);
        mLLdata.setVisibility(View.GONE);
        oops.setVisibility(View.GONE);
        timeOut.setVisibility(View.GONE);
        mPBloading.setVisibility(View.VISIBLE);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY,0);

        Log.e("TAG", "userItemClick: "+name);
    }

    @Click(R.id.mETsearch)
    public void mETsearchClick(){
        mPBloading.setVisibility(View.GONE);
        oops.setVisibility(View.GONE);
        timeOut.setVisibility(View.GONE);
        mRVsuggestion.setVisibility(View.VISIBLE);
        mLLdata.setVisibility(View.GONE);
    }

    @Click(R.id.mIVsearchClear)
    public void mIVsearchClearClick() {
        mPBloading.setVisibility(View.GONE);
        mETsearch.setText("");
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mETsearch.getWindowToken(), 0);
    }

    @Click(R.id.mIVfilter)
    public void mIVfilterClick() {
        Intent intent = new Intent(SearchActivity.this, SearchFilterActivity_.class);
        startActivityForResult(intent, 1);
        overridePendingTransition(R.anim.slide_in_up, R.anim.stay);
    }

    @OnActivityResult(1)
    void onResult(int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK){
            final FilterSearch fs = new FilterSearch();
            fs.setQuery("");
            fs.setIsMyCard(data.getStringExtra("isMyCard"));
            fs.setSortBy(data.getStringExtra("sortBy"));
            fs.setLatitude(myLocation.latitude + "");
            fs.setLongitude(myLocation.longitude + "");

            ArrayList<String> ct = new ArrayList<>();
            if (data.getStringArrayListExtra("cardTypes").isEmpty())
            {
                ct.add("Credit");
                fs.setCardTypes(ct);
            }
            else
            {
                fs.setCardTypes(data.getStringArrayListExtra("cardTypes"));
            }

            ArrayList<String> cat = new ArrayList<>();
            if (data.getStringArrayListExtra("categoryTypes").isEmpty())
            {
                cat.add("Dining");
                fs.setCategoryTypes(cat);
            }
            else
            {
                fs.setCategoryTypes(data.getStringArrayListExtra("categoryTypes"));
            }

            retrofit = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
            promoService = retrofit.create(PromoService.class);
            token = loginPref.token().get();
            Log.e("TAG", "Result Query: "+fs.getQuery() );
            Log.e("TAG", "Result isMyCard: "+fs.getIsMyCard() );
            Log.e("TAG", "Result sortBy: "+fs.getSortBy() );
            Log.e("TAG", "Result Latitude: "+fs.getLatitude() );
            Log.e("TAG", "Result Longitude: "+fs.getLongitude() );
            for (int i = 0; i < fs.getCardTypes().size(); i++) {
                Log.e("TAG", "Result CardType: " + fs.getCardTypes().get(i) );
            }
            for (int i = 0; i < fs.getCategoryTypes().size(); i++) {
                Log.e("TAG", "Result CategoryType: " + fs.getCategoryTypes().get(i) );
            }

            Call<SearchPromotionHeader> promoSearchFilterCall = promoService.doFilterSearch(token, 1, fs); // cek data yang dikirim udah bener dan sama yang diminta backend
            promoSearchFilterCall.enqueue(new Callback<SearchPromotionHeader>() {
                @Override
                public void onResponse(Response<SearchPromotionHeader> response, Retrofit retrofit) {
                    Log.e("TAG", "responseCode: " + response.code());
                    if (response.isSuccess()) {
                        Log.e("TAG", "responseSearchFilter: "+ response.code());
                        if (response.code() == 200) {
                            maxPage = Integer.parseInt(response.body().getMaxPage());
                            if (response.body().getPromotions().size()==0){
                                //progressBar.setVisibility(View.GONE);
                                mLLdata.setVisibility(View.GONE);
                                oops.setVisibility(View.VISIBLE);
                                timeOut.setVisibility(View.GONE);
                                mLLdata.setVisibility(View.GONE);
                            }
                            else {
                                Log.e("TAG", "callSearchFilter: " + response.code());
                                //listCategoryOutlet.clear();
                                //listCategoryOutlet.addAll(response.body());
                                listFilteredPromo = new ArrayList<>();
                                listFilteredPromo.addAll(response.body().getPromotions());
                                adapterFilteredPromo = new PromoFromFilterAdapter(SearchActivity.this, listFilteredPromo, promoService, token, mRVresult);
                                adapterFilteredPromo.notifyDataSetChanged();
                                mRVresult.setAdapter(adapterFilteredPromo);

                                //progressBar.setVisibility(View.GONE);
                                mLLdata.setVisibility(View.VISIBLE);
                                mRVresult.setVisibility(View.VISIBLE);
                                oops.setVisibility(View.GONE);
                                timeOut.setVisibility(View.GONE);

                            }
                            EventBus.getDefault().postSticky(new TrendingFinishedEventFilterResult(response.body()));
                            //mTBmyCards.setOnCheckedChangeListener(FilterResultActivity.this);
                            //mixpanel();
                            //firebase();
                        }
                    }
                }
                @Override
                public void onFailure(Throwable t) {
                    //progressBar.setVisibility(View.GONE);
                    oops.setVisibility(View.GONE);
                    mLLdata.setVisibility(View.GONE);
                    mRVresult.setVisibility(View.GONE);
                    timeOut.setVisibility(View.VISIBLE);
                }
            });
            mRVresult.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                @Override
                public void onScrollChanged() {
                    View view = mRVresult.getChildAt(mRVresult.getChildCount() - 1);
                    if (listFilteredPromo.size()!=0){
                        diff = (view.getBottom() - (mRVresult.getHeight() + mRVresult.getScrollY()));
                    }
                    heightY = mRVresult.getHeight();
                    scrollY = mRVresult.getScrollY();

                    Log.e("diff", "diff: "+diff );
                    if (diff < 0) {
                        if (!loading && lastPage < maxPage) {
                            loading = true;
                            Log.e("TAG", "masukScroll: "+myCards);

                            final int defaultHeight = mRVresult.getLayoutParams().height;
                            //mRVresult.getLayoutParams().height = mRVresult.getHeight() - progressBarScroll.getHeight();
                            //progressBarScroll.bringToFront();
                            //progressBarScroll.setVisibility(View.VISIBLE);
                            //LLloadMore.setVisibility(View.VISIBLE);

                            ////progressBarScroll.showContextMenu();
                            //if (myCards){
                            Call<SearchPromotionHeader> promoMoreSearchFilterCall = promoService.doFilterSearch(token, lastPage + 1, fs);;

                            //if (myCards){
//                            if (sortValue.equals("distance")) {
//                                promoByCategoryCall = promoService.getPromoSortByDistance(token, idPromo, lastPage + 1, myCards + "", myLocation.longitude + "", myLocation.latitude + "");
//                            } else {
//                                promoByCategoryCall = promoService.getPromoMyCard(token, idPromo, lastPage + 1, myCards + "", sortValue);
//                            }

                            Log.e("onScrollChanged", "onScrollChanged: "+lastPage+1);
                            //Call<CategoryHeader> headerCall = promoService.u1(token, id, lastPage+1, sortValue, String.valueOf(myLocation.longitude), String.valueOf(myLocation.latitude));
                            promoMoreSearchFilterCall.enqueue(new Callback<SearchPromotionHeader>() {
                                @Override
                                public void onResponse(Response<SearchPromotionHeader> response, Retrofit retrofit) {
                                    loading = false;
                                    mRVresult.getLayoutParams().height = defaultHeight;
                                    //progressBarScroll.setVisibility(View.GONE);
                                    //LLloadMore.setVisibility(View.GONE);
                                    if (response.isSuccess()) {
                                        if (response.code() == 200) {
                                            listFilteredPromo.addAll(response.body().getPromotions());
                                            adapterFilteredPromo.notifyDataSetChanged();
                                            maxPage = Integer.parseInt(response.body().getMaxPage());
                                            lastPage++;
                                            lastClick += lastPage;

                                        }
                                    }
                                }
                                @Override
                                public void onFailure(Throwable t) {
                                    mRVresult.getLayoutParams().height = defaultHeight;
                                    //progressBar.setVisibility(View.GONE);
                                    //progressBarScroll.setVisibility(View.GONE);
                                    //LLloadMore.setVisibility(View.GONE);
                                    loading = false;

                                }
                            });
                            //}
//                        else {
//                            Log.e("TAG", "onScrollChanged: "+"masuk+NotMyCard");
//                            Call<CategoryOutlet> promoByCategoryCall = promoService.getPromo(token, id, lastPage+1, sortValue, String.valueOf(myLocation.longitude), String.valueOf(myLocation.latitude));
//                            promoByCategoryCall.enqueue(new Callback<CategoryOutlet>() {
//                                @Override
//                                public void onResponse(Response<CategoryOutlet> response, Retrofit retrofit) {
//                                    loading = false;
//                                    if (response.isSuccess()) {
//                                        if (response.code() == 200) {
//                                            listTrendingPromo.addAll(response.body().getPromotions());
//                                            adapterTrendingPromo.notifyDataSetChanged();
//                                            maxPage = response.body().getMaxPage();
//                                            lastPage++;
//                                        }
//                                    }
//                                }
//                                @Override
//                                public void onFailure(Throwable t) {
//                                    //progressBar.setVisibility(View.GONE);
//                                    loading = false;
//                                }
//                            });
//                        }
                        }
                    }
                }
            });
        }
    }
/*
    @Click(R.id.toCategory)
    public void toCategoryClick() {
        Intent intent = new Intent(SearchActivity.this, MainActivity_.class);
        intent.putExtra("category", "1");
        startActivity(intent);
        finish();
    }
*/
    @Click(R.id.reload)
    public void reloadClick() {
        search(query, sortValue);
    }

    void suggestion(String preQuery){
        Call<SearchSuggestion> searchSuggestionCall = promoService.getSearchRecent(token, preQuery);
        searchSuggestionCall.enqueue(new Callback<SearchSuggestion>() {
            @Override
            public void onResponse(Response<SearchSuggestion> response, Retrofit retrofit) {
                Log.e("TAG", "suggesstion: "+response.code());
                if(response.code() == 200) {
                    Log.e("TAG", "onResponse: "+response.body().getSearchResults().size());
                    EventBus.getDefault().postSticky(new RecentEvent(response.body().getSearchResults()));
                    adapterRecent.notifyDataSetChanged();
                    mRVsuggestion.setVisibility(View.VISIBLE);
                    mLLdata.setVisibility(View.GONE);

                }
                else {

                }
            }
            @Override
            public void onFailure(Throwable t) {
            }
        });
    }

    void search(String query, String sortValue) {
        if(query != null){
            if(sortValue!=null){
                mPBloading.setVisibility(View.VISIBLE);
                mRVresult.setVisibility(View.GONE);
                mLLdata.setVisibility(View.GONE);
                oops.setVisibility(View.GONE);
                timeOut.setVisibility(View.GONE);
                mRVsuggestion.setVisibility(View.GONE);

                Call<ArrayList<DiscoverPromotionCategory>> searchCall = promoService.searchAll(token, query, sortValue, myLocation.latitude+"", myLocation.longitude+"");
                searchCall.enqueue(new Callback<ArrayList<DiscoverPromotionCategory>>() {
                    @Override
                    public void onResponse(Response<ArrayList<DiscoverPromotionCategory>> response, Retrofit retrofit) {
                        Log.e("TAG", "responseSearch: "+response.code() );
                        if(response.code() == 200) {
                            Log.e("TAG", "responselenghtKedua: "+response.body().size() );
                            if(response.body().size()==0){
                                mPBloading.setVisibility(View.GONE);
                                oops.setVisibility(View.VISIBLE);
                                timeOut.setVisibility(View.GONE);
                                mLLdata.setVisibility(View.GONE);
                                mRVresult.setVisibility(View.GONE);
                            }

                            else {
                                timeOut.setVisibility(View.GONE);
                                mLLdata.setVisibility(View.VISIBLE);
                                oops.setVisibility(View.GONE);
                                mRVresult.setVisibility(View.VISIBLE);

                                listCategoryOutlet.clear();
                                listCategoryOutlet.addAll(response.body());
                                adapter.notifyDataSetChanged();
                                mRVresult.setAdapter(adapter);
                                //mPBloading.setVisibility(View.GONE);

                            }
                        }
                        else if (response.code() == 401){
                            Toast.makeText(SearchActivity.this, "Your session has expired, please login again", Toast.LENGTH_LONG).show();
                            loginPref.name().remove();
                            loginPref.email().remove();
                            loginPref.token().remove();
                            loginPref.urlPhoto().remove();
                            loginPref.level().remove();
                            loginPref.uid().remove();
                            loginPref.type().remove();
                            loginPref.fromBitmap().remove();
                            loginPref.addcard().remove();
                            loginPref.regid().remove();
                            loginPref.notifID().remove();
                            //LoginManager.getInstance().logOut();
                            FacebookSdk.sdkInitialize(getApplicationContext());
                            LoginManager.getInstance().logOut();
                            if (mGoogleApiClient.isConnected()) {
                                Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                                mGoogleApiClient.disconnect();
                            }
                            Intent intent2 = new Intent(SearchActivity.this, LandingActivity_.class);
                            startActivity(intent2);
                            finish();
                        }

                        else {
                            //timeOut.setVisibility(View.VISIBLE);
                            //oops.setVisibility(View.GONE);
                            //nbv   mLLdata.setVisibility(View.GONE);
                            mPBloading.setVisibility(View.GONE);
                        }
                    }
                    @Override
                    public void onFailure(Throwable t) {
                        timeOut.setVisibility(View.VISIBLE);
                        oops.setVisibility(View.GONE);
                        mLLdata.setVisibility(View.GONE);
                        mPBloading.setVisibility(View.GONE);
                    }
                });
            }

        }
    }

    public void sharedPreference(){
        SharedPreferences.Editor edit = prefs.edit();
        Gson gson = new Gson();
        String setCardList = gson.toJson(recentList);
        Log.e("TAG", "sharedPreference: "+setCardList);

        edit.putString("recentKey", setCardList);
        edit.apply();
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

    public LatLng getCurrentLocation() {
        gps = new GPSTracker(this);
        double latitude;
        double longitude;
        // check if GPS enabled
        if(gps.canGetLocation()){
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            Log.e("TAG", "getCurrentLocation: "+latitude+longitude );
            return new LatLng(latitude, longitude);
        }
        else{
            return new LatLng(0, 0);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().registerSticky(this);
    }

    @Override
    public void onResume() {
        if (recentList!=null){
            recentList.clear();
        }
        super.onResume();
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    public void onEvent(RecentEvent event) {
        recentList.clear();
        Log.e("TAG", "onEvent:"+event.getRecentList().get(0).toString());
        recentList.addAll(event.getRecentList());
        adapterRecent.notifyDataSetChanged();
    }

}
