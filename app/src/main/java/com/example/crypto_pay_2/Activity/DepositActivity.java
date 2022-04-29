package com.example.crypto_pay_2.Activity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.crypto_pay_2.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class DepositActivity extends AppCompatActivity {

    String my_email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("user");
    DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("address/lvcoin");

    ImageButton back;

    TextInputEditText balance;
    AutoCompleteTextView autoCplt;

    TextInputEditText address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_deposit);

        initUI();

        createUI();

        getInfo();


    }

    private void initUI() {
        balance = (TextInputEditText) findViewById(R.id.coin_balance);
        autoCplt = findViewById(R.id.coin_dropdown);
        back = (ImageButton) findViewById(R.id.back_icon);
        address = findViewById(R.id.coin_address);
    }

    private void createUI() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DepositActivity.super.onBackPressed();
            }
        });
    }

    private void getInfo() {
        String[] item = {"bitcoin","ethereum","lvcoin"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.dropdown,item);
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
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        ref.orderByChild("mail").equalTo(my_email).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String userId = snapshot.getKey();
                ref2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot child:snapshot.getChildren()){
                            if(child.getValue().toString().equals(userId)){
                                address.setText(child.getKey());
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}