package co.kartoo.app.cards;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import co.kartoo.app.R;
import co.kartoo.app.events.DeleteCardEvent;
import co.kartoo.app.landing.LandingActivity_;
import co.kartoo.app.models.LoginPref_;
import co.kartoo.app.rest.CardService;
import co.kartoo.app.rest.model.Bank;
import co.kartoo.app.rest.model.newest.Availablecards;
import co.kartoo.app.views.MeasuredStaggeredGridLayoutManager;
import de.greenrobot.event.EventBus;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

@EFragment(R.layout.fragment_my_cards)
public class FragmentCards extends Fragment{
    @ViewById
    RecyclerView mRVmyCards;
    @ViewById
    RelativeLayout mIVnoCard;
    @ViewById
    ProgressBar progressBar;
    @ViewById
    RelativeLayout timeOut;
    @ViewById
    Button reload;

    @Pref
    LoginPref_ loginPref;
    ArrayList<Bank> listOfBank;
    Retrofit retrofit;
    CardService service;
    MeasuredStaggeredGridLayoutManager layoutManagerLatestPromo;
    Map<String, ArrayList<Availablecards>> map;
    ArrayList<Availablecards> listCard;
    CardsAdapter adapter;
    Bundle bundle;
    private Activity mActivity;
    private Context mContext;
    private FirebaseAnalytics mFirebaseAnalytics;

    @AfterViews
    public void init() {

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());

        mIVnoCard.setVisibility(View.GONE);
        listCard = new ArrayList<>();
        bundle = getArguments();

        adapter = new CardsAdapter(listCard,getActivity(), loginPref.token().get());
        progressBar.setVisibility(View.VISIBLE);
        timeOut.setVisibility(View.GONE);
        mRVmyCards.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRVmyCards.setAdapter(adapter);
        setHasOptionsMenu(true);
        retrofit = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
        service = retrofit.create(CardService.class);
        if (bundle != null) {
            Log.e("TAG", "init: "+"BUNDLE");
            progressBar.setVisibility(View.GONE);
            timeOut.setVisibility(View.GONE);

            listCard.addAll((ArrayList<Availablecards>) bundle.getSerializable("listCards"));
            adapter.notifyDataSetChanged();
        } else {
            reload();
        }

        //layoutManagerLatestPromo = new MeasuredStaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        //int spacingInPixels = 2;
        //mRVmyCards.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
        //mRVmyCards.setLayoutManager(layoutManagerLatestPromo);
        //mRVmyCards.setAdapter(adapter);
        mRVmyCards.setNestedScrollingEnabled(true);

        mixpanel();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }
