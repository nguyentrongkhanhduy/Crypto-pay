package com.example.crypto_pay_2.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.crypto_pay_2.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterPage extends AppCompatActivity {

    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("user");

    ImageButton backButton;
    EditText email;
    EditText password;
    EditText rePassword;
    EditText name;
    EditText phone;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_register_page);

        initUI();

        createUI();
    }

    private void initUI() {
        backButton = findViewById(R.id.back_button_after_fill_in);

        email = findViewById(R.id.edit_text_email);
        password = findViewById(R.id.edit_text_password);
        rePassword = findViewById(R.id.edit_text_confirm_password);
        name = findViewById(R.id.edit_text_name);
        phone = findViewById(R.id.edit_text_phone);

        loginButton = findViewById(R.id.sign_up_after_fill_in);
    }

    private void createUI() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterPage.super.onBackPressed();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_email = email.getText().toString().trim();
                String txt_password = password.getText().toString();
                String txt_rePassword = rePassword.getText().toString();
                String txt_name = name.getText().toString();
                String txt_phone = phone.getText().toString();

                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password) || TextUtils.isEmpty(txt_name) || TextUtils.isEmpty(txt_phone)){
                    Toast.makeText(RegisterPage.this,"Vui lòng điền đầy đủ thông tin!",Toast.LENGTH_SHORT).show();
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(txt_email).matches()){
                    email.setError("Vui lòng nhập email đúng định dạng");
                    email.requestFocus();
                }
                else if (txt_password.equals(txt_rePassword) == false){
                    Toast.makeText(RegisterPage.this,"Sai mật khẩu!", Toast.LENGTH_SHORT).show();
                }
                else {
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot child : snapshot.getChildren()){
                                if(txt_email.equals(child.child("mail").getValue().toString())){
                                    email.setError("Email đã được sử dụng!");
                                    return;
                                }
                                if(txt_phone.equals(child.child("phone").getValue().toString())){
                                    phone.setError("Số điện thoại đã được sử dụng!");
                                    return;
                                }
                            }
                            Intent intent = new Intent(RegisterPage.this, SecondaryPasswordActivity.class);
                            intent.putExtra("email",txt_email);
                            intent.putExtra("password",txt_password);
                            intent.putExtra("name",txt_name);
                            intent.putExtra("phone",txt_phone);
                            startActivity(intent);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }

}