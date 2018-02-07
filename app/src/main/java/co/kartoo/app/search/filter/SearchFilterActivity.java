package co.kartoo.app.search.filter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.ArrayList;

import co.kartoo.app.R;
import co.kartoo.app.models.LoginPref_;
import co.kartoo.app.rest.PromoService;
import co.kartoo.app.rest.model.newest.FilterCategories;
import co.kartoo.app.rest.model.newest.SearchPromotionHeader;
import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

@EActivity(R.layout.activity_search_filter)
public class    SearchFilterActivity extends AppCompatActivity implements FilterCategoriesInterface{

    @ViewById
    Toolbar mToolbar;
    @ViewById
    TextView mTVtitle;
    @ViewById
    Switch mTBmyCards;
    @ViewById
    RecyclerView mRVcategories;
    @ViewById
    RadioGroup radioGroup;
    @ViewById
    RelativeLayout mRLselectCredit, mRLselectDebit, mRLselectMyCard;
    @ViewById
    CheckBox checkboxCredit, checkboxDebit, checkboxMyCard, checkboxDining, checkboxElectronics, checkboxEnthobby, checkboxFashion, checkboxHealthbea, checkboxHotel, checkboxNightlife, checkboxOnline, checkboxShopping, checkboxTravel, checkboxInstallment;
    @ViewById
    Button mBtnApply, mTVreset;
    @ViewById
    RadioButton radioDistance, radioAlphabet, radioEnding, radioPopular;

    @Pref
    LoginPref_ pref;

    Retrofit retrofit;
    ArrayList<FilterCategories> listFilterCategories;
    FilterCategoriesAdapter adapterFilterCategories;
    ArrayList cardType;
    ArrayList<String> categoryList;
    String sort;
    Boolean myCards;
    PromoService filterCallService;
    Call<SearchPromotionHeader> getSearchResult;

    @AfterViews
    public void init() {
        overridePendingTransition(R.anim.slide_in_up, R.anim.stay);

        myCards = false;
        sort = "";
//        listFilterCategories = new ArrayList<>();
//        adapterFilterCategories = new FilterCategoriesAdapter(this, listFilterCategories);
//        mRVcategories.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//
//        mRVcategories.setAdapter(adapterFilterCategories);
//        mRVcategories.setNestedScrollingEnabled(false);
//        mRVcategories.setFocusable(false);

//        FilterCategories category = new FilterCategories(ic_dining, "Dining");
//        listFilterCategories.add(category);
//        category = new FilterCategories(R.drawable.ic_electronic, "Electronics");
//        listFilterCategories.add(category);
//        category = new FilterCategories(R.drawable.ic_enthobby, "Entertainment & Hobbies");
//        listFilterCategories.add(category);
//        category = new FilterCategories(R.drawable.ic_fashion, "Fashion");
//        listFilterCategories.add(category);
//        category = new FilterCategories(R.drawable.ic_healthbea, "Health & Beauty");
//        listFilterCategories.add(category);
//        category = new FilterCategories(R.drawable.ic_hotel, "Hotel");
//        listFilterCategories.add(category);
//        category = new FilterCategories(R.drawable.ic_nightlife, "Nightlife");
//        listFilterCategories.add(category);
//        category = new FilterCategories(R.drawable.ic_online, "Online");
//        listFilterCategories.add(category);
//        category = new FilterCategories(R.drawable.ic_shopping, "Shopping");
//        listFilterCategories.add(category);
//        category = new FilterCategories(R.drawable.ic_travel, "Travel");
//        listFilterCategories.add(category);
//        category = new FilterCategories(R.drawable.ic_installment, "Installment");
//        listFilterCategories.add(category);
//        adapterFilterCategories.notifyDataSetChanged();

        retrofit = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
        filterCallService = retrofit.create(PromoService.class);
        String authorization = pref.token().get();

        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
        setUpNavDrawer();
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        cardType = new ArrayList();
        categoryList = new ArrayList();
    }

