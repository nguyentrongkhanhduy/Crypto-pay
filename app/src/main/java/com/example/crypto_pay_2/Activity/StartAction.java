package com.example.crypto_pay_2.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.crypto_pay_2.R;

public class StartAction extends AppCompatActivity {

    String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
    public static final int MY_REQUEST_CODE = 4;

    Button signinButton;
    Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_start_action);

        checkRequestPermission();

        initUI();

        creatUI();
    }

    private void initUI(){
        signinButton = (Button) findViewById(R.id.sign_in);
        registerButton = (Button) findViewById(R.id.sign_up_button);
    }

    private void creatUI(){
        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartAction.this, Login.class);
                startActivity(intent);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartAction.this,RegisterPage.class);
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