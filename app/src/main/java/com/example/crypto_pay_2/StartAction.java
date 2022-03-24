package com.example.crypto_pay_2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class StartAction extends AppCompatActivity {

    String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
    public static final int MY_REQUEST_CODE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();


        checkRequestPermission();

        setContentView(R.layout.activity_start_action);

        Button registerButton = (Button) findViewById(R.id.sign_up_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartAction.this,RegisterPage.class);
                startActivity(intent);
            }
        });

        Button signinButton = (Button) findViewById(R.id.sign_in);
        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartAction.this, com.example.crypto_pay_2.Login.class);
                startActivity(intent);
            }
        });


    }

    private void checkRequestPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkSelfPermission(permissions[0]) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(permissions[1]) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(permissions, MY_REQUEST_CODE);
            }

        }
    }
}