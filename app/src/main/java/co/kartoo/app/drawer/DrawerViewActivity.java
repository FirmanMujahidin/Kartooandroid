package co.kartoo.app.drawer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;

import com.google.android.gms.common.api.GoogleApiClient;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import co.kartoo.app.EditProfile_;
import co.kartoo.app.R;
import co.kartoo.app.bank.ActivityBankList;
import co.kartoo.app.cards.FragmentCards_;
import co.kartoo.app.cards.MyCardsActivity_;
import co.kartoo.app.cards.suggestioncard.applycard.ApplyLandingActivity_;
import co.kartoo.app.models.LoginPref_;
import co.kartoo.app.newsletter.NewsNotificationActivity;
import co.kartoo.app.newsletter.NewsNotificationActivity_;
import co.kartoo.app.newsletter.ReadNotificationActivity;
import co.kartoo.app.newsletter.ReadNotificationActivity_;
import co.kartoo.app.promo.PromoLikedListFragment_;
@EActivity(R.layout.activity_drawer_view)
public class DrawerViewActivity extends AppCompatActivity {
    @ViewById
    FrameLayout mFLcontainer;
    @ViewById
    Toolbar mToolbar;
    @ViewById
    SearchView mIVsearch;
    @ViewById
    RelativeLayout mDLdrawer;
    @ViewById
    LinearLayout mLLsearch;
    @ViewById
    EditText mETsearch;
    @ViewById
    ImageView mIVsearchClear;
    @ViewById
    ImageView toolbar_title;


    @Pref
    LoginPref_ loginPref;

    GoogleApiClient mGoogleApiClient;

    @AfterViews
    public void init() {
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
        setUpNavDrawer();
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Bundle bundle = getIntent().getExtras();
        String selectedPage = bundle.getString("selectedPage");
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = null;
        mIVsearch.setVisibility(View.GONE);

        //if (selectedPage.equals("my_feed")) {
            //fragment = new FragmentFeed_();
            //setTitle("My Feed");

        //}
        if (selectedPage.equals("editprofile")) {
            if (loginPref.type().get().equals("guest")){
                fragment = new FragmentCards_();
                //Toast.makeText(getApplicationContext(), "You need to Login first", Toast.LENGTH_LONG).show();
                //dialog();
                getSupportFragmentManager().popBackStackImmediate();
                finish();
            }
            else {
                fragment = new FragmentCards_();
                getSupportFragmentManager().popBackStackImmediate();
                Intent intent2 = new Intent(getApplicationContext(), EditProfile_.class);
                intent2.putExtra("token", loginPref.token().get());
                intent2.putExtra("email",loginPref.email().get());

                if (loginPref.urlPhoto().get()!=null){
                    intent2.putExtra("image", loginPref.urlPhoto().get());
                }

                if (loginPref.name().get()!=null){
                    intent2.putExtra("name", loginPref.name().get());
                }
                startActivity(intent2);
                getSupportFragmentManager().popBackStackImmediate();
                finish();
            }
        }
        else  if (selectedPage.equals("my_cards")) {
            if (loginPref.type().get().equals("guest")){
                fragment = new FragmentCards_();
                getSupportFragmentManager().popBackStackImmediate();
                //Toast.makeText(getApplicationContext(), "You need to Login first", Toast.LENGTH_LONG).show();
                //dialog();
                getSupportFragmentManager().popBackStackImmediate();
                finish();
            }
            else {
                fragment = new FragmentCards_();

                getSupportFragmentManager().popBackStackImmediate();
                Intent intent2 = new Intent(getApplicationContext(), MyCardsActivity_.class);

                startActivity(intent2);
                getSupportFragmentManager().popBackStackImmediate();
                finish();

            }
            //setTitle("My Cards");
        }
        //else if (selectedPage.equals("search")) {
          //  fragment = new FragmentSearch_();
            //setTitle("Search");
        //}
        if (selectedPage.equals("likedPromo")) {
            if (loginPref.type().get().equals("guest")){
                fragment = new FragmentCards_();
                getSupportFragmentManager().popBackStackImmediate();
                //dialog();
                //Toast.makeText(getApplicationContext(), "You need to Login first", Toast.LENGTH_LONG).show();
                getSupportFragmentManager().popBackStackImmediate();
                finish();
            }
            else {
                fragment = new PromoLikedListFragment_();
            }
        } else if (selectedPage.equals("tnc")) {
            fragment = new FragmentTnC_();
        }

        else if (selectedPage.equals("privacy")) {
            fragment = new FragmentPrivacy_();
        }
        else if (selectedPage.equals("about")) {
            fragment = new FragmentCards_();

            getSupportFragmentManager().popBackStackImmediate();
            Intent intent2 = new Intent(getApplicationContext(), AboutUs.class);

            startActivity(intent2);
            getSupportFragmentManager().popBackStackImmediate();
            finish();
        }

        if (selectedPage.equals("bankPage")) {
            fragment = new FragmentCards_();

            getSupportFragmentManager().popBackStackImmediate();
            Intent intent2 = new Intent(getApplicationContext(), ActivityBankList.class);

            startActivity(intent2);
            getSupportFragmentManager().popBackStackImmediate();
            finish();
        }

        if (selectedPage.equals("notif")) {
            if (loginPref.type().get().equals("guest")) {
                fragment = new FragmentCards_();
                getSupportFragmentManager().popBackStackImmediate();
                //dialog();
                //Toast.makeText(getApplicationContext(), "You need to Login first", Toast.LENGTH_LONG).show();
                getSupportFragmentManager().popBackStackImmediate();
                finish();
            } else {
                fragment = new FragmentCards_();
                getSupportFragmentManager().popBackStackImmediate();
                Intent intent2 = new Intent(getApplicationContext(), NewsNotificationActivity_.class);
                startActivity(intent2);
                getSupportFragmentManager().popBackStackImmediate();
                finish();
            }
        }

        if (selectedPage.equals("creditCard")) {
            if (loginPref.type().get().equals("guest")) {
                fragment = new FragmentCards_();
                getSupportFragmentManager().popBackStackImmediate();
                //dialog();
                //Toast.makeText(getApplicationContext(), "You need to Login first", Toast.LENGTH_LONG).show();
                getSupportFragmentManager().popBackStackImmediate();
                finish();
            } else {
                fragment = new FragmentCards_();
                getSupportFragmentManager().popBackStackImmediate();
                Intent intent2 = new Intent(getApplicationContext(), ApplyLandingActivity_.class);
                startActivity(intent2);
                getSupportFragmentManager().popBackStackImmediate();
                finish();
            }
        }
        fragmentManager.beginTransaction().replace(R.id.mFLcontainer,fragment).commit();
    }

    private void setUpNavDrawer() {
        if (mToolbar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mToolbar.setNavigationIcon(R.drawable.ic_back_orange);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
                        getSupportFragmentManager().popBackStackImmediate();
                    } else {
                        finish();
                    }
                }
            });
        }
    }

    @Override
    public void onBackPressed() {

        /*
        Log.d("CDA", "onBackPressed Called");
        Intent intent = new Intent(this, MainActivity_.class);
        startActivity(intent);
*/

        if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
            getSupportFragmentManager().popBackStackImmediate();
        } else {
            super.onBackPressed();
        }

    }
}
