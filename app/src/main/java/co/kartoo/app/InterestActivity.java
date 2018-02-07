package co.kartoo.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.ArrayList;

import co.kartoo.app.category.CategoryEvent;
import co.kartoo.app.interest.InterestAdapter;
import co.kartoo.app.interest.InterestInterface;
import co.kartoo.app.models.LoginPref_;
import co.kartoo.app.rest.MainService;
import co.kartoo.app.rest.PromoService;
import co.kartoo.app.rest.model.Category;
import co.kartoo.app.rest.model.ResponseDefault;
import co.kartoo.app.rest.model.newest.Interest;
import co.kartoo.app.rest.model.newest.ListCategory;
import co.kartoo.app.views.SpaceItemDecoration;
import de.greenrobot.event.EventBus;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

@EActivity(R.layout.activity_interest)
public class InterestActivity extends AppCompatActivity implements InterestInterface{

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    @ViewById
    RecyclerView mRVinterest;
    @ViewById
        Button mBtnDone;
    @ViewById
    ProgressBar progressBar;
    @Pref
    LoginPref_ loginPref;
    ArrayList<ListCategory> listInterest;
    ArrayList<String> interestList;
    ArrayList<String> storeInterest;
    InterestAdapter adapterInterest;
    ProgressDialog loadingDialog;

    String from, authorization;

    Boolean found;
    private ArrayList<Category> categories = new ArrayList<>();

    @AfterViews
    public void init() {

        Intent intent = getIntent();
        from = intent.getStringExtra("from");
        Log.e("TAG", "init: " + from);

        authorization = loginPref.token().get();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
        PromoService promoService = retrofit.create(PromoService.class);

        loadingDialog = new ProgressDialog(InterestActivity.this);
        loadingDialog.setMessage("Loading...");
        loadingDialog.setIndeterminate(true);
        loadingDialog.setCancelable(false);

        listInterest = new ArrayList<>();
        adapterInterest = new InterestAdapter(this, listInterest, promoService, authorization);
        interestList = new ArrayList<>();
        storeInterest = new ArrayList<>();

        //3 lines below, added by Eric
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) mRVinterest.getLayoutParams();
        marginLayoutParams.setMargins(0, 10, 0, 10);
        mRVinterest.setLayoutParams(marginLayoutParams);
        mRVinterest.setAdapter(adapterInterest);

        GridLayoutManager  gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setSpanCount(2);
        mRVinterest.setLayoutManager(gridLayoutManager);

        //mRVinterest.setLayoutManager(new GridLayoutManager(this, 2));
        mRVinterest.addItemDecoration(new SpaceItemDecoration(3));

        progressBar.setVisibility(View.VISIBLE);
        mRVinterest.setVisibility(View.GONE);

        ListCategory category = new ListCategory("0C2737C0-6F66-40C8-9B97-43346F481760", "", "Dining", "https://kartoostorage.blob.core.windows.net/images/x/category_dining.jpg");
        listInterest.add(category);
        category = new ListCategory("F97CFE3B-B292-4344-BCE9-8B7C8E984D23", "", "Electronics", "https://kartoostorage.blob.core.windows.net/images/x/category_electronics.jpg");
        listInterest.add(category);
        category = new ListCategory("B4DCF4FA-F40E-46B9-9870-F258F239B12A", "", "Entertainment & Hobbies", "https://kartoostorage.blob.core.windows.net/images/x/category_entertainment.jpg");
        listInterest.add(category);
        category = new ListCategory("4A39CAA9-8369-4DAC-BFAF-D146F8E3F573", "", "Fashion", "https://kartoostorage.blob.core.windows.net/images/x/category_fashion.jpg");
        listInterest.add(category);
        category = new ListCategory("F61E6E24-29F6-4D30-8613-AB5389227077", "", "Health & Beauty", "https://kartoostorage.blob.core.windows.net/images/x/category_health.jpg");
        listInterest.add(category);
        category = new ListCategory("4B730A87-014A-495E-9602-85606CD50F30", "", "Hotel", "https://kartoostorage.blob.core.windows.net/images/x/category_hotel.jpg");
        listInterest.add(category);
        category = new ListCategory("C777B230-B05A-473C-AD53-0103FD0BE4AD", "", "Nightlife", "https://kartoostorage.blob.core.windows.net/images/x/category_nightlife.jpg");
        listInterest.add(category);
        category = new ListCategory("F28E59-D235-4324-A290-480F8C99CAAC", "", "Online", "https://kartoostorage.blob.core.windows.net/images/x/category_online.jpg");
        listInterest.add(category);
        category = new ListCategory("39CD1D9E-12EE-4700-82A0-B1105905178F", "", "Shopping", "https://kartoostorage.blob.core.windows.net/images/x/category_shopping.jpg");
        listInterest.add(category);
        category = new ListCategory("DF34388D-E063-434A-9CC1-C36F8C0F0576", "", "Travel", "https://kartoostorage.blob.core.windows.net/images/x/category_travel.jpg");
        listInterest.add(category);
        category = new ListCategory("6CA1D26C-9013-4249-9414-72E3FDA076A1", "", "Installment", "https://kartoostorage.blob.core.windows.net/images/x/category_installment.jpg");
        listInterest.add(category);
        adapterInterest.notifyDataSetChanged();

