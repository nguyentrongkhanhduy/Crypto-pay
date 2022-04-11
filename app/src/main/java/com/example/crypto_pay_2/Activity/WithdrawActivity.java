package com.example.crypto_pay_2.Activity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.example.crypto_pay_2.Model.History;
import com.example.crypto_pay_2.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class WithdrawActivity extends AppCompatActivity {

    String my_email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("user");
    DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("history");

    TextInputEditText balance;
    AutoCompleteTextView autoCplt;

    ImageButton back;
    TextInputEditText amount;
    TextInputEditText address;

    Button beginWithdraw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_withdraw);

        initUI();

        createUI();

        getInfo();
    }

    private void initUI() {
        balance = (TextInputEditText) findViewById(R.id.coin_balance);
        autoCplt = findViewById(R.id.coin_dropdown);
        back = (ImageButton) findViewById(R.id.back_icon);
        amount = findViewById(R.id.coin_to_withdraw);
        address = findViewById(R.id.coin_address);
        beginWithdraw = findViewById(R.id.begin_withdraw_button);
    }

    private void createUI() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WithdrawActivity.super.onBackPressed();
            }
        });

        beginWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    withdraw();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getInfo() {
        String[] item = {"bitcoin","ethereum","lvcoin"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(WithdrawActivity.this, R.layout.dropdown,item);
        autoCplt.setAdapter(adapter);

        autoCplt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("user");
                ref.orderByChild("mail").equalTo(my_email).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Map<String,String> coinOwn = new HashMap<>();
                        for (DataSnapshot child: snapshot.getChildren()){
                            coinOwn = (HashMap) child.child("own").getValue();
                        }
                        balance.setText(String.valueOf(coinOwn.get(item)));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    private void withdraw() throws InterruptedException {
        if(autoCplt.getText().toString().equals("")){
            Toast.makeText(this, "Vui lòng chọn loại coin muốn rút!", Toast.LENGTH_SHORT).show();
        }
        else if(amount.getText().toString().equals("")){
            Toast.makeText(this, "Vui lòng nhập lượng coin muốn rút!", Toast.LENGTH_SHORT).show();
        }
        else if(address.getText().toString().equals("")){
            Toast.makeText(this, "Vui lòng nhập địa chỉ ví!", Toast.LENGTH_SHORT).show();
        }
        else if(Integer.parseInt(balance.getText().toString()) < Integer.parseInt(amount.getText().toString())){
            Toast.makeText(this, "Số dư trong ví không đủ!", Toast.LENGTH_SHORT).show();
        }
        else {
            int amountWithdraw = Integer.parseInt(amount.getText().toString());
            int currentBalance = Integer.parseInt(balance.getText().toString());
            int updateBalance = currentBalance - amountWithdraw;
            String addressWithdraw = address.getText().toString();
            String coinName = autoCplt.getText().toString();

            ClientWithdraw clientWithdraw = new ClientWithdraw(amountWithdraw,addressWithdraw);
            Thread thread = new Thread(clientWithdraw);
            thread.start();

            TimeUnit.MILLISECONDS.sleep(1000);
            thread.interrupt();

            SimpleDateFormat formatToDisplayDate = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat formatToDisplayTime = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat formatToAdd = new SimpleDateFormat("ddMMyyyyHHMMss");
            Calendar c = Calendar.getInstance();
            String date = formatToDisplayDate.format(c.getTime());
            String time = formatToDisplayTime.format(c.getTime());
            String historyId = formatToAdd.format(c.getTime());

            ref.orderByChild("mail").equalTo(my_email).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String userId = "";
                    String senderName = "";
                    for (DataSnapshot child : snapshot.getChildren())
                    {
                        userId = child.getKey().toString();
                        senderName = child.child("name").getValue().toString();
                    }
                    ref.child(userId).child("own").child(coinName).setValue(updateBalance);
                    History history = new History("Rút coin",
                            amountWithdraw + " " + coinName,
                            date,
                            time,
                            senderName,
                            "Wallet",
                            historyId,
                            "Rút coin về ví HDWallet");
                    ref.child(userId).child("history").child(historyId).setValue(historyId);
                    ref2.child(historyId).setValue(history);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            amount.setText("");
            address.setText("");
            Toast.makeText(this, "Giao dịch thành công!", Toast.LENGTH_SHORT).show();

        }

    }

    class ClientWithdraw implements Runnable{
        int amount;
        String address;

        public ClientWithdraw(int amount, String address) {
            this.amount = amount;
            this.address = address;
        }

        @Override
        public void run() {
            if(!Python.isStarted()){
                Python.start(new AndroidPlatform(WithdrawActivity.this));
            }

            Python py = Python.getInstance();

            PyObject pyobj = py.getModule("blockchain");

            PyObject obj = pyobj.callAttr("widthdraw", amount, address);

            System.out.println(obj.toString());
        }
    }

}