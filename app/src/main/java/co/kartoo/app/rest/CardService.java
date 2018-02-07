package co.kartoo.app.rest;

import com.squareup.okhttp.ResponseBody;

import java.util.ArrayList;

import co.kartoo.app.cards.suggestioncard.applycard.ApplyDoneActivity;
import co.kartoo.app.rest.model.Bank;
import co.kartoo.app.rest.model.Card;
import co.kartoo.app.rest.model.ResponseDefault;
import co.kartoo.app.rest.model.newest.Availablecards;
import co.kartoo.app.rest.model.newest.CardDetail;
import co.kartoo.app.rest.model.newest.Notifications;
import co.kartoo.app.rest.model.newest.ProceedApply;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

public interface CardService {

    @GET("api/banks")
    Call<ArrayList<Bank>> getAllBank(@Header("x-zumo-auth") String authorization);

    @GET("api/cards")
    Call<ArrayList<Availablecards>> getCardStatus(@Header("x-zumo-auth") String apikey);

    @POST("api/cards/{id}/add")
    Call<ResponseDefault> saveUserCard(@Header("x-zumo-auth") String token, @Path("id") String idCard);

    @GET("api/banks/{id}/cards")
    Call<ArrayList<Availablecards>> getBankCard(@Header("x-zumo-auth") String token, @Path("id") String idBank);

    @DELETE("api/cards/{id}/remove")
    Call<ResponseDefault> deleteUserCard(@Header("x-zumo-auth") String token, @Path("id") String idCard);

    @POST("api/notifications/register?")
    Call<String> notifiication(@Header("x-zumo-auth") String token, @Query("handle") String handle);

    @PUT("api/notifications/register/{id}")
    Call<ResponseDefault> putnotification(@Header("x-zumo-auth") String token, @Path("id") String id, @Body Notifications notif);

    @GET("api/cards/{id}")
    Call<CardDetail> getCardDetail(@Header("x-zumo-auth") String auth, @Path("id") String id);

    @GET("api/cards/suggested/all/{sortBy}")
    Call<ArrayList<Availablecards>> getMoreCards(@Header("x-zumo-auth") String auth, @Path("sortBy") String sortBy);

    @POST("api/creditcard/apply/{cardID}")
    Call<ResponseDefault> doApply(@Header("x-zumo-auth") String token, @Path("cardID") String cardID);

    @POST("api/creditcard/proceed/{applicationID}")
    Call<ResponseDefault> doProceed(@Header("x-zumo-auth") String token, @Path("applicationID") String applicationID, @Body ProceedApply proceedApply);
}
