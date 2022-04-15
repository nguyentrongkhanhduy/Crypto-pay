package com.example.crypto_pay_2.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.crypto_pay_2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    EditText emailReset;
    Button sendRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_reset_password);

        initUI();

        createUI();
    }

    private void initUI(){
        emailReset = findViewById(R.id.email_reset);
        sendRequest = findViewById(R.id.send_request);
    }

    private void createUI(){
        sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(emailReset.getText().toString().equals("")){
                    Toast.makeText(ResetPasswordActivity.this, "Vui lòng nhập email đã đăng ký tài khoản!", Toast.LENGTH_SHORT).show();
                    emailReset.requestFocus();
                    return;
                }

                String email = emailReset.getText().toString();
                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(ResetPasswordActivity.this, "Đã gửi email yêu cầu reset mật khẩu!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}