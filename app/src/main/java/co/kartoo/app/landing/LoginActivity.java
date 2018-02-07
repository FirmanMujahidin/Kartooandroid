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
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

import co.kartoo.app.MainActivity_;
import co.kartoo.app.R;
import co.kartoo.app.forgotpassword.ForgotYourPassword;
import co.kartoo.app.models.LoginPref_;
import co.kartoo.app.rest.MainService;
import co.kartoo.app.rest.model.LoginCredentials;
import co.kartoo.app.rest.model.ResponseLogin;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class LoginActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    static final int RC_SIGN_IN = 0;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    EditText mETEmail, mETPassword;
    LoginPref_ loginPref;
    ProgressDialog loadingDialog;
    TextView BtnSignup, TVForgotPassword;
    GoogleApiClient mGoogleApiClient;
    boolean mIntentInProgress;
    boolean mSignInClicked;
    ConnectionResult mConnectionResult;
    CallbackManager callbackManager;
    LoginManager loginManager;
    Context mContext;
    String APIkey = "exxAFFEyJOgPDZFAvPBFGcaIugSNhy49";
    String api = "api-key" + ":" + APIkey;
    final String basic = "Basic " + Base64.encodeToString(api.getBytes(), Base64.NO_WRAP);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loadingDialog = new ProgressDialog(LoginActivity.this);
        loadingDialog.setMessage("Loading...");
        loadingDialog.setIndeterminate(true);
        loadingDialog.setCancelable(false);

        mETEmail = (EditText) findViewById(R.id.mETEmail);
        mETPassword = (EditText) findViewById(R.id.mETPassword);

        String APIkey = "exxAFFEyJOgPDZFAvPBFGcaIugSNhy49";
        String api = "api-key" + ":" + APIkey;
        final String basic = "Basic " + Base64.encodeToString(api.getBytes(), Base64.NO_WRAP);

        findViewById(R.id.BtnSignup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, JoinNowActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.TVForgotPassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotYourPassword.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.mBtnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!loadingDialog.isShowing())
                    loadingDialog.show();

                final String type = "email";
                final String email = mETEmail.getText().toString();
                final String password = mETPassword.getText().toString();

                Retrofit retrofit = new Retrofit.Builder().baseUrl("http://kartoo-dev.azure-mobile.net/").addConverterFactory(GsonConverterFactory.create()).build();
                final MainService service = retrofit.create(MainService.class);
                LoginCredentials credentials = new LoginCredentials(type, email, password);
                Call<ResponseLogin> loginResponseCall = service.doLogin(basic, credentials);
                loginResponseCall.enqueue(new Callback<ResponseLogin>() {
                    @Override
                    public void onResponse(Response<ResponseLogin> response, Retrofit retrofit) {

                        if (response.code()==200) {
                            if (loadingDialog.isShowing())
                                loadingDialog.dismiss();

                            ResponseLogin responseServerBody = response.body();

                            boolean mustChangePassword = response.body().getMustChangePassword();
                            String token = responseServerBody.getTokenValue();
                            String uid = responseServerBody.getUid();
                            String name = responseServerBody.getName();

                            Log.e("TAG", "onResponse: " + responseServerBody.getTokenValue());

                            Intent intent = new Intent(LoginActivity.this, MainActivity_.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                            intent.putExtra("email", email);
                            intent.putExtra("mustChangePassword", mustChangePassword);
                            intent.putExtra("name", name);
                            intent.putExtra("auth", token);
                            intent.putExtra("type", type);
                            startActivity(intent);
                            finish();

                        } if (response.code() == 401){
                            Toast.makeText(getApplicationContext(), "Username or password is incorrect", Toast.LENGTH_LONG).show();
                            if (loadingDialog.isShowing())
                                loadingDialog.dismiss();

                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        if (loadingDialog.isShowing())
                            loadingDialog.dismiss();
                    }
                });
            }
        });

        findViewById(R.id.mBtnGoogle).setOnClickListener(new View.OnClickListener() {
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

        findViewById(R.id.mBtnFacebook).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callbackManager = CallbackManager.Factory.create();
                loginManager = LoginManager.getInstance();
                loginManager.logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "email", "user_friends"));
                loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(final LoginResult loginResult) {
                        Log.d("loginResult", loginResult.toString());
                        GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(final JSONObject object, final GraphResponse response) {
                                if (response.getError() != null) {
                                    Log.e("errorGraphRequest", response.getError().toString());
                                } else {
                                    Log.d("graphRequestResult", object.toString());
                                    try {
                                        if (!loadingDialog.isShowing())
                                            loadingDialog.show();

                                        final String type = "facebook";
                                        final String id = loginResult.getAccessToken().getUserId();
                                        final String accessToken = loginResult.getAccessToken().getToken();
                                        final String name = object.getString("name");
                                        final String email = object.getString("email");
                                        //"Cek untukregistrasi/login dengan facebook apakah working on popular Android firmwares.

                                        //final String gender = object.getString("gender");
                                        //final String birthday = object.getString("birthday");
                                        Log.e("TAG", "onCompleted: "+ id );

                                        //.e("TAG", "onCompleted: "+gender+""+birthday);
                                        final String urlPhoto = object.getJSONObject("picture").getJSONObject("data").getString("url");

                                        Retrofit retrofit = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
                                        MainService service = retrofit.create(MainService.class);
                                        // perform login
                                        final LoginCredentials loginCredentials = new LoginCredentials(type, id, "dedicated");
                                        Call<ResponseLogin> responseServer = service.doLogin(basic, loginCredentials);
                                        responseServer.enqueue(new Callback<ResponseLogin>() {
                                            @Override
                                            public void onResponse(Response<ResponseLogin> response, Retrofit retrofit) {

                                                int status = response.code();
                                                Log.e("success", status + "");
                                                if (response.isSuccess()) {
                                                    if (loadingDialog.isShowing())
                                                        loadingDialog.dismiss();
                                                    ResponseLogin responseServerBody = response.body();
                                                    String token = responseServerBody.getTokenValue();
                                                    String uid = responseServerBody.getUid();

                                                    Intent intent = new Intent(LoginActivity.this, MainActivity_.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    intent.putExtra("email", email);
                                                    intent.putExtra("name", name);
                                                    intent.putExtra("auth", token);
                                                    intent.putExtra("uidFacebook", accessToken);
                                                    intent.putExtra("urlPhoto", urlPhoto);
                                                    startActivity(intent);
                                                    finish();

                                                } else if (status == 401 || status == 404) {
                                                    Bundle bundle = new Bundle();
                                                    String email = "";
                                                    if (loadingDialog.isShowing())
                                                        loadingDialog.dismiss();
                                                    Toast.makeText(getApplicationContext(), "Your email has already registered", Toast.LENGTH_LONG).show();


                                                    try {
                                                        email = object.getString("email");
                                                    } catch (JSONException e) {

                                                    }
                                                    bundle.putString("email", email);
                                                    bundle.putString("uid", id);
                                                    bundle.putString("type", "facebook");
                                                    bundle.putString("accessToken", accessToken);
                                                    bundle.putString("name", name);
                                                    bundle.putString("urlPhoto", urlPhoto);
                                                    //Intent intent = new Intent(LoginActivity.this, RegisterActivity_.class);
                                                    //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    //finish();
                                                    //intent.putExtras(bundle);
                                                    //startActivity(intent);
                                                }
                                            }

                                            @Override
                                            public void onFailure(Throwable t) {
                                                Log.e("asdf", t.getLocalizedMessage());
                                                if (loadingDialog.isShowing())
                                                    loadingDialog.dismiss();
                                            }
                                        });
                                    } catch (JSONException e) {
                                        if (loadingDialog.isShowing())
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
                        if (loadingDialog.isShowing())
                            loadingDialog.dismiss();
                    }

                    @Override
                    public void onError(FacebookException error) {
                        if (loadingDialog.isShowing())
                            loadingDialog.dismiss();
                        Log.e("asdf", error.getLocalizedMessage());
                        Toast.makeText(getApplicationContext(), "Failed to login, please try again later", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void check() {
        if (Build.VERSION.SDK_INT >= 23) {
            int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.GET_ACCOUNTS);
            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.GET_ACCOUNTS)) {
                    showMessageOKCancel("You need to allow access to Google Account",
                            new DialogInterface.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.M)
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
        new AlertDialog.Builder(LoginActivity.this)
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
                    Toast.makeText(LoginActivity.this, "You can't login with Google", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void signInWithGplus() {
        if (!mGoogleApiClient.isConnecting()) {
            if (!loadingDialog.isShowing()) {
                loadingDialog.dismiss();
            }
            mSignInClicked = true;
            resolveSignInError();
        }

    }

    /**
     * Method to resolve any signin errors
     * */
    private void resolveSignInError() {
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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
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
                    token = GoogleAuthUtil.getToken(getApplicationContext(), Plus.AccountApi.getAccountName(mGoogleApiClient), SCOPES);
                } catch (IOException e) {
                    if (loadingDialog.isShowing()) {
                        loadingDialog.dismiss();
                    }
                    Toast.makeText(getApplicationContext(), "Failed to login, please try again later", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                } catch (GoogleAuthException e) {
                    if (loadingDialog.isShowing()) {
                        loadingDialog.dismiss();
                    }
                    /*
                    LoginActivity().this,runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Failed to login, please try again later", Toast.LENGTH_LONG).show();
                        }
                    });
                    */
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
        if (loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }

        Toast.makeText(getApplicationContext(), "Failed to login, please try again later", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (!connectionResult.hasResolution()) {
            if (loadingDialog.isShowing()) {
                loadingDialog.dismiss();
            }
            String errorGoogleConnection = GooglePlayServicesUtil.getErrorString(0);
            return;
        }

        if (!mIntentInProgress) {
            // Store the ConnectionResult for later usage
            mConnectionResult = connectionResult;

            if (mSignInClicked) {
                // The user has already clicked 'sign-in' so we attempt to
                // resolve all
                // errors until the user is signed in, or they cancel.
                resolveSignInError();
            }
        }
    }

    private void getProfileInformation(final String token) {
        try {
            Person currentPerson = Plus.PeopleApi
                    .getCurrentPerson(mGoogleApiClient);
            if (currentPerson != null) {
                if (!loadingDialog.isShowing()) {
                    loadingDialog.show();
                }
                final String type = "google";
                final String id = currentPerson.getId();
                final String name = currentPerson.getDisplayName();
                final String urlPhoto = currentPerson.getImage().getUrl();
                final String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
                Retrofit retrofit = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
                MainService service = retrofit.create(MainService.class);
                // perform login
                Log.e("idgoogle","idgoogle"+id );
                final LoginCredentials loginCredentials = new LoginCredentials(type, id, "dedicated");
                Call<ResponseLogin> responseServer = service.doLogin(basic, loginCredentials);
                responseServer.enqueue(new Callback<ResponseLogin>() {
                    @Override
                    public void onResponse(Response<ResponseLogin> response, Retrofit retrofit) {
                        int status = response.code();
                        Log.e("success", status + "");
                        Log.e("TAG", "onResponseSuccsess: " + response.code());

                        if (response.isSuccess()) {
                            if (loadingDialog.isShowing())
                                loadingDialog.dismiss();
                            ResponseLogin responseServerBody = response.body();
                            String token = responseServerBody.getTokenValue();
                            String uid = responseServerBody.getUid();
                            Log.e("tokenmasuk", "tokenmasuk"+token);
                            Log.e("TAG", "tokenMsk: " + token);
                            Intent intent = new Intent(LoginActivity.this, MainActivity_.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                            intent.putExtra("email", email);
                            intent.putExtra("name", name);
                            intent.putExtra("auth", token);
                            intent.putExtra("uidGplus", id);
                            intent.putExtra("urlPhoto", urlPhoto);

                            startActivity(intent);
                            finish();
                        } else if (status == 401 || status == 404) {
                            Toast.makeText(getApplicationContext(), "Your email has already Registered", Toast.LENGTH_LONG).show();
                            if (loadingDialog.isShowing())
                                loadingDialog.dismiss();
                            Bundle bundle = new Bundle();
                            bundle.putString("email", email);
                            bundle.putString("uid", id);
                            bundle.putString("type", "google");
                            bundle.putString("accessToken", token);
                            bundle.putString("name", name);
                            bundle.putString("urlPhoto", urlPhoto);
                                                   //Intent intent = new Intent(LoginActivity.this, JoinNowActivity.class);
                                                   //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                   //finish();
                                                   //intent.putExtras(bundle);
                                                   //startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        if (loadingDialog.isShowing())
                            loadingDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Failed to login, please try again later", Toast.LENGTH_LONG).show();
                    }
                });

            } else {
                if (loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }
                Toast.makeText(getApplicationContext(), "Failed to login, please try again later", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            if (loadingDialog.isShowing()) {
                loadingDialog.dismiss();
            }
            Toast.makeText(getApplicationContext(), "Failed to login, please try again later", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(LoginActivity.this, LandingActivity_.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

}
