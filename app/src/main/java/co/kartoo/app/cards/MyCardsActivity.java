package co.kartoo.app.cards;

import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import co.kartoo.app.R;


@EActivity(R.layout.activity_my_cards)
public class MyCardsActivity extends AppCompatActivity {

    @ViewById
    Toolbar mToolbar;
    @ViewById
    TextView mTVtitle;
    @ViewById(R.id.mVPmain)
    ViewPager mVPmain;
    @ViewById
    TabLayout mTLtab;

    TabAdapterCards tabAdapter;

    @AfterViews
    void init() {
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
        setUpNavDrawer();
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.mTLtab);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // ini untuk Fragment card dan suggestion card
//        tabLayout.addTab(tabLayout.newTab().setText("Personal Cards"));
//        tabLayout.addTab(tabLayout.newTab().setText("Suggested Cards"));


//        Intent intent = getIntent();
//        String from = intent.getStringExtra("from");

        /***
         * Belum pasti
         */
        /*
        if (from!=null){
            mVPmain.setCurrentItem(1);
            tabAdapter.notifyDataSetChanged();
        }
        */

        final ViewPager viewPager = (ViewPager) findViewById(R.id.mVPmain);
        final PagerAdapter adapter = new TabAdapterCards(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setVisibility(View.GONE);

        // ini untuk Fragment card dan suggestion card
//        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                viewPager.setCurrentItem(tab.getPosition());
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
//
    }

    public void onBackPressed() {
        finish();
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
