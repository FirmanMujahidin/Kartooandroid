package co.kartoo.app.bank.detail;

import android.content.Intent;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import co.kartoo.app.R;
import co.kartoo.app.bank.detail.ATMLocator.ActivityLocator_;
import co.kartoo.app.chrome.CustomTabActivityHelper;
import co.kartoo.app.chrome.WebViewFallBack;
import co.kartoo.app.models.LoginPref_;
import co.kartoo.app.rest.BankService;
import co.kartoo.app.rest.model.newest.BankFeed;
import co.kartoo.app.rest.model.newest.BankFeedItem;
import co.kartoo.app.rest.model.newest.BankPage;
import de.greenrobot.event.EventBus;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;


@EActivity(R.layout.activity_bank_detail)
public class ActivityBankDetail extends AppCompatActivity  {

    @ViewById
    RecyclerView mRVbank;

    @ViewById
    Toolbar mToolbar;

    @ViewById
    RelativeLayout oops, BtnWeb, BtnTel, BtnShare, BtnLoc;

    @ViewById
    Button bweb, btel, bshare, bloc;

    @ViewById
    RelativeLayout timeOut;
    @ViewById
    ImageView reload;
    @ViewById
    ImageView IVheader;

    @ViewById
    TextView TVBankName;
    @ViewById
    TextView TVBankDesc;
    @ViewById
    ImageView IVbankPicture;
    @ViewById
    Button BtnFollow;
    @ViewById
    RelativeLayout header;
    @ViewById
    ProgressBar progressBar;

    String TAG = "ActivityBankDetail";

    @Pref
    LoginPref_ loginPref;

    CollapsingToolbarLayout collapsingTolbarLayout;
    BankService bankService;
    String token, Id, name, image, desc, phone, web;
    ArrayList<BankFeedItem> feedItem;
    BankDetilAdapter feedAdapter;
    private boolean loading = false;

    Boolean isFollowing;

    int lastPage=  1;
    int maxPage = -1;
    int diff;

    private CustomTabActivityHelper mCustomTabActivityHelper;

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

        mCustomTabActivityHelper = new CustomTabActivityHelper();

        Intent intent = getIntent();
        Id = intent.getStringExtra("Id");
        name = intent.getStringExtra("name");
        image = intent.getStringExtra("image");
        desc = intent.getStringExtra("desc");

        Log.e(TAG, "Id: "+Id );

        collapsingTolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingTolbarLayout.setTitle(name);
                    collapsingTolbarLayout.setBackgroundColor(getResources().getColor(R.color.ColorPrimary));
                    collapsingTolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.ColorPrimaryYellow));
                    header.setVisibility(View.INVISIBLE);
                    isShow = true;
                } else if(isShow) {
                    header.setVisibility(View.VISIBLE);
                    collapsingTolbarLayout.setTitle("");
                    collapsingTolbarLayout.setBackgroundColor(getResources().getColor(R.color.transparent));
                    isShow = false;
                }
            }
        });

        Picasso.with(getApplicationContext()).load(image)
                .fit()
                .centerCrop()
                .into(IVbankPicture);

        TVBankName.setText(name);
        TVBankDesc.setText(desc);

        feedItem = new ArrayList<>();
        feedAdapter = new BankDetilAdapter(this, feedItem);
        mRVbank.setLayoutManager(new LinearLayoutManager(this));
        mRVbank.setAdapter(feedAdapter);

        loadData();

    }

    public void loadData() {
        oops.setVisibility(View.GONE);
        mRVbank.setVisibility(View.GONE);
        timeOut.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        Retrofit retrofit = new Retrofit.Builder().baseUrl(getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        bankService = retrofit.create(BankService.class);
        token = loginPref.token().get();

        Log.e(TAG, "loadData: "+bankService );
        Log.e(TAG, "loadData: "+token);

        //Header
        Call<BankPage> bankPage = bankService.getBankID(token, Id);
        bankPage.enqueue(new Callback<BankPage>() {
            @Override
            public void onResponse(Response<BankPage> response, Retrofit retrofit) {
                    Log.e(TAG, "KEDUA" + response.code());
                    if (response.code() == 200) {
                        Picasso.with(getApplicationContext()).load(response.body().getUrl_bgimg())
                                .fit()
                                .centerCrop()
                                .into(IVheader);

                        phone = response.body().getCall_center();
                        web = response.body().getWebsite();
                    }
            }

            @Override
            public void onFailure(Throwable t) {
                progressBar.setVisibility(View.GONE);
                oops.setVisibility(View.GONE);
                mRVbank.setVisibility(View.GONE);
                timeOut.setVisibility(View.VISIBLE);
            }
        });

        Call<BankFeed> bankFeed = bankService.getBankFeed(token, Id, 1+"");
        bankFeed.enqueue(new Callback<BankFeed>() {
            @Override
            public void onResponse(Response<BankFeed> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    Log.e(TAG, "Feed" + response.code());
                    if (response.code() == 200) {
                        Log.e(TAG, "onResponse: "+response.body().getFeedItems().size());

                        if (response.body().getFeedItems().size() == 0){
                            oops.setVisibility(View.VISIBLE);
                            mRVbank.setVisibility(View.GONE);
                            timeOut.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);

                        }
                        else {
                            oops.setVisibility(View.GONE);
                            mRVbank.setVisibility(View.VISIBLE);
                            timeOut.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);

                            EventBus.getDefault().postSticky(new BankDetilEvent(response.body()));
                        }
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                progressBar.setVisibility(View.GONE);
                oops.setVisibility(View.GONE);
                mRVbank.setVisibility(View.GONE);
                timeOut.setVisibility(View.VISIBLE);
            }
        });

        mRVbank.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                View view = mRVbank.getChildAt(mRVbank.getChildCount() - 1);
                if (feedItem.size()!=0){
                    diff = (view.getBottom() - (mRVbank.getHeight() + mRVbank.getScrollY()));
                }
                Log.e("TAG", "diff: "+diff);

                if (diff == 0) {
                    if (!loading && lastPage < maxPage) {
                        loading = true;
                        //progressBarScroll.setVisibility(View.VISIBLE);
                            Log.e("TAG", "onScrollChanged: "+lastPage+1);
                            Call<BankFeed> bankFeedCall = bankService.getBankFeed(token, Id, lastPage+1+"");
                            bankFeedCall.enqueue(new Callback<BankFeed>() {
                                @Override
                                public void onResponse(Response<BankFeed> response, Retrofit retrofit) {
                                    loading = false;
                                        if (response.code() == 200) {
                                            //progressBarScroll.setVisibility(View.GONE);
                                            feedItem.addAll(response.body().getFeedItems());
                                            feedAdapter.notifyDataSetChanged();
                                            maxPage = response.body().getMaxPage();
                                            lastPage++;
                                    }
                                }
                                @Override
                                public void onFailure(Throwable t) {
                                    //progressBar.setVisibility(View.GONE);
                                    loading = false;
                                }
                            });
                    }
                }
            }
        });
    }

    public void mixpanel(){
        String projectToken = getString(R.string.mixpanel_token);
        MixpanelAPI mixpanel = MixpanelAPI.getInstance(this, projectToken);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTimeStamp = dateFormat.format(new Date());
        try {
            JSONObject props = new JSONObject();
            props.put("TimeStamp", currentTimeStamp);
            props.put("email", loginPref.email().get());
            //props.put("category", name);
            mixpanel.track("View Promos by category", props);
        } catch (JSONException e) {
            Log.e("MYAPP", "Unable to add properties to JSONObject", e);
        }
    }


    @Click(R.id.reload)
    public void reloadClick() {
        loadData();
    }

    @Click(R.id.BtnTel)
    public void BtnTelClick() {
        if (phone==null){
            Toast.makeText(getApplicationContext(), "There is no Phone Number", Toast.LENGTH_LONG).show();
        }
        else {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:"+phone));
            startActivity(callIntent);
        }
    }

    @Click(R.id.btel)
    public void btelClick() {
        if (phone==null){
            Toast.makeText(getApplicationContext(), "There is no Phone Number", Toast.LENGTH_LONG).show();
        }
        else {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:"+phone));
            startActivity(callIntent);
        }
    }

    @Click(R.id.BtnWeb)
    public void BtnWebClick() {
        if (web != null) {

            if (web!=null){
                mCustomTabActivityHelper.mayLaunchUrl(Uri.parse(web), null, null);
            }

            CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();
            int color = ContextCompat.getColor(ActivityBankDetail.this, R.color.ColorPrimary);
            intentBuilder.setToolbarColor(color);
            intentBuilder.setShowTitle(true);
            CustomTabActivityHelper.openCustomTab(ActivityBankDetail.this, intentBuilder.build(), Uri.parse(web), new WebViewFallBack());
/*
            String url = web;
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
            */
        }
    }

    @Click(R.id.bweb)
    public void bwebClick() {
        if (web != null) {

            if (web!=null){
                mCustomTabActivityHelper.mayLaunchUrl(Uri.parse(web), null, null);
            }

            CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();
            int color = ContextCompat.getColor(ActivityBankDetail.this, R.color.ColorPrimary);
            intentBuilder.setToolbarColor(color);
            intentBuilder.setShowTitle(true);
            CustomTabActivityHelper.openCustomTab(ActivityBankDetail.this, intentBuilder.build(), Uri.parse(web), new WebViewFallBack());
/*
            String url = web;
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
            */
        }
    }

    @Click(R.id.BtnShare)
    public void BtnShareClick() {
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Kartoo");
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Dapatkan promo menarik dari "+name+" - download Kartoo untuk promo dari bank lainnya!\nhttp://app.kartoo.co/");
        startActivity(Intent.createChooser(shareIntent, "Share via"));
    }
    @Click(R.id.bshare)
    public void bshareClick() {
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Kartoo");
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Dapatkan promo menarik dari "+name+" - download Kartoo untuk promo dari bank lainnya!\nhttp://app.kartoo.co/");
        startActivity(Intent.createChooser(shareIntent, "Share via"));
    }

    @Click(R.id.BtnLoc)
    public void BtnLocClick() {
        Intent intent = new Intent(this, ActivityLocator_.class);
        intent.putExtra("Id", Id);
        startActivity(intent);

    }
    @Click(R.id.bloc)
    public void blocClick() {
        Intent intent = new Intent(this, ActivityLocator_.class);
        intent.putExtra("Id", Id);
        startActivity(intent);

    }

    @Override
    public void onStart() {
        super.onStart();
        feedItem.clear();
        EventBus.getDefault().registerSticky(this);
        mCustomTabActivityHelper.bindCustomTabsService(this);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
        mCustomTabActivityHelper.unbindCustomTabsService(this);
    }

    public void onEvent(BankDetilEvent event) {
        Log.e("eventTrending", "caught");

        feedItem.clear();
        maxPage = event.getListPromo().getMaxPage();
        feedItem.addAll(event.getListPromo().getFeedItems());
        feedAdapter.notifyDataSetChanged();

    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
