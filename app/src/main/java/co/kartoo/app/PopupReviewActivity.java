package co.kartoo.app;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.ArrayList;

import co.kartoo.app.models.LoginPref_;
import co.kartoo.app.rest.MainService;
import co.kartoo.app.rest.model.ResponseDefault;
import co.kartoo.app.rest.model.newest.PopupReviewObject;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

@EActivity(R.layout.activity_popup_review)
public class PopupReviewActivity extends AppCompatActivity {
    @ViewById
    Toolbar mToolbar;
    @ViewById
    TextView mTVtitle;
    @ViewById
    RelativeLayout mRLselect1, mRLselect2, mRLselect3, mRLselect4, mRLselect5;
    @ViewById
    Button mBtnError;
    @ViewById
    CheckBox checkbox1, checkbox2, checkbox3, checkbox4, checkbox5;
    @ViewById
    EditText fill_text;
    @ViewById
    ProgressBar progressBars;

    @Pref
    LoginPref_ pref;

    String token, idPromotion, errorString;

    ArrayList error;

    @AfterViews
    void init() {
        progressBars.setVisibility(View.GONE);
        mBtnError.setVisibility(View.VISIBLE);

        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
        setUpNavDrawer();
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        token = pref.token().get();

        error = new ArrayList();
    }

    @Click(R.id.mBtnError)
    public void mBtnErrorClick () {
        progressBars.setVisibility(View.VISIBLE);
        mBtnError.setVisibility(View.GONE);

        String fillText = fill_text.getText().toString();

        errorString = " ";

        if (checkbox1.isChecked()){
            error.add("Title");
        }
        if (checkbox2.isChecked()){
            error.add("Valid date");
        }
        if (checkbox3.isChecked()){
            error.add("Terms and condition");
        }
        if (checkbox4.isChecked()){
            error.add("Cards");
        }
        if (checkbox5.isChecked()){
            error.add("Location");
        }

        if (error.size()!=0){
            for (int i = 0; i < error.size(); i++) {
                if (errorString.length()==1){
                    errorString = error.get(0).toString();
                }
                else {
                    errorString = errorString.replaceAll(errorString, errorString+", "+error.get(i).toString());
                }
            }
        }

        PopupReviewObject object = new PopupReviewObject(pref.email().get(), fillText, errorString, "Android");
        Log.e("TAG", "mBtnErrorClick: "+pref.email().get()+" "+errorString+" "+fillText);

        Retrofit retrofit = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
        MainService mainService = retrofit.create(MainService.class);

        Call<ResponseDefault> doFeedback = mainService.doFeedback(token, object);
        doFeedback.enqueue(new Callback<ResponseDefault>() {
            @Override
            public void onResponse(Response<ResponseDefault> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    Log.e("TAG", "Interest" + response.code());
                    if (response.code() == 201) {
                        Toast.makeText(PopupReviewActivity.this, "Thanks for your feedback", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(PopupReviewActivity.this, "Something went wrong, please try again later", Toast.LENGTH_LONG).show();
                progressBars.setVisibility(View.GONE);
                mBtnError.setVisibility(View.VISIBLE);
            }
        });
    }

    @Click(R.id.mRLselect1)
    public void mRLselect1Click () {
        if (checkbox1.isChecked()){
            error.remove("Title");
            checkbox1.setChecked(false);
        }else {
            error.add("Title");
            checkbox1.setChecked(true);
        }
    }
    @Click(R.id.mRLselect2)
    public void mRLselect2Click () {
        if (checkbox2.isChecked()){
            //error.remove("Valid date");
            checkbox2.setChecked(false);
        }else {
            //error.add("Valid date");
            checkbox2.setChecked(true);
        }
    }
    @Click(R.id.mRLselect3)
    public void mRLselect3Click () {
        if (checkbox3.isChecked()){
            //error.remove("Terms and condition");
            checkbox3.setChecked(false);
        }else {
            //error.add("Terms and condition");
            checkbox3.setChecked(true);
        }
    }
    @Click(R.id.mRLselect4)
    public void mRLselect4Click () {
        if (checkbox4.isChecked()){
           // error.remove("Cards");
            checkbox4.setChecked(false);
        }else {
            //error.add("Cards");
            checkbox4.setChecked(true);
        }    }
    @Click(R.id.mRLselect5)
    public void mRLselectClick () {
        if (checkbox5.isChecked()){
            //error.remove("Location");
            checkbox5.setChecked(false);
        }else {
            //error.add("Location");
            checkbox5.setChecked(true);
        }
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
