package co.kartoo.app.landing;

import android.support.v4.app.Fragment;
import android.widget.ImageView;
import android.widget.TextView;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import co.kartoo.app.R;

/**
 * Created by teresa on 9/24/2015.
 */
@EFragment(R.layout.fragment_landing_1)
public class Landing1Fragment extends Fragment {
    @ViewById
    ImageView imageView;
    @ViewById
    TextView textView2;
}
