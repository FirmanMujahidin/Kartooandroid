package co.kartoo.app;

import android.content.Intent;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import co.kartoo.app.cards.suggestioncard.applycard.ApplyDoneActivity;

@EActivity(R.layout.activity_web_view)
public class WebViewActivity extends AppCompatActivity {

    @ViewById
    WebView webview;

    String url;

    public static final String EXTRA_URL =
            "co.kartoo.app.EXTRA_URL";

    @AfterViews
    void init() {

        url = "";
        Intent intent = getIntent();
        url = intent.getStringExtra("url");

        if (!url.equals("")) {

            webview.setWebViewClient(new WebViewClient());
            WebSettings webSettings = webview.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webview.loadUrl(url);
            setupActionBar(url);
        }
        else {
            Toast.makeText(WebViewActivity.this, "Error has occured, please try again", Toast.LENGTH_SHORT).show();
        }

    }

    private void setupActionBar(String url) {
        setTitle(url);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
    }

}
