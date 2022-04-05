package com.example.crypto_pay_2.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.example.crypto_pay_2.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class BuyCardActivity extends AppCompatActivity {

    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("user");
    String my_email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

    ImageButton back;
    RadioButton mobifone;
    RadioButton vinaphone;
    RadioButton viettel;
    RadioButton vietnamobile;

    CheckBox p10k;
    CheckBox p20k;
    CheckBox p50k;
    CheckBox p100k;
    CheckBox p200k;
    CheckBox p500k;

    ImageButton plus;
    ImageButton minus;

    TextView amount;
    TextView sum;

    Button toContinue;
    Button toListCard;

    AutoCompleteTextView autoCplt;
    TextInputEditText balance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_buy_card);



        initUI();

        creatUI();

        getInfo();



    }

    private void initUI() {
        back = findViewById(R.id.back_icon);
        mobifone = findViewById(R.id.mobi);
        vinaphone = findViewById(R.id.vina);
        viettel = findViewById(R.id.viettel);
        vietnamobile = findViewById(R.id.vnmobile);
        p10k = findViewById(R.id.p10k);
        p100k = findViewById(R.id.p100k);
        p20k = findViewById(R.id.p20k);
        p500k = findViewById(R.id.p500k);
        p200k = findViewById(R.id.p200k);
        p50k = findViewById(R.id.p50k);
        plus = findViewById(R.id.plus);
        minus = findViewById(R.id.minus);
        amount = findViewById(R.id.amount);
        sum = findViewById(R.id.sum);
        toContinue = findViewById(R.id.to_continue);
        toListCard = findViewById(R.id.to_list_card);

        balance = findViewById(R.id.coin_balance);
        autoCplt = findViewById(R.id.coin_dropdown);
    }

    private void creatUI() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BuyCardActivity.super.onBackPressed();
            }
        });

        p10k.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                p20k.setChecked(false);
                p50k.setChecked(false);
                p200k.setChecked(false);
                p500k.setChecked(false);
                p100k.setChecked(false);
                upDateSum();
            }
        });

        p20k.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                p10k.setChecked(false);
                p50k.setChecked(false);
                p200k.setChecked(false);
                p500k.setChecked(false);
                p100k.setChecked(false);
                upDateSum();
            }
        });

        p50k.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                p20k.setChecked(false);
                p10k.setChecked(false);
                p200k.setChecked(false);
                p500k.setChecked(false);
                p100k.setChecked(false);
                upDateSum();
            }
        });

        p100k.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                p20k.setChecked(false);
                p50k.setChecked(false);
                p200k.setChecked(false);
                p500k.setChecked(false);
                p10k.setChecked(false);
                upDateSum();
            }
        });

        p200k.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                p20k.setChecked(false);
                p50k.setChecked(false);
                p10k.setChecked(false);
                p500k.setChecked(false);
                p100k.setChecked(false);
                upDateSum();
            }
        });

        p500k.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                p20k.setChecked(false);
                p50k.setChecked(false);
                p200k.setChecked(false);
                p10k.setChecked(false);
                p100k.setChecked(false);
                upDateSum();
            }
        });

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentAmount = Integer.parseInt(amount.getText().toString());
                int result = 0;
                if(currentAmount < 5){
                    ++currentAmount;
                }
                amount.setText(String.valueOf(currentAmount));
                upDateSum();
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentAmount = Integer.parseInt(amount.getText().toString());
                int result = 0;
                if(currentAmount > 1){
                    --currentAmount;
                }
                amount.setText(String.valueOf(currentAmount));
                upDateSum();
            }
        });

        toContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean check = checkChecked();
                if (!check){
                    Toast.makeText(BuyCardActivity.this, "Vui lòng chọn đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                }

                else {
                    boolean checkBL = checkBalance();
                    if(!checkBL){
                        Toast.makeText(BuyCardActivity.this, "Số dư không đủ!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        String type = "";
                        if(mobifone.isChecked()) type ="mobifone";
                        if(vinaphone.isChecked()) type ="vinaphone";
                        if(viettel.isChecked()) type ="viettel";
                        if(vietnamobile.isChecked()) type ="vietnamobile";

                        String single = "";
                        if(p10k.isChecked()) single = "100";
                        if(p20k.isChecked()) single = "200";
                        if(p50k.isChecked()) single = "500";
                        if(p100k.isChecked()) single = "1000";
                        if(p200k.isChecked()) single = "2000";
                        if(p500k.isChecked()) single = "5000";

                        Intent intent = new Intent(BuyCardActivity.this,ConfirmBuyCardActivity.class);
                        intent.putExtra("amount", amount.getText().toString());
                        intent.putExtra("typeCard", type);
                        intent.putExtra("sum", sum.getText().toString());
                        intent.putExtra("typeCoin",autoCplt.getText().toString());
                        intent.putExtra("single", single);
                        startActivity(intent);
                    }
                }
            }
        });

        toListCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BuyCardActivity.this, PhoneCardsListActivity.class));
            }
        });
    }

    private void getInfo(){
        String[] item = {"bitcoin","ethereum","lvcoin"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(BuyCardActivity.this, R.layout.dropdown,item);
        autoCplt.setAdapter(adapter);

        autoCplt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                        balance.setText(String.valueOf(coinOwn.get(item)));
                        upDateSum();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
    }

    private void upDateSum(){
        int currentAmount = Integer.parseInt(amount.getText().toString());
        int result = 0;
        if(p10k.isChecked()){
            result = 10000 * currentAmount;
        }

        if(p20k.isChecked()){
            result = 20000 * currentAmount;
        }

        if(p50k.isChecked()){
            result = 50000 * currentAmount;
        }

        if(p100k.isChecked()){
            result = 100000 * currentAmount;
        }

        if(p200k.isChecked()){
            result = 200000 * currentAmount;
        }

        if(p500k.isChecked()){
            result = 500000 * currentAmount;
        }

        if(autoCplt.getText().toString().equals("lvcoin")){
            result /= 100;
            sum.setText(String.valueOf(result));
        }
    }

    private boolean checkChecked(){
        if(!mobifone.isChecked() && !vinaphone.isChecked() && !vietnamobile.isChecked() && !viettel.isChecked())
            return false;

        if(!p10k.isChecked() && !p20k.isChecked() & !p50k.isChecked() && !p100k.isChecked() && !p200k.isChecked() && !p500k.isChecked())
            return false;

        if(autoCplt.getText().toString().equals(""))
            return false;

        return true;
    }

    private boolean checkBalance(){
        Double curSum = Double.parseDouble(sum.getText().toString());
        Double curBalance = Double.parseDouble(balance.getText().toString());
        if(curSum > curBalance) return false;
        return true;
    }
}


