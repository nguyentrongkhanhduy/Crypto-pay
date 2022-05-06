package com.example.crypto_pay_2.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.crypto_pay_2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartPage extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.start_page);

        Handler hand = new Handler();
        hand.postDelayed(new Runnable() {
            @Override
            public void run() {
                startAction();
            }
        }, 1500);
    }

    private void startAction(){
        boolean check = checkUser();

        if (!check){
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }
        else{
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            user.reload();
            boolean isVerify = user.isEmailVerified();
            if(!isVerify){
                startActivity(new Intent(this, VerifyPleaseActivity.class));
            }
            else{
                Intent intent = new Intent(this, MainPage.class);
                intent.putExtra("email",user.getEmail());
                startActivity(intent);
            }
        }
        finish();
    }

    private boolean checkUser(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return false;
        return true;
    }
}