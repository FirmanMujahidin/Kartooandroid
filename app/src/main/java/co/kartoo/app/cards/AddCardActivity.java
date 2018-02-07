package co.kartoo.app.cards;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
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
import co.kartoo.app.cards.model.BankSpinnerItem;
import co.kartoo.app.cards.model.CardSpinnerItem;
import co.kartoo.app.models.LoginPref_;
import co.kartoo.app.rest.CardService;
import co.kartoo.app.rest.model.Bank;
import co.kartoo.app.rest.model.ResponseDefault;
import co.kartoo.app.rest.model.newest.Availablecards;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;


@EActivity(R.layout.activity_add_card)
public class AddCardActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,RadioGroup.OnCheckedChangeListener, DialogInterface.OnCancelListener{

    @ViewById
    Toolbar mToolbar;
    @ViewById
    TextView mTVtitle;

    @ViewById
    RadioGroup mRGcardType;
    @ViewById
    Spinner mSbank;
    @ViewById
    Spinner mScardEdition;
    @ViewById
    Button mBtnAdd;
    @ViewById
    ImageView mIVpreview;
    @ViewById
    TextView textView20;
    @ViewById
    RadioButton mRBdebit;
    @ViewById
    RadioButton mRBcredit;
    @ViewById
    TextView textView21;
    @ViewById
    TextView textView22;
    @ViewById
    TextView mTVskip;

    @Pref
    LoginPref_ pref;

    Retrofit retrofit;
    CardService service;
    Map<String, ArrayList<Availablecards>> map;
    ArrayList<Bank> listOfBank;
    ArrayList<BankSpinnerItem> bankSpinnerItems;
    ArrayList<CardSpinnerItem> cardSpinnerItems;
    ArrayAdapter<CardSpinnerItem> cardAdapter;

    ArrayList debitCards = new ArrayList();
    ArrayList creditCards = new ArrayList();

    Call<ResponseDefault> saveUserCardCall;
    ProgressDialog dialog;
    ArrayList<Availablecards> listMyCards;
    String type;
    int n;

    ArrayList imageCard;

    String idBank, cardname, cardid;

    @AfterViews
    void init() {
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
        setUpNavDrawer();
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mixpanelmodal();

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        listOfBank = (ArrayList<Bank>) bundle.getSerializable("listOfBank");
        listMyCards = (ArrayList<Availablecards>) bundle.getSerializable("listMyCards");


        retrofit = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build();
        service = retrofit.create(CardService.class);
        String authorization = pref.token().get();
        bankSpinnerItems = new ArrayList<>();
        Log.e("TAG", "RetrofitAddCard" + retrofit);

        bankSpinnerItems.add(new BankSpinnerItem("-1","Select Bank"));

        cardSpinnerItems = new ArrayList<>();
        cardSpinnerItems.add(new CardSpinnerItem("-1", "Select Card Edition"));
        map = new HashMap<String, ArrayList<Availablecards>>();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Adding cards...");
        dialog.setIndeterminate(true);
        dialog.setOnCancelListener(this);

        int selectedId = mRGcardType.getCheckedRadioButtonId();

        if (selectedId == R.id.mRBcredit) {
            type="Credit";
        } else {
            type="Debit";
        }

        for (int i = 0; i < listOfBank.size(); i++) {

            Bank bank = listOfBank.get(i);
            Log.e("listBank", bank.getId());
            final String bankId = bank.getId();

            Call<ArrayList<Availablecards>> listOfCardCall = service.getBankCard(authorization, bankId);
            listOfCardCall.enqueue(new Callback<ArrayList<Availablecards>>() {
                @Override
                public void onResponse(Response<ArrayList<Availablecards>> response, Retrofit retrofit) {
                    if (response.isSuccess()) {
                        if (response.code() == 200) {
                            Log.e("getBankCardInfo", bankId + " " + response.body().toString());
                            map.put(bankId, response.body());
                        }
                    } else {
                        if (response.code() == 401) {
                            Toast.makeText(AddCardActivity.this, "Sorry, your session has expired. Please login again", Toast.LENGTH_LONG).show();
                            //TODO do logout here
                        }
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Toast.makeText(AddCardActivity.this, "We cannot process your request now, please try again later", Toast.LENGTH_LONG).show();
                }
            });
            bankSpinnerItems.add(new BankSpinnerItem(bank.getId(), bank.getName()));
        }

        ArrayAdapter<BankSpinnerItem> bankAdapter = new ArrayAdapter<BankSpinnerItem>(this,android.R.layout.simple_spinner_dropdown_item, bankSpinnerItems);
        mSbank.setAdapter(bankAdapter);
        mSbank.setOnItemSelectedListener(this);
        mRGcardType.setOnCheckedChangeListener(this);

        mScardEdition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    ArrayList<Availablecards> listOfCards = map.get(idBank);

                    Log.e("TAG", "CardEditionID: "+ listOfCards.get(position-1).getId());
                    Log.e("TAG", "getCard_Type: "+ listOfCards.get(position-1).getCard_Type());
                    Log.e("TAG", "Type: "+ type);
                    Log.e("TAG", "position: "+ listOfCards.size());

                    String image = imageCard.get(position-1).toString();

                    Picasso.with(getApplicationContext()).load(image)
                            .resize(400,300).centerInside()
                            .placeholder(R.color.placeholder)
                            .into(mIVpreview);

                    /*
                    if (type.equals("Debit")){
                        Picasso.with(getActivity()).load(listOfCards.get((position-1)+n).getUrl_img())
                                .resize(400,300).centerInside()
                                .placeholder(R.color.placeholder)
                                .into(mIVpreview);
                        //n=0;
                    }
                    else if (type.equals("Credit")){
                        Picasso.with(getActivity()).load(listOfCards.get(position-1).getUrl_img())
                                .resize(400,300).centerInside()
                                .placeholder(R.color.placeholder)
                                .into(mIVpreview);
                        //n=0;

                    }
*/

                    //listOfCards.get(position-1).getCard_Type()

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    @Click(R.id.mBtnAdd)
    public void mBtnAddClick() {
        int selectedId = mRGcardType.getCheckedRadioButtonId();
        String type = "";
        if (selectedId == R.id.mRBcredit) {
            type="Credit";
        } else {
            type="Debit";
        }

        final CardSpinnerItem selectedCard = ((CardSpinnerItem) mScardEdition.getSelectedItem());
        BankSpinnerItem selectedBank = ((BankSpinnerItem) mSbank.getSelectedItem());

        if (selectedBank.getId().equals("-1")) {
            Toast.makeText(AddCardActivity.this, "Please select your bank card", Toast.LENGTH_LONG).show();
            return;
        }
        if (selectedCard.getId().equals("-1")) {
            Toast.makeText(AddCardActivity.this, "Please select your card edition", Toast.LENGTH_LONG).show();
            return;
        }
        for (int i = 0; i < listMyCards.size(); i++) {
            if (selectedCard.getId().equals(listMyCards.get(i).getId())){
                Toast.makeText(AddCardActivity.this, "You had already registered this card", Toast.LENGTH_LONG).show();
                return;
            }
        }
        if (!dialog.isShowing())
            dialog.show();

        saveUserCardCall = service.saveUserCard(pref.token().get() ,selectedCard.getId());
        saveUserCardCall.enqueue(new Callback<ResponseDefault>() {
            @Override
            public void onResponse(Response<ResponseDefault> response, Retrofit retrofit) {
                Log.e("TAG", "onResponse: "+response.code());
                if (dialog.isShowing())
                    dialog.dismiss();
                if (response.isSuccess()) {
                    if (response.code() == 200) {

                        n=0;

                        //final FragmentManager fragmentManager = getSupportFragmentManager();
                        //final Fragment fragment = new FragmentCards_();

                        cardname = selectedCard.getName();
                        cardid = selectedCard.getId();

                        pref.addcard().put(true);

                        Toast.makeText(AddCardActivity.this, "Congratulations! Your card has been added", Toast.LENGTH_LONG).show();
                        //Intent intent = new Intent(getActivity(), FragmentCards_.class);
                        //fragmentManager.beginTransaction().replace(R.id.mFLcontainer, fragment).commit();
                        //startActivity(intent);

                        //Bundle bundle = new Bundle();

                        Intent intent = new Intent(AddCardActivity.this, MyCardsActivity_.class);

                        //bundle.putString("selectedPage", "my_cards");
                        //intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                        mixpanel();
                    }
                } else {
                    if (response.code() == 401) {
                        Toast.makeText(AddCardActivity.this, "Your login session has expired. Please login again", Toast.LENGTH_LONG).show();
                        //TODO relogin here
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (dialog.isShowing())
                    dialog.dismiss();
            }
        });
    }

    public void mixpanelmodal(){
        String projectToken = getString(R.string.mixpanel_token);
        MixpanelAPI mixpanel = MixpanelAPI.getInstance(this, projectToken);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTimeStamp = dateFormat.format(new Date());
        try {
            JSONObject props = new JSONObject();
            props.put("TimeStamp", currentTimeStamp);
            props.put("email", pref.email().get());
            mixpanel.track("Add a Card modal", props);
        } catch (JSONException e) {
            Log.e("MYAPP", "Unable to add properties to JSONObject", e);
        }
    }

    public void mixpanel(){
        String projectToken = getString(R.string.mixpanel_token);
        MixpanelAPI mixpanel = MixpanelAPI.getInstance(this, projectToken);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTimeStamp = dateFormat.format(new Date());
        try {
            JSONObject props = new JSONObject();
            props.put("TimeStamp", currentTimeStamp);
            props.put("email", pref.email().get());
            props.put("card name", cardname);
            props.put("card id", cardid);
            mixpanel.track("Successfully added a card", props);

        } catch (JSONException e) {
            Log.e("MYAPP", "Unable to add properties to JSONObject", e);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        idBank = bankSpinnerItems.get(position).getId();
        getCard();
        Log.e("asdf", "success get card");
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        cardSpinnerItems.clear();
        cardAdapter = new ArrayAdapter<CardSpinnerItem>(this, android.R.layout.simple_spinner_dropdown_item, cardSpinnerItems);
        mScardEdition.setAdapter(cardAdapter);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        getCard();
    }

    public void getCard() {
        if (idBank != null && !idBank.equals("-1")) {
            Log.e("asdf", "sadf");
            ArrayList<Availablecards> listOfCards = map.get(idBank);

            String type = "";
            int selectedId = mRGcardType.getCheckedRadioButtonId();
            if (selectedId == R.id.mRBcredit) {
                type="Credit";
            } else {
                type="Debit";
            }
            cardSpinnerItems.clear();
            cardSpinnerItems.add(new CardSpinnerItem("-1", "Select Card Edition"));

            n = 0;

            imageCard = new ArrayList();

            if(listOfCards!=null){
                for (int i = 0; i < listOfCards.size(); i++) {
                    if (listOfCards.get(i).getCard_Type().equals(type)) {
                        cardSpinnerItems.add(new CardSpinnerItem(listOfCards.get(i).getId(), listOfCards.get(i).getCard_Edition()));
                        Log.e("TAG", "getCard"+i+" :" +listOfCards.get(i).getUrl_img());
                        imageCard.add(listOfCards.get(i).getUrl_img());
                    }
                }
                for (int j = 0; j < listOfCards.size(); j++) {
                    if (listOfCards.get(j).getCard_Type().equals(type)) {
                        n=j;
                        break;
                    }
                    Log.e("TAG", "getCard JJJ"+j+" :" +listOfCards.get(j).getUrl_img());
                }

                Log.e("TAG", "Jumlah Credit: "+n);

            }

            cardAdapter = new ArrayAdapter<CardSpinnerItem>(this, android.R.layout.simple_spinner_dropdown_item, cardSpinnerItems);
            mScardEdition.setAdapter(cardAdapter);
        }
    }


    @Override
    public void onCancel(DialogInterface dialog) {
        dialog.dismiss();
        //saveUserCardCall.cancel();
    }

    private void setUpNavDrawer() {
        if (mToolbar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mToolbar.setNavigationIcon(R.drawable.ic_back_orange);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }
}
