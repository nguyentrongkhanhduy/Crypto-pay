package com.example.crypto_pay_2.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.example.crypto_pay_2.AESCrypt;
import com.example.crypto_pay_2.Model.Coin;
import com.example.crypto_pay_2.Model.User;
import com.example.crypto_pay_2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class SecondaryPasswordActivity extends AppCompatActivity {

    FirebaseAuth auth = FirebaseAuth.getInstance();
    DatabaseReference user = FirebaseDatabase.getInstance().getReference("user");

    ImageButton backButton;
    EditText transactionCode;
    EditText reTransactionCode;
    Button loginButton;

    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_secondary_password);

        initUI();

        createUI();
    }

    private void initUI(){
        backButton = findViewById(R.id.back_button_after_fill_in);
        transactionCode = findViewById(R.id.second_password);
        reTransactionCode = findViewById(R.id.confirm_second_password);
        loginButton = findViewById(R.id.sign_up_after_fill_in);
    }

    private void createUI(){
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SecondaryPasswordActivity.super.onBackPressed();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tC = transactionCode.getText().toString();
                String rTc = reTransactionCode.getText().toString();
                if (TextUtils.isEmpty(tC)){
                    Toast.makeText(SecondaryPasswordActivity.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                }
                else if(!tC.equals(rTc)){
                    Toast.makeText(SecondaryPasswordActivity.this,"Sai mã!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Bundle extras = getIntent().getExtras();
                    String email = extras.getString("email");
                    String password = extras.getString("password");
                    String name = extras.getString("name");
                    String phone = extras.getString("phone");

                    registerUser(email,password,name,phone,tC);
                }
            }
        });
    }

    private void registerUser(String email, String password, String name, String phone, String transactionCode){

        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(SecondaryPasswordActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    user.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            count = (int) snapshot.getChildrenCount();
                            try {
                                addNewMember(count, name, email, phone, transactionCode);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    Toast.makeText(SecondaryPasswordActivity.this,"Đăng ký thành công",Toast.LENGTH_SHORT).show();
                    FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
                    fuser.sendEmailVerification();
                    Intent intent = new Intent(SecondaryPasswordActivity.this,VerifyPleaseActivity.class);
                    startActivity(intent);
                    finishAffinity();
                }
                else{
                    Toast.makeText(SecondaryPasswordActivity.this,"Đăng ký thất bại!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addNewMember(int count, String name, String email, String phone, String transactionCode) throws Exception {
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

        String encrypted = AESCrypt.encrypt(transactionCode);

        User registered = new User(name,phone,email,unknown,unknown,unknown,unknown,unknown,unknown,unknown,unknown,entropy,encrypted);
        user.child(nextChild).setValue(registered);
        Coin newCoin = new Coin(0,0,0);
        user.child(nextChild).child("own").setValue(newCoin);

        ClientThread clientThread = new ClientThread(entropy,nextChild);
        Thread thread = new Thread(clientThread, "connectServer");
        thread.start();

        TimeUnit.MILLISECONDS.sleep(1000);
        thread.interrupt();
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
                Python.start(new AndroidPlatform(SecondaryPasswordActivity.this));
            }

            Python py = Python.getInstance();

            PyObject pyobj = py.getModule("blockchain");

            PyObject obj = pyobj.callAttr("createUser", nextChild, entropy);

            System.out.println(obj.toString());
        }
    }
}