    public void onRadioButtonClicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.radioAlphabet:
                if (checked)
                    break;
            case R.id.radioDistance:
                if (checked)
                    break;
            case R.id.radioEnding:
                if (checked)
                    break;
            case R.id.radioPopular:
                if (checked)
                    break;
        }
    }



    @Override
    public void userItemClick(int pos) {
        String label = listFilterCategories.get(pos).getLabel();
        Log.e("TAG", "userItemClick: "+label);
        if (categoryList.size() == 0){
            categoryList.add(label);
        }else {
            if(categoryList.contains(label)){
                categoryList.remove(label);
            }else {
                categoryList.contains(label);
            }
        }
    }
    @Click(R.id.mRVcategoriesDining)
    public void mRVcategoriesDiningClick(){
        if (checkboxDining.isChecked()){
            checkboxDining.setChecked(false);
        }else {
            checkboxDining.setChecked(true);
        }
    }
    @Click(R.id.mRVcategoriesElectronics)
    public void mRVcategoriesElectronicsClick(){
        if (checkboxElectronics.isChecked()){
            checkboxElectronics.setChecked(false);
        }else {
            checkboxElectronics.setChecked(true);
        }
    }
    @Click(R.id.mRVcategoriesEnthobby)
    public void mRVcategoriesEnthobbyClick(){
        if (checkboxEnthobby.isChecked()){
            checkboxEnthobby.setChecked(false);
        }else {
            checkboxEnthobby.setChecked(true);
        }
    }
    @Click(R.id.mRVcategoriesFashion)
    public void mRVcategoriesFashionClick(){
        if (checkboxFashion.isChecked()){
            checkboxFashion.setChecked(false);
        }else {
            checkboxFashion.setChecked(true);
        }
    }
    @Click(R.id.mRVcategoriesHealthbea)
    public void mRVcategoriesHealthbeaClick(){
        if (checkboxHealthbea.isChecked()){
            checkboxHealthbea.setChecked(false);
        }else {
            checkboxHealthbea.setChecked(true);
        }
    }
    @Click(R.id.mRVcategoriesHotel)
    public void mRVcategoriesHotelClick(){
        if (checkboxHotel.isChecked()){
            checkboxHotel.setChecked(false);
        }else {
            checkboxHotel.setChecked(true);
        }
    }
    @Click(R.id.mRVcategoriesNightlife)
    public void mRVcategoriesNightlifeClick(){
        if (checkboxNightlife.isChecked()){
            checkboxNightlife.setChecked(false);
        }else {
            checkboxNightlife.setChecked(true);
        }
    }
    @Click(R.id.mRVcategoriesOnline)
    public void mRVcategoriesOnlineClick(){
        if (checkboxOnline.isChecked()){
            checkboxOnline.setChecked(false);
        }else {
            checkboxOnline.setChecked(true);
        }
    }
    @Click(R.id.mRVcategoriesShopping)
    public void mRVcategoriesShoppingClick(){
        if (checkboxShopping.isChecked()){
            checkboxShopping.setChecked(false);
        }else {
            checkboxShopping.setChecked(true);
        }
    }
    @Click(R.id.mRVcategoriesTravel)
    public void mRVcategoriesTravelClick(){
        if (checkboxTravel.isChecked()){
            checkboxTravel.setChecked(false);
        }else {
            checkboxTravel.setChecked(true);
        }
    }
    @Click(R.id.mRVcategoriesInstallment)
    public void mRVcategoriesInstallmentClick(){
        if (checkboxInstallment.isChecked()){
            checkboxInstallment.setChecked(false);
        }else {
            checkboxInstallment.setChecked(true);
        }
    }


    @Click(R.id.mRLselectCredit)
    public void mRLselectCreditClick () {
        if (checkboxCredit.isChecked()){
            checkboxCredit.setChecked(false);
        }else {
            checkboxCredit.setChecked(true);
        }
    }
    @Click(R.id.mRLselectDebit)
    public void mRLselectDebitClick () {
        if (checkboxDebit.isChecked()){
            checkboxDebit.setChecked(false);
        }else {
            checkboxDebit.setChecked(true);
        }
    }
    @Click(R.id.mRLselectMyCard)
    public void mRLselectMyCardClick () {
        if (checkboxMyCard.isChecked()){
            checkboxMyCard.setChecked(false);
        }else {
            checkboxMyCard.setChecked(true);
        }
    }

    @Click(R.id.mTVreset)
    public void mTVresetClick() {
        //Sort by
        radioAlphabet.setChecked(false);
        radioDistance.setChecked(false);
        radioEnding.setChecked(false);
        radioPopular.setChecked(false);

        //Card Type
        checkboxCredit.setChecked(false);
        checkboxDebit.setChecked(false);
        checkboxMyCard.setChecked(false);

        //Category
        checkboxDining.setChecked(false);
        checkboxElectronics.setChecked(false);
        checkboxEnthobby.setChecked(false);
        checkboxFashion.setChecked(false);
        checkboxHealthbea.setChecked(false);
        checkboxHotel.setChecked(false);
        checkboxNightlife.setChecked(false);
        checkboxOnline.setChecked(false);
        checkboxShopping.setChecked(false);
        checkboxTravel.setChecked(false);
        checkboxInstallment.setChecked(false);
    }

    @Click(R.id.mBtnApply)
    public void mBtnApplyClick () {

        int selectedId = radioGroup.getCheckedRadioButtonId();
        String type = "";
        if (selectedId == radioAlphabet.getId()){
            sort = "alphabet";
        }else if (selectedId == radioDistance.getId()){
            sort = "distance";
        }else if (selectedId == radioEnding.getId()){
            sort = "endingsoon";
        }else if (selectedId == radioPopular.getId()){
            sort = "popular";
        }

        if (checkboxMyCard.isChecked()){
            myCards = true;
        }

        if (checkboxCredit.isChecked()){
            cardType.add("Credit");
        }
        if (checkboxDebit.isChecked()){
            cardType.add("Debit");
        }

        if (checkboxDining.isChecked()){
            categoryList.add("Dining");
        }
        if (checkboxElectronics.isChecked()){
            categoryList.add("Electronics");
        }
        if (checkboxEnthobby.isChecked()){
            categoryList.add("Entertainment & Hobbies");
        }
        if (checkboxFashion.isChecked()){
            categoryList.add("Fashion");
        }
        if (checkboxHealthbea.isChecked()){
            categoryList.add("Health & Beauty");
        }
        if (checkboxHotel.isChecked()){
            categoryList.add("Hotel");
        }
        if (checkboxNightlife.isChecked()){
            categoryList.add("Nightlife");
        }
        if (checkboxOnline.isChecked()){
            categoryList.add("Online");
        }
        if (checkboxShopping.isChecked()){
            categoryList.add("Shopping");
        }
        if (checkboxTravel.isChecked()){
            categoryList.add("Travel");
        }
        if (checkboxInstallment.isChecked()){
            categoryList.add("Installment");
        }

        /*SharedPreferences prefs = getSharedPreferences("filter", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();

        Gson gson = new Gson();
        String setCardType = gson.toJson(cardType);
        String setCategoryList = gson.toJson(categoryList);

        edit.putString("sort", sort);
        edit.putBoolean("myCards", myCards);
        edit.putString("categoryList", setCategoryList);
        edit.putString("cardType", setCardType);
        edit.apply();*/

//        final FilterSearch passParameter = new FilterSearch();
        //passParameter.setMyCard(myCards);
        //passParameter.setSortBy(sort);
        //passParameter.setCategoryTypes(categoryList);

        Intent returnIntent = new Intent();
        returnIntent.putExtra("isMyCard", myCards + "");
        returnIntent.putExtra("sortBy", sort);
        returnIntent.putStringArrayListExtra("cardTypes", cardType);
        returnIntent.putStringArrayListExtra("categoryTypes",categoryList);
        setResult(Activity.RESULT_OK, returnIntent);

        finish();

        /* Should be on search result activity */
//        getSearchResult = filterCallService.doFilterSearch(pref.token().get(), 1, passParameter);
//        getSearchResult.enqueue(new Callback<SearchPromotionHeader>() {
//
//            @Override
//            public void onResponse(Response<SearchPromotionHeader> response, Retrofit retrofit) {
//                Log.e("TAG", "filterCallService -> onResponse: " + response.code());
//                if (response.code() == 200)
//                {
//
//                }
//                else
//                {
//                    if (response.code() == 401) {
//                        Toast.makeText(SearchFilterActivity.this, "Error hitting search result.", Toast.LENGTH_LONG).show();
//                    }
//                }
//
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//
//            }
//        });
    }

    private void setUpNavDrawer() {
        if (mToolbar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mToolbar.setNavigationIcon(R.drawable.ic_back_orange);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    overridePendingTransition(R.anim.stay, R.anim.slide_out_down);
                }
            });
        }
    }
    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.stay, R.anim.slide_out_down);
    }
}
