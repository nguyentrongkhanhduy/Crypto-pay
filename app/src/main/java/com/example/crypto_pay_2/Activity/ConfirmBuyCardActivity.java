package com.example.crypto_pay_2.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

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

public class ConfirmBuyCardActivity extends AppCompatActivity {

    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("user");
    String my_email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

    ImageButton back;

    AutoCompleteTextView autoCplt;
    TextInputEditText balance;

    TextView typeCard;
    TextView single;
    TextView amount;
    TextView sum;

    Button confirm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_confirm_buy_card);

        initUI();

        getInfo();

        createUI();
    }

    private void initUI(){
        back = findViewById(R.id.back_icon);

        balance = findViewById(R.id.coin_balance);
        autoCplt = findViewById(R.id.coin_dropdown);

        confirm = findViewById(R.id.confirm);

        typeCard = findViewById(R.id.typeCard);
        single = findViewById(R.id.single);
        amount = findViewById(R.id.amount);
        sum = findViewById(R.id.sum);
    }

    private void getInfo(){
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            typeCard.setText(extras.getString("typeCard"));
            amount.setText(extras.getString("amount"));
            single.setText(extras.getString("single") + "lvcoin");
            sum.setText(extras.getString("sum") + "lvcoin");
            autoCplt.setText(extras.getString("typeCoin"));
            getBalance(extras.getString("typeCoin"));
        }
    }

    private void getBalance(String typeCoin) {
        ref.orderByChild("mail").equalTo(my_email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String,String> coinOwn = new HashMap<>();
                for (DataSnapshot child: snapshot.getChildren()){
                    coinOwn = (HashMap) child.child("own").getValue();
                }
                balance.setText(String.valueOf(coinOwn.get(typeCoin)));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void createUI(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfirmBuyCardActivity.super.onBackPressed();
            }
        });
    }
}