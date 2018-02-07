package co.kartoo.app.rest;

import java.util.ArrayList;

import co.kartoo.app.rest.model.Bank;
import co.kartoo.app.rest.model.Card;
import co.kartoo.app.rest.model.ResponseDefault;
import co.kartoo.app.rest.model.newest.AtmLocator;
import co.kartoo.app.rest.model.newest.Availablecards;
import co.kartoo.app.rest.model.newest.BankFeed;
import co.kartoo.app.rest.model.newest.BankPage;
import co.kartoo.app.rest.model.newest.FollowBank;
import co.kartoo.app.rest.model.newest.Notifications;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

public interface BankService {
    @GET("api/banks/{idBank}/feed/{page}")
    Call<BankFeed> getBankFeed(@Header("x-zumo-auth") String token, @Path("idBank") String id, @Path("page") String page);

    @GET("api/banks/{id}")
    Call<BankPage> getBankID(@Header("x-zumo-auth") String token, @Path("id") String id);

    @GET("api/banks/{id}/nearby/{latitude}/{longitude}?")
    Call<ArrayList<AtmLocator>> getAtmLocator(@Header("x-zumo-auth") String token, @Path("id") String id, @Path("latitude") String latitude, @Path("longitude") String longitude, @Query("atm") String atm, @Query("branch") String branch, @Query("cdm") String cdm);
}
