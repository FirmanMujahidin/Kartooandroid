package co.kartoo.app.drawer;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.webkit.WebView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.io.InputStream;

import co.kartoo.app.R;

/**
 * Created by MartinOenang on 11/28/2015.
 */
@EFragment(R.layout.fragment_tnc)
public class FragmentPrivacy extends Fragment {
    @ViewById
    WebView mWVtncContent;
    String content;

    @AfterViews
    void init() {
        try {
            Resources res = getResources();
            InputStream in_s = res.openRawResource(R.raw.privacy);

            byte[] b = new byte[in_s.available()];
            in_s.read(b);
            content = new String(b);
        } catch (Exception e) {
            // e.printStackTrace();
            content = "";
        }
        mWVtncContent.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
    }

}
