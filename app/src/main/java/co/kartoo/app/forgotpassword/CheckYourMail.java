package co.kartoo.app.forgotpassword;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import co.kartoo.app.R;
import co.kartoo.app.landing.LoginActivity;

public class CheckYourMail extends AppCompatActivity {
    Button mBtnReset;
    TextView mTVCancel, mTVtimer;
    private ProgressBar progressBarCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_your_mail);

        mBtnReset = (Button) findViewById(R.id.mBtnReset);
        mTVCancel = (TextView) findViewById(R.id.mTVCancel);
        mTVtimer = (TextView) findViewById(R.id.mTVtimer);
        progressBarCount = (ProgressBar) findViewById(R.id.progressBarCount);
        progressBarCount.setVisibility(View.VISIBLE);
        //progressBarCount.setProgress(0);


        new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {

                final Integer count = (int) (long) millisUntilFinished/300;
                Log.e("TAG", "onTick: "+count+"" );

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBarCount.setProgress(count);
                    }
                });



                mBtnReset.setBackground(getResources().getDrawable(R.drawable.shape_btn_coral_dead_button));
                mTVtimer.setText(millisUntilFinished / 1000+"");
            }
            public void onFinish() {
                mBtnReset.setBackground(getResources().getDrawable(R.drawable.shape_btn_coral_rc));
                mBtnReset.setText("Continue");
                mTVCancel.setVisibility(View.VISIBLE);
                progressBarCount.setVisibility(View.GONE);
                mTVtimer.setVisibility(View.GONE);
                clickButton();
            }
        }.start();
    }

    public void clickButton() {
        mBtnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckYourMail.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        mTVCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckYourMail.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }
}
