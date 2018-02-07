package co.kartoo.app.drawer;

import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import co.kartoo.app.R;
import co.kartoo.app.models.LoginPref_;

/**
 * Created by MartinOenang on 10/11/2015.
 */
@EFragment(R.layout.fragment_settings)
public class FragmentSettings extends Fragment {
    @ViewById
    Switch mSnotif;
    @ViewById
    LinearLayout mLLnotif;
    @ViewById
    TextView textView24;
    @ViewById
    Button mBtnOk;


    @Pref
    LoginPref_ loginPref;
    @AfterViews
    void init() {
        mSnotif.setChecked(loginPref.isNotifOn().get());
    }
    @Click(R.id.mBtnOk)
    public void mBtnOkClick() {
        loginPref.isNotifOn().put(mSnotif.isChecked());
    }
}