        int a = listInterest.size();
        for (int i = 0; i < a; i++)
        {
            storeInterest.add(listInterest.get(i).getId());
        }

        Call<ArrayList<Interest>> getInterest = promoService.getInterest(loginPref.token().get());
        getInterest.enqueue(new Callback<ArrayList<Interest>>() {
            @Override
            public void onResponse(Response<ArrayList<Interest>> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    Log.e("TAG", "Interest" + response.code());
                    if (response.code() == 200) {

                        progressBar.setVisibility(View.GONE);
                        mRVinterest.setVisibility(View.VISIBLE);

                        int a = response.body().size();
                        for (int i = 0; i < a; i++) {
                            ArrayList<Interest> interestChoosen = response.body();
                            adapterInterest.setInterest(interestChoosen);
                            adapterInterest.notifyDataSetChanged();
                            interestList.add(response.body().get(i).getCategory().getId());
                            Log.e("TAG", "onResponse: " + response.body().get(i).getId());
                            Log.e("TAG", "interestList: " + interestList);

                        }
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });

    }

    private void setMargins (View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }



    @Override
    public void userItemClick(int pos) {
        found = false;
        if (interestList.size() !=0){
            for (int i = 0; i < interestList.size(); i++) {
                Log.e("TAG", "Value: "+"LOOP"+i);
                if (interestList.get(i).equals(listInterest.get(pos).getId())){
                    interestList.remove(listInterest.get(pos).getId());
                    found = true;
                    break;
                }
            }
            if(!found){
                interestList.add(listInterest.get(pos).getId());
            }
        }
        else if (interestList.size()==0){
            interestList.add(listInterest.get(pos).getId());
        }
    }

    public void doPost(){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
        final MainService service = retrofit.create(MainService.class);

        Call<ResponseDefault> postInterest = service.doInterest(loginPref.token().get(), interestList);
        postInterest.enqueue(new Callback<ResponseDefault>() {
            @Override
            public void onResponse(Response<ResponseDefault> response, Retrofit retrofit) {

                Log.e("TAG", "onResponse: " + response.code());
                if (response.code() == 200) {
                    interestList.clear();
                    if (from.equals("MainActivity")){
                        Intent intent = new Intent(InterestActivity.this, MainActivity_.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                        Toast.makeText(getApplicationContext(), "Change this later in Edit Profile", Toast.LENGTH_LONG).show();
                    }
                    else if(from.equals("EditProfile")){
                        finish();
                    }

                    loginPref.interest().put(true);
                    if (loadingDialog.isShowing())
                        loadingDialog.dismiss();

                }
                if (response.code() == 401) {
                    Toast.makeText(getApplicationContext(), "Failed to save your interest", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Throwable t) {
                if (loadingDialog.isShowing())
                    loadingDialog.dismiss();

                Toast.makeText(getApplicationContext(), "Failed to save your interest", Toast.LENGTH_LONG).show();

            }
        });

    }


    @Click(R.id.mBtnDone)
    public void mBtnDoneClick() {
        Log.e("TAG", "mBtnDoneClick: "+interestList );
        loadingDialog.show();
        doPost();
    }

    @Override
    public void onStart() {
        super.onStart();
        listInterest.clear();
        EventBus.getDefault().registerSticky(this);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    public void onEvent(CategoryEvent event) {
        listInterest.clear();
        listInterest.addAll(event.getListPromo());
        adapterInterest.notifyDataSetChanged();
    }

}
