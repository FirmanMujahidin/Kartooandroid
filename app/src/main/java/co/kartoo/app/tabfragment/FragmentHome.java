package co.kartoo.app.tabfragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.plus.Plus;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import co.kartoo.app.CustomSwipeToRefresh;
import co.kartoo.app.EditProfile_;
import co.kartoo.app.R;
import co.kartoo.app.category.ActivityListPromoCategory_;
import co.kartoo.app.drawer.DrawerViewActivity_;
import co.kartoo.app.events.LatestFinishedEvent;
import co.kartoo.app.events.TrendingFinishedEvent;
import co.kartoo.app.home.HomePopularAdapterViewPager;
import co.kartoo.app.landing.LandingActivity_;
import co.kartoo.app.models.LoginPref_;
import co.kartoo.app.models.ProfilePref_;
import co.kartoo.app.promo.DiscoverHomeAdapter;
import co.kartoo.app.promo.HighlightPromoAdapter;
import co.kartoo.app.rest.CardService;
import co.kartoo.app.rest.MainService;
import co.kartoo.app.rest.PromoService;
import co.kartoo.app.rest.model.LatestPromo;
import co.kartoo.app.rest.model.Outlet;
import co.kartoo.app.rest.model.Promo;
import co.kartoo.app.rest.model.newest.Availablecards;
import co.kartoo.app.rest.model.newest.Discover;
import co.kartoo.app.rest.model.newest.DiscoverPromotion;
import co.kartoo.app.rest.model.newest.EditorsPick;
import co.kartoo.app.rest.model.newest.Home;
import co.kartoo.app.rest.model.newest.Popular;
import co.kartoo.app.views.MeasuredStaggeredGridLayoutManager;
import de.greenrobot.event.EventBus;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

@EFragment(R.layout.fragment_home)
public class FragmentHome extends Fragment {

    @ViewById(R.id.mIVindicator1)
    View mVindicator1;
    @ViewById(R.id.mIVindicator2)
    View mVindicator2;
    @ViewById(R.id.mIVindicator3)
    View mVindicator3;
    @ViewById(R.id.mIVindicator4)
    View mVindicator4;
    @ViewById(R.id.mIVindicator5)
    View mVindicator5;

    @ViewById
    ImageView mIVprofilePicture;
    @ViewById
    RecyclerView mRVdiscover;
    @ViewById
    RecyclerView mRVhighlight;
    @ViewById
    ViewPager mVPpopularPromo;
    @ViewById
    TextView mTVname;
    @ViewById
    ScrollView mSVmain;
    @ViewById
    ProgressBar progressBar;
    @ViewById
    ProgressBar progressBarPopular;
    @ViewById
    ImageView mIVhomeheader;
    @ViewById
    TextView mTVmessage;
    @ViewById
    TextView mTVexp;
    @ViewById
    ProgressBar mPBlevel;
    @ViewById
    FrameLayout Popular;
    @ViewById
    ImageView mIVexp;
    @ViewById
    TextView mTVnotificationCount;
    @ViewById
    TextView textView28;
    @ViewById
    LinearLayout linearLayout4;
    @ViewById
    LinearLayout linearLayout6;
    @ViewById
    TextView textView31;
    @ViewById
    RelativeLayout timeOut;
    @ViewById
    Button reload;

    @ViewById
    CustomSwipeToRefresh swiperefresh;

    @ViewById
    TextView addCard;
    @ViewById
    RelativeLayout noCard;

    @Pref
    LoginPref_ loginPref;

    @Pref
    ProfilePref_ profilePref;
    MeasuredStaggeredGridLayoutManager layoutManagerLatestPromo;
    ArrayList<Promo> listPushedPromo;
    ArrayList<DiscoverPromotion> listDiscoverPromo;
    ArrayList<EditorsPick> listHighlightPromo;
    ArrayList<Popular> popularHome;
    ArrayList<Outlet> listCustomizedPromo;
    HighlightPromoAdapter adapterHighlightPromo;
    DiscoverHomeAdapter adapterDiscoverHome;
    int lastPage = 1;
    int maxPage = -1;
    int lastClick;
    int heightY;
    int scrollY;
    int diff;
    Call<LatestPromo> latestPromoCall;
    Retrofit retrofit;
    PromoService promoService;
    String authorization;
    LatLng myLocation;
    CardService cardService;
    PagerAdapter adapter;
    String[] Id;
    String[] Name;
    String[] Promo;
    String[] UrlImage;
    String[] Bank;
    String[] Band;
    String idHeaderCategory;
    String nameCategory;
    GoogleApiClient mGoogleApiClient;
    private boolean loading = false;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Click(R.id.mIVindicator1)
    void mVindicator1() {
        mVPpopularPromo.setCurrentItem(0);
    }

    @Click(R.id.mIVindicator2)
    void mVindicator2() {
        mVPpopularPromo.setCurrentItem(1);
    }

    @Click(R.id.mIVindicator3)
    void mVindicator3() {
        mVPpopularPromo.setCurrentItem(2);
    }

    @Click(R.id.mIVindicator4)
    void mVindicator4() {
        mVPpopularPromo.setCurrentItem(3);
    }

