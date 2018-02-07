package co.kartoo.app.cards.suggestioncard.applycard;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import co.kartoo.app.R;
import co.kartoo.app.cards.MyCardsActivity_;
import co.kartoo.app.models.ApplyCreditCardPref_;
import co.kartoo.app.models.LoginPref_;
import co.kartoo.app.rest.CardService;
import co.kartoo.app.rest.model.ResponseDefault;
import co.kartoo.app.rest.model.newest.ProceedApply;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

@EActivity(R.layout.activity_apply_done)
public class ApplyDoneActivity extends AppCompatActivity {
    @ViewById
    Toolbar mToolbar;
    @ViewById
    TextView mTVtitle;
    @ViewById
    Button mBtnApply;

    @Pref
    ApplyCreditCardPref_ applyCreditCardPref;
    @Pref
    LoginPref_ loginPref;

    Retrofit retrofit;
    CardService service;
    String cardID, applicationID, authorization;

    ProgressDialog loadingDialog;
    SharedPreferences prefs;

    @AfterViews
    void init() {
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
        setUpNavDrawer();
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mTVtitle.setText(applyCreditCardPref.nameCardApplied().get());

        loadingDialog = new ProgressDialog(ApplyDoneActivity.this);
        loadingDialog.setMessage("Loading...");
        loadingDialog.setIndeterminate(true);
        loadingDialog.setCancelable(false);

        retrofit = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
        service = retrofit.create(CardService.class);
        authorization = loginPref.token().get();
    }

    @Click(R.id.mBtnApply)
    public void mBtnApplyClick () {

        loadingDialog.show();

        prefs = getSharedPreferences(applyCreditCardPref.nameCardApplied().get(), 0);

        String userID = loginPref.email().get();
        String cardID = applyCreditCardPref.cardID().get();
        String applicationID = applyCreditCardPref.applicationID().get();
        String currentStatus = "";
        String employment = ""; //gue gak ngerti
        String position = prefs.getString("mETpositionKey", "");
        String income = prefs.getString("mETincomeKey", "");
        String officeAddress = prefs.getString("mETofficeAddressKey", "");
        String officePhoneNumber = prefs.getString("mETofficeNumberKey","");
        String emergencyContactNumber = ""; //gue gak ngerti
        String preferredTimeOfCall = prefs.getString("mETprefferedTimeKey", "");
        String url_Img_KTP = "";
        String url_Img_NPW = "";
        String url_Img_IncomeStatement = "";
        String ownershipStatus = prefs.getString("mETownershipStatusKey","");
        String familyReferenceName = prefs.getString("mETfamilyReferenceKey","");
        String familyReferenceRelationship = "";
        String familyReferenceContactNumber = "";
        String billingAddress = prefs.getString("mETbillingKey","");
        String maritalStatus = prefs.getString("mETmaritalKey","");
        String jenisPekerjaan = prefs.getString("mETjenisPekerjaanKey","");
        String industry = prefs.getString("mETindustriKey","");
        String department = prefs.getString("mETdepartmentKey","");
        String statusPekerjaan = prefs.getString("mETstatusPekerjaanKey","");
        String lamaBekerja = prefs.getString("mETlamaBekerjaKey","");
        String companyName = prefs.getString("mETcompanyNameKey","");

        ProceedApply proceedApply = new ProceedApply(userID, cardID, applicationID, currentStatus, employment, position, income,
                officeAddress, officePhoneNumber, emergencyContactNumber, preferredTimeOfCall, url_Img_KTP, url_Img_NPW,
                url_Img_IncomeStatement, ownershipStatus, familyReferenceName, familyReferenceRelationship, familyReferenceContactNumber,
                billingAddress, maritalStatus, jenisPekerjaan, industry, department, statusPekerjaan, lamaBekerja, companyName);

        Call<ResponseDefault> doProceed = service.doProceed(loginPref.token().get(), applicationID, proceedApply);
        Log.e("TAG", "init: "+doProceed);
        doProceed.enqueue(new Callback<ResponseDefault>() {
            @Override
            public void onResponse(final Response<ResponseDefault> response, Retrofit retrofit) {
                Log.e("TAG", "responseCode: " + response.code());
                if (response.code() == 200) {
                    loadingDialog.dismiss();

                    Intent intent = new Intent(ApplyDoneActivity.this, MyCardsActivity_.class);
                    //intent.putExtra("from","apply");
                    startActivity(intent);
                    finish();
                }
                else {
                    loadingDialog.dismiss();
                    Toast.makeText(ApplyDoneActivity.this, "Error has occured, please try again", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Throwable t) {
                loadingDialog.dismiss();
                Toast.makeText(ApplyDoneActivity.this, "Error has occured, please try again", Toast.LENGTH_SHORT).show();
                Log.e("TAG", "onFailure: "+t);
            }
        });
        //Intent intent = new Intent(this, ApplyScanningActivity_.class);
        //startActivity(intent);
    }
    public void onBackPressed() {

        Intent intent = new Intent(this, ApplyScanningActivity_.class);
        startActivity(intent);
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
