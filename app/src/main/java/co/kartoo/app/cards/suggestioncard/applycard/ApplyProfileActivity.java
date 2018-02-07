package co.kartoo.app.cards.suggestioncard.applycard;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.kartoo.app.R;
import co.kartoo.app.ToStringConverterFactory;
import co.kartoo.app.models.ApplyCreditCardPref_;
import co.kartoo.app.models.LoginPref_;
import co.kartoo.app.models.ProfilePref_;
import co.kartoo.app.receiver.SmsReceiver;
import co.kartoo.app.rest.MainService;
import co.kartoo.app.rest.model.ResponseDefault;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

@EActivity(R.layout.activity_apply_profile)
public class ApplyProfileActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    @ViewById
    Toolbar mToolbar;
    @ViewById
    TextView mTVtitle;
    @ViewById
    EditText mETname, mETphoneNumber, mETemail, mETbirth, mETage, mETaddress, mETfamilyReference,
            mETbilling ;
    @ViewById
    AutoCompleteTextView mETownershipStatus, mETgender, mETmarital, mETeducation;
    @ViewById
    TextInputLayout inputOwnStatus;
    @ViewById
    Button mBtnNext, mBtnVerification;
    @Pref
    ProfilePref_ profilePref;
    @Pref
    LoginPref_ loginPref;
    @Pref
    ApplyCreditCardPref_ applyCreditCardPref;
    String age, defDate, defMonth, defYear, verify
            , mETnameKey, mETphoneNumberKey, mETemailKey, mETbirthKey, mETageKey, mETaddressKey, mETfamilyReferenceKey,
            mETbillingKey, mETownershipStatusKey, mETgenderKey, mETmaritalKey, mETeducationKey, cardName, phoneNumber ;
    int indexGender;
    ProgressDialog loadingDialog;
    SharedPreferences prefs;
    SmsReceiver smsReceiver;

    private static String formatDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(year, month, day);
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(date);
    }

    @AfterViews
    void init() {
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
        setUpNavDrawer();
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mTVtitle.setText(applyCreditCardPref.nameCardApplied().get());

        //applyCreditCardPref.isVerified().put(false);

        if(applyCreditCardPref.isVerified().get()){
            mBtnVerification.setBackgroundColor(getResources().getColor(R.color.green));
            mBtnVerification.setText("Verified");
            mBtnVerification.setEnabled(false);
        }

        loadingDialog = new ProgressDialog(ApplyProfileActivity.this);
        loadingDialog.setMessage("Loading...");
        loadingDialog.setIndeterminate(true);
        loadingDialog.setCancelable(false);

        mETbirth.setFocusable(false);
        mETbirth.setClickable(true);

        Log.e("TAG", "nameCardApplied: "+applyCreditCardPref.nameCardApplied().get());
        cardName = applyCreditCardPref.nameCardApplied().get();

        //Buat Apply yg disimpen
        //if (nama kartu yang disimpern != kosong){
        prefs = getSharedPreferences(cardName, 0);
        //}

        Log.e("TAG", "ApplyProfile: "+profilePref.name().get());
        Log.e("TAG", "sharedNameCard: "+prefs.getAll());

        if (prefs.getAll() == null || prefs.getAll().size()==0){
            if (loginPref.name().get() != null)
                if (!loginPref.name().get().equals("")) {
                    mETname.setText(loginPref.name().get());
                }

            if (profilePref.phone().get() != null) {
                if (!profilePref.phone().get().equals("")) {
                    mETphoneNumber.setText(profilePref.phone().get());
                }
            }
            if (loginPref.email().get() != null){
                if (!loginPref.email().get().equals("")) {
                    mETemail.setText(loginPref.email().get());
                }
            }
            if (profilePref.dob().get() != null) {
                if (!profilePref.dob().get().equals("")) {
                    mETbirth.setText(profilePref.dob().get());

                    Log.e("TAG", "DOB: "+profilePref.dob().get() );
                    String dateOfBirth = profilePref.dob().get();

                    if(dateOfBirth.length()<9){
                        defDate = dateOfBirth.substring(0,2);
                        defMonth = dateOfBirth.substring(3,5);
                        defYear = dateOfBirth.substring(6,10);

                        Log.e("TAG", "date: "+defDate);
                        Log.e("TAG", "month: "+defMonth);
                        Log.e("TAG", "year: "+defYear);
                    }
                    mETage.setText(String.valueOf(getAge(Integer.parseInt(defYear), Integer.parseInt(defMonth), Integer.parseInt(defDate))));
                }
            }

            if (profilePref.gender().get() != null) {
                if (!profilePref.gender().get().equals("")) {

                    if (profilePref.gender().get().equals("Male")) {
                        indexGender = 1;
                    } else if (profilePref.gender().get().equals("Female")) {
                        indexGender = 2;
                    }
                    // mETgender.setText(profilePref.gender().get());
                }
                phoneNumber = mETphoneNumber.getText().toString();
            }


        }
        else {
            mETname.setText(prefs.getString("mETnameKey", ""));
            mETphoneNumber.setText(prefs.getString("mETphoneNumberKey", ""));
            mETemail.setText(prefs.getString("mETemailKey", ""));
            mETbirth.setText(prefs.getString("mETbirthKey", ""));
            mETage.setText(prefs.getString("mETageKey", ""));
            mETaddress.setText(prefs.getString("mETaddressKey", ""));
            mETfamilyReference.setText(prefs.getString("mETfamilyReferenceKey", ""));
            mETbilling.setText(prefs.getString("mETbillingKey", ""));
            mETownershipStatus.setText(prefs.getString("mETownershipStatusKey", ""));
            mETgender.setText(prefs.getString("mETgenderKey", ""));
            mETmarital.setText(prefs.getString("mETmaritalKey", ""));
            mETeducation.setText(prefs.getString("mETeducationKey", ""));

            phoneNumber = mETphoneNumber.getText().toString();

            String dateOfBirth = prefs.getString("mETbirthKey", "");
            if (dateOfBirth.length()>9){
                defDate = dateOfBirth.substring(0,2);
                defMonth = dateOfBirth.substring(3,5);
                defYear = dateOfBirth.substring(6,10);
            }
        }

        //Change Phone Number to Verification
        mETphoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mBtnVerification.setBackgroundColor(getResources().getColor(R.color.ColorPrimaryYellow));
                mBtnVerification.setText("Verify");
                mBtnVerification.setEnabled(true);
                applyCreditCardPref.isVerified().put(false);
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (phoneNumber.equals(mETphoneNumber.getText().toString())){
                    mBtnVerification.setBackgroundColor(getResources().getColor(R.color.green));
                    mBtnVerification.setText("Verified");
                    mBtnVerification.setEnabled(false);
                    applyCreditCardPref.isVerified().put(true);
                }
            }
        });

        //Ownership Status
        String [] ownershipStatus = {"Company Own", "Rent", "Credit", "Private Own", "Family Own", "Kost"};
        ArrayAdapter<String> adapterOwn = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ownershipStatus);
        mETownershipStatus.setAdapter(adapterOwn);
        mETownershipStatus.setInputType(InputType.TYPE_NULL);
        mETownershipStatus.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    mETownershipStatus.showDropDown();
                    v.performClick();
                }
                return false;
            }
        });

        //Gender
        String [] gender = {"Male", "Female"};
        mETgender.setText(gender[indexGender]);

        ArrayAdapter<String> adapterGender = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, gender);
        mETgender.setAdapter(adapterGender);
        mETgender.setInputType(InputType.TYPE_NULL);
        mETgender.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    mETgender.showDropDown();
                    v.performClick();
                }
                return false;
            }
        });

        //Marital Status
        String [] maritalStatus = {"Single", "Married", "Divorce", "Widow/er"};
        ArrayAdapter<String> adapterMarital = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, maritalStatus);
        mETmarital.setAdapter(adapterMarital);
        mETmarital.setInputType(InputType.TYPE_NULL);
        mETmarital.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    mETmarital.showDropDown();
                    v.performClick();
                }
                return false;
            }
        });

        //Marital Status
        String [] education = {"S1", "S2", "S3", "SMA", "SMP", "SD", "D3"};
        ArrayAdapter<String> adapterEducation = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, education);
        mETeducation.setAdapter(adapterEducation);
        mETeducation.setInputType(InputType.TYPE_NULL);
        mETeducation.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    mETeducation.showDropDown();
                    v.performClick();
                }
                return false;
            }
        });
    }

    public int getAge (int _year, int _month, int _day) {
        Calendar cal = Calendar.getInstance();
        int y, m, d, a;
        y = cal.get(Calendar.YEAR);
        m = cal.get(Calendar.MONTH);
        d = cal.get(Calendar.DAY_OF_MONTH);
        cal.set(_year, _month, _day);
        a = y - cal.get(Calendar.YEAR);

        if ((m < cal.get(Calendar.MONTH))
                || ((m == cal.get(Calendar.MONTH)) && (d < cal
                .get(Calendar.DAY_OF_MONTH)))) {
            --a;
        }
        if(a < 0)
            throw new IllegalArgumentException("Age < 0");
        return a;
    }

    @Click(R.id.mETbirth)
    public void mETbirthClick () {
        Calendar now = Calendar.getInstance();

        if(defDate!=null){
            if(!defDate.equals("")){
                now.set(Integer.parseInt(defYear),Integer.parseInt(defMonth)-1,Integer.parseInt(defDate));
            }
        }

        DatePickerDialog dpd = DatePickerDialog.newInstance(ApplyProfileActivity.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }

    @Click(R.id.mBtnNext)
    public void mBtnNextClick () {
        mETnameKey = mETname.getText().toString();
        mETphoneNumberKey = mETphoneNumber.getText().toString();
        mETemailKey = mETemail.getText().toString();
        mETbirthKey = mETbirth.getText().toString();
        mETageKey = mETage.getText().toString();
        mETaddressKey = mETaddress.getText().toString();
        mETfamilyReferenceKey = mETfamilyReference.getText().toString();
        mETbillingKey = mETbilling.getText().toString();
        mETownershipStatusKey = mETownershipStatus.getText().toString();
        mETgenderKey = mETgender.getText().toString();
        mETmaritalKey = mETmarital.getText().toString();
        mETeducationKey = mETeducation.getText().toString();

        sharedPreference();

        Intent intent = new Intent(this, ApplyInformationActivity_.class);
        startActivity(intent);
        finish();
    }

    @Click(R.id.mBtnVerification)
    public void mBtnVerificationClick () {
        mETphoneNumberKey = mETphoneNumber.getText().toString();
        sharedPreference();
        check();
    }

    public void sharedPreference(){
        prefs = getSharedPreferences(cardName, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();

        edit.putString("mETnameKey", mETnameKey);
        edit.putString("mETphoneNumberKey", mETphoneNumberKey);
        edit.putString("mETemailKey", mETemailKey);
        edit.putString("mETbirthKey", mETbirthKey);
        edit.putString("mETageKey", mETageKey);
        edit.putString("mETaddressKey", mETaddressKey);
        edit.putString("mETfamilyReferenceKey", mETfamilyReferenceKey);
        edit.putString("mETbillingKey", mETbillingKey);
        edit.putString("mETownershipStatusKey", mETownershipStatusKey);
        edit.putString("mETgenderKey", mETgenderKey);
        edit.putString("mETmaritalKey", mETmaritalKey);
        edit.putString("mETeducationKey", mETeducationKey);

        edit.apply();

    }

    public void callingNetwork(){
        loadingDialog.show();

        //String authorization = loginPref.token().get();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).addConverterFactory(new ToStringConverterFactory()).build();
        MainService mainService = retrofit.create(MainService.class);

        Call<ResponseDefault> doChallenge = mainService.doChallenge(loginPref.token().get(), mETphoneNumber.getText().toString());
        doChallenge.enqueue(new Callback<ResponseDefault>() {
            @Override
            public void onResponse(Response<ResponseDefault> response, Retrofit retrofit) {
                if (response.code() == 200) {
                    if (loadingDialog.isShowing()){
                        loadingDialog.dismiss();
                    }
                    String verificationID = response.body().getMessage();
                    Log.e("TAG", "onResponseVerificationID: "+verificationID );

                    Intent intent = new Intent(ApplyProfileActivity.this, PhoneVerificationActivity_.class);
                    intent.putExtra("verificationID", verificationID);
                    startActivity(intent);
                    finish();
                }
            }
            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(ApplyProfileActivity.this,"Error has occurred, please try again",Toast.LENGTH_LONG).show();
                if (loadingDialog.isShowing()){
                    loadingDialog.dismiss();
                }
            }
        });
    }

    private void check() {
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissionsNeeded = new ArrayList<String>();
            final List<String> permissionsList = new ArrayList<String>();
            if (!addPermission(permissionsList, Manifest.permission.RECEIVE_SMS))
                permissionsNeeded.add("Receive SMS");
            if (!addPermission(permissionsList, Manifest.permission.READ_SMS))
                permissionsNeeded.add("Read SMS");


            if (permissionsList.size() > 0) {
                if (permissionsNeeded.size() > 0) {
                    // Need Rationale
                    String message = "You need to grant access to " + permissionsNeeded.get(0);
                    for (int i = 1; i < permissionsNeeded.size(); i++)
                        message = message + ", " + permissionsNeeded.get(i);
                    showMessageOKCancel(message,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                            REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                                }
                            });
                    return;
                }
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                return;
            }
            callingNetwork();

        }else{
            callingNetwork();

        }

    }

    private boolean addPermission(List<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!shouldShowRequestPermissionRationale(permission))
                return false;
        }
        return true;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new android.support.v7.app.AlertDialog.Builder(ApplyProfileActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
            {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.RECEIVE_SMS, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_SMS, PackageManager.PERMISSION_GRANTED);

                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
                    callingNetwork();

                    // All Permissions Granted
                } else {
                    // Permission Denied
                    Toast.makeText(ApplyProfileActivity.this, "Some Permission is Denied", Toast.LENGTH_SHORT).show();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void onBackPressed(){
        mETnameKey = mETname.getText().toString();
        mETphoneNumberKey = mETphoneNumber.getText().toString();
        mETemailKey = mETemail.getText().toString();
        mETbirthKey = mETbirth.getText().toString();
        mETageKey = mETage.getText().toString();
        mETaddressKey = mETaddress.getText().toString();
        mETfamilyReferenceKey = mETfamilyReference.getText().toString();
        mETbillingKey = mETbilling.getText().toString();
        mETownershipStatusKey = mETownershipStatus.getText().toString();
        mETgenderKey = mETgender.getText().toString();
        mETmaritalKey = mETmarital.getText().toString();
        mETeducationKey = mETeducation.getText().toString();

        sharedPreference();

        Intent intent = new Intent(this, ApplyLandingActivity_.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        age = String.valueOf(getAge(year,monthOfYear,dayOfMonth));

        mETbirth.setText(formatDate(year,monthOfYear,dayOfMonth));

        defYear = String.valueOf(year);
        defMonth = String.valueOf(monthOfYear+1);
        defDate = String.valueOf(dayOfMonth);

        mETage.setText(String.valueOf(getAge(year,monthOfYear,dayOfMonth)));
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