/*
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }
*/
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (bundle == null) {
            MenuItem menuAdd = menu.add("Add");
            menuAdd.setIcon(R.drawable.ic_add_white_24px);
            menuAdd.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

            menuAdd.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    Log.e("TAG", "onMenuItemClick: "+"pluscard" );

                    //final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    //final Fragment fragment = new FragmentAddCard_();

                    map = new HashMap<String, ArrayList<Availablecards>>();

                    final String authorization = loginPref.token().get();
                    final Call<ArrayList<Bank>> listOfBankCall = service.getAllBank(authorization);
                    listOfBankCall.enqueue(new Callback<ArrayList<Bank>>() {
                        @Override
                        public void onResponse(Response<ArrayList<Bank>> response, Retrofit retrofit) {
                            if (response.isSuccess()) {
                                if (response.code() == 200) {
                                    Log.e("fragmentCards", "get bank success");
                                    listOfBank = response.body();
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("listOfBank", listOfBank);
                                    bundle.putSerializable("listMyCards", listCard);

                                    Log.e("fragmentCards", "listMyCards"+listCard.toString());
                                    Log.e("fragmentCards", "listMyCards"+listOfBank.toString());

                                    //FragmentActivity activity = (FragmentActivity) getActivity();

                                    Intent intent = new Intent(mActivity, AddCardActivity_.class);
                                    intent.putExtras(bundle);
                                    mActivity.startActivity(intent);

                                    //startActivity(intent);
                                    mActivity.getFragmentManager().popBackStack();
                                    mActivity.finish();

                                    //fragment.setArguments(bundle);
                                    //fragmentManager.beginTransaction().replace(R.id.mFLcontainer, fragment).commit();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Throwable t) {

                        }
                    });
                    return true;
                }
            });
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void reload(){
        progressBar.setVisibility(View.VISIBLE);
        timeOut.setVisibility(View.GONE);
        mRVmyCards.setVisibility(View.GONE);

        Call<ArrayList<Availablecards>> getMyCardsCall = service.getCardStatus(loginPref.token().get());
        getMyCardsCall.enqueue(new Callback<ArrayList<Availablecards>>() {
            @Override
            public void onResponse(Response<ArrayList<Availablecards>> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    if (response.code() == 200) {
                        progressBar.setVisibility(View.GONE);
                        timeOut.setVisibility(View.GONE);
                        mRVmyCards.setVisibility(View.VISIBLE);

                        ArrayList<Availablecards> responseListCard = response.body();
                        if (response.body().size()==0) {
                            mIVnoCard.setVisibility(View.VISIBLE);
                            loginPref.addcard().put(false);
                        }
                        for (int i = 0; i < responseListCard.size(); i++) {
                            listCard.add(responseListCard.get(i));
                        }
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    if (response.code() == 401) {
                        Toast.makeText(mActivity, "Your session has expired, please login again", Toast.LENGTH_LONG).show();
                        loginPref.name().remove();
                        loginPref.token().remove();
                        loginPref.urlPhoto().remove();
                        loginPref.level().remove();
                        loginPref.uid().remove();
                        loginPref.type().remove();
                        Intent intent2 = new Intent(mActivity, LandingActivity_.class);
                        startActivity(intent2);
                        mActivity.finish();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                progressBar.setVisibility(View.GONE);
                timeOut.setVisibility(View.VISIBLE);
                mRVmyCards.setVisibility(View.GONE);

                //Toast.makeText(getActivity(), "Sorry, we can't get your cards. Please try again later", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Click(R.id.timeOut)
    public void timeOutClick(){
        Log.e("TAG", "timeOutClick: "+"timeout" );
        reload();
    }

    @Click(R.id.add)
    public void addClick(){
        Log.e("TAG", "onMenuItemClick: "+"pluscard" );
        //final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        //final Fragment fragment = new FragmentAddCard_();
        map = new HashMap<String, ArrayList<Availablecards>>();
        final String authorization = loginPref.token().get();
        final Call<ArrayList<Bank>> listOfBankCall = service.getAllBank(authorization);
        listOfBankCall.enqueue(new Callback<ArrayList<Bank>>() {
            @Override
            public void onResponse(Response<ArrayList<Bank>> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    if (response.code() == 200) {
                        Log.e("fragmentCards", "get bank success");
                        listOfBank = response.body();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("listOfBank", listOfBank);
                        bundle.putSerializable("listMyCards", listCard);
                        Log.e("fragmentCards", "listMyCards"+listCard.toString());
                        Log.e("fragmentCards", "listMyCards"+listOfBank.toString());

                        Intent intent = new Intent(mActivity, AddCardActivity_.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        mActivity.finish();
                        mActivity.getFragmentManager().popBackStack();


                        //fragment.setArguments(bundle);
                        //fragmentManager.beginTransaction().replace(R.id.mFLcontainer, fragment).commit();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    public void mixpanel(){
        String projectToken = getString(R.string.mixpanel_token);
        MixpanelAPI mixpanel = MixpanelAPI.getInstance(getContext(), projectToken);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTimeStamp = dateFormat.format(new Date());
        try {
            JSONObject props = new JSONObject();
            props.put("TimeStamp", currentTimeStamp);
            props.put("email", loginPref.email().get());
            mixpanel.track("My Cards Page", props);
        } catch (JSONException e) {
        }
    }

    public void firebase(){
        Bundle params = new Bundle();
        params.putString("email", loginPref.email().get());
        mFirebaseAnalytics.logEvent("my_cards_page", params);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().registerSticky(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    public void onEvent(DeleteCardEvent event) {
        Log.e("eventDelete", "caught"+listCard.size());
        listCard.remove(event.getDeletedCard());
        //if (listCard.size() == 0) {
          //  mIVnoCard.setVisibility(View.VISIBLE);
        //}
        adapter.notifyDataSetChanged();
    }
}
