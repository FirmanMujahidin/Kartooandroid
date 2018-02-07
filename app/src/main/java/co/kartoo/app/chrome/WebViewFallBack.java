package co.kartoo.app.chrome;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import co.kartoo.app.WebViewActivity;

/**
 * Created by Luthfi Apriyanto on 1/26/2017.
 */

public class WebViewFallBack implements CustomTabActivityHelper.CustomTabFallback {

    @Override
    public void openUri(Activity activity, Uri uri) {
        Intent intent = new Intent(activity, WebViewActivity.class);
        intent.putExtra(WebViewActivity.EXTRA_URL, uri.toString());
        activity.startActivity(intent);
    }
}