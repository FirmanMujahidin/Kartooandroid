package co.kartoo.app.landing;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;

import com.facebook.login.LoginResult;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import co.kartoo.app.MainActivity_;
import co.kartoo.app.R;
import co.kartoo.app.drawer.DrawerViewActivity_;
import co.kartoo.app.models.LoginPref_;
import co.kartoo.app.rest.MainService;
import co.kartoo.app.rest.model.LoginCredentials;
import co.kartoo.app.rest.model.RegisterUser;
import co.kartoo.app.rest.model.ResponseDefault;
import co.kartoo.app.rest.model.ResponseLogin;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class JoinNowActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    static final int RC_SIGN_IN = 0;
    GoogleApiClient mGoogleApiClient;
    boolean mIntentInProgress;
    boolean mSignInClicked;
    ConnectionResult mConnectionResult;

    CallbackManager callbackManager;
    LoginManager loginManager;
    Context mContext;

    LoginPref_ loginPref;

    ProgressDialog dialog;
    ProgressDialog loadingDialog;

    EditText mETFullname, mETEmail, mETPassword, mETConfirmPassword;
    Button mBtnSignupEmail, mBtnFb, mBtnGplus;
    TextView BtnSignup, TermAndCondition, Privacy;

    String accessToken, uid, urlPhoto, mixemail, mixID, mixby;

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_now);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        loadingDialog = new ProgressDialog(this);
        loadingDialog.setMessage("Loading...");
        loadingDialog.setIndeterminate(true);
        loadingDialog.setCancelable(false);

        dialog = new ProgressDialog(JoinNowActivity.this);
        dialog.setMessage("Registering...");
        dialog.setCancelable(false);
        dialog.setIndeterminate(true);

        mETFullname = (EditText) findViewById(R.id.mETFullname);
        mETEmail = (EditText) findViewById(R.id.mETEmail);
        mETPassword = (EditText) findViewById(R.id.mETPassword);
        mETConfirmPassword = (EditText) findViewById(R.id.mETConfirmPassword);
        Privacy = (TextView) findViewById(R.id.Privacy);
        TermAndCondition = (TextView) findViewById(R.id.TermAndCondition);
        TermAndCondition.setText("Terms & Conditions");

        String APIkey = "exxAFFEyJOgPDZFAvPBFGcaIugSNhy49";
        String credentials = "api-key" + ":" + APIkey;
        final String basic = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        findViewById(R.id.mTVlogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JoinNowActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.TermAndCondition).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Intent intent = new Intent(JoinNowActivity.this, DrawerViewActivity_.class);
                bundle.putString("selectedPage", "tnc");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        findViewById(R.id.Privacy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Intent intent = new Intent(JoinNowActivity.this, DrawerViewActivity_.class);
                bundle.putString("selectedPage", "privacy");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


        findViewById(R.id.mBtnSignupEmail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String type = "email";
                final String name = mETFullname.getText().toString();
                final String email = mETEmail.getText().toString();
                final String password = mETPassword.getText().toString();
                boolean isValid = true;
                if (mETFullname.getText().toString().length() == 0) {
                    mETFullname.setError("Full Name is required!");
                    isValid = false;
                    return;
                }
                if (mETEmail.getText().toString().length() == 0) {
                    mETEmail.setError("Email is required!");
                    isValid = false;
                    return;
                }

                if (!mETEmail.getText().toString().contains("@")) {
                    mETEmail.setError("Your email is not valid!");
                    isValid = false;
                    return;
                }

                if (mETPassword.getText().toString().length() == 0 && mETPassword.getText().toString().length() < 8) {
                    mETPassword.setError("Password is required!");
                    isValid = false;
                    return;
                }
                if (mETPassword.getText().toString().length() < 8) {
                    mETPassword.setError("Password must be at least 8 character");
                    isValid = false;
                    return;
                }
                if (mETConfirmPassword.getText().toString().length() == 0) {
                    mETConfirmPassword.setError("Confirm Password is required!");
                    isValid = false;
                    return;
                }
                if (!mETConfirmPassword.getText().toString().equals(mETPassword.getText().toString())) {
                    mETConfirmPassword.setError("Your Password didn't match");
                    isValid = false;
                    return;
                }
                if (isValid) {
                    if (!dialog.isShowing())
                        dialog.show();

                    Retrofit retrofit = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
                    final MainService service = retrofit.create(MainService.class);
                    RegisterUser user = null;

                    user = new RegisterUser(name, email, password, "", null, null);
                    Call<ResponseDefault> serverCall = service.doRegister(basic, user);
                    serverCall.enqueue(new Callback<ResponseDefault>() {
                        @Override
                        public void onResponse(Response<ResponseDefault> response, Retrofit retrofit) {
                            Log.e("TAG", "register: "+response.code() );

                            if (response.isSuccess()) {
                                if (response.code() == 201) {
                                    dialog.setMessage("Logging in...");

                                    LoginCredentials credentials;
                                    credentials = new LoginCredentials(type, email, password);
                                    Log.e("TAG", "onResponse: "+"Register" );

                                    Call<ResponseLogin> loginResponseCall = service.doLogin(basic, credentials);
                                    loginResponseCall.enqueue(new Callback<ResponseLogin>() {
                                        @Override
                                        public void onResponse(Response<ResponseLogin> response, Retrofit retrofit) {
                                            Log.e("TAG", "onResponse: "+response.code());

                                            if (response.isSuccess()) {
                                                if (dialog.isShowing())
                                                    dialog.dismiss();
                                                if(loadingDialog.isShowing())
                                                    loadingDialog.dismiss();
                                                ResponseLogin responseServerBody = response.body();
                                                String token = responseServerBody.getTokenValue();
                                                String uid = responseServerBody.getUid();
                                                Intent intent = new Intent(JoinNowActivity.this, MainActivity_.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                                mixemail = email;
                                                mixID = uid;
                                                mixby = "email";

                                                intent.putExtra("email", email);
                                                intent.putExtra("name", name);
                                                intent.putExtra("auth", token);
                                                intent.putExtra("urlPhoto", urlPhoto);
                                                intent.putExtra("type", type);
                                                mixpanel();
                                                firebase();
                                                startActivity(intent);
                                                finish();

                                            } else if (response.code() == 401) {
                                                //Toast.makeText(getApplicationContext(), "Balikan 401", Toast.LENGTH_LONG).show();

                                                Toast.makeText(getApplicationContext(), "Username or password is incorrect", Toast.LENGTH_LONG).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Throwable t) {
                                            if (dialog.isShowing())
                                                dialog.dismiss();
                                            if(loadingDialog.isShowing())
                                                loadingDialog.dismiss();
                                        }
                                    });
                                }
                            } else {
                                int statusCode = response.code();
                                if (statusCode == 409) {
                                    Toast.makeText(JoinNowActivity.this, "You have already registered, Please Login", Toast.LENGTH_LONG).show();
                                    if (dialog.isShowing())
                                        dialog.dismiss();
                                    if(loadingDialog.isShowing())
                                        loadingDialog.dismiss();
                                }
                            }
                        }
                        @Override
                        public void onFailure(Throwable t) {
                            Log.e("TAG", "onFailure: "+"register fail" );
                            //Toast.makeText(JoinNowActivity.this, "Trouble here, please try again later", Toast.LENGTH_LONG).show();
                            if (dialog.isShowing())
                                dialog.dismiss();
                            if(loadingDialog.isShowing())
                                loadingDialog.dismiss();

                            LoginCredentials credentials;
                            credentials = new LoginCredentials(type, email, password);
                            Log.e("TAG", "onResponse: "+"Register" );
                            Call<ResponseLogin> loginResponseCall = service.doLogin(basic, credentials);
                            loginResponseCall.enqueue(new Callback<ResponseLogin>() {
                                @Override
                                public void onResponse(Response<ResponseLogin> response, Retrofit retrofit) {
                                    Log.e("TAG", "onResponseLogin: "+response.code());

                                    if (response.isSuccess()) {
                                        if (dialog.isShowing())
                                            dialog.dismiss();
                                        if(loadingDialog.isShowing())
                                            loadingDialog.dismiss();
                                        ResponseLogin responseServerBody = response.body();
                                        String token = responseServerBody.getTokenValue();
                                        String uid = responseServerBody.getUid();
                                        Intent intent = new Intent(JoinNowActivity.this, MainActivity_.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                        mixemail = email;
                                        mixID = uid;
                                        mixby = "email";

                                        intent.putExtra("email", email);
                                        intent.putExtra("name", name);
                                        intent.putExtra("auth", token);
                                        intent.putExtra("urlPhoto", urlPhoto);
                                        intent.putExtra("type", type);
                                        mixpanel();
                                        firebase();
                                        startActivity(intent);
                                        finish();

                                    } else if (response.code() == 401) {
                                        Toast.makeText(getApplicationContext(), "Username or password is incorrect", Toast.LENGTH_LONG).show();
                                        if (dialog.isShowing())
                                            dialog.dismiss();
                                        if(loadingDialog.isShowing())
                                            loadingDialog.dismiss();
                                    }
                                }

                                @Override
                                public void onFailure(Throwable t) {
                                    if (dialog.isShowing())
                                        dialog.dismiss();
                                    if(loadingDialog.isShowing())
                                        loadingDialog.dismiss();
                                }
                            });

                        }
                    });
                }
            }
        });

        findViewById(R.id.mBtnFb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callbackManager = CallbackManager.Factory.create();
                loginManager = LoginManager.getInstance();
                loginManager.logInWithReadPermissions(JoinNowActivity.this, Arrays.asList("public_profile", "email", "user_friends"));
                loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(final LoginResult loginResult) {
                        Log.d("loginResult", loginResult.toString());
                        GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(final JSONObject object, final GraphResponse response) {
                                if (response.getError() != null) {
                                    Log.e("errorGraphRequest", response.getError().toString());
                                }
                                else {
                                    Log.d("graphRequestResult", object.toString());
                                    try {
                                        if (!loadingDialog.isShowing())
                                            loadingDialog.show();

                                        final String type = "facebook";
                                        final String email = object.getString("email");
                                        final String id = loginResult.getAccessToken().getUserId();
                                        final String accessToken = loginResult.getAccessToken().getToken();
                                        final String name = object.getString("name");
                                        final String urlPhoto = object.getJSONObject("picture").getJSONObject("data").getString("url");
                                        //"Cek untukregistrasi/login dengan facebook apakah working on popular Android firmwares.

                                        //final String gender = object.getString("gender");
                                        //final String birthday = object.getString("birthday");

                                        //Log.e("TAG", "onCompleted: "+gender+""+birthday);

                                        Retrofit retrofit = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
                                        final MainService service = retrofit.create(MainService.class);
                                        RegisterUser user = null;

                                        user = new RegisterUser(name, email, "dedicated", urlPhoto, id, null);
                                        String APIkey = "exxAFFEyJOgPDZFAvPBFGcaIugSNhy49";
                                        String api = "api-key" + ":" + APIkey;
                                        final String basic = "Basic " + Base64.encodeToString(api.getBytes(), Base64.NO_WRAP);

                                        Call<ResponseDefault> serverCall = service.doRegister(basic, user);
                                        serverCall.enqueue(new Callback<ResponseDefault>() {
                                            @Override
                                            public void onResponse(Response<ResponseDefault> response, Retrofit retrofit) {
                                                if (response.isSuccess()) {
                                                    if (response.code() == 201) {


                                                        LoginCredentials credentials;
                                                        credentials = new LoginCredentials(type, id, "dedicated");
                                                        Call<ResponseLogin> loginResponseCall = service.doLogin(basic, credentials);
                                                        loginResponseCall.enqueue(new Callback<ResponseLogin>() {
                                                            @Override
                                                            public void onResponse(Response<ResponseLogin> response, Retrofit retrofit) {

                                                                if (response.isSuccess()) {
                                                                    if (dialog.isShowing())
                                                                        dialog.dismiss();
                                                                    if(loadingDialog.isShowing())
                                                                        loadingDialog.dismiss();
                                                                    ResponseLogin responseServerBody = response.body();
                                                                    String token = responseServerBody.getTokenValue();
                                                                    String uid = responseServerBody.getUid();
                                                                    Intent intent = new Intent(JoinNowActivity.this, MainActivity_.class);
                                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                                                    mixemail = email;
                                                                    mixID = uid;
                                                                    mixby = "facebook";

                                                                    intent.putExtra("email", email);
                                                                    intent.putExtra("name", name);
                                                                    intent.putExtra("auth", token);
                                                                    intent.putExtra("urlPhoto", urlPhoto);
                                                                    intent.putExtra("type", type);
                                                                    intent.putExtra("uidFacebook", id);
                                                                    mixpanel();
                                                                    firebase();
                                                                    startActivity(intent);
                                                                    finish();

                                                                } else if (response.code() == 401) {
                                                                    //PERTANYAAN!!
                                                                    Toast.makeText(getApplicationContext(), "Username or password is incorrect", Toast.LENGTH_LONG).show();
                                                                    if (dialog.isShowing())
                                                                        dialog.dismiss();
                                                                    if(loadingDialog.isShowing())
                                                                        loadingDialog.dismiss();
                                                                }
                                                            }

                                                            @Override
                                                            public void onFailure(Throwable t) {
                                                                if (dialog.isShowing())
                                                                    dialog.dismiss();
                                                                if(loadingDialog.isShowing())
                                                                    loadingDialog.dismiss();

                                                            }
                                                        });

                                                    }
                                                } else {
                                                    int statusCode = response.code();
                                                    if (statusCode == 409) {
                                                        //Toast.makeText(JoinNowActivity.this, "Your email has already registered", Toast.LENGTH_LONG).show();
                                                        if (dialog.isShowing())
                                                            dialog.dismiss();
                                                        if(loadingDialog.isShowing())
                                                            loadingDialog.dismiss();



                                                        dialog.setMessage("Logging in...");
                                                        LoginCredentials credentials;
                                                        credentials = new LoginCredentials(type, id, "dedicated");
                                                        Call<ResponseLogin> loginResponseCall = service.doLogin(basic, credentials);
                                                        loginResponseCall.enqueue(new Callback<ResponseLogin>() {
                                                            @Override
                                                            public void onResponse(Response<ResponseLogin> response, Retrofit retrofit) {

                                                                if (response.isSuccess()) {
                                                                    if (dialog.isShowing())
                                                                        dialog.dismiss();
                                                                    if(loadingDialog.isShowing())
                                                                        loadingDialog.dismiss();
                                                                    ResponseLogin responseServerBody = response.body();
                                                                    String token = responseServerBody.getTokenValue();
                                                                    String uid = responseServerBody.getUid();
                                                                    Intent intent = new Intent(JoinNowActivity.this, MainActivity_.class);
                                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                                                    mixemail = email;
                                                                    mixID = uid;
                                                                    mixby = "facebook";

                                                                    intent.putExtra("email", email);
                                                                    intent.putExtra("name", name);
                                                                    intent.putExtra("auth", token);
                                                                    intent.putExtra("urlPhoto", urlPhoto);
                                                                    intent.putExtra("type", type);
                                                                    intent.putExtra("uidFacebook", id);
                                                                    mixpanel();
                                                                    firebase();
                                                                    startActivity(intent);
                                                                    finish();

                                                                } else if (response.code() == 401) {
                                                                    //PERTANYAAN!!
                                                                    Toast.makeText(getApplicationContext(), "Username or password is incorrect", Toast.LENGTH_LONG).show();
                                                                    if (dialog.isShowing())
                                                                        dialog.dismiss();
                                                                    if(loadingDialog.isShowing())
                                                                        loadingDialog.dismiss();
                                                                }
                                                            }

                                                            @Override
                                                            public void onFailure(Throwable t) {
                                                                if (dialog.isShowing())
                                                                    dialog.dismiss();
                                                                if(loadingDialog.isShowing())
                                                                    loadingDialog.dismiss();

                                                            }
                                                        });

                                                    }
                                                }
                                            }

                                            @Override
                                            public void onFailure(Throwable t) {

                                                LoginCredentials credentials;
                                                credentials = new LoginCredentials(type, id, "dedicated");
                                                Call<ResponseLogin> loginResponseCall = service.doLogin(basic, credentials);
                                                loginResponseCall.enqueue(new Callback<ResponseLogin>() {
                                                    @Override
                                                    public void onResponse(Response<ResponseLogin> response, Retrofit retrofit) {
                                                        if (dialog.isShowing())
                                                            dialog.dismiss();
                                                        if(loadingDialog.isShowing())
                                                            loadingDialog.dismiss();
                                                        if (response.isSuccess()) {
                                                            ResponseLogin responseServerBody = response.body();
                                                            String token = responseServerBody.getTokenValue();
                                                            String uid = responseServerBody.getUid();
                                                            Intent intent = new Intent(JoinNowActivity.this, MainActivity_.class);
                                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                                            mixemail = email;
                                                            mixID = uid;
                                                            mixby = "facebook";

                                                            intent.putExtra("email", email);
                                                            intent.putExtra("name", name);
                                                            intent.putExtra("auth", token);
                                                            intent.putExtra("urlPhoto", urlPhoto);
                                                            intent.putExtra("type", type);
                                                            intent.putExtra("uidFacebook", id);
                                                            mixpanel();
                                                            firebase();
                                                            startActivity(intent);
                                                            finish();

                                                        } else if (response.code() == 401) {
                                                            Toast.makeText(getApplicationContext(), "Username or password is incorrect", Toast.LENGTH_LONG).show();
                                                            if (dialog.isShowing())
                                                                dialog.dismiss();
                                                            if(loadingDialog.isShowing())
                                                                loadingDialog.dismiss();
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Throwable t) {
                                                        if (dialog.isShowing())
                                                            dialog.dismiss();
                                                        if(loadingDialog.isShowing())
                                                            loadingDialog.dismiss();
                                                    }
                                                });
                                            }
                                        });
                                    }

                                    catch (JSONException e) {
                                        if (dialog.isShowing())
                                            dialog.dismiss();
                                        if(loadingDialog.isShowing())
                                            loadingDialog.dismiss();
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,link,picture.type(large)");
                        graphRequest.setParameters(parameters);
                        graphRequest.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        if (dialog.isShowing())
                            dialog.dismiss();
                        if(loadingDialog.isShowing())
                            loadingDialog.dismiss();
                    }

                    @Override
                    public void onError(FacebookException error) {
                        if (dialog.isShowing())
                            dialog.dismiss();
                        if(loadingDialog.isShowing())
                            loadingDialog.dismiss();
                        Log.e("asdf", error.getLocalizedMessage());
                        Toast.makeText(JoinNowActivity.this, "Failed to login, please try again later", Toast.LENGTH_LONG).show();
                    }
                });
            }

        });

        findViewById(R.id.mBtnGplus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();

            }
        });

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .addScope(new Scope(Scopes.PROFILE))
                .addScope(new Scope(Scopes.EMAIL))
                .build();
    }

    private void check() {
        if (Build.VERSION.SDK_INT >= 23) {
            int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.GET_ACCOUNTS);
            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.GET_ACCOUNTS)) {
                    showMessageOKCancel("You need to allow access to Google Account",
                            new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissions(new String[] {Manifest.permission.GET_ACCOUNTS},
                                    REQUEST_CODE_ASK_PERMISSIONS);
                        }
                    });
                    return;
                }
                requestPermissions(new String[] {Manifest.permission.GET_ACCOUNTS},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return;
            }
            signInWithGplus();
        }
        else{
            signInWithGplus();
        }

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(JoinNowActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    signInWithGplus();
                } else {
                    // Permission Denied
                    Toast.makeText(JoinNowActivity.this, "You can't login with Google", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void mixpanel(){
        String projectToken = getString(R.string.mixpanel_token);
        MixpanelAPI mixpanel = MixpanelAPI.getInstance(this, projectToken);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTimeStamp = dateFormat.format(new Date());
        try {
            JSONObject propse = new JSONObject();
            propse.put("TimeStamp", currentTimeStamp);
            mixpanel.track("Sign up modal", propse);
        } catch (JSONException e) {
        }
        try {
            JSONObject props = new JSONObject();
            props.put("TimeStamp", currentTimeStamp);
            props.put("email", mixemail);
            props.put("Sign up method", mixby);
            mixpanel.track("Successfully signed up", props);
        } catch (JSONException e) {
        }
    }

    public void firebase(){
        Bundle params = new Bundle();
        params.putString("email", mixemail);
        params.putString("sign_up_method", mixby);
        mFirebaseAnalytics.logEvent("join_now", params);
    }

    private void signInWithGplus() {

        if (!mGoogleApiClient.isConnecting()) {
            if (!loadingDialog.isShowing()) {
                loadingDialog.show();
            }

            mSignInClicked = true;
            resolveSignInError();
        }
    }

    private void resolveSignInError() {
        if (dialog.isShowing())
            dialog.dismiss();
        if(loadingDialog.isShowing())
            loadingDialog.dismiss();
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        if (dialog.isShowing())
            dialog.dismiss();
        if(loadingDialog.isShowing())
            loadingDialog.dismiss();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode != RESULT_OK) {
                mSignInClicked = false;
            }
            mIntentInProgress = false;
            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }
        else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public void onConnected(Bundle bundle) {
        mSignInClicked = false;
        final String SCOPES = "oauth2:profile email";
        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                String token = null;
                try {
                    token = GoogleAuthUtil.getToken(JoinNowActivity.this, Plus.AccountApi.getAccountName(mGoogleApiClient), SCOPES);
                } catch (IOException e) {
                    if (dialog.isShowing())
                        dialog.dismiss();
                    if(loadingDialog.isShowing())
                        loadingDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Failed to login, please try again later", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                } catch (GoogleAuthException e) {
                    if (dialog.isShowing())
                        dialog.dismiss();
                    if(loadingDialog.isShowing())
                        loadingDialog.dismiss();
                    JoinNowActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Failed to login, please try again later", Toast.LENGTH_LONG).show();
                        }
                    });
                    e.printStackTrace();
                }
                return token;
            }

            @Override
            protected void onPostExecute(String s) {
//                Log.e("token", s);
                if (!s.equals(""))
                    getProfileInformation(s);
            }
        }.execute();
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (dialog.isShowing())
            dialog.dismiss();
        if(loadingDialog.isShowing())
            loadingDialog.dismiss();
        Toast.makeText(getApplicationContext(), "Failed to login, please try again later", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (dialog.isShowing())
            dialog.dismiss();
        if(loadingDialog.isShowing())
            loadingDialog.dismiss();

        if (!connectionResult.hasResolution()) {


            GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), this,0).show();
            return;
        }
        if (!mIntentInProgress) {
            // Store the ConnectionResult for later usage
            mConnectionResult = connectionResult;

            if (mSignInClicked) {
                // The user has already clicked 'sign-in' so we attempt to
                // resolve all
                // errors until the user is signed in, or they cancel.
                //resolveSignInError();
            }
        }
    }



    private void getProfileInformation(final String token) {
        try {
            Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
            if (currentPerson != null) {
                if (!loadingDialog.isShowing()) {
                    loadingDialog.show();
                }
                final String id = currentPerson.getId();
                final String name = currentPerson.getDisplayName();
                final String urlPhoto = currentPerson.getImage().getUrl();
                final String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
                final String type = "google";
                Retrofit retrofit = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
                final MainService service = retrofit.create(MainService.class);

                RegisterUser user = null;
                user = new RegisterUser(name, email, "dedicated", urlPhoto, null, id);

                String APIkey = "exxAFFEyJOgPDZFAvPBFGcaIugSNhy49";
                String api = "api-key" + ":" + APIkey;
                final String basic = "Basic " + Base64.encodeToString(api.getBytes(), Base64.NO_WRAP);

                Call<ResponseDefault> serverCall = service.doRegister(basic, user);
                serverCall.enqueue(new Callback<ResponseDefault>() {
                    @Override
                    public void onResponse(Response<ResponseDefault> response, Retrofit retrofit) {
                        if (response.isSuccess()) {
                            if (response.code() == 201) {
                                dialog.setMessage("Logging in...");
                                LoginCredentials credentials;
                                credentials = new LoginCredentials(type, id, "dedicated");
                                Call<ResponseLogin> loginResponseCall = service.doLogin(basic, credentials);
                                loginResponseCall.enqueue(new Callback<ResponseLogin>() {
                                    @Override
                                    public void onResponse(Response<ResponseLogin> response, Retrofit retrofit) {

                                        if (response.isSuccess()) {
                                            if (dialog.isShowing())
                                                dialog.dismiss();
                                            if(loadingDialog.isShowing())
                                                loadingDialog.dismiss();
                                            ResponseLogin responseServerBody = response.body();
                                            String token = responseServerBody.getTokenValue();
                                            String uid = responseServerBody.getUid();
                                            Intent intent = new Intent(JoinNowActivity.this, MainActivity_.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                            mixemail = email;
                                            mixID = uid;
                                            mixby = "google";

                                            intent.putExtra("email", email);
                                            intent.putExtra("name", name);
                                            intent.putExtra("auth", token);
                                            intent.putExtra("urlPhoto", urlPhoto);
                                            intent.putExtra("type", type);
                                            intent.putExtra("uidGplus", id);
                                            mixpanel();
                                            firebase();
                                            startActivity(intent);
                                            finish();

                                        } else if (response.code() == 401) {
                                            //PERTANYAAN!!
                                            Toast.makeText(getApplicationContext(), "Username or password is incorrect", Toast.LENGTH_LONG).show();
                                            if (dialog.isShowing())
                                                dialog.dismiss();
                                            if(loadingDialog.isShowing())
                                                loadingDialog.dismiss();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Throwable t) {
                                        if (dialog.isShowing())
                                            dialog.dismiss();
                                        if(loadingDialog.isShowing())
                                            loadingDialog.dismiss();
                                    }
                                });


                            }
                        } else {
                            int statusCode = response.code();
                            if (statusCode == 409) {
                                //Toast.makeText(JoinNowActivity.this, "Your email has already registered", Toast.LENGTH_LONG).show();
                                if (dialog.isShowing())
                                    dialog.dismiss();
                                if(loadingDialog.isShowing())
                                    loadingDialog.dismiss();

                                LoginCredentials credentials;
                                credentials = new LoginCredentials(type, id, "dedicated");
                                Call<ResponseLogin> loginResponseCall = service.doLogin(basic, credentials);
                                loginResponseCall.enqueue(new Callback<ResponseLogin>() {
                                    @Override
                                    public void onResponse(Response<ResponseLogin> response, Retrofit retrofit) {
                                        dialog.setMessage("Logging in...");

                                        if (response.isSuccess()) {
                                            if (dialog.isShowing())
                                                dialog.dismiss();
                                            if(loadingDialog.isShowing())
                                                loadingDialog.dismiss();
                                            ResponseLogin responseServerBody = response.body();
                                            String token = responseServerBody.getTokenValue();
                                            String uid = responseServerBody.getUid();
                                            Intent intent = new Intent(JoinNowActivity.this, MainActivity_.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                            mixemail = email;
                                            mixID = uid;
                                            mixby = "google";

                                            intent.putExtra("email", email);
                                            intent.putExtra("name", name);
                                            intent.putExtra("auth", token);
                                            intent.putExtra("urlPhoto", urlPhoto);
                                            intent.putExtra("type", type);
                                            intent.putExtra("uidGplus", id);
                                            mixpanel();
                                            firebase();
                                            startActivity(intent);
                                            finish();

                                        } else if (response.code() == 401) {
                                            //PERTANYAAN!!
                                            Toast.makeText(getApplicationContext(), "Username or password is incorrect", Toast.LENGTH_LONG).show();
                                            if (dialog.isShowing())
                                                dialog.dismiss();
                                            if(loadingDialog.isShowing())
                                                loadingDialog.dismiss();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Throwable t) {
                                        if (dialog.isShowing())
                                            dialog.dismiss();
                                        if(loadingDialog.isShowing())
                                            loadingDialog.dismiss();
                                    }
                                });

                            }
                            /*
                                dialog.setMessage("Logging in...");
                                LoginCredentials credentials;
                                credentials = new LoginCredentials(type, id, "dedicated");

                                Log.e("creden", "creden "+type+id);

                                Call<ResponseLogin> loginResponseCall = service.doLogin(basic, credentials);
                                loginResponseCall.enqueue(new Callback<ResponseLogin>() {
                                    @Override
                                    public void onResponse(Response<ResponseLogin> response, Retrofit retrofit) {
                                        Log.e("response", "onResponse "+response.toString());
                                        if (response.isSuccess()) {
                                            ResponseLogin responseServerBody = response.body();
                                            String token = responseServerBody.getTokenValue();
                                            String uid = responseServerBody.getUid();
                                                               //loginPref.name().put(name);
                                                               //loginPref.token().put(token);
                                                               //loginPref.uid().put(uid);
                                                               //loginPref.type().put(type);
                                                               //loginPref.urlPhoto().put(urlPhoto);
                                            if (dialog.isShowing())
                                                dialog.dismiss();
                                            //loginPref.isFirstLogin().put(true);

                                            Intent intent = new Intent(JoinNowActivity.this, MainActivity_.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                            intent.putExtra("email", email);
                                            intent.putExtra("name", name);
                                            intent.putExtra("auth", token);
                                            intent.putExtra("urlPhoto", urlPhoto);
                                            intent.putExtra("type", type);

                                            intent.putExtra("uidGplus", id);
                                                               //intent.putExtra("uidFacebook", uidFacebook);

                                            startActivity(intent);
                                            finish();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Throwable t) {

                                        if (dialog.isShowing())
                                            dialog.dismiss();
                                    }
                                });

                            }
                            */
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        LoginCredentials credentials;
                        credentials = new LoginCredentials(type, id, "dedicated");
                        Call<ResponseLogin> loginResponseCall = service.doLogin(basic, credentials);
                        loginResponseCall.enqueue(new Callback<ResponseLogin>() {
                            @Override
                            public void onResponse(Response<ResponseLogin> response, Retrofit retrofit) {
                                dialog.setMessage("Logging in...");

                                if (response.isSuccess()) {
                                    if (dialog.isShowing())
                                        dialog.dismiss();
                                    if(loadingDialog.isShowing())
                                        loadingDialog.dismiss();
                                    ResponseLogin responseServerBody = response.body();
                                    String token = responseServerBody.getTokenValue();
                                    String uid = responseServerBody.getUid();
                                    Intent intent = new Intent(JoinNowActivity.this, MainActivity_.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                    mixemail = email;
                                    mixID = uid;
                                    mixby = "google";

                                    intent.putExtra("email", email);
                                    intent.putExtra("name", name);
                                    intent.putExtra("auth", token);
                                    intent.putExtra("urlPhoto", urlPhoto);
                                    intent.putExtra("type", type);
                                    intent.putExtra("uidGplus", id);
                                    mixpanel();
                                    firebase();
                                    startActivity(intent);
                                    finish();

                                } else if (response.code() == 401) {
                                    //PERTANYAAN!!
                                    Toast.makeText(getApplicationContext(), "Username or password is incorrect", Toast.LENGTH_LONG).show();
                                    if (dialog.isShowing())
                                        dialog.dismiss();
                                    if(loadingDialog.isShowing())
                                        loadingDialog.dismiss();
                                }
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                if (dialog.isShowing())
                                    dialog.dismiss();
                                if(loadingDialog.isShowing())
                                    loadingDialog.dismiss();
                            }
                        });
                    }
                });

            } else {
                if (dialog.isShowing())
                    dialog.dismiss();
                if(loadingDialog.isShowing())
                    loadingDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Failed to login, please try again later", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            if (dialog.isShowing())
                dialog.dismiss();
            if(loadingDialog.isShowing())
                loadingDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Failed to login, please try again later", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed()
    {
        if (loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
        Intent intent = new Intent(JoinNowActivity.this, LandingActivity_.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
