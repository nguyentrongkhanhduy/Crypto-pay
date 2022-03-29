package com.example.crypto_pay_2.Activity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConvertActivity extends AppCompatActivity {


    CoinService mService;

    private static final String API_URL = "https://min-api.cryptocompare.com";
    private static final String BASE_URL = "https://cryptocompare.com";

    public static CoinService getCoinService(){
        return RetrofitClient.getClient(API_URL).create(CoinService.class);
    }

    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("user");
    String my_email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

    ImageButton back;
    AutoCompleteTextView from;
    AutoCompleteTextView to;

    TextInputEditText balance;

    TextInputEditText amount;
    TextInputEditText result;

    TextInputEditText priceFrom;
    TextInputEditText priceTo;

    Button conver;
    Button confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_convert);
        
        initUI();
        
        createUI();
    }

    private void initUI() {
        back = findViewById(R.id.back_icon);
        from = findViewById(R.id.from);
        to = findViewById(R.id.to);
        amount = findViewById(R.id.amount);
        result = findViewById(R.id.result);
        priceFrom = findViewById(R.id.price_from);
        priceTo = findViewById(R.id.price_to);
        conver = findViewById(R.id.do_convert);
        confirm = findViewById(R.id.confirm_convert);
        balance = findViewById(R.id.coin_balance);

        mService = getCoinService();
    }

    private void createUI() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               ConvertActivity.super.onBackPressed();
            }
        });

        String[] item = {"BTC","ETH"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.dropdown,item);
        from.setAdapter(adapter);
        to.setAdapter(adapter);

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
                        if(item.equals("BTC")) balance.setText(String.valueOf(coinOwn.get("bitcoin")));
                        if(item.equals("ETH")) balance.setText(String.valueOf(coinOwn.get("ethereum")));

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                if(item.equals("BTC")){
                    mService.calculate(item,"USD").enqueue(new Callback<Price>() {
                        @Override
                        public void onResponse(Call<Price> call, Response<Price> response) {
                            showDataFrom(response.body().getUSD());
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
                            showDataFrom(response.body().getUSD());
                        }

                        @Override
                        public void onFailure(Call<Price> call, Throwable t) {

                        }
                    });
                }
            }
        });

        to.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();

                if(item.equals("BTC")){
                    mService.calculate(item,"USD").enqueue(new Callback<Price>() {
                        @Override
                        public void onResponse(Call<Price> call, Response<Price> response) {
                            showDataTo(response.body().getUSD());
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
                            showDataTo(response.body().getUSD());
                        }

                        @Override
                        public void onFailure(Call<Price> call, Throwable t) {

                        }
                    });
                }
            }
        });

        conver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(priceFrom.getText().toString().equals("") || priceTo.getText().toString().equals("")){
                    Toast.makeText(ConvertActivity.this,"Xin hãy đợi trong giây lát!", Toast.LENGTH_SHORT).show();
                }
                else if (amount.getText().toString().equals("")){
                    Toast.makeText(ConvertActivity.this,"Vui lòng nhập lượng muốn quy đổi!", Toast.LENGTH_SHORT).show();
                }
                else if (Double.parseDouble(amount.getText().toString()) > Double.parseDouble(balance.getText().toString())){
                    Toast.makeText(ConvertActivity.this,"Số dư không đủ!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Double fromA = Double.parseDouble(priceFrom.getText().toString().substring(1));
                    Double toA = Double.parseDouble(priceTo.getText().toString().substring(1));
                    Double am = Double.parseDouble(amount.getText().toString());
                    Double resultA = am * (fromA / toA);
                    DecimalFormat df = new DecimalFormat("#.##########");
                    result.setText(String.valueOf(df.format(resultA)));
                }
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(result.getText().toString().equals("")){
                    Toast.makeText(ConvertActivity.this,"Vui lòng quy đổi trước khi xác nhận!", Toast.LENGTH_SHORT).show();

                }
                else{
                    Double fromA = Double.parseDouble(priceFrom.getText().toString().substring(1));
                    Double toA = Double.parseDouble(priceTo.getText().toString().substring(1));
                    Double am = Double.parseDouble(amount.getText().toString());
                    Double resultA = am * (fromA / toA);

                    ref.orderByChild("mail").equalTo(my_email).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String userId = "";
                            String coinFromBalance = "";
                            String coinToBalance = "";
                            String coinFromName = "";
                            String coinToName = "";

                            for (DataSnapshot child : snapshot.getChildren())
                            {
                                userId = child.getKey().toString();
                                if(from.getText().toString().equals("BTC")){
                                    coinFromBalance = child.child("own").child("bitcoin").getValue().toString();
                                    coinFromName = "bitcoin";
                                    coinToName = "ethereum";
                                    coinToBalance = child.child("own").child("ethereum").getValue().toString();
                                }
                                if(from.getText().toString().equals("ETH")){
                                    coinToBalance = child.child("own").child("bitcoin").getValue().toString();
                                    coinToName = "bitcoin";
                                    coinFromName = "ethereum";
                                    coinFromBalance = child.child("own").child("ethereum").getValue().toString();
                                }
                            }

                            Double upDateBalanceFrom = Double.parseDouble(coinFromBalance) - am;
                            Double upDateBalanceTo = Double.parseDouble(coinToBalance) + resultA;

                            ref.child(userId).child("own").child(coinFromName).setValue(upDateBalanceFrom);
                            ref.child(userId).child("own").child(coinToName).setValue(upDateBalanceTo);

                            Toast.makeText(ConvertActivity.this,"Quy đổi thành công!", Toast.LENGTH_SHORT).show();
                            finish();
                            overridePendingTransition( 0, 0);
                            startActivity(getIntent());
                            overridePendingTransition( 0, 0);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
        });

    }

    private void showDataFrom(String value){
        priceFrom.setText("$" + value);
    }

    private void showDataTo(String value){
        priceTo.setText("$" + value);
    }


}