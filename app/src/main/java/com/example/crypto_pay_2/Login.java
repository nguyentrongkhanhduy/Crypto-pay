package com.example.crypto_pay_2;

import static com.example.crypto_pay_2.R.*;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    FirebaseAuth auth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        EditText ed_txtEmail = (EditText) findViewById(id.email_or_phone);
        EditText ed_txtPassword = (EditText) findViewById(id.password);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            String email = extras.getString("email");
            String password = extras.getString("password");

            ed_txtEmail.setText(email);
            ed_txtPassword.setText(password);
        }

        Button returnButton = (Button) findViewById(R.id.back_button_after_register);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, StartAction.class);
                startActivity(intent);
            }
        });

        Button loginButton = (Button) findViewById(R.id.log_in_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText email = (EditText) findViewById(R.id.email_or_phone);
                EditText password = (EditText) findViewById(R.id.password);

                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();
                loginUser(txt_email,txt_password);
            }
        });
    }

    private void loginUser(String email, String password){
        auth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(Login.this,"Đăng nhập thành công",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Login.this,MainPage.class);
                intent.putExtra("email",email);
                startActivity(intent);
            }
        });
    }
}