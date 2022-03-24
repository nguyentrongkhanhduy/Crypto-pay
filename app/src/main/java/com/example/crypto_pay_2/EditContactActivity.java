package com.example.crypto_pay_2;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditContactActivity extends AppCompatActivity {

    ImageButton back;
    TextView save;
    TextInputEditText address;
    TextInputEditText mail;


    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("user");
    FirebaseUser current = FirebaseAuth.getInstance().getCurrentUser();
    String my_email = current.getEmail();
    DatabaseReference thisUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_edit_contact);


        createUI();

        getInfo();

        initUI();
    }

    private void createUI(){
        back = findViewById(R.id.back_icon);
        save = findViewById(R.id.save_info);

        address = findViewById(R.id.address);
        mail = findViewById(R.id.email);

    }

    private void getInfo(){
        mail.setText(my_email);
        ref.orderByChild("mail").equalTo(my_email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child: snapshot.getChildren()){
                    if(!String.valueOf(child.child("address").getValue()).equals("unknown")){
                        address.setText(String.valueOf(child.child("address").getValue()));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void initUI(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditContactActivity.super.onBackPressed();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ref.orderByChild("mail").equalTo(my_email).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot child : snapshot.getChildren()){
                            thisUser = child.getRef();
                            break;
                        }

                        if(!address.getText().toString().matches("")){
                            thisUser.child("address").setValue(address.getText().toString());
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                Toast.makeText(EditContactActivity.this,"Cập nhật thông tin thành công!",Toast.LENGTH_SHORT).show();
                finish();
                overridePendingTransition( 0, 0);
                startActivity(getIntent());
                overridePendingTransition( 0, 0);
            }
        });
    }

}