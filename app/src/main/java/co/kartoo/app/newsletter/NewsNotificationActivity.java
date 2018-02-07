package co.kartoo.app.newsletter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.ArrayList;
import java.util.List;

import co.kartoo.app.R;
import co.kartoo.app.bank.AdapterBankList;
import co.kartoo.app.bank.EventBankList;
import co.kartoo.app.models.LoginPref_;
import co.kartoo.app.rest.MainService;
import co.kartoo.app.rest.PromoService;
import co.kartoo.app.rest.model.newest.BankPage;
import co.kartoo.app.rest.model.newest.Newsletter;
import co.kartoo.app.views.SpaceItemDecoration;
import de.greenrobot.event.EventBus;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

@EActivity(R.layout.activity_news_notification)
public class NewsNotificationActivity extends AppCompatActivity {
    @Pref
    LoginPref_ loginPref;

    private Toolbar mToolbar;
    private String mNewsListJson;
    private List<Newsletter> mNewsList;
    private RelativeLayout mRLOops;
    private LinearLayout mLLNewsList;
    private RecyclerView mRvNewsList;
    private RecyclerView.Adapter mNewsAdapter;
    private RecyclerView.LayoutManager mNewsLayout;

    @AfterViews
    public void init() {
        mRLOops = (RelativeLayout) findViewById(R.id.mRLOops);
        mLLNewsList = (LinearLayout) findViewById(R.id.mLLNewsList);
        mToolbar = (Toolbar) findViewById(R.id.mToolbar);
        mRvNewsList = (RecyclerView) findViewById(R.id.mRvNewsList);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
        setUpNavDrawer();
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //pushNewsletter();

        SharedPreferences sharedPref = getSharedPreferences("MyNewsPref", Context.MODE_PRIVATE);
        mNewsListJson = sharedPref.getString("listofNewsJson", "");

        if (mNewsListJson != "") {
            mRLOops.setVisibility(View.GONE);
            mLLNewsList.setVisibility(View.VISIBLE);
            Gson gson = new Gson();
            mNewsList = new ArrayList<>();
            mNewsList = gson.fromJson(mNewsListJson, new TypeToken<List<Newsletter>>(){}.getType());

            mNewsLayout = new LinearLayoutManager(this);
            mRvNewsList.setLayoutManager(mNewsLayout);
            mNewsAdapter = new NewsAdapter(this, mNewsList);
            mRvNewsList.setAdapter(mNewsAdapter);
        } else {
            mRLOops.setVisibility(View.VISIBLE);
            mLLNewsList.setVisibility(View.GONE);
        }

    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_news_notification);
//        mToolbar = (Toolbar) findViewById(R.id.mToolbar);
//        mRvNewsList = (RecyclerView) findViewById(R.id.mRvNewsList);
//        if (mToolbar != null) {
//            setSupportActionBar(mToolbar);
//        }
//        setUpNavDrawer();
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//
//        pushNewsletter();
//
//        SharedPreferences sharedPref = getSharedPreferences("MyNewsPref", Context.MODE_PRIVATE);
//        mNewsListJson = sharedPref.getString("listofNewsJson", "");
//
//        Gson gson = new Gson();
//        mNewsList = gson.fromJson(mNewsListJson, new TypeToken<List<Newsletter>>(){}.getType());
//
//        mNewsLayout = new LinearLayoutManager(this);
//        mRvNewsList.setLayoutManager(mNewsLayout);
//        mNewsAdapter = new NewsAdapter(mNewsList);
//        mRvNewsList.setAdapter(mNewsAdapter);
//    }

    private void pushNewsletter() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
        final MainService mainService = retrofit.create(MainService.class);
        final String token = loginPref.token().get();
        Call<String> pushNewsletterCall = mainService.pushNewsletter(token);
        pushNewsletterCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                if (response.code() == 200) {
                    Log.e("PushNewsletter", "OK");
                }
            }

            @Override
            public void onFailure(Throwable t) {

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
}
