package co.kartoo.app.cards.suggestioncard.applycard;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import co.kartoo.app.EditProfile;
import co.kartoo.app.R;
import co.kartoo.app.models.ApplyCreditCardPref_;

@EActivity(R.layout.activity_apply_information)
public class ApplyInformationActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    @ViewById
    Toolbar mToolbar;
    @ViewById
    TextView mTVtitle;
    @ViewById
    Button mBtnNext;
    @ViewById
    EditText mETcompanyName, mETdepartment, mETposition
            ,mETlamaBekerja, mETofficeAddress, mETofficeNumber, mETprefferedTime;

    @ViewById
    AutoCompleteTextView mETjenisPekerjaan, mETindustri, mETstatusPekerjaan, mETincome;

    String mETcompanyNameKey, mETdepartmentKey, mETpositionKey
            ,mETlamaBekerjaKey, mETofficeAddressKey, mETofficeNumberKey, mETprefferedTimeKey,
            mETjenisPekerjaanKey, mETindustriKey, mETstatusPekerjaanKey, mETincomeKey, cardName;

    int hour, minute;

    SharedPreferences prefs;

    @Pref
    ApplyCreditCardPref_ applyCreditCardPref;

    @AfterViews
    void init() {
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
        setUpNavDrawer();
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mTVtitle.setText(applyCreditCardPref.nameCardApplied().get());

        cardName = applyCreditCardPref.nameCardApplied().get();

        prefs = getSharedPreferences(cardName, 0);

        if (prefs.getAll() != null || prefs.getAll().size()!=0) {
            mETcompanyName.setText(prefs.getString("mETcompanyNameKey", ""));
            mETdepartment.setText(prefs.getString("mETdepartmentKey", ""));
            mETposition.setText(prefs.getString("mETpositionKey", ""));
            mETlamaBekerja.setText(prefs.getString("mETlamaBekerjaKey", ""));
            mETofficeAddress.setText(prefs.getString("mETofficeAddressKey", ""));
            mETofficeNumber.setText(prefs.getString("mETofficeNumberKey", ""));
            mETprefferedTime.setText(prefs.getString("mETprefferedTimeKey", ""));
            mETjenisPekerjaan.setText(prefs.getString("mETjenisPekerjaanKey", ""));
            mETindustri.setText(prefs.getString("mETindustriKey", ""));
            mETstatusPekerjaan.setText(prefs.getString("mETstatusPekerjaanKey", ""));
            mETincome.setText(prefs.getString("mETincomeKey", ""));
        }
            //Jenis Pekerjaan
        String [] jenisPekerjaan = {"BUMN", "Negeri", "Swasta", "Professional", "Wiraswasta", "Houswife"};
        ArrayAdapter<String> adapterPekerjaan = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, jenisPekerjaan);
        mETjenisPekerjaan.setAdapter(adapterPekerjaan);
        mETjenisPekerjaan.setInputType(InputType.TYPE_NULL);
        mETjenisPekerjaan.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    mETjenisPekerjaan.showDropDown();
                    v.performClick();
                }
                return false;
            }
        });

        //Industry
        String [] industry = {""};
        ArrayAdapter<String> adapterIndustry = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, industry);
        mETindustri.setAdapter(adapterIndustry);
        mETindustri.setInputType(InputType.TYPE_NULL);
        mETindustri.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    mETindustri.showDropDown();
                    v.performClick();
                }
                return false;
            }
        });

        //Status Pekerjaan
        String [] statusPekerjaan = {"Permanen", "Kontrak", "Paruh waktu", "Lain-lain"};
        ArrayAdapter<String> adapterStatusPekerjaan = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, statusPekerjaan);
        mETstatusPekerjaan.setAdapter(adapterStatusPekerjaan);
        mETstatusPekerjaan.setInputType(InputType.TYPE_NULL);
        mETstatusPekerjaan.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    mETstatusPekerjaan.showDropDown();
                    v.performClick();
                }
                return false;
            }
        });
        //Income
        String [] income = {"<3.500.000","3.500.001 - 5.000.000", "5.000.001 - 10.000.000", "10.000.001 - 20.000.000", "20.000.001 - 50.000.000", ">50.000.001"};
        ArrayAdapter<String> adapterIncome = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, income);
        mETincome.setAdapter(adapterIncome);
        mETincome.setInputType(InputType.TYPE_NULL);
        mETincome.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    mETincome.showDropDown();
                    v.performClick();
                }
                return false;
            }
        });
    }

    @Click(R.id.mETprefferedTime)
    public void mETprefferedTimeClick () {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(ApplyInformationActivity.this, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getFragmentManager(), "Datepickerdialog");


    }

    public void timePicker(){
        final Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        Log.e("TAG", "timePicker: "+minute );

        final TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        mETprefferedTime.setText(date+" "+ String.format("%02d",hourOfDay)+":"+String.format("%02d",minute));
                    }
                }, hour, minute, false);
        timePickerDialog.show();
    }



    private static String formatDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(year, month, day);
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(date);
    }

    String date;
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        timePicker();
        date = formatDate(year,monthOfYear,dayOfMonth);
    }

    @Click(R.id.mBtnNext)
    public void mBtnNextClick () {

        mETcompanyNameKey = mETcompanyName.getText().toString();
        mETdepartmentKey = mETdepartment.getText().toString();
        mETpositionKey = mETposition.getText().toString();
        mETlamaBekerjaKey = mETlamaBekerja.getText().toString();
        mETofficeAddressKey = mETofficeAddress.getText().toString();
        mETofficeNumberKey = mETofficeNumber.getText().toString();
        mETprefferedTimeKey = mETprefferedTime.getText().toString();
        mETjenisPekerjaanKey = mETjenisPekerjaan.getText().toString();
        mETindustriKey = mETindustri.getText().toString();
        mETstatusPekerjaanKey = mETstatusPekerjaan.getText().toString();
        mETincomeKey = mETincome.getText().toString();

        sharedPreference();

        Intent intent = new Intent(this, ApplyConfirmCardActivity_.class);
        startActivity(intent);
        finish();
    }

    public void onBackPressed() {
        mETcompanyNameKey = mETcompanyName.getText().toString();
        mETdepartmentKey = mETdepartment.getText().toString();
        mETpositionKey = mETposition.getText().toString();
        mETlamaBekerjaKey = mETlamaBekerja.getText().toString();
        mETofficeAddressKey = mETofficeAddress.getText().toString();
        mETofficeNumberKey = mETofficeNumber.getText().toString();
        mETprefferedTimeKey = mETprefferedTime.getText().toString();
        mETjenisPekerjaanKey = mETjenisPekerjaan.getText().toString();
        mETindustriKey = mETindustri.getText().toString();
        mETstatusPekerjaanKey = mETstatusPekerjaan.getText().toString();
        mETincomeKey = mETincome.getText().toString();

        sharedPreference();

        Intent intent = new Intent(this, ApplyProfileActivity_.class);
        startActivity(intent);
        finish();
    }

        public void sharedPreference(){
        prefs = getSharedPreferences(cardName, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();

        edit.putString("mETcompanyNameKey", mETcompanyNameKey);
        edit.putString("mETdepartmentKey", mETdepartmentKey);
        edit.putString("mETpositionKey", mETpositionKey);
        edit.putString("mETlamaBekerjaKey", mETlamaBekerjaKey);
        edit.putString("mETofficeAddressKey", mETofficeAddressKey);
        edit.putString("mETofficeNumberKey", mETofficeNumberKey);
        edit.putString("mETprefferedTimeKey", mETprefferedTimeKey);
        edit.putString("mETjenisPekerjaanKey", mETjenisPekerjaanKey);
        edit.putString("mETindustriKey", mETindustriKey);
        edit.putString("mETstatusPekerjaanKey", mETstatusPekerjaanKey);
        edit.putString("mETincomeKey", mETincomeKey);

        edit.apply();

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