    @Click(R.id.mIVindicator5)
    void mVindicator5() {
        mVPpopularPromo.setCurrentItem(4);
    }

    @AfterViews
    public void init() {

        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addApi(Plus.API)
                .addApi(LocationServices.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();
        mGoogleApiClient.connect();

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());

        linearLayout6.setVisibility(View.INVISIBLE);
        progressBarPopular.setVisibility(View.VISIBLE);

        mRVdiscover.setNestedScrollingEnabled(false);
        mRVhighlight.setFocusable(false);
        mRVdiscover.setFocusable(false);


        retrofit = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
        MainService service = retrofit.create(MainService.class);

        try {
            mTVname.setText("Hi, " + loginPref.name().get().split(" ")[0]);
            profilePref.name().put(loginPref.name().get());
        } catch (ArrayIndexOutOfBoundsException e) {
            profilePref.name().put(loginPref.name().get());
            mTVname.setText("Hi, " + loginPref.name().get());
        }
        if (!loginPref.poin().get().equals("")) {
            mPBlevel.setProgress(Integer.parseInt(loginPref.poin().get()));
        } else {
            mPBlevel.setProgress(0);
        }
        if (loginPref.urlPhoto().get() == null || loginPref.urlPhoto().get().equals("")) {
            mIVprofilePicture.setImageDrawable(getResources().getDrawable(R.drawable.ic_profil_pict));

        } else {
            //if(loginPref.fromBitmap().get()==1){
            //mIVprofilePicture.setImageBitmap(StringToBitMap(loginPref.urlPhoto().get()));
            //}
            //else {
            Log.e("TAG", "urlPhoto: " + loginPref.urlPhoto().get());
            Picasso.with(getActivity()).load(loginPref.urlPhoto().get()).into(mIVprofilePicture);
            //}
        }
        setUpRecyclerViews();
        Log.e("whatthe", "whatthe" + loginPref.token().get());

        //getPromoCategory();
        //myLocation = getCurrentLocation();

        authorization = loginPref.token().get();

        SharedPreferences prefs = getActivity().getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString("MID", authorization);
        edit.putString("mail", loginPref.email().get());
        edit.commit();

        /* SharedPref for caching */
        final SharedPreferences personalPrefs = getActivity().getSharedPreferences("personal_prefs", Context.MODE_PRIVATE);
        final SharedPreferences popularPrefs = getActivity().getSharedPreferences("popular_prefs", Context.MODE_PRIVATE);
        final SharedPreferences editorsPickPrefs = getActivity().getSharedPreferences("editorspick_prefs", Context.MODE_PRIVATE);
        final SharedPreferences discoverPrefs = getActivity().getSharedPreferences("discover_prefs", Context.MODE_PRIVATE);

        final SharedPreferences datePrefs = getActivity().getSharedPreferences("date_prefs", Context.MODE_PRIVATE);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(c.getTime());
        Log.d("currentDate", "date: " + formattedDate);

        String savedDate = datePrefs.getString("savedDate", "");
        Log.d("savedDate", "date: " + savedDate);
        if (!formattedDate.equals(savedDate)) {
            SharedPreferences.Editor personalPrefsEditor = personalPrefs.edit();
            SharedPreferences.Editor popularPrefsEditor = popularPrefs.edit();
            SharedPreferences.Editor editorsPickPrefsEditor = editorsPickPrefs.edit();
            SharedPreferences.Editor discoverPrefsEditor = discoverPrefs.edit();

            personalPrefsEditor.clear();
            personalPrefsEditor.commit();
            popularPrefsEditor.clear();
            popularPrefsEditor.commit();
            editorsPickPrefsEditor.clear();
            editorsPickPrefsEditor.commit();
            discoverPrefsEditor.clear();
            discoverPrefsEditor.commit();

            SharedPreferences.Editor datePrefsEditor = datePrefs.edit();
            datePrefsEditor.putString("savedDate", formattedDate);
            datePrefsEditor.commit();
        }

        swiperefresh.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i("LOG_TAG", "onRefresh called from SwipeRefreshLayout");
                        SharedPreferences.Editor personalPrefsEditor = personalPrefs.edit();
                        SharedPreferences.Editor popularPrefsEditor = popularPrefs.edit();
                        SharedPreferences.Editor editorsPickPrefsEditor = editorsPickPrefs.edit();
                        SharedPreferences.Editor discoverPrefsEditor = discoverPrefs.edit();

                        personalPrefsEditor.clear();
                        personalPrefsEditor.commit();
                        popularPrefsEditor.clear();
                        popularPrefsEditor.commit();
                        editorsPickPrefsEditor.clear();
                        editorsPickPrefsEditor.commit();
                        discoverPrefsEditor.clear();
                        discoverPrefsEditor.commit();

                        reload();
                    }
                }
        );
        swiperefresh.setColorSchemeResources(R.color.refresh_progress_1, R.color.refresh_progress_2, R.color.refresh_progress_3);

        checkCard();

        promoService = retrofit.create(PromoService.class);

        String personalPrefsId = personalPrefs.getString("id", "");
        if (!personalPrefsId.isEmpty()) {
            Gson gson = new Gson();
            linearLayout6.setVisibility(View.VISIBLE);
            progressBarPopular.setVisibility(View.GONE);
            timeOut.setVisibility(View.GONE);

            //Personalized Promotions
            nameCategory = personalPrefs.getString("name", "");
            mTVmessage.setText(personalPrefs.getString("caption", "")); //Dont forget for luch
            mIVhomeheader.setBackground(null);
            Picasso.with(getActivity())
                    .load(personalPrefs.getString("url_img", ""))
                    .fit()
                    .centerCrop()
                    .into(mIVhomeheader);
            idHeaderCategory = personalPrefs.getString("id", "");

            //Popular Promotions
            Id = new String[5];
            Name = new String[5];
            Promo = new String[5];
            Bank = new String[5];
            UrlImage = new String[5];
            Band = new String[5];
            Id = popularPrefs.getString("ids", "").split(",");
            Name = popularPrefs.getString("names", "").split(",");
            Promo = popularPrefs.getString("promos", "").split(",");
            Bank = popularPrefs.getString("banks", "").split(",");
            UrlImage = popularPrefs.getString("urlImages", "").split(",");
            Band = popularPrefs.getString("bands", "").split(",");
            if (Band.length == 0) {
                Band = new String[5];
                for (int i = 0; i < 5; i++) {
                    Band[i] = "";
                }
            }
            PopularViewPager();

            //Editors Pick
            String promotionsJson = editorsPickPrefs.getString("promotions", "");
            ArrayList<EditorsPick> promotionsList = gson.fromJson(promotionsJson, new TypeToken<ArrayList<EditorsPick>>() {
            }.getType());
            EventBus.getDefault().postSticky(new TrendingFinishedEvent(promotionsList));
        } else {
            Call<Home> headerHome = promoService.getHomeHeader(authorization);
            headerHome.enqueue(new Callback<Home>() {
                @Override
                public void onResponse(final Response<Home> response, Retrofit retrofit) {
                    Log.e("TAG", "onResponseheader: " + response.code());
                    if (response.isSuccess()) {
                        if (response.code() == 200) {
                            Log.e("TAG", "sizeHome: " + response.body().getEditorsPickPromotionList().size());
                            linearLayout6.setVisibility(View.VISIBLE);
                            progressBarPopular.setVisibility(View.GONE);
                            timeOut.setVisibility(View.GONE);

                            //Personalized Promotions
                            nameCategory = response.body().getPersonalizedPromotionCategory().getName();
                            mTVmessage.setText(response.body().getPersonalizedPromotionCategory().getCaption()); //Dont forget for luch
                            mIVhomeheader.setBackground(null);
                            Picasso.with(getActivity())
                                    .load(response.body().getPersonalizedPromotionCategory().getUrl_img())
                                    .fit()
                                    .centerCrop()
                                    .into(mIVhomeheader);
                            idHeaderCategory = response.body().getPersonalizedPromotionCategory().getId();

                            SharedPreferences.Editor personalPrefsEditor = personalPrefs.edit();
                            personalPrefsEditor.putString("name", response.body().getPersonalizedPromotionCategory().getName());
                            personalPrefsEditor.putString("caption", response.body().getPersonalizedPromotionCategory().getCaption());
                            personalPrefsEditor.putString("url_img", response.body().getPersonalizedPromotionCategory().getUrl_img());
                            personalPrefsEditor.putString("id", response.body().getPersonalizedPromotionCategory().getId());
                            personalPrefsEditor.commit();

                            //Popular Promotions
                            int a = response.body().getPopularPromotionList().size();
                            Id = new String[a];
                            Name = new String[a];
                            Promo = new String[a];
                            Bank = new String[a];
                            UrlImage = new String[a];
                            Band = new String[a];

                            for (int i = 0; i < a; i++) {
                                Id[i] = response.body().getPopularPromotionList().get(i).getId();
                                Name[i] = response.body().getPopularPromotionList().get(i).getMerchant().getName();
                                Promo[i] = response.body().getPopularPromotionList().get(i).getName();
                                Bank[i] = response.body().getPopularPromotionList().get(i).getBank().getName();
                                UrlImage[i] = response.body().getPopularPromotionList().get(i).getUrl_img();

                                if (response.body().getPopularPromotionList().get(i).getBand() != null) {
                                    Band[i] = response.body().getPopularPromotionList().get(i).getBand();
                                } else {
                                    Band[i] = "";
                                }
                                Log.e("TAG", "namefromapi: " + response.body().getPopularPromotionList().get(i).getUrl_img());
                            }

                            SharedPreferences.Editor popularPrefsEditor = popularPrefs.edit();
                            StringBuilder sb = new StringBuilder();
                            for (int i = 0; i < Id.length; i++) {
                                sb.append(Id[i]).append(",");
                            }
                            popularPrefsEditor.putString("ids", sb.toString());

                            sb = new StringBuilder();
                            for (int i = 0; i < Name.length; i++) {
                                sb.append(Name[i]).append(",");
                            }
                            popularPrefsEditor.putString("names", sb.toString());

                            sb = new StringBuilder();
                            for (int i = 0; i < Promo.length; i++) {
                                sb.append(Promo[i]).append(",");
                            }
                            popularPrefsEditor.putString("promos", sb.toString());

                            sb = new StringBuilder();
                            for (int i = 0; i < Bank.length; i++) {
                                sb.append(Bank[i]).append(",");
                            }
                            popularPrefsEditor.putString("banks", sb.toString());

                            sb = new StringBuilder();
                            for (int i = 0; i < UrlImage.length; i++) {
                                sb.append(UrlImage[i]).append(",");
                            }
                            popularPrefsEditor.putString("urlImages", sb.toString());

                            sb = new StringBuilder();
                            for (int i = 0; i < Band.length; i++) {
                                sb.append(Band[i]).append(",");
                            }
                            popularPrefsEditor.putString("bands", sb.toString());
                            popularPrefsEditor.commit();

                            PopularViewPager();

                            //EditorsPick
                            EventBus.getDefault().postSticky(new TrendingFinishedEvent(response.body().getEditorsPickPromotionList()));

                            SharedPreferences.Editor editorsPickPrefsEditor = editorsPickPrefs.edit();
                            Gson editorsPickGson = new Gson();
                            String promotions = editorsPickGson.toJson(response.body().getEditorsPickPromotionList());
                            editorsPickPrefsEditor.putString("promotions", promotions);
                            editorsPickPrefsEditor.commit();
                        } else if (response.code() == 401) {
                            Toast.makeText(getContext(), "Your session has expired, please login again", Toast.LENGTH_LONG).show();
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
                            // LoginManager.getInstance().logOut();
                            FacebookSdk.sdkInitialize(getContext());
                            LoginManager.getInstance().logOut();
                            if (mGoogleApiClient.isConnected()) {
                                Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                                mGoogleApiClient.disconnect();
                                Log.e("asdf", "logged out");
                            }

                            Log.e("sesudah", "sesudah" + loginPref.name().get());
                            Intent intent2 = new Intent(getActivity(), LandingActivity_.class);
                            startActivity(intent2);
                            getActivity().finish();
                        }
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.e("TAG", "onFailure: " + "Fail");
                    //do Refresh
                    linearLayout6.setVisibility(View.GONE);
                    progressBarPopular.setVisibility(View.GONE);
                    timeOut.setVisibility(View.VISIBLE);

                    Activity activity = getActivity();
                    if (activity != null && isAdded()) {
                        return;
                    }
                }
            });
        }

        String discoverJson = discoverPrefs.getString("discover", "");
        if (!discoverJson.isEmpty()) {
            Gson gson = new Gson();
            linearLayout6.setVisibility(View.VISIBLE);
            progressBarPopular.setVisibility(View.GONE);

            //Discover
            Discover discover = gson.fromJson(discoverJson, new TypeToken<Discover>() {
            }.getType());
            EventBus.getDefault().postSticky(new LatestFinishedEvent(discover));
        } else {
            Call<Discover> discover = promoService.getDiscover(authorization, lastPage);
            discover.enqueue(new Callback<Discover>() {
                @Override
                public void onResponse(Response<Discover> response, Retrofit retrofit) {
                    Log.e("TAG", "onResponsediscover: " + response.code());
                    if (response.isSuccess()) {
                        if (response.code() == 200) {
                            linearLayout6.setVisibility(View.VISIBLE);
                            progressBarPopular.setVisibility(View.GONE);

                            //Discover
                            EventBus.getDefault().postSticky(new LatestFinishedEvent(response.body()));

                            SharedPreferences.Editor discoverPrefsEditor = discoverPrefs.edit();
                            Gson discoverGson = new Gson();
                            String discover = discoverGson.toJson(response.body());
                            discoverPrefsEditor.putString("discover", discover);
                            discoverPrefsEditor.commit();
                        }
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    linearLayout6.setVisibility(View.GONE);
                    progressBarPopular.setVisibility(View.GONE);
                    timeOut.setVisibility(View.VISIBLE);
                }
            });
        }


        mSVmain.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                View view = mSVmain.getChildAt(mSVmain.getChildCount() - 1);
                if (listHighlightPromo.size() != 0) {
                    diff = (view.getBottom() - (mSVmain.getHeight() + mSVmain.getScrollY()));
                }
                heightY = mSVmain.getHeight();
                scrollY = mSVmain.getScrollY();

                Log.e("TAG", "onScrollChangedHome: " + (view.getBottom() - (mSVmain.getHeight() + mSVmain.getScrollY())));
                Log.e("TAG", "heightY: " + heightY);
                Log.e("TAG", "scrollY: " + scrollY);

                if (diff == 0) {
                    if (!loading && lastPage < maxPage) {
                        //String lastClick ="";
                        loading = true;

                        progressBar.setVisibility(View.VISIBLE);
                        Call<Discover> discover = promoService.getDiscover(authorization, lastPage + 1);
                        discover.enqueue(new Callback<Discover>() {
                            @Override
                            public void onResponse(Response<Discover> response, Retrofit retrofit) {
                                loading = false;
                                progressBar.setVisibility(View.GONE);
                                if (response.isSuccess()) {
                                    if (response.code() == 200) {
                                        listDiscoverPromo.addAll(response.body().getPromotions());
                                        adapterDiscoverHome.notifyDataSetChanged();
                                        maxPage = Integer.parseInt(response.body().getMaxPage());
                                        lastPage++;
                                        lastClick += lastPage;
                                        mixpanel();
                                        firebase();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                progressBar.setVisibility(View.GONE);
                                loading = false;
                            }
                        });
                    }
                }
            }
        });
    }

    private void enableDisableSwipeRefresh(boolean enable) {
        if (swiperefresh != null) {
            swiperefresh.setEnabled(enable);
        }
    }

    private void PopularViewPager() {
        adapter = new HomePopularAdapterViewPager(getActivity(), Id, Name, Bank, Promo, UrlImage, Band);
        updateIndicators(0);
        if (Name.length != 0) {
            mVPpopularPromo.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    updateIndicators(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    enableDisableSwipeRefresh(state == ViewPager.SCROLL_STATE_IDLE);
                }

            });
            mVPpopularPromo.setAdapter(adapter);
        }
    }

    public void updateIndicators(int position) {
        position = position % 5;
        switch (position) {
            case 0:
                mVindicator1.setBackgroundResource(R.drawable.ic_indicator_active);
                mVindicator1.requestLayout();
                mVindicator2.setBackgroundResource(R.drawable.ic_indicator_inactive);
                mVindicator2.requestLayout();
                mVindicator3.setBackgroundResource(R.drawable.ic_indicator_inactive);
                mVindicator3.requestLayout();
                mVindicator4.setBackgroundResource(R.drawable.ic_indicator_inactive);
                mVindicator4.requestLayout();
                mVindicator5.setBackgroundResource(R.drawable.ic_indicator_inactive);
                mVindicator5.requestLayout();
                break;

            case 1:
                mVindicator1.setBackgroundResource(R.drawable.ic_indicator_inactive);
                mVindicator1.requestLayout();
                mVindicator2.setBackgroundResource(R.drawable.ic_indicator_active);
                mVindicator2.requestLayout();
                mVindicator3.setBackgroundResource(R.drawable.ic_indicator_inactive);
                mVindicator3.requestLayout();
                mVindicator4.setBackgroundResource(R.drawable.ic_indicator_inactive);
                mVindicator4.requestLayout();
                mVindicator5.setBackgroundResource(R.drawable.ic_indicator_inactive);
                mVindicator5.requestLayout();
                break;

            case 2:
                mVindicator1.setBackgroundResource(R.drawable.ic_indicator_inactive);
                mVindicator1.requestLayout();
                mVindicator2.setBackgroundResource(R.drawable.ic_indicator_inactive);
                mVindicator2.requestLayout();
                mVindicator3.setBackgroundResource(R.drawable.ic_indicator_active);
                mVindicator3.requestLayout();
                mVindicator4.setBackgroundResource(R.drawable.ic_indicator_inactive);
                mVindicator4.requestLayout();
                mVindicator5.setBackgroundResource(R.drawable.ic_indicator_inactive);
                mVindicator5.requestLayout();
                break;

            case 3:
                mVindicator1.setBackgroundResource(R.drawable.ic_indicator_inactive);
                mVindicator1.requestLayout();
                mVindicator2.setBackgroundResource(R.drawable.ic_indicator_inactive);
                mVindicator2.requestLayout();
                mVindicator3.setBackgroundResource(R.drawable.ic_indicator_inactive);
                mVindicator3.requestLayout();
                mVindicator4.setBackgroundResource(R.drawable.ic_indicator_active);
                mVindicator4.requestLayout();
                mVindicator5.setBackgroundResource(R.drawable.ic_indicator_inactive);
                mVindicator5.requestLayout();
                break;
            case 4:
                mVindicator1.setBackgroundResource(R.drawable.ic_indicator_inactive);
                mVindicator1.requestLayout();
                mVindicator2.setBackgroundResource(R.drawable.ic_indicator_inactive);
                mVindicator2.requestLayout();
                mVindicator3.setBackgroundResource(R.drawable.ic_indicator_inactive);
                mVindicator3.requestLayout();
                mVindicator4.setBackgroundResource(R.drawable.ic_indicator_inactive);
                mVindicator4.requestLayout();
                mVindicator5.setBackgroundResource(R.drawable.ic_indicator_active);
                mVindicator5.requestLayout();
                break;

        }
    }

    public void reload() {
        /* SharedPref for caching */
        final SharedPreferences personalPrefs = getActivity().getSharedPreferences("personal_prefs", Context.MODE_PRIVATE);
        final SharedPreferences popularPrefs = getActivity().getSharedPreferences("popular_prefs", Context.MODE_PRIVATE);
        final SharedPreferences editorsPickPrefs = getActivity().getSharedPreferences("editorspick_prefs", Context.MODE_PRIVATE);
        final SharedPreferences discoverPrefs = getActivity().getSharedPreferences("discover_prefs", Context.MODE_PRIVATE);
        promoService = retrofit.create(PromoService.class);

        String personalPrefsId = personalPrefs.getString("id", "");
        if (!personalPrefsId.isEmpty()) {
            Gson gson = new Gson();
            linearLayout6.setVisibility(View.VISIBLE);
            progressBarPopular.setVisibility(View.GONE);
            timeOut.setVisibility(View.GONE);

            //Personalized Promotions
            nameCategory = personalPrefs.getString("name", "");
            mTVmessage.setText(personalPrefs.getString("caption", "")); //Dont forget for luch
            mIVhomeheader.setBackground(null);
            Picasso.with(getActivity())
                    .load(personalPrefs.getString("url_img", ""))
                    .fit()
                    .centerCrop()
                    .into(mIVhomeheader);
            idHeaderCategory = personalPrefs.getString("id", "");

            //Popular Promotions
            Id = new String[5];
            Name = new String[5];
            Promo = new String[5];
            Bank = new String[5];
            UrlImage = new String[5];
            Band = new String[5];
            Id = popularPrefs.getString("ids", "").split(",");
            Name = popularPrefs.getString("names", "").split(",");
            Promo = popularPrefs.getString("promos", "").split(",");
            Bank = popularPrefs.getString("banks", "").split(",");
            UrlImage = popularPrefs.getString("urlImages", "").split(",");
            Band = popularPrefs.getString("bands", "").split(",");
            if (Band.length == 0) {
                Band = new String[5];
                for (int i = 0; i < 5; i++) {
                    Band[i] = "";
                }
            }
            PopularViewPager();

            //Editors Pick
            String promotionsJson = editorsPickPrefs.getString("promotions", "");
            ArrayList<EditorsPick> promotionsList = gson.fromJson(promotionsJson, new TypeToken<ArrayList<EditorsPick>>() {
            }.getType());
            EventBus.getDefault().postSticky(new TrendingFinishedEvent(promotionsList));
        } else {
            linearLayout6.setVisibility(View.GONE);
            progressBarPopular.setVisibility(View.VISIBLE);
            timeOut.setVisibility(View.GONE);

            Call<Home> headerHome = promoService.getHomeHeader(authorization);
            headerHome.enqueue(new Callback<Home>() {
                @Override
                public void onResponse(final Response<Home> response, Retrofit retrofit) {
                    Log.e("TAG", "onResponseheader: " + response.code());
                    if (response.isSuccess()) {
                        if (response.code() == 200) {
                            swiperefresh.setRefreshing(false);
                            linearLayout6.setVisibility(View.VISIBLE);
                            Popular.setVisibility(View.VISIBLE);
                            progressBarPopular.setVisibility(View.GONE);

                            //Personalized Promotions
                            nameCategory = response.body().getPersonalizedPromotionCategory().getName();
                            mTVmessage.setText(response.body().getPersonalizedPromotionCategory().getCaption()); //Dont forget for luch
                            mIVhomeheader.setBackground(null);
                            Picasso.with(getActivity())
                                    .load(response.body().getPersonalizedPromotionCategory().getUrl_img())
                                    .fit()
                                    .centerCrop()
                                    .into(mIVhomeheader);
                            idHeaderCategory = response.body().getPersonalizedPromotionCategory().getId();

                            SharedPreferences.Editor personalPrefsEditor = personalPrefs.edit();
                            personalPrefsEditor.putString("name", response.body().getPersonalizedPromotionCategory().getName());
                            personalPrefsEditor.putString("caption", response.body().getPersonalizedPromotionCategory().getCaption());
                            personalPrefsEditor.putString("url_img", response.body().getPersonalizedPromotionCategory().getUrl_img());
                            personalPrefsEditor.putString("id", response.body().getPersonalizedPromotionCategory().getId());
                            personalPrefsEditor.commit();

                            //Popular Promotions
                            int a = response.body().getPopularPromotionList().size();
                            Id = new String[a];
                            Name = new String[a];
                            Promo = new String[a];
                            Bank = new String[a];
                            UrlImage = new String[a];
                            Band = new String[a];

                            for (int i = 0; i < a; i++) {
                                Id[i] = response.body().getPopularPromotionList().get(i).getId();
                                Name[i] = response.body().getPopularPromotionList().get(i).getMerchant().getName();
                                Promo[i] = response.body().getPopularPromotionList().get(i).getName();
                                Bank[i] = response.body().getPopularPromotionList().get(i).getBank().getName();
                                UrlImage[i] = response.body().getPopularPromotionList().get(i).getUrl_img();

                                if (response.body().getPopularPromotionList().get(i).getBand() != null) {
                                    Band[i] = response.body().getPopularPromotionList().get(i).getBand();
                                } else {
                                    Band[i] = "";
                                }
                                Log.e("TAG", "namefromapi: " + response.body().getPopularPromotionList().get(i).getUrl_img());
                            }

                            SharedPreferences.Editor popularPrefsEditor = popularPrefs.edit();
                            StringBuilder sb = new StringBuilder();
                            for (int i = 0; i < Id.length; i++) {
                                sb.append(Id[i]).append(",");
                            }
                            popularPrefsEditor.putString("ids", sb.toString());

                            sb = new StringBuilder();
                            for (int i = 0; i < Name.length; i++) {
                                sb.append(Name[i]).append(",");
                            }
                            popularPrefsEditor.putString("names", sb.toString());

                            sb = new StringBuilder();
                            for (int i = 0; i < Promo.length; i++) {
                                sb.append(Promo[i]).append(",");
                            }
                            popularPrefsEditor.putString("promos", sb.toString());

                            sb = new StringBuilder();
                            for (int i = 0; i < Bank.length; i++) {
                                sb.append(Bank[i]).append(",");
                            }
                            popularPrefsEditor.putString("banks", sb.toString());

                            sb = new StringBuilder();
                            for (int i = 0; i < UrlImage.length; i++) {
                                sb.append(UrlImage[i]).append(",");
                            }
                            popularPrefsEditor.putString("urlImages", sb.toString());

                            sb = new StringBuilder();
                            for (int i = 0; i < Band.length; i++) {
                                sb.append(Band[i]).append(",");
                            }
                            popularPrefsEditor.putString("bands", sb.toString());
                            popularPrefsEditor.commit();

                            PopularViewPager();

                            //EditorsPick
                            EventBus.getDefault().postSticky(new TrendingFinishedEvent(response.body().getEditorsPickPromotionList()));

                            SharedPreferences.Editor editorsPickPrefsEditor = editorsPickPrefs.edit();
                            Gson editorsPickGson = new Gson();
                            String promotions = editorsPickGson.toJson(response.body().getEditorsPickPromotionList());
                            editorsPickPrefsEditor.putString("promotions", promotions);
                            editorsPickPrefsEditor.commit();
                        }
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    linearLayout6.setVisibility(View.GONE);
                    progressBarPopular.setVisibility(View.GONE);
                    timeOut.setVisibility(View.VISIBLE);
                }
            });
        }

        String discoverJson = discoverPrefs.getString("discover", "");
        if (!discoverJson.isEmpty()) {
            Gson gson = new Gson();
            linearLayout6.setVisibility(View.VISIBLE);
            progressBarPopular.setVisibility(View.GONE);

            //Discover
            Discover discover = gson.fromJson(discoverJson, new TypeToken<Discover>() {
            }.getType());
            EventBus.getDefault().postSticky(new LatestFinishedEvent(discover));
        } else {
            Call<Discover> discover = promoService.getDiscover(authorization, lastPage);
            discover.enqueue(new Callback<Discover>() {
                @Override
                public void onResponse(Response<Discover> response, Retrofit retrofit) {
                    Log.e("TAG", "onResponsediscover: " + response.code());
                    if (response.isSuccess()) {
                        if (response.code() == 200) {
                            linearLayout6.setVisibility(View.VISIBLE);
                            progressBarPopular.setVisibility(View.GONE);

                            //Discover
                            EventBus.getDefault().postSticky(new LatestFinishedEvent(response.body()));

                            SharedPreferences.Editor discoverPrefsEditor = discoverPrefs.edit();
                            Gson discoverGson = new Gson();
                            String discover = discoverGson.toJson(response.body());
                            discoverPrefsEditor.putString("discover", discover);
                            discoverPrefsEditor.commit();
                        }
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    linearLayout6.setVisibility(View.GONE);
                    progressBarPopular.setVisibility(View.GONE);
                    timeOut.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    public void mixpanel() {
        String projectToken = getString(R.string.mixpanel_token);
        MixpanelAPI mixpanel = MixpanelAPI.getInstance(getContext(), projectToken);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTimeStamp = dateFormat.format(new Date());
        try {
            JSONObject props = new JSONObject();
            props.put("TimeStamp", currentTimeStamp);
            props.put("email", loginPref.email().get());
            mixpanel.track("Scroll Discover Promotion", props);
        } catch (JSONException e) {
            Log.e("MYAPP", "Unable to add properties to JSONObject", e);
        }
    }

    public void firebase() {
        Bundle params = new Bundle();
        params.putString("email", loginPref.email().get());
        mFirebaseAnalytics.logEvent("scroll_discover_promotion", params);
    }

    public void checkCard() {
        authorization = loginPref.token().get();
        retrofit = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
        cardService = retrofit.create(CardService.class);
        Call<ArrayList<Availablecards>> getUserCardCall = cardService.getCardStatus(authorization);
        getUserCardCall.enqueue(new Callback<ArrayList<Availablecards>>() {
            @Override
            public void onResponse(Response<ArrayList<Availablecards>> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    if (response.code() == 200) {
                        Log.e("TAG", "sizeBody: " + response.body().size());
                        if (response.body().size() == 0 || loginPref.type().get().equals("guest")) {
                            Log.e("TAG", "VISIBLE: " + "VISIBLE");
                            noCard.setVisibility(View.VISIBLE);
                        } else {
                            noCard.setVisibility(View.GONE);
                            Log.e("TAG", "INVIS: " + "INVIS");
                        }
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    /*
        public Bitmap StringToBitMap(String encodedString){
            try {
                byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
                Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                return bitmap;
            } catch(Exception e) {
                e.getMessage();
                return null;
            }
        }
        */
    public void setUpRecyclerViews() {
        //listPushedPromo = new ArrayList<>();
        listHighlightPromo = new ArrayList<>();
        listDiscoverPromo = new ArrayList<>();
        listCustomizedPromo = new ArrayList<>();

        int spacingInPixels = 3;

        /*
        layoutManagerLatestPromo = new MeasuredStaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRVlatestPromoTop.setLayoutManager(layoutManagerLatestPromo);
        mRVlatestPromoTop.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
        adapterListPromo = new DiscoverHomeAdapter(getActivity(), listLatestPromo);
        mRVlatestPromoTop.setAdapter(adapterListPromo);
*/

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        gridLayoutManager.setSpanCount(2);
        mRVdiscover.setLayoutManager(gridLayoutManager);
        adapterDiscoverHome = new DiscoverHomeAdapter(getActivity(), listDiscoverPromo);
        mRVdiscover.setAdapter(adapterDiscoverHome);

        //mRVtrendingPromo.addItemDecoration(new SpaceItemDecoration(spacingInPixels));

        adapterHighlightPromo = new HighlightPromoAdapter(getActivity(), listHighlightPromo);

        mRVhighlight.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        mRVhighlight.setAdapter(adapterHighlightPromo);
    }

    @Click(R.id.reload)
    public void reloadClick() {
        reload();
    }

    @Click(R.id.mIVhomeheader)
    public void mIVhomeheaderClick() {
        Intent intent = new Intent(getActivity(), ActivityListPromoCategory_.class);
        intent.putExtra("id", idHeaderCategory);
        intent.putExtra("name", nameCategory);
        intent.putExtra("from", "Open Personalized Promotion");
        Log.e("TAG", "mIVhomeheaderClick: " + idHeaderCategory);
        getActivity().startActivity(intent);
    }

    @Click(R.id.mIVprofilePicture)
    public void mIVprofilepictureClick() {
        Intent intent = new Intent(getActivity(), EditProfile_.class);
        intent.putExtra("token", loginPref.token().get());
        intent.putExtra("email", loginPref.email().get());

        if (loginPref.urlPhoto().get() != null) {
            intent.putExtra("image", loginPref.urlPhoto().get());
        }

        if (loginPref.name().get() != null) {
            intent.putExtra("name", loginPref.name().get());
        }
        startActivity(intent);
    }


    @Click(R.id.addCard)
    public void addCardClick() {
        Log.d("Action Button", "onClick triggered");
        if (loginPref.type().get().equals("guest")) {
            dialog();
        } else {
            Bundle bundle = new Bundle();
            Intent intent = new Intent(getActivity(), DrawerViewActivity_.class);
            bundle.putString("selectedPage", "my_cards");
            intent.putExtras(bundle);
            getActivity().startActivity(intent);
            getActivity().finish();
        }

    }

    public void dialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setTitle("Oops!");
        alertDialogBuilder.setMessage("You need to login first");
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.e("sebelum", "sebelum" + loginPref.name().get());
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
                // LoginManager.getInstance().logOut();

                Log.e("sesudah", "sesudah" + loginPref.name().get());
                Intent intent2 = new Intent(getActivity(), LandingActivity_.class);
                getActivity().startActivity(intent2);
                getActivity().finish();

            }
        });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().registerSticky(this);
        checkCard();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("TAG", "onResume: " + "onresume");
        checkCard();

        Log.e("TAG", "onResume: " + loginPref.addcard().get());
        if (loginPref.addcard().get()) {
            reload();
            loginPref.addcard().put(false);
        }
/*
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        mRVlatestPromoTop.scrollToPosition(preferences.getInt("position", 0));

        //mRVlatestPromoTop.scrollTo(0, preferences.getInt("position", 0));
        mSVmain.scrollTo(0, preferences.getInt("msv", 0));

        //lastPage = preferences.getInt("page", 0);

        Log.e("TAG", "onResume: "+ preferences.getInt("position", 0));
        Log.e("TAG", "onResume: "+ preferences.getInt("page", 0));
        */
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
        Activity activity = getActivity();
        if (activity != null && isAdded()) {
            return;
        }
    }

    public void onEvent(LatestFinishedEvent event) {
        Log.e("eventLatest", "caught");
        listDiscoverPromo.clear();
        maxPage = Integer.parseInt(event.getListPromo().getMaxPage());
        listDiscoverPromo.addAll(event.getListPromo().getPromotions());
        adapterDiscoverHome.notifyDataSetChanged();
    }

    public void onEvent(TrendingFinishedEvent event) {
        Log.e("eventTrending", "caught");
        listHighlightPromo.clear();
        //Collections.sort(event.getListPromo());
        listHighlightPromo.addAll(event.getListPromo());
        adapterHighlightPromo.notifyDataSetChanged();

    }

    @Override
    public void onPause() {
        adapter = new HomePopularAdapterViewPager(getActivity(), Id, Name, Bank, Promo, UrlImage, Band);
        super.onPause();

        Activity activity = getActivity();
        if (activity != null && isAdded()) {
            return;
        }

    }
}