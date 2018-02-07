package co.kartoo.app.forgotpassword;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import co.kartoo.app.R;
import co.kartoo.app.landing.LoginActivity;
import co.kartoo.app.rest.MainService;
import co.kartoo.app.rest.model.ResponseLogin;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class ForgotYourPassword extends AppCompatActivity {
    Button mBtnReset;
    EditText mETEmailChangePassword;
    TextView mTVCancel;

    Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_your_password);
        mBtnReset =(Button) findViewById(R.id.mBtnReset);
        mETEmailChangePassword = (EditText) findViewById(R.id.mETEmailChangePassword);
        mTVCancel = (TextView) findViewById(R.id.mTVCancel);

        String APIkey = "exxAFFEyJOgPDZFAvPBFGcaIugSNhy49";
        String credentials = "api-key" + ":" + APIkey;
        final String basic = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        findViewById(R.id.mTVCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotYourPassword.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });


        findViewById(R.id.mBtnReset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mETEmailChangePassword.getText().toString();
                retrofit = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
                MainService mainService = retrofit.create(MainService.class);
                Log.e("TAG", "onClick: "+basic+email);
                Call<ResponseLogin> loginResponseCall = mainService.doForgotPassword(basic, email);
                loginResponseCall.enqueue(new Callback<ResponseLogin>() {
                    @Override
                    public void onResponse(Response<ResponseLogin> response, Retrofit retrofit) {
                        Log.e("TAG", "onResponseForgot: "+response.code());
                        if (response.isSuccess()) {
                            Intent intent = new Intent(ForgotYourPassword.this, CheckYourMail.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }

                        if (response.code() == 400) {
                            Toast.makeText(getApplicationContext(), "That user does not exist", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Intent intent = new Intent(ForgotYourPassword.this, CheckYourMail.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });
    }
    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(ForgotYourPassword.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
