package co.kartoo.app.drawer;

import android.content.Intent;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import co.kartoo.app.PopupReviewActivity_;
import co.kartoo.app.R;
import co.kartoo.app.chrome.CustomTabActivityHelper;
import co.kartoo.app.chrome.WebViewFallBack;

public class AboutUs extends AppCompatActivity {

    Toolbar mToolbar;
    TextView btnFb, Tnc, Privacy, web, Feedback;

    private CustomTabActivityHelper mCustomTabActivityHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        btnFb = (TextView) findViewById(R.id.btnFb);
        Tnc = (TextView) findViewById(R.id.Tnc);
        Privacy = (TextView) findViewById(R.id.Privacy);
        web = (TextView) findViewById(R.id.web);
        Feedback = (TextView) findViewById(R.id.Feedback);

        mToolbar = (Toolbar) findViewById(R.id.mToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        setUpNavDrawer();

        mCustomTabActivityHelper = new CustomTabActivityHelper();

        btnFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.facebook.com/kartooapp";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://www.kartoo.co";

                if (url!=null){
                    mCustomTabActivityHelper.mayLaunchUrl(Uri.parse(url), null, null);
                }

                CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();
                int color = ContextCompat.getColor(AboutUs.this, R.color.ColorPrimary);
                intentBuilder.setToolbarColor(color);
                intentBuilder.setShowTitle(true);
                CustomTabActivityHelper.openCustomTab(AboutUs.this, intentBuilder.build(), Uri.parse(url), new WebViewFallBack());

                /*
                String url = "http://www.kartoo.co";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                */
            }
        });
        Tnc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutUs.this, DrawerViewActivity_.class);
                Bundle bundle = new Bundle();

                bundle.putString("selectedPage", "tnc");
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });
        Privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutUs.this, DrawerViewActivity_.class);
                Bundle bundle = new Bundle();

                bundle.putString("selectedPage", "privacy");
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });
        Feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutUs.this, PopupReviewActivity_.class);
                startActivity(intent);
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
        mCustomTabActivityHelper.bindCustomTabsService(this);
    }
    @Override
    public void onStop() {
        super.onStop();
        mCustomTabActivityHelper.unbindCustomTabsService(this);
    }
}
