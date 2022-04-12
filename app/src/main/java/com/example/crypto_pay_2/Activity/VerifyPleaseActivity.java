package com.example.crypto_pay_2.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.crypto_pay_2.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class VerifyPleaseActivity extends AppCompatActivity {

    Button verify;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    Button testLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_verify_please);

        verify = findViewById(R.id.resent_verification_button);
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(VerifyPleaseActivity.this, "Email xác thực đã được gửi!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        testLogout = findViewById(R.id.log_out_button);
        testLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(VerifyPleaseActivity.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
    }
}