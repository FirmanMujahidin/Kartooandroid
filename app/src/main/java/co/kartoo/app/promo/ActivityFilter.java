package co.kartoo.app.promo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.ArrayList;

import co.kartoo.app.R;
import co.kartoo.app.models.LoginPref_;
import co.kartoo.app.promo.view_model.SimpleSpinnerItem;
import co.kartoo.app.rest.CardService;
import co.kartoo.app.rest.PromoService;
import co.kartoo.app.rest.model.Bank;
import co.kartoo.app.rest.model.Category;
import co.kartoo.app.rest.model.CategoryPromo;
import co.kartoo.app.rest.model.Outlet;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by MartinOenang on 10/30/2015.
 */
@EActivity(R.layout.activity_filter)
public class ActivityFilter extends AppCompatActivity {
    @ViewById
    Spinner mSpromoCategory;
    @ViewById
    Spinner mSoutletCategory;
    @ViewById
    Spinner mSbank;
    @ViewById
    Button mBtnFilter;

    ArrayList<SimpleSpinnerItem> promoCategorySpinnerItems;
    ArrayList<SimpleSpinnerItem> outletCategorySpinnerItems;
    ArrayList<SimpleSpinnerItem> bankSpinnerItems;
    ArrayAdapter<SimpleSpinnerItem> promoCategoryAdapter;
    ArrayAdapter<SimpleSpinnerItem> outletCategoryAdapter;
    ArrayAdapter<SimpleSpinnerItem> bankAdapter;
    Retrofit retrofit;
    PromoService promoService;
    String token;
    @Pref
    LoginPref_ loginPref;
    @AfterViews
    void init() {
        retrofit = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
        promoService = retrofit.create(PromoService.class);
        token = "Bearer " + loginPref.token().get();
        outletCategorySpinnerItems = new ArrayList<>();
        outletCategoryAdapter = new ArrayAdapter<SimpleSpinnerItem>(this, android.R.layout.simple_spinner_dropdown_item, outletCategorySpinnerItems);

        Call<ArrayList<Category>> getOutletCategoryCall = promoService.getAllCategory(token);
        getOutletCategoryCall.enqueue(new Callback<ArrayList<Category>>() {
            @Override
            public void onResponse(Response<ArrayList<Category>> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    ArrayList<Category> listCategory = response.body();
                    for (int i = 0; i < listCategory.size(); i++) {
                        Log.e("adsf", "promo " + listCategory.get(i).getId());
                        outletCategorySpinnerItems.add(new SimpleSpinnerItem(listCategory.get(i).getId(), listCategory.get(i).getPromotion()));
                    }
                    outletCategoryAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });

        promoCategorySpinnerItems = new ArrayList<>();

        Call<ArrayList<CategoryPromo>> getPromoCategoryCall = promoService.getAllPromoCategory(token);
        getPromoCategoryCall.enqueue(new Callback<ArrayList<CategoryPromo>>() {
            @Override
            public void onResponse(Response<ArrayList<CategoryPromo>> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    ArrayList<CategoryPromo> listCategory = response.body();
                    for (int i = 0; i < listCategory.size(); i++) {
                        Log.e("adsf", "promo " + listCategory.get(i).getId());
                        promoCategorySpinnerItems.add(new SimpleSpinnerItem(listCategory.get(i).getId(), listCategory.get(i).getName()));
                    }
                    promoCategoryAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
        promoCategoryAdapter = new ArrayAdapter<SimpleSpinnerItem>(this, android.R.layout.simple_spinner_dropdown_item, promoCategorySpinnerItems);

        bankSpinnerItems = new ArrayList<>();
        bankAdapter = new ArrayAdapter<SimpleSpinnerItem>(this, android.R.layout.simple_spinner_dropdown_item, bankSpinnerItems);
        CardService cardService = retrofit.create(CardService.class);
        Call<ArrayList<Bank>> listBankCall = cardService.getAllBank(token);
        listBankCall.enqueue(new Callback<ArrayList<Bank>>() {
            @Override
            public void onResponse(Response<ArrayList<Bank>> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    ArrayList<Bank> listOfBanks = response.body();
                    for (int i = 0; i < listOfBanks.size(); i++) {
                        bankSpinnerItems.add(new SimpleSpinnerItem(listOfBanks.get(i).getId(),listOfBanks.get(i).getName()));
                    }
                    bankAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
        mSpromoCategory.setAdapter(promoCategoryAdapter);
        mSoutletCategory.setAdapter(outletCategoryAdapter);
        mSbank.setAdapter(bankAdapter);

    }

    @Click(R.id.mBtnFilter)
    public void mBtnFilterClick() {
        final String idPromoCategory = ((SimpleSpinnerItem) mSpromoCategory.getSelectedItem()).getId();
        String idOutletCategory = ((SimpleSpinnerItem) mSoutletCategory.getSelectedItem()).getId();
        final String idBank = ((SimpleSpinnerItem) mSbank.getSelectedItem()).getId();
        Log.e("adsf", idPromoCategory + " " + idOutletCategory + " " + idBank);

        Call<ArrayList<Outlet>> getPromoCall = promoService.filterPromo(token, idPromoCategory, idOutletCategory, idBank);
        getPromoCall.enqueue(new Callback<ArrayList<Outlet>>() {
            @Override
            public void onResponse(Response<ArrayList<Outlet>> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    if (response.code() == 200) {
                        Intent retval = new Intent();
                        Bundle bundle = new Bundle();
                        Log.e("asdf",response.body().size()+"");
                        bundle.putSerializable("result", response.body());
                        retval.putExtras(bundle);
                        setResult(RESULT_OK, retval);
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
//        Call<CategoryOutlet> getPromoCall = promoService.getPromoByCategory(token, idOutletCategory);
//        getPromoCall.enqueue(new Callback<CategoryOutlet>() {
//            @Override
//            public void onResponse(Response<CategoryOutlet> response, Retrofit retrofit) {
//                Log.e("asdf", idPromoCategory+" "+ idBank);
//                if (response.isSuccess()) {
//                    ArrayList<Outlet> outletResponse = response.body().getOutlets();
//                    for (Outlet outlet : outletResponse){
//                        ArrayList<Promo> listOutletPromo = outlet.getPromotions();
//                        for (Promo promo : listOutletPromo) {
//                            ArrayList<Card> listOfCards = promo.getCards();
//                            for (Card card : listOfCards) {
//                                if (!card.getBank().getId().equals(idBank)) {
//                                    listOfCards.remove(card);
//                                }
//                            }
//                            if (listOfCards.size() == 0) {
//                                listOutletPromo.remove(promo);
//                            }
//                        }
//                        if (listOutletPromo.size() == 0) {
//                            outletResponse.remove(outlet);
//                        }
//                    }
//                    Intent retval = new Intent();
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("result", outletResponse);
//                    retval.putExtras(bundle);
//                    setResult(RESULT_OK, retval);
//                    finish();
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//
//            }
//        });
    }
}
