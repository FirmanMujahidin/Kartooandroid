package co.kartoo.app.rest;

import java.util.ArrayList;

import co.kartoo.app.rest.model.ChangePassword;
import co.kartoo.app.rest.model.LoginCredentials;
import co.kartoo.app.rest.model.RegisterUser;
import co.kartoo.app.rest.model.ResponseDefault;
import co.kartoo.app.rest.model.ResponseLogin;
import co.kartoo.app.rest.model.UpdateProfile;
import co.kartoo.app.rest.model.newest.AnswerOTP;
import co.kartoo.app.rest.model.newest.Cities;
import co.kartoo.app.rest.model.newest.ErrorObject;
import co.kartoo.app.rest.model.newest.Newsletter;
import co.kartoo.app.rest.model.newest.PopupReviewObject;
import co.kartoo.app.rest.model.newest.UserProfile;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;


public interface MainService {

    @POST("api/auth/changepassword")
    Call<ResponseDefault> doChangePassword(@Header("x-zumo-auth") String apikey, @Body ChangePassword userChange);

    @POST("api/auth/forgotpassword/{email}")
    Call<ResponseLogin> doForgotPassword(@Header("Authorization") String apikey, @Path("email") String email);

    @POST("api/auth/login")
    Call<ResponseLogin> doLogin(@Header("Authorization") String apikey, @Body LoginCredentials loginCredentials);

    @POST("api/users/update")
    Call<ResponseDefault> doUpdateProfile(@Header("x-zumo-auth") String authorization, @Body UpdateProfile updateProfile);

    @GET("api/users/profile")
    Call<UserProfile> getUserProfile(@Header("x-zumo-auth") String authorization);

    @GET("api/cities")
    Call<Cities> getCities(@Header("x-zumo-auth") String authorization);

    @GET("api/newsletter/push")
    Call<String> pushNewsletter(@Header("x-zumo-auth") String authorization);

    @GET("api/newsletter/{id}")
    Call<Newsletter> getNewsletter(@Header("x-zumo-auth") String authorization, @Path("id") String id);

    @POST("api/auth/register")
    Call<ResponseDefault> doRegister(@Header("Authorization") String apikey, @Body RegisterUser user);

    @POST("api/notifications/receive/{id}")
    Call<ResponseDefault> sendNotifId(@Header("x-zumo-auth") String authorization, @Path("id") String notificationid);

    @POST("api/users/interest")
    Call<ResponseDefault> doInterest(@Header("x-zumo-auth") String authorization, @Body ArrayList<String> interest);

    @POST("api/suggest")
    Call<ResponseDefault> doReport(@Header("x-zumo-auth") String authorization, @Body ErrorObject errorObject);

    @POST("api/feedback")
    Call<ResponseDefault> doFeedback(@Header("x-zumo-auth") String authorization, @Body PopupReviewObject popupReviewObject);

    @POST("api/users/verify/phone/challenge/{phoneNumber}")
    Call<ResponseDefault> doChallenge(@Header("x-zumo-auth") String authorization, @Path("phoneNumber") String phoneNumber);

    @POST("api/users/verify/phone/answer")
    Call<ResponseDefault> doAnswer(@Header("x-zumo-auth") String authorization, @Body AnswerOTP answerOTP);
}