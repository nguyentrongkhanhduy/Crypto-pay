package com.example.crypto_pay_2.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.crypto_pay_2.Model.Price;
import com.example.crypto_pay_2.R;
import com.example.crypto_pay_2.Remote.CoinService;
import com.example.crypto_pay_2.Remote.RetrofitClient;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuyCoinActivity extends AppCompatActivity {

    CoinService mService;

    private static final String API_URL = "https://min-api.cryptocompare.com";
    private static final String BASE_URL = "https://cryptocompare.com";

    public static CoinService getCoinService(){
        return RetrofitClient.getClient(API_URL).create(CoinService.class);
    }

    ImageButton back;

    RadioButton buyCoin;
    RadioButton sellCoin;

    TextView convertFromCurrency;
    TextView convertToPrice;
    LinearLayout convertPriceToBuyAndSell;

    TextInputEditText usdBalance;
    LinearLayout realBalance;
    LinearLayout buyCoinUsd;
    TextInputEditText amountProvideUsd;
    AutoCompleteTextView wantCoin;
    TextInputEditText amountRecieveCoin;

    AutoCompleteTextView from;
    TextInputEditText coinBalance;
    LinearLayout coinBalanceFrame;
    LinearLayout sellCoinUsd;
    TextInputEditText amountProvideCoin;
    TextInputEditText amountRecieveUsd;

    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("user");
    String my_email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_buy_coin);

        initUI();

        createUI();

        getInfo();

    }

    private void initUI(){
        back = findViewById(R.id.back_icon);

        buyCoin = findViewById(R.id.buy_coin);
        sellCoin = findViewById(R.id.sell_coin);

        convertFromCurrency = findViewById(R.id.convert_from_currency);
        convertToPrice = findViewById(R.id.convert_to_price);
        convertPriceToBuyAndSell = findViewById(R.id.convert_price_to_buy_and_sell);

        usdBalance = findViewById(R.id.usd_balance);
        realBalance = findViewById(R.id.real_balance);
        buyCoinUsd = findViewById(R.id.buy_coin_usd);
        amountProvideUsd = findViewById(R.id.amount_provide_usd);
        wantCoin = findViewById(R.id.wantCoin);
        amountRecieveCoin = findViewById(R.id.amount_receive_coin);

        from = findViewById(R.id.from);
        coinBalance = findViewById(R.id.coin_balance);
        coinBalanceFrame = findViewById(R.id.coin_balance_frame);
        sellCoinUsd = findViewById(R.id.sell_coin_usd);
        amountProvideCoin = findViewById(R.id.amount_provide_coin);
        amountRecieveUsd = findViewById(R.id.amount_receive_usd);

        buyCoin.setChecked(true);
        coinBalanceFrame.setVisibility(View.GONE);
        sellCoinUsd.setVisibility(View.GONE);

        mService = getCoinService();
    }

    private void createUI(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BuyCoinActivity.super.onBackPressed();
            }
        });

        buyCoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                realBalance.setVisibility(View.VISIBLE);
                buyCoinUsd.setVisibility(View.VISIBLE);

                coinBalanceFrame.setVisibility(View.GONE);
                sellCoinUsd.setVisibility(View.GONE);

                convertPriceToBuyAndSell.setVisibility(View.GONE);
            }
        });

        sellCoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                coinBalanceFrame.setVisibility(View.VISIBLE);
                sellCoinUsd.setVisibility(View.VISIBLE);

                realBalance.setVisibility(View.GONE);
                buyCoinUsd.setVisibility(View.GONE);

                convertPriceToBuyAndSell.setVisibility(View.GONE);
            }
        });

        amountProvideUsd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (convertPriceToBuyAndSell.getVisibility() == View.GONE){
                    Toast.makeText(BuyCoinActivity.this, "Vui lòng chọn lại Coin.", Toast.LENGTH_SHORT).show();
                }
                else{
                    calculate(editable.toString());
                }
            }
        });

        amountProvideCoin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (convertPriceToBuyAndSell.getVisibility() == View.GONE){
                    Toast.makeText(BuyCoinActivity.this, "Vui lòng chọn lại Coin.", Toast.LENGTH_SHORT).show();
                }
                else{
                    calculateSell(editable.toString());
                }
            }
        });
    }

    private void getInfo() {
        ref.orderByChild("mail").equalTo(my_email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String,String> coinOwn = new HashMap<>();
                for (DataSnapshot child: snapshot.getChildren()){
                    coinOwn = (HashMap) child.child("own").getValue();
                }
                usdBalance.setText(String.valueOf(coinOwn.get("usd")));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        String[] item = {"BTC","ETH"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.dropdown,item);
        wantCoin.setAdapter(adapter);
        from.setAdapter(adapter);

        wantCoin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();

                if(item.equals("BTC")){
                    mService.calculate(item,"USD").enqueue(new Callback<Price>() {
                        @Override
                        public void onResponse(Call<Price> call, Response<Price> response) {
                            showConvert(response.body().getUSD(),item);
                        }

                        @Override
                        public void onFailure(Call<Price> call, Throwable t) {

                        }
                    });
                }
                else if(item.equals("ETH")){
                    mService.calculate(item,"USD").enqueue(new Callback<Price>() {
                        @Override
                        public void onResponse(Call<Price> call, Response<Price> response) {
                            showConvert(response.body().getUSD(),item);
                        }

                        @Override
                        public void onFailure(Call<Price> call, Throwable t) {

                        }
                    });
                }
            }
        });

        from.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();

                ref.orderByChild("mail").equalTo(my_email).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Map<String,String> coinOwn = new HashMap<>();
                        for (DataSnapshot child: snapshot.getChildren()){
                            coinOwn = (HashMap) child.child("own").getValue();
                        }
                        if(item.equals("BTC")) coinBalance.setText(String.valueOf(coinOwn.get("bitcoin")));
                        if(item.equals("ETH")) coinBalance.setText(String.valueOf(coinOwn.get("ethereum")));

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                if(item.equals("BTC")){
                    mService.calculate(item,"USD").enqueue(new Callback<Price>() {
                        @Override
                        public void onResponse(Call<Price> call, Response<Price> response) {
                            showConvertSell(response.body().getUSD(),item);
                        }

                        @Override
                        public void onFailure(Call<Price> call, Throwable t) {

                        }
                    });
                }
                else if(item.equals("ETH")){
                    mService.calculate(item,"USD").enqueue(new Callback<Price>() {
                        @Override
                        public void onResponse(Call<Price> call, Response<Price> response) {
                            showConvertSell(response.body().getUSD(),item);
                        }

                        @Override
                        public void onFailure(Call<Price> call, Throwable t) {

                        }
                    });
                }
            }
        });
    }

    private void showConvert(String value, String cur){
        convertPriceToBuyAndSell.setVisibility(View.VISIBLE);
        convertFromCurrency.setText(cur);
        Double before = Double.parseDouble(value);
        Double after = before * (1 + 1.5/100);
        convertToPrice.setText(String.valueOf(after));
        calculate(amountProvideUsd.getText().toString());
    }

    private void showConvertSell(String value, String cur){
        convertPriceToBuyAndSell.setVisibility(View.VISIBLE);
        convertFromCurrency.setText(cur);
        Double before = Double.parseDouble(value);
        Double after = before * (1 - 1.5/100);
        convertToPrice.setText(String.valueOf(after));
        calculateSell(amountProvideCoin.getText().toString());
    }

    private void calculate(String value){
        if(value.equals("")) value = "0";

        Double to = Double.parseDouble(convertToPrice.getText().toString());
        Double x = Double.parseDouble(value);
        Double ans = x/to;
        amountRecieveCoin.setText(String.valueOf(ans));
    }

    private void calculateSell(String value){
        if(value.equals("")) value = "0";

        Double to = Double.parseDouble(convertToPrice.getText().toString());
        Double x = Double.parseDouble(value);
        Double ans = x*to;
        amountRecieveUsd.setText(String.valueOf(ans));
    }
}