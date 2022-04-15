package com.example.crypto_pay_2.Activity;

import static com.example.crypto_pay_2.R.id;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.crypto_pay_2.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    FirebaseAuth auth = FirebaseAuth.getInstance();

    EditText ed_txtEmail;
    EditText ed_txtPassword;

    TextView returnButton; //register button
    Button loginButton;
    TextView forgotPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        initUI();

        createUI();
    }

    private void initUI(){
        ed_txtEmail = (EditText) findViewById(id.email_or_phone);
        ed_txtPassword = (EditText) findViewById(id.password);
        returnButton = findViewById(R.id.back_button_after_register);
        loginButton = findViewById(R.id.log_in_button);
        forgotPass = findViewById(id.forget_password);
    }

    private void createUI(){
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, RegisterPage.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText email = findViewById(R.id.email_or_phone);
                EditText password = findViewById(R.id.password);

                String txt_email = email.getText().toString().trim();
                String txt_password = password.getText().toString().trim();

                if(txt_email.equals("") || txt_password.equals("")){
                    Toast.makeText(Login.this, "Vui lòng nhập đầy đủ thông tin đăng nhập!", Toast.LENGTH_SHORT).show();
                }
                else{
                    loginUser(txt_email,txt_password);
                }
            }
        });

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, ResetPasswordActivity.class));
            }
        });
    }

    private void loginUser(String email, String password){
        auth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(Login.this,"Đăng nhập thành công",Toast.LENGTH_SHORT).show();
                FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
                fuser.reload();
                if(fuser.isEmailVerified()){
                    Intent intent = new Intent(Login.this,MainPage.class);
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(Login.this,VerifyPleaseActivity.class);
                    startActivity(intent);
                }
                finishAffinity();
            }
        });
    }
}