package com.example.crypto_pay_2.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.example.crypto_pay_2.Model.Coin;
import com.example.crypto_pay_2.Model.User;
import com.example.crypto_pay_2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterPage extends AppCompatActivity {
    private static final String TAG = "RegisterPage";
    
    FirebaseAuth auth = FirebaseAuth.getInstance();

    DatabaseReference user = FirebaseDatabase.getInstance().getReference("user");

    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_register_page);

        Button backButton = (Button) findViewById(R.id.back_button_after_fill_in);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterPage.super.onBackPressed();
            }
        });

        EditText email = (EditText) findViewById(R.id.edit_text_email);
        EditText password = (EditText) findViewById(R.id.edit_text_password);
        EditText rePassword = (EditText) findViewById(R.id.edit_text_confirm_password);
        EditText name = (EditText) findViewById(R.id.edit_text_name);
        EditText phone = (EditText) findViewById(R.id.edit_text_phone);


        Button loginButton = (Button) findViewById(R.id.sign_up_after_fill_in);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();
                String txt_rePassword = rePassword.getText().toString();
                String txt_name = name.getText().toString();
                String txt_phone = phone.getText().toString();

                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password) || TextUtils.isEmpty(txt_name) || TextUtils.isEmpty(txt_phone)){
                    Toast.makeText(RegisterPage.this,"Vui lòng điền đầy đủ thông tin!",Toast.LENGTH_SHORT).show();
                }
                else if (txt_password.equals(txt_rePassword) == false){
                    Toast.makeText(RegisterPage.this,"Sai mật khẩu!", Toast.LENGTH_SHORT).show();
                }
                else {
                    registerUser(txt_email,txt_password,txt_name,txt_phone);
                }


            }
        });
    }

    private void registerUser(String email, String password, String name, String phone){

        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(RegisterPage.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    user.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            count = (int) snapshot.getChildrenCount();
                            addNewMember(count, name, email, phone);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    Toast.makeText(RegisterPage.this,"Đăng ký thành công",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterPage.this,MainPage.class);
                    startActivity(intent);
                    finishAffinity();
                }
                else{
                    Toast.makeText(RegisterPage.this,"Đăng ký thất bại!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addNewMember(int count, String name, String email, String phone){
        String nextChild = String.valueOf(count+1);
        String unknown = "unknown";
        String entropy = "";

        if(!Python.isStarted()){
            Python.start(new AndroidPlatform(this));
        }

        Python py = Python.getInstance();

        PyObject pyobj = py.getModule("entropy");
        PyObject obj = pyobj.callAttr("generateEntropy", 128);

        entropy = obj.toString();

        new Thread(new ClientThread(entropy,nextChild)).start();

        User registered = new User(name,phone,email,unknown,unknown,unknown,unknown,unknown,unknown,unknown,unknown,entropy);
        user.child(nextChild).setValue(registered);
        Coin newCoin = new Coin(0,0,10000);
        user.child(nextChild).child("own").setValue(newCoin);
    }

    class ClientThread implements Runnable {
        String entropy;
        String nextChild;

        public ClientThread(String entropy, String nextChild) {
            this.entropy = entropy;
            this.nextChild = nextChild;
        }

        @Override
        public void run() {
            if(!Python.isStarted()){
                Python.start(new AndroidPlatform(RegisterPage.this));
            }

            Python py = Python.getInstance();

            PyObject pyobj = py.getModule("blockchain");

            PyObject obj = pyobj.callAttr("createUser", nextChild, entropy);

            System.out.println(obj.toString());
        }
    }
}