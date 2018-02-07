package co.kartoo.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.squareup.picasso.Picasso;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.kartoo.app.cards.suggestioncard.applycard.PhoneVerificationActivity_;
import co.kartoo.app.forgotpassword.ChangeYourPassword_;
import co.kartoo.app.models.LoginPref_;
import co.kartoo.app.models.ProfilePref_;
import co.kartoo.app.rest.MainService;
import co.kartoo.app.rest.model.ResponseDefault;
import co.kartoo.app.rest.model.UpdateProfile;
import co.kartoo.app.rest.model.newest.Cities;
import co.kartoo.app.rest.model.newest.UserProfile;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

@EActivity (R.layout.activity_edit_profile)
public class EditProfile extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    public static final String storageConnectionString =
            "DefaultEndpointsProtocol=https;AccountName=kartoostorage;AccountKey=uKFP4jlPuDGlmyH7WoPSXoi9plqPej2/43HaPEjiS9K9QR2P5wohYDZaaiPa4CLZdInXDGPEzjTSlOlQDffGFw==";
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    @ViewById
    Toolbar mToolbar;
    @ViewById
    TextView datePicker, mTVtitle, mTVeditProfileEmail, mTVchangePassword, mTVinterest;
    @ViewById
    FrameLayout mFLprofilePict;
    @ViewById
    ImageView mIVeditProfileProfilePict;
    @ViewById
    EditText mETeditProfileFullname, mETeditProfileAdress, mETeditProfilePhonenumber;
    //@ViewById
    //AutoCompleteTextView mETeditProfileCity;
    @ViewById
    ProgressBar progressBar;
    @ViewById
    Button mBtnSave, mBtnVerification;
    @ViewById
    ImageButton mBtnEdit;

    //@ViewById
    //ProgressDialog loadingDialog;
    @ViewById
    EditText editBrthday;
    @ViewById
    Button datePickerBrthday;
    @ViewById
    RadioButton male, female;
    @Pref
    ProfilePref_ profilePref;
    AutoCompleteTextView mETeditProfileCity;
    @Pref
    LoginPref_ loginPref;
    String Gender, mETphoneNumberKey, phoneNumber;
    //Button mBtnSave;
    ProgressDialog loadingDialog;
    SharedPreferences prefs;
    Uri selectedImage;
    //Bitmap bitMap;
    String token, fileName;
    String name;
    String image;
    String email;
    String isBitmap;
    String[] cities;
    String mday = "02";
    String mmonth="07";
    String myear="2013";
    String originalPhoneNumber;
    boolean checked;
    //RadioButton male, female;
    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    String exception;

    private FirebaseAnalytics mFirebaseAnalytics;

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

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        name = intent.getStringExtra("name");
        image = intent.getStringExtra("image");
        email = intent.getStringExtra("email");
        //isBitmap = intent.getStringExtra("isBitmap");

     /*   if (profilePref.isVerified().get()) {
            mBtnVerification.setBackgroundColor(getResources().getColor(R.color.green));
            mBtnVerification.setText("Verified");
            mBtnVerification.setEnabled(false);
        }*/

        loadingDialog = new ProgressDialog(EditProfile.this);
        loadingDialog.setMessage("Loading...");
        loadingDialog.setIndeterminate(true);
        loadingDialog.setCancelable(false);
        mETeditProfileCity = (AutoCompleteTextView) findViewById(R.id.mETeditProfileCity);

        mTVeditProfileEmail.setText(email);

        String str = loginPref.email().get();
        fileName = str.replaceAll("[@.]", "");

        mTVtitle.setText("Edit Profile");

        if (!name.equals("")){
            mETeditProfileFullname.setText(name);
        }
        Log.e("TAG", "ProfilePict: "+ loginPref.urlPhoto().get());
        String imgUrl = loginPref.urlPhoto().get();
        if(imgUrl !=null  && !imgUrl.equals("")){
            Picasso.with(this).load(imgUrl).into(mIVeditProfileProfilePict);
        }

        if (!image.equals("")) {
                Picasso.with(this).load(image).into(mIVeditProfileProfilePict);
        }

        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            mToolbar.setCollapsible(true);
        }

        if (profilePref.dob().get() != null || !profilePref.dob().get().equals("")) {
            datePicker.setText(profilePref.dob().get());
            editBrthday.setText(profilePref.dob().get());
        }
        if (profilePref.city().get() != null || !profilePref.city().get().equals("")) {
            mETeditProfileCity.setText(profilePref.city().get());
        }
        if (profilePref.address().get() != null || !profilePref.address().get().equals("")) {
            mETeditProfileAdress.setText(profilePref.address().get());
        }
        if (profilePref.phone().get() != null || !profilePref.phone().get().equals("")) {
            mETeditProfilePhonenumber.setText(profilePref.phone().get());

            if (profilePref.phone().get() != null) {
                if (!profilePref.phone().get().equals("")) {
                    mETeditProfilePhonenumber.setText(profilePref.phone().get());
                }
            }
        }
        if ((profilePref.phone().get() != null || !profilePref.phone().get().equals("")) && profilePref.isVerified().get()) {
            mBtnVerification.setBackgroundColor(getResources().getColor(R.color.green));
            mBtnVerification.setText("Verified");
            setMargins(mBtnVerification, 0, 0, 150, 0);
            mBtnVerification.setEnabled(false);
            mBtnEdit.setVisibility(View.VISIBLE);

            mETeditProfilePhonenumber.setEnabled(false);
        } else {
            mBtnVerification.setBackgroundColor(getResources().getColor(R.color.ColorPrimaryYellow));
            mBtnVerification.setText("Verify");
            mBtnVerification.setEnabled(true);
            setMargins(mBtnVerification, 0, 0, 10, 0);
            mETeditProfilePhonenumber.setEnabled(true);
        }

        if (profilePref.gender().get() != null || !profilePref.gender().get().equals("")) {
                if (profilePref.gender().get().equals("Male")){
                    male.setChecked(true);
                }else if (profilePref.gender().get().equals("Female")){
                    female.setChecked(true);
                }
        } else {
            mETeditProfilePhonenumber.setText(prefs.getString("mETphoneNumberKey", ""));
            phoneNumber = mETeditProfilePhonenumber.getText().toString();
            originalPhoneNumber = mETeditProfilePhonenumber.getText().toString();

            SharedPreferences phonenumberPref = getSharedPreferences("phonenumberPref", Context.MODE_PRIVATE);
            SharedPreferences.Editor phonenumberPrefEditor = phonenumberPref.edit();
            phonenumberPrefEditor.putString("originalPhoneNumber", originalPhoneNumber);
            phonenumberPrefEditor.commit();
        }

        originalPhoneNumber = mETeditProfilePhonenumber.getText().toString();

        SharedPreferences phonenumberPref = getSharedPreferences("phonenumberPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor phonenumberPrefEditor = phonenumberPref.edit();
        phonenumberPrefEditor.putString("originalPhoneNumber", originalPhoneNumber);
        phonenumberPrefEditor.commit();

        //Change Phone Number to Verification
//        mETeditProfilePhonenumber.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                mBtnVerification.setBackgroundColor(getResources().getColor(R.color.ColorPrimaryYellow));
//                mBtnVerification.setText("Verify");
//                mBtnVerification.setEnabled(true);
//                profilePref.isVerified().put(false);
//            }
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//            @Override
//            public void afterTextChanged(Editable s) {
//                Log.e("TAG", "afterTextChanged: "+phoneNumber);
//                if (phoneNumber.equals(mETeditProfilePhonenumber.getText().toString())){
//                    mBtnVerification.setBackgroundColor(getResources().getColor(R.color.green));
//                    mBtnVerification.setText("Verified");
//                    mBtnVerification.setEnabled(false);
//                    profilePref.isVerified().put(true);
//                }
//            }
//        });

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://kartoo-dev.azure-mobile.net/").addConverterFactory(GsonConverterFactory.create()).build();
        final MainService service = retrofit.create(MainService.class);

        Call<Cities> getCities = service.getCities(token);
        getCities.enqueue(new Callback<Cities>() {
            @Override
            public void onResponse(Response<Cities> response, Retrofit retrofit) {
                Log.e("TAG", "onResponse: " + response.code());
                if (response.code() == 200) {
                    Log.e("TAG", "editProfile: "+response.body());
                    int a = response.body().getCities().size();
                    cities = new String[a];

                    for (int i = 0; i < a; i++) {
                        cities[i] = response.body().getCities().get(i).getName();
                    }
                    AutoCompleteCity();
                }
            }
            @Override
            public void onFailure(Throwable t) {
                if (loadingDialog.isShowing())
                    loadingDialog.dismiss();
            }
        });

        Call<UserProfile> getProfile = service.getUserProfile(token);
        getProfile.enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Response<UserProfile> response, Retrofit retrofit) {
                if (response.code() == 200) {
                    progressBar.setVisibility(View.GONE);
                    mETeditProfileFullname.setText(response.body().getName());
                    mETeditProfileAdress.setText(response.body().getAddress());
                    mETeditProfilePhonenumber.setText(response.body().getPhonenumber());
                    datePicker.setText(response.body().getDateofbirth());
                    editBrthday.setText(response.body().getDateofbirth());
                    mTVeditProfileEmail.setText(response.body().getEmail());
                    mETeditProfileCity.setText(response.body().getCity());
                    profilePref.isVerified().put(Boolean.parseBoolean(response.body().getIsVerified()));

                    if ((profilePref.phone().get() != null || !profilePref.phone().get().equals("")) && profilePref.isVerified().get()) {
                        mBtnVerification.setBackgroundColor(getResources().getColor(R.color.green));
                        mBtnVerification.setText("Verified");
                        setMargins(mBtnVerification, 0, 0, 150, 0);
                        mETeditProfilePhonenumber.setEnabled(false);
                        mBtnVerification.setEnabled(false);
                        mBtnEdit.setVisibility(View.VISIBLE);
                    } else {
                        mBtnVerification.setBackgroundColor(getResources().getColor(R.color.ColorPrimaryYellow));
                        mBtnVerification.setText("Verify");
                        mBtnEdit.setVisibility(View.GONE);
                        setMargins(mBtnVerification, 0, 0, 10, 0);
                        mETeditProfilePhonenumber.setEnabled(true);
                        mBtnVerification.setEnabled(true);
                    }

                    Log.e("TAG", "gender: "+response.body().getGender() );

                    if (response.body().getGender()!=null){
                        if (response.body().getGender().equals("Male")){
                            male.setChecked(true);
                        }else if (response.body().getGender().equals("Female")){
                            female.setChecked(true);
                        }
                    }
                }
            }
            @Override
            public void onFailure(Throwable t) {
                if (loadingDialog.isShowing())
                    loadingDialog.dismiss();
            }
        });

        editBrthday.addTextChangedListener(new TextWatcher() {

            private String current = "";
            private String ddmmyyyy = "DDMMYYYY";
            private Calendar cal = Calendar.getInstance();

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {



                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]", "");
                    String cleanC = current.replaceAll("[^\\d.]", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8){
                        clean = clean + ddmmyyyy.substring(clean.length());
                    }else{
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int day  = Integer.parseInt(clean.substring(0,2));
                        int mon  = Integer.parseInt(clean.substring(2,4));
                        int year = Integer.parseInt(clean.substring(4,8));

                        if(mon > 12) mon = 12;
                        cal.set(Calendar.MONTH, mon-1);
                        year = (year<1900)?1900:(year>2100)?2100:year;
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE):day;
                        clean = String.format("%02d%02d%02d",day, mon, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    editBrthday.setText(current);
                    editBrthday.setSelection(sel < current.length() ? sel : current.length());
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        setUpNavDrawer();
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    private void setMargins (View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }

    @Click(R.id.mBtnVerification)
    public void mBtnVerification() {
        if (mETeditProfilePhonenumber.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "Please input number", Toast.LENGTH_LONG).show();

        } else {
            callingNetwork();
            //updateProfile();
        }
        /*
        1. profilepref.isverified -> true:
            alert -> Your number has been verified. Do you want to change your number?
                yes -> isverified dijadiin false
                        mETeditProfilePhonenumber -> enabled
                        buttonverify -> mBtnVerification.setBackgroundColor(getResources().getColor(R.color.ColorPrimaryYellow));
                                        mBtnVerification.setText("Verify");
                                        mBtnVerification.setEnabled(true);
                no -> ga terjadi apa2
        2. profilepref.isverified -> false:
            sesuai yang di bawah
                            mETphoneNumberKey = mETeditProfilePhonenumber.getText().toString();
                            mETeditProfilePhonenumber.setEnabled(false);
        */

    }

    public void sharedPreference() {
        prefs = getSharedPreferences(phoneNumber, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString("mETphoneNumberKey", mETphoneNumberKey);
        edit.apply();
    }

    @Click(R.id.mBtnEdit)
    public void mBtnEditClick() {
        profilePref.isVerified();
        if (mETeditProfilePhonenumber.isEnabled()) {
            callingNetwork();
            sharedPreference();
            checkVerification();
            updateProfile();
        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EditProfile.this);
            alertDialogBuilder.setTitle("Your number has been verified.");
            alertDialogBuilder.setMessage("Do you want to change your number ?");
            alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mBtnVerification.setBackgroundColor(getResources().getColor(R.color.ColorPrimaryYellow));
                    mBtnVerification.setText("Verify");
                    mETeditProfilePhonenumber.setEnabled(true);
                    mBtnVerification.setEnabled(true);
                    mBtnEdit.setVisibility(View.GONE);
                    setMargins(mBtnVerification, 0, 0, 10, 0);
                    profilePref.isVerified().put(false);

                    final SharedPreferences verifyCounterPref = getSharedPreferences("verifyCounterPref", Context.MODE_PRIVATE);
                    final SharedPreferences.Editor verifyEditCounter = verifyCounterPref.edit();

                    verifyEditCounter.putInt("counterKey", 0);
                    verifyEditCounter.apply();

                }
            });

            alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //mETphoneNumberKey = mETeditProfilePhonenumber.getText().toString();
                    //mETeditProfilePhonenumber.setEnabled(false);
                   /* sharedPreference();
                    checkVerification();*/

                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    @Click(R.id.mIVeditProfileProfilePict)
    public void mIVeditProfileProfilePictClick() {
        check();
    }

//    @Click(R.id.datePicker)
//    public void datePickerClick () {
//
//        Calendar now = Calendar.getInstance();
//        now.set(1990,1,1);
//        DatePickerDialog dpd = DatePickerDialog.newInstance(EditProfile.this, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)
//        );
//        dpd.show(getFragmentManager(), "Datepickerdialog");
//    }

    @Click(R.id.mBtnSave)
    public void mBtnSaveClick () {
//        Set<String> strings = new HashSet<>();
//        strings.add("D");
//        strings.add("M");
//        strings.add("Y");
//
//        String getBirthDay = editBrthday.getText().toString();

        String[] matches = new String[] {"D", "M", "Y"};

        for (String s : matches)
        {
            if (editBrthday.getText().toString().contains(s))
            {
                Toast.makeText(getApplicationContext(), "Date not valid", Toast.LENGTH_LONG).show();
                break;
            }else{

                updateProfile();
                firebase();
                if (!loadingDialog.isShowing())
                    loadingDialog.show();
            }
        }
    }

    @Click(R.id.mTVinterest)
    public void mTVinterestClick () {
        Intent intent = new Intent(EditProfile.this, InterestActivity_.class);
        intent.putExtra("from", "EditProfile");
        startActivity(intent);
    }

    @Click(R.id.mTVchangePassword)
    public void mTVchangePasswordClick(){
        if(loginPref.type().get().equals("email")){
            Intent intent = new Intent(EditProfile.this, ChangeYourPassword_.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(getApplicationContext(), "You are not logged in using an email address", Toast.LENGTH_LONG).show();
        }
    }

    @Click(R.id.datePickerBrthday)
    public void datePickerClick () {
//      date of birth di form kartu kredit bisa gak cuma diketik doang (dd/mm/yyyy) dan ga pakai date picker?
        Calendar now = Calendar.getInstance();

        Log.e("datepicker click", editBrthday.getText().toString());

        if (editBrthday.getText().toString().equals("DD/MM/YYYY")){
            now.set(1990,11,1);
        }
        else {
            String year = editBrthday.getText().toString().substring(6,10);
            String month = editBrthday.getText().toString().substring(3,5);
            String day = editBrthday.getText().toString().substring(0,2);
            Log.e("TAG", "datePickerClick: " + day);
            Log.e("TAG", "datePickerClick: " + month);
            Log.e("TAG", "datePickerClick: " + year);

            now.set(Integer.valueOf(year),Integer.valueOf(month)-1,Integer.valueOf(day));

        }
        DatePickerDialog dpd = DatePickerDialog.newInstance(EditProfile.this, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
        dpd.show(getFragmentManager(), "Datepickerdialog");

    }

    public void AutoCompleteCity(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cities);
        mETeditProfileCity.setAdapter(adapter);
    }



    public void updateProfile() {

        final String fullname = mETeditProfileFullname.getText().toString();
        String gender = Gender;

//      String dateofbirth = datePicker.getText().toString();
        String dateofbirth = editBrthday.getText().toString();

        String city = mETeditProfileCity.getText().toString();
        String address = mETeditProfileAdress.getText().toString();
        String phonenumber = mETeditProfilePhonenumber.getText().toString();

        Log.e("TAG", "isina: "+fullname );
        Log.e("TAG", "isina: "+Gender );
        Log.e("TAG", "isina: "+dateofbirth );
        Log.e("TAG", "isina: "+city );
        Log.e("TAG", "isina: "+address );
        Log.e("TAG", "isina: "+phonenumber );
        Log.e("TAG", "token: "+token);

        profilePref.name().put(fullname);
        profilePref.gender().put(Gender);
        profilePref.dob().put(dateofbirth);
        profilePref.city().put(city);
        profilePref.address().put(address);
        profilePref.phone().put(phonenumber);
        boolean isVerified = profilePref.isVerified().get();

        SharedPreferences phonenumberPref = getSharedPreferences("phonenumberPref", Context.MODE_PRIVATE);
        originalPhoneNumber = phonenumberPref.getString("originalPhoneNumber","");
        Log.e("TAG", "originalPhoneNumber: "+originalPhoneNumber);
        if (!originalPhoneNumber.equals(phonenumber)){
            isVerified = false;
        }

        profilePref.isVerified().put(isVerified);

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://kartoo-dev.azure-mobile.net/").addConverterFactory(GsonConverterFactory.create()).build();
        final MainService service = retrofit.create(MainService.class);
        UpdateProfile credentials = new UpdateProfile("","","","",fullname,"","", address,"","", phonenumber,Boolean.toString(isVerified), dateofbirth, Gender, "", city);

        Log.e("TAG", "isina: "+credentials.toString());

        Call<ResponseDefault> updateProfile = service.doUpdateProfile(token, credentials);
        updateProfile.enqueue(new Callback<ResponseDefault>() {
            @Override
            public void onResponse(Response<ResponseDefault> response, Retrofit retrofit) {
                if (loadingDialog.isShowing())
                    loadingDialog.dismiss();
                Log.e("TAG", "onResponse: " + response.code());
                if (response.code() == 200) {
                    Intent intent = new Intent(EditProfile.this, MainActivity_.class);
                    intent.putExtra("FromEditProfile", "true");
                    intent.putExtra("name", fullname);
                    //intent.putExtra("BitmapImage", bitMap);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
                if (response.code() == 401) {
                    Toast.makeText(getApplicationContext(), "Failed to edit Your Profile", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Throwable t) {
                if (loadingDialog.isShowing())
                    loadingDialog.dismiss();
                Log.e("TAG", "onFailure: "+"fail" );

                Intent intent = new Intent(EditProfile.this, MainActivity_.class);

                intent.putExtra("FromEditProfile", "true");
                intent.putExtra("name", fullname);
                //intent.putExtra("BitmapImage", bitMap);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    public void onRadioButtonClicked(View view) {
        checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.male:
                if (checked) {
                    Gender = "Male";
                }
                break;

            case R.id.female:
                if (checked) {
                    Gender = "Female";
                }
                break;
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library"};

        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfile.this);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent imageIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                    File imagesFolder = new File(Environment.getExternalStorageDirectory(), "KartooProfilePict");
                    imagesFolder.mkdirs();

                    File image = new File(imagesFolder, fileName + ".jpg");
                    selectedImage = Uri.fromFile(image);

                    imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, selectedImage);
                    startActivityForResult(imageIntent, REQUEST_CAMERA);

                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE){
                onSelectFromGalleryResult(data);
            }
            else if (requestCode == REQUEST_CAMERA){

                File imagesFolder = new File(Environment.getExternalStorageDirectory(), "KartooProfilePict");
                imagesFolder.mkdirs();
                File image = new File(imagesFolder, fileName + ".jpg");

                //Get Thumbnail Image
                final int THUMBSIZE = 512;
                Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(image.getAbsolutePath()),
                        THUMBSIZE, THUMBSIZE);

                Bitmap mBitmap = BitmapFactory.decodeFile(image.getAbsolutePath());
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                mBitmap.compress(Bitmap.CompressFormat.JPEG, 10, bytes);

                FileOutputStream fo;
                try {
                    image.createNewFile();
                    fo = new FileOutputStream(image);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                new BlobUpload().execute();
                mIVeditProfileProfilePict.setBackgroundDrawable(null);
                mIVeditProfileProfilePict.setBackgroundResource(0);
                mIVeditProfileProfilePict.setImageBitmap(null);
                mIVeditProfileProfilePict.setImageBitmap(ThumbImage);
                mIVeditProfileProfilePict.getRotation();

                Log.e("TAG", "selectedImageBackground: "+selectedImage );
            }
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        selectedImage = data.getData();

        String[] projection = { MediaStore.MediaColumns.DATA };
        Cursor cursor = managedQuery(selectedImage, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();

        String selectedImagePath = cursor.getString(column_index);

        //Get Thumbnail Image
        final int THUMBSIZE = 512;
        Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(selectedImagePath),
                THUMBSIZE, THUMBSIZE);

        Bitmap bm;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(selectedImagePath, options);
        final int REQUIRED_SIZE = 2000;
        int scale = 1;
        while (options.outWidth / scale / 2 >= REQUIRED_SIZE && options.outHeight / scale / 2 >= REQUIRED_SIZE) scale *= 2;
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;

        bm = BitmapFactory.decodeFile(selectedImagePath, options);

        File imagesFolder = new File(Environment.getExternalStorageDirectory(), "KartooProfilePict");
        imagesFolder.mkdirs();
        File image = new File(imagesFolder, fileName + ".jpg");

        Log.e("TAG", "onActivityResult: "+image.getAbsolutePath());

        Bitmap mBitmap = bm;
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 10, bytes);

        FileOutputStream fo;
        try {
            image.createNewFile();
            fo = new FileOutputStream(image);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        new BlobUpload().execute();

        mIVeditProfileProfilePict.setBackgroundDrawable(null);
        mIVeditProfileProfilePict.setBackgroundResource(0);
        mIVeditProfileProfilePict.setBackgroundColor(0);
        mIVeditProfileProfilePict.setImageBitmap(ThumbImage);
    }

    private void checkVerification() {
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissionsNeeded = new ArrayList<String>();
            final List<String> permissionsList = new ArrayList<String>();
            if (!addPermission(permissionsList, android.Manifest.permission.RECEIVE_SMS))
                permissionsNeeded.add("Receive SMS");
            if (!addPermission(permissionsList, android.Manifest.permission.READ_SMS))
                permissionsNeeded.add("Read SMS");


            if (permissionsList.size() > 0) {
                if (permissionsNeeded.size() > 0) {
                    // Need Rationale
                    String message = "You need to grant access to " + permissionsNeeded.get(0);
                    for (int i = 1; i < permissionsNeeded.size(); i++)
                        message = message + ", " + permissionsNeeded.get(i);
                    showMessageOKCancel(message,
                            new DialogInterface.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.M)
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
            //callingNetwork();

        } else {
            callingNetwork();

        }

    }

    public void callingNetwork() {
        loadingDialog.show();

        String authorization = loginPref.token().get();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
        MainService mainService = retrofit.create(MainService.class);


        Call<ResponseDefault> doChallenge = mainService.doChallenge(loginPref.token().get(), mETeditProfilePhonenumber.getText().toString());
        doChallenge.enqueue(new Callback<ResponseDefault>() {
            @Override
            public void onResponse(Response<ResponseDefault> response, Retrofit retrofit) {
                if (response.code() == 200) {
                    if (loadingDialog.isShowing()) {
                        loadingDialog.dismiss();
                    }
                    String verificationID = response.body().getMessage();
                    Log.e("TAG", "onResponseVerificationID: " + verificationID);

                    profilePref.phone().put(mETeditProfilePhonenumber.getText().toString());
                    Intent intent = new Intent(EditProfile.this, PhoneVerificationActivity_.class);
                    intent.putExtra("verificationID", verificationID);
                    intent.putExtra("phoneNumber", mETeditProfilePhonenumber.getText().toString());
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(EditProfile.this, "Error has occurred, please try again", Toast.LENGTH_LONG).show();
                if (loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }
            }
        });
    }


    private void check() {
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissionsNeeded = new ArrayList<String>();
            final List<String> permissionsList = new ArrayList<String>();
            if (!addPermission(permissionsList, android.Manifest.permission.CAMERA))
                permissionsNeeded.add("Camera");
            if (!addPermission(permissionsList, android.Manifest.permission.WRITE_EXTERNAL_STORAGE))
                permissionsNeeded.add("Write Storage");
            if (!addPermission(permissionsList, android.Manifest.permission.READ_EXTERNAL_STORAGE))
                permissionsNeeded.add("Read Storage");

            if (permissionsList.size() > 0) {
                if (permissionsNeeded.size() > 0) {
                    // Need Rationale
                    String message = "You need to grant access to " + permissionsNeeded.get(0);
                    for (int i = 1; i < permissionsNeeded.size(); i++)
                        message = message + ", " + permissionsNeeded.get(i);
                    showMessageOKCancel(message,
                            new DialogInterface.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.M)
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
            selectImage();

        }else{
            selectImage();

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
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
        new android.support.v7.app.AlertDialog.Builder(EditProfile.this)
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
                perms.put(android.Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);

                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && perms.get(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    selectImage();

                    // All Permissions Granted
                } else {
                    // Permission Denied
                    Toast.makeText(EditProfile.this, "Some Permission is Denied", Toast.LENGTH_SHORT).show();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        //date of birth di form kartu kredit bisa gak cuma diketik doang (dd/mm/yyyy) dan ga pakai date picker?
        String date = ""+dayOfMonth+"/"+(monthOfYear)+"/"+year;
        datePicker.setText(formatDate(year,monthOfYear,dayOfMonth));
        editBrthday.setText(formatDate(year,monthOfYear,dayOfMonth));
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

    public void firebase(){
        Bundle params = new Bundle();
        params.putString("name", "");
        //params.putString("gender", Gender);
        mFirebaseAnalytics.logEvent("edit_profile", params);
    }

    private class BlobUpload extends AsyncTask<Void, String, String> {
        @Override
        protected String doInBackground(Void... params) {
            try {
                CloudStorageAccount account = CloudStorageAccount.parse(storageConnectionString);
                CloudBlobClient serviceClient = account.createCloudBlobClient();

                // Container name must be lower case.
                CloudBlobContainer container = serviceClient.getContainerReference("profilepict");
                container.createIfNotExists();

                // Upload an image file.
                CloudBlockBlob blob = container.getBlockBlobReference(fileName + ".jpg");

                File imagesFolder = new File(Environment.getExternalStorageDirectory(), "KartooProfilePict");
                File sourceFile = new File(imagesFolder, fileName + ".jpg");

                blob.getProperties().setContentType("image/jpeg");
                blob.upload(new FileInputStream(sourceFile), sourceFile.length());
                String uri = blob.getUri().toString();
                //Log.e("TAG", "URI uri: "+uri);
                if (uri != null) {
                    loginPref.urlPhoto().put(uri);
                }
            } catch (Exception e) {
                exception = e.toString();
                Log.e("TAG", "UPLOADBLOB: " + e);
            }
            return "resp";
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), "Upload successful", Toast.LENGTH_LONG).show();
            Log.e("TAG", "onPostExecute: " + exception);
        }

        @Override
        protected void onPreExecute() {
            exception = null;
        }

        @Override
        protected void onProgressUpdate(String... progress) {
        }
    }
}