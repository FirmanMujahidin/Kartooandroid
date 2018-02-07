package co.kartoo.app.newsletter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import co.kartoo.app.R;
import co.kartoo.app.models.LoginPref_;
import co.kartoo.app.rest.MainService;
import co.kartoo.app.rest.model.newest.Newsletter;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

@EActivity(R.layout.activity_read_notification)
public class ReadNotificationActivity extends AppCompatActivity {
    @ViewById
    Toolbar mToolbar;
    @ViewById
    ImageView mIVNewsImage;
    @ViewById
    WebView mWvNews;
    @Pref
    LoginPref_ loginPref;
    private String mUrlImg;
    private String mKeyword;
    private String mNewsListJson;
    private List<Newsletter> mNewsList;

    @AfterViews
    public void init() {
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
        setUpNavDrawer();
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Intent i = getIntent();
        mKeyword = i.getStringExtra("id");

        ViewGroup.MarginLayoutParams WvLayout = (ViewGroup.MarginLayoutParams) mWvNews.getLayoutParams();
        WvLayout.leftMargin = 0;
        WvLayout.rightMargin = 0;
        mWvNews.setLayoutParams(WvLayout);

        loadData();
    }

    private boolean isExistInList(List<Newsletter> mNewsList, Newsletter mNews) {
        for(Newsletter news : mNewsList) {
            if (news.getId().equals(mNews.getId())) {
                return true;
            }
        }
        return false;
    }

    public void loadData() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
        final MainService mainService = retrofit.create(MainService.class);
        final String token = loginPref.token().get();
        String id = mKeyword;
        Call<Newsletter> newsletterCall = mainService.getNewsletter(token, id);
        newsletterCall.enqueue(new Callback<Newsletter>() {
            @Override
            public void onResponse(Response<Newsletter> response, Retrofit retrofit) {
                if (response.code() == 200) {
                    mUrlImg = response.body().getUrl_img();
                    Picasso.with(ReadNotificationActivity.this)
                            .load(response.body().getUrl_img())
                            .fit()
                            .centerCrop()
                            .placeholder(R.color.placeholder)
                            .into(mIVNewsImage);
                    //Picasso.with(ReadNotificationActivity.this).load(response.body().getUrl_img()).resize(6000, 2000).onlyScaleDown().into(mIVNewsImage);
                    String header = "<h3>" + response.body().getHeader() + "</h3>";
                    String body = "<div style=\"font-size:18px\">" + header + response.body().getBody() + "</div>";

                    mWvNews.loadDataWithBaseURL(null, body, "text/html", "utf-8", null);
                    Newsletter newsletter = new Newsletter(response.body().getId(), response.body().getUrl_img(), response.body().getHeader(), response.body().getBody(), response.body().getTimeSent());
                    saveData(newsletter);
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
        mIVNewsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

                builder.setPositiveButton("Get Pro", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).setNegativeButton("No thanks", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                final AlertDialog dialog = builder.create();
                LayoutInflater inflater = getLayoutInflater();
                View dialogLayout = inflater.inflate(R.layout.news_dialog_layout, null);
                dialog.setView(dialogLayout);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                dialog.show();
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface d) {
                        ImageView image = (ImageView) dialog.findViewById(R.id.news_dialog_image);
                        Bitmap icon = BitmapFactory.decodeResource(ReadNotificationActivity.this.getResources(),
                                R.drawable.ic_online);
                        float imageWidthInPX = (float)image.getWidth();

                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Math.round(imageWidthInPX),
                                Math.round(imageWidthInPX * (float)icon.getHeight() / (float)icon.getWidth()));
                        image.setLayoutParams(layoutParams);
                        Picasso.with(ReadNotificationActivity.this)
                                .load(mUrlImg)
                                .fit()
                                .centerCrop()
                                .placeholder(R.color.placeholder)
                                .into(image);
                    }
                });
                */
                View view = View.inflate(ReadNotificationActivity.this, R.layout.news_dialog_layout, null);
                ImageView imgRefInflated = (ImageView) view.findViewById(R.id.news_dialog_image);
                Picasso.with(ReadNotificationActivity.this)
                        .load(mUrlImg)
                        .fit()
                        .centerCrop()
                        .into(imgRefInflated);

                AlertDialog.Builder builder = new AlertDialog.Builder(ReadNotificationActivity.this);

//                builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });

                AlertDialog dialog = builder.create();
                dialog.setView(view);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.show();
            }
        });

    }

    public void saveData(Newsletter newsletter) {
        SharedPreferences sharedPref = getSharedPreferences("MyNewsPref", Context.MODE_PRIVATE);
        mNewsListJson = sharedPref.getString("listofNewsJson", "");

        Gson gson = new Gson();
        mNewsList = new ArrayList<>();
        if (mNewsListJson != "") {

            mNewsList = gson.fromJson(mNewsListJson, new TypeToken<List<Newsletter>>(){}.getType());
        }

        if (!this.isExistInList(mNewsList, newsletter)) {
            mNewsList.add(newsletter);
            mNewsListJson = new String();
            mNewsListJson = gson.toJson(mNewsList);
            SharedPreferences.Editor sharedPrefEditor = sharedPref.edit();
            sharedPrefEditor.putString("listofNewsJson", mNewsListJson);
            sharedPrefEditor.apply();
        }
    }
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_read_notification);
//        mToolbar = (Toolbar) findViewById(R.id.mToolbar);
//        if (mToolbar != null) {
//            setSupportActionBar(mToolbar);
//        }
//        setUpNavDrawer();
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//
//        SharedPreferences loginPref = getSharedPreferences("LoginPref", Context.MODE_PRIVATE);
//        final String token = loginPref.getString("token", "");
//
//        Intent i = getIntent();
//        String keyword = i.getStringExtra("id");
//
//        Retrofit retrofit = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
//        final MainService promoService = retrofit.create(MainService.class);
//
//    }

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
