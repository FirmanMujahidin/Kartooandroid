package co.kartoo.app.cards.suggestioncard.applycard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import co.kartoo.app.R;

@EActivity(R.layout.activity_input_card_number)
public class InputCardNumberActivity extends AppCompatActivity {
    @ViewById
    Toolbar mToolbar;
    @ViewById
    TextView mTVtitle;
    @ViewById
    EditText mETccType, mETccNumber, mETvalid, mETcvv;
    @ViewById
    Button mBtnAdd;

    //int multiplier = 4;

    String cardName, cardNumber, cardValid, cardCvv;
    boolean keyDel = false;
    boolean keyDel1 = false;


    @AfterViews
    void init() {
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
        setUpNavDrawer();
        getSupportActionBar().setDisplayShowTitleEnabled(false);

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
    }

    @Click(R.id.mBtnAdd)
    public void mBtnAddClick () {
        Intent intent = new Intent(this, ApplyConfirmCardActivity_.class);
        intent.putExtra("cardName",mETccType.getText().toString());
        intent.putExtra("cardNumber",mETccNumber.getText().toString());
        intent.putExtra("cardValid",mETvalid.getText().toString());
        intent.putExtra("cardCvv",mETcvv.getText().toString());
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
