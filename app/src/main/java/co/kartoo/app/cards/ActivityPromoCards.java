package co.kartoo.app.cards;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import co.kartoo.app.R;

/**
 * Created by MartinOenang on 11/6/2015.
 */

@EActivity(R.layout.activity_check_in)
public class ActivityPromoCards extends AppCompatActivity{
    @ViewById
    Toolbar mToolbar;
    @ViewById
    LinearLayout mLLsearch;
    @ViewById
    ImageView mIVsearchClear;
    @ViewById
    ImageView mIVok;
    @ViewById
    FrameLayout mFLcontainer;

    static TextView mTVtitle;


    @AfterViews
    void init() {
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
        setUpNavDrawer();
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mTVtitle = (TextView) findViewById(R.id.mTVtitle);
        mTVtitle.setText("Applicable Cards");
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = new FragmentCards_();
        fragment.setArguments(getIntent().getExtras());
        fragmentManager.beginTransaction().replace(R.id.mFLcontainer,fragment).commit();
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
