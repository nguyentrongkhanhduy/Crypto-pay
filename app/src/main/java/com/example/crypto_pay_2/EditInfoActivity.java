package com.example.crypto_pay_2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditInfoActivity extends AppCompatActivity {

    ImageButton back;
    TextView save;

    TextInputEditText city;
    TextInputEditText birth;
    TextInputEditText hometown;
    TextInputEditText occupation;
    AutoCompleteTextView gender;
    AutoCompleteTextView status;
    AutoCompleteTextView education;

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

        setContentView(R.layout.activity_edit_info);

        creatUI();

        getInfo();

        initUI();
    }

    private void creatUI(){
        back = findViewById(R.id.back_icon);
        save = findViewById(R.id.save_info);

        city = findViewById(R.id.city);
        birth = findViewById(R.id.birth_day);
        hometown = findViewById(R.id.home_town);
        occupation = findViewById(R.id.occupation);
        gender = findViewById(R.id.gender);
        status = findViewById(R.id.status);
        education = findViewById(R.id.education);
    }

    private void getInfo(){

        ref.orderByChild("mail").equalTo(my_email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child: snapshot.getChildren()){
                    if(!String.valueOf(child.child("city").getValue()).equals("unknown")){
                        city.setText(String.valueOf(child.child("city").getValue()));
                    }

                    if(!String.valueOf(child.child("birth").getValue()).equals("unknown")){
                        birth.setText(String.valueOf(child.child("birth").getValue()));
                    }

                    if(!String.valueOf(child.child("hometown").getValue()).equals("unknown")){
                        hometown.setText(String.valueOf(child.child("hometown").getValue()));
                    }

                    if(!String.valueOf(child.child("occupation").getValue()).equals("unknown")){
                        occupation.setText(String.valueOf(child.child("occupation").getValue()));
                    }

                    if(!String.valueOf(child.child("gender").getValue()).equals("unknown")){
                        gender.setText(String.valueOf(child.child("gender").getValue()));
                    }

                    if(!String.valueOf(child.child("status").getValue()).equals("unknown")){
                        status.setText(String.valueOf(child.child("status").getValue()));
                    }

                    if(!String.valueOf(child.child("education").getValue()).equals("unknown")){
                        education.setText(String.valueOf(child.child("education").getValue()));
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
                EditInfoActivity.super.onBackPressed();
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

                        if(!city.getText().toString().matches("")){
                            thisUser.child("city").setValue(city.getText().toString());
                        }
                        if(!birth.getText().toString().matches("")){
                            thisUser.child("birth").setValue(birth.getText().toString());
                        }
                        if(!gender.getText().toString().matches("")){
                            thisUser.child("gender").setValue(gender.getText().toString());
                        }
                        if(!hometown.getText().toString().matches("")){
                            thisUser.child("hometown").setValue(hometown.getText().toString());
                        }
                        if(!education.getText().toString().matches("")){
                            thisUser.child("education").setValue(education.getText().toString());
                        }
                        if(!occupation.getText().toString().matches("")){
                            thisUser.child("occupation").setValue(occupation.getText().toString());
                        }
                        if(!status.getText().toString().matches("")){
                            thisUser.child("status").setValue(status.getText().toString());
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                Toast.makeText(EditInfoActivity.this,"Cập nhật thông tin thành công!",Toast.LENGTH_SHORT).show();
                finish();
                overridePendingTransition( 0, 0);
                startActivity(getIntent());
                overridePendingTransition( 0, 0);
            }
        });

        String[] listGender = {"Nam", "Nữ", "Khác"};
        ArrayAdapter<String> adapterGender = new ArrayAdapter<String>(EditInfoActivity.this, R.layout.dropdown,listGender);
        gender.setAdapter(adapterGender);

        String[] listStatus = {"Độc thân", "Trong mối quan hệ", "Đã kết hôn"};
        ArrayAdapter<String> adapterStatus = new ArrayAdapter<String>(EditInfoActivity.this, R.layout.dropdown,listStatus);
        status.setAdapter(adapterStatus);

        String[] listEducation = {"Trung học", "Phổ thông", "Trung cấp", "Cao đẳng", "Đại học", "Thạc sĩ", "Tiến sĩ"};
        ArrayAdapter<String> adapterEducation = new ArrayAdapter<String>(EditInfoActivity.this, R.layout.dropdown,listEducation);
        education.setAdapter(adapterEducation);
    }

}