package co.kartoo.app.cards.suggestioncard.applycard;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import co.kartoo.app.R;
import co.kartoo.app.category.CategoryEvent;
import co.kartoo.app.interest.InterestInterface;
import co.kartoo.app.models.ApplyCreditCardPref_;
import de.greenrobot.event.EventBus;
import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

@EActivity(R.layout.activity_apply_confirm_card)
public class ApplyConfirmCardActivity extends AppCompatActivity implements ApplyCardInterface {
    @ViewById
    Toolbar mToolbar;
    @ViewById
    TextView mTVtitle;
    @ViewById
    Button mBtnNext;
    @ViewById
    EditText mETaccount;
    @ViewById
    LinearLayout mLLaddCreditCard;
    @ViewById
    RecyclerView mRVcreditCard;
    @ViewById
    ImageButton mIBadd;

    @Pref
    ApplyCreditCardPref_ applyCreditCardPref;

    private AdapterCreditCard mAdapter;

    boolean keyDel = false;
    boolean keyDel1 = false;

    String cardName, mETaccountKey, bankName;

    SharedPreferences prefs;

    final Context c = this;

    private List<co.kartoo.app.rest.model.newest.CreditCard> cardList = new ArrayList<>();

    @AfterViews
    void init() {
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
        setUpNavDrawer();
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mTVtitle.setText(applyCreditCardPref.nameCardApplied().get());

        cardName = applyCreditCardPref.nameCardApplied().get();
        //bankName = applyCreditCardPref.n

        prefs = getSharedPreferences(cardName, 0);

        mRVcreditCard.setNestedScrollingEnabled(false);
        mAdapter = new AdapterCreditCard(this, cardList);
        mRVcreditCard.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRVcreditCard.setItemAnimator(new DefaultItemAnimator());
        mRVcreditCard.setAdapter(mAdapter);

        EventBus.getDefault().postSticky(new CreditCardEvent(cardList));

        mETaccount.setHint("Input your "+applyCreditCardPref.bankName().get()+" account number");

        if (prefs.getString("setCardListKey", null) != null) {

            mETaccount.setText(prefs.getString("mETaccountKey", ""));

            Gson gson = new Gson();
            Type type = new TypeToken<List<co.kartoo.app.rest.model.newest.CreditCard>>(){}.getType();
            String listCard = prefs.getString("setCardListKey", null);
            List<co.kartoo.app.rest.model.newest.CreditCard> cardList1 = new ArrayList<>();
            cardList1 = gson.fromJson(listCard, type);
            EventBus.getDefault().postSticky(new CreditCardEvent(cardList1));
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void userItemClick(int pos) {
        cardList.remove(pos);
        mAdapter.notifyDataSetChanged();
        Log.e("TAG", "userItemClick: "+pos);
    }

    @Click(R.id.mBtnNext)
    public void mBtnNextClick () {
        mETaccountKey = mETaccount.getText().toString();

        Log.e("TAG", "mBtnNextClick: "+cardList);
        sharedPreference();
        Intent intent = new Intent(this, ApplyScanningActivity_.class);
        startActivity(intent);
        finish();
    }

    @Click(R.id.mIBadd)
    public void mIBaddClick() {

        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(c);
        View mView = layoutInflaterAndroid.inflate(R.layout.activity_input_card_number, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(c);
        alertDialogBuilderUserInput.setView(mView);

        final EditText mETccType = (EditText) mView.findViewById(R.id.mETccType);
        final EditText mETccNumber = (EditText) mView.findViewById(R.id.mETccNumber);
        final EditText mETvalid = (EditText) mView.findViewById(R.id.mETvalid);
        final EditText mETcvv = (EditText) mView.findViewById(R.id.mETcvv);


        mETccType.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });


        //Card Number
        mETccNumber.addTextChangedListener(new TextWatcher() {
            int len=0;
            @Override
            public void afterTextChanged(Editable s) {
                String str = mETccNumber.getText().toString();
                if (keyDel){
                    if(str.length() == 4  || str.length() == 9 || str.length() == 14){
                        mETccNumber.setText(str.substring(0, str.length() - 1));
                        if (len>0){
                            mETccNumber.setSelection(len-1);
                        }
                    }
                }
                if(str.length() == 4  || str.length() == 9 || str.length() == 14 && len <str.length()
                        || mETccNumber.getSelectionEnd() ==4 || mETccNumber.getSelectionEnd()==9
                        || mETccNumber.getSelectionEnd() ==14){
                    if (!keyDel) {
                        String data = mETccNumber.getText().toString();
                        mETccNumber.setText(data + " ");
                        if (len<19){
                            mETccNumber.setSelection(len + 1);
                        }
                        else {
                            mETccNumber.setSelection(len);
                        }
                    }
                }
            }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                String str = mETccNumber.getText().toString();
                len = str.length();
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = mETccNumber.getText().toString();
                if (str.length()==19) {
                    mETvalid.requestFocus();
                }
            }
        });
        mETccNumber.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL){
                    keyDel = true;
                }else{
                    keyDel = false;
                }
                return false;
            }
        });

        //Valid Thru
        mETvalid.addTextChangedListener(new TextWatcher() {
            int len1=0;
            @Override
            public void afterTextChanged(Editable s) {
                String str = mETvalid.getText().toString();
                if (keyDel1){
                    if(str.length() == 2){
                        mETvalid.setText(str.substring(0, str.length() - 1));
                        if (len1>0){
                            mETvalid.setSelection(len1-1);
                        }
                    }
                }
                if(str.length() == 2 && len1 <str.length()){
                    if (!keyDel1) {
                        String data = mETvalid.getText().toString();
                        mETvalid.setText(data + "/");
                        if (len1<5){
                            mETvalid.setSelection(len1 + 1);
                        }
                        else {
                            mETvalid.setSelection(len1);
                        }
                    }
                }
            }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                String str = mETvalid.getText().toString();
                len1 = str.length();
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = mETvalid.getText().toString();
                if (str.length()==5) {
                    mETcvv.requestFocus();
                }
            }
        });
        mETvalid.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL){
                    keyDel1 = true;
                }else{
                    keyDel1 = false;
                }
                return false;
            }
        });

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        boolean isValid = true;
                        if (mETccType.getText().toString().length() == 0) {
                            mETccType.setError("Full Name is required!");
                            isValid = false;
                            return;

                        }
                        if (mETccNumber.getText().toString().length() == 0) {
                            mETccNumber.setError("Full Name is required!");
                            isValid = false;
                            return;
                        }
                        if (mETvalid.getText().toString().length() == 0) {
                            mETvalid.setError("Full Name is required!");
                            isValid = false;
                            return;
                        }
                        if (isValid){
                            Log.e("TAG", "onClick: "+mETccNumber.getText().toString());
                            String number = mETccNumber.getText().toString();
                            //String masking = maskString(number,0,12,'*');
                            co.kartoo.app.rest.model.newest.CreditCard creditCard = new co.kartoo.app.rest.model.newest.CreditCard(mETccType.getText().toString(), number, mETvalid.getText().toString());
                            cardList.add(creditCard);
                            mAdapter.notifyDataSetChanged();
                            Log.e("TAG", "onClick: "+cardList);

                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(mETvalid.getWindowToken(), 0);
                            dialogBox.dismiss();
                        }
                    }
                })

                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });
        final AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
    }

    public void sharedPreference(){
        SharedPreferences.Editor edit = prefs.edit();
        Gson gson = new Gson();
        String setCardList = gson.toJson(cardList);
        Log.e("TAG", "sharedPreference: "+setCardList);

        edit.putString("setCardListKey", setCardList);
        edit.putString("mETaccountKey", mETaccountKey);
        edit.apply();

    }

    @Override
    public void onStart() {
        super.onStart();
        cardList.clear();
        EventBus.getDefault().registerSticky(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    public void onBackPressed() {

        mETaccountKey = mETaccount.getText().toString();

        sharedPreference();

        Intent intent = new Intent(this, ApplyInformationActivity_.class);
        startActivity(intent);
        finish();
    }

    public void onEvent(CreditCardEvent event) {
        cardList.clear();
        cardList.addAll(event.getListCard());
        mAdapter.notifyDataSetChanged();
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
