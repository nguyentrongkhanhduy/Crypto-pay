package com.example.crypto_pay_2.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.crypto_pay_2.AESCrypt;
import com.example.crypto_pay_2.Model.History;
import com.example.crypto_pay_2.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class TransferActivity extends AppCompatActivity {

    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("user");
    DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("history");
    String my_email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

    ImageButton back;
    TextInputEditText phone;
    TextInputEditText name;
    TextInputEditText balance;
    TextInputEditText transferCoin;
    TextInputEditText message;
    AutoCompleteTextView coin;
    Button find;
    Button transferBegin;
    Button scan;
    Button recieveBegin;
    TextView checkTC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_transfer);

        initUI();

        creatUI();

        getInfo();
    }

    private void initUI(){
        back = findViewById(R.id.back_icon);
        phone = findViewById(R.id.phone);
        name = findViewById(R.id.name);
        balance = findViewById(R.id.coin_balance);
        coin = findViewById(R.id.coin_dropdown);
        transferCoin = findViewById(R.id.coin_to_transfer);
        message = findViewById(R.id.message);
        find = findViewById(R.id.find_user);
        transferBegin = findViewById(R.id.begin_transfer_button);
        scan = findViewById(R.id.to_scan);
        recieveBegin = findViewById(R.id.begin_recieve_button);
        checkTC = findViewById(R.id.check_tC);
    }

    private void creatUI(){

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TransferActivity.this, ScanActivity.class);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TransferActivity.super.onBackPressed();
            }
        });

        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name.setText("");
                if (phone.getText().toString().equals("")){
                    Toast.makeText(TransferActivity.this, "Vui lòng nhập số điện thoại!", Toast.LENGTH_SHORT).show();
                }
                else{
                    String getUser = phone.getText().toString();
                    ref.orderByChild("phone").equalTo(getUser).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.getChildrenCount() == 0) Toast.makeText(TransferActivity.this, "Không tìm thấy người dùng!", Toast.LENGTH_SHORT).show();
                            else{
                                for (DataSnapshot child: snapshot.getChildren()){
                                    name.setText(child.child("name").getValue().toString());
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
            }
        });

        transferBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(phone.getText().toString().equals("") || name.getText().toString().equals("")){
                    Toast.makeText(TransferActivity.this, "Vui lòng tìm người muốn giao dịch!", Toast.LENGTH_SHORT).show();
                }
                else if (coin.getText().toString().equals("")){
                    Toast.makeText(TransferActivity.this, "Vui lòng kiểm tra số dư!", Toast.LENGTH_SHORT).show();
                }
                else if (transferCoin.getText().toString().equals("")){
                    Toast.makeText(TransferActivity.this, "Vui lòng nhập khoảng muốn giao dịch!", Toast.LENGTH_SHORT).show();
                }
                else if (Integer.parseInt(transferCoin.getText().toString()) > Integer.parseInt(balance.getText().toString())){
                    Toast.makeText(TransferActivity.this, "Số dư của bạn không đủ để thực hiện giao dịch!", Toast.LENGTH_SHORT).show();
                }
                else if (message.getText().toString().equals("")){
                    Toast.makeText(TransferActivity.this, "Vui lòng nhập lời nhắn!", Toast.LENGTH_SHORT).show();
                }
                else {
                    openTransactionCodeDialog(Gravity.CENTER);
//                    String checkCode = checkTC.getText().toString();
//                    if(checkCode.equals("true")){
//                        Toast.makeText(TransferActivity.this, "true", Toast.LENGTH_SHORT).show();
//                    }
//                    else{
//                        Toast.makeText(TransferActivity.this, "false", Toast.LENGTH_SHORT).show();
//                    }
                }
            }
        });

        recieveBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (coin.getText().toString().equals("")){
                    Toast.makeText(TransferActivity.this, "Vui lòng chọn loại Coin!", Toast.LENGTH_SHORT).show();
                }
                else if (transferCoin.getText().toString().equals("")){
                    Toast.makeText(TransferActivity.this, "Vui lòng nhập khoảng muốn giao dịch!", Toast.LENGTH_SHORT).show();
                }
                else{
                    ref.orderByChild("mail").equalTo(my_email).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            Intent intent = new Intent(TransferActivity.this, RecieveActivity.class);
                            intent.putExtra("type",coin.getText().toString());
                            intent.putExtra("amount",transferCoin.getText().toString());
                            for(DataSnapshot child: snapshot.getChildren()){
                                intent.putExtra("name",child.child("name").getValue().toString());
                                intent.putExtra("phone",child.child("phone").getValue().toString());
                            }
                            startActivity(intent);
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

    }

    private void getInfo(){
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            String phoneQR = extras.getString("phone");
            String amountQR = extras.getString("amount");
            String typeQR = extras.getString("type");

            phone.setText(phoneQR);
            name.setText("");
            if (phone.getText().toString().equals("")){
                Toast.makeText(TransferActivity.this, "Vui lòng nhập số điện thoại!", Toast.LENGTH_SHORT).show();
            }
            else{
                String getUser = phone.getText().toString();
                ref.orderByChild("phone").equalTo(getUser).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getChildrenCount() == 0) Toast.makeText(TransferActivity.this, "Không tìm thấy người dùng!", Toast.LENGTH_SHORT).show();
                        else{
                            for (DataSnapshot child: snapshot.getChildren()){
                                name.setText(child.child("name").getValue().toString());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
            transferCoin.setText(amountQR);
            coin.setText(typeQR);
            getBalance(typeQR);
        }

        String[] item = {"bitcoin","ethereum","lvcoin"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.dropdown,item);
        coin.setAdapter(adapter);

        coin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();

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

    private void getBalance(String typeQR) {
        ref.orderByChild("mail").equalTo(my_email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String,String> coinOwn = new HashMap<>();
                for (DataSnapshot child: snapshot.getChildren()){
                    coinOwn = (HashMap) child.child("own").getValue();
                }
                balance.setText(String.valueOf(coinOwn.get(typeQR)));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void openTransactionCodeDialog(int gravity){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_confirm_transaction);

        Window window = dialog.getWindow();
        if(window == null){
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams attribute = window.getAttributes();
        attribute.gravity = gravity;
        window.setAttributes(attribute);

        if (Gravity.BOTTOM == gravity) dialog.setCancelable(true);
        else dialog.setCancelable(false);

        EditText code = dialog.findViewById(R.id.transaction_code);
        Button confirm = dialog.findViewById(R.id.confirm_tC);
        Button cancel = dialog.findViewById(R.id.cancel_tC);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tC = code.getText().toString();
                ref.orderByChild("mail").equalTo(my_email).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String realTc = "";
                        for (DataSnapshot child: snapshot.getChildren()){
                            realTc = child.child("transactionCode").getValue().toString();
                        }
                            BCrypt.Result result = BCrypt.verifyer().verify(tC.toCharArray(),realTc);
                            if(!result.verified){
                                Toast.makeText(TransferActivity.this, "Mã xác thực không đúng!", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                            else{
                                doTransaction();
                                dialog.dismiss();
                            }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        dialog.show();
    }

    private void doTransaction(){
        String coinName = coin.getText().toString();

        int transferAmount = Integer.parseInt(transferCoin.getText().toString());
        int currentAmount = Integer.parseInt(balance.getText().toString());
        int result = currentAmount - transferAmount;
        String updateBalance = String.valueOf(result);

        String recieverPhone = phone.getText().toString();
        String recieverName = name.getText().toString();
        String senderMessage = message.getText().toString();

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
                History history = new History("Giao dịch coin",
                        transferAmount + " " + coinName,
                        date,
                        time,
                        senderName,
                        recieverName,
                        historyId,
                        senderMessage);
                ref.child(userId).child("history").child(historyId).setValue(historyId);
                ref2.child(historyId).setValue(history);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ref.orderByChild("phone").equalTo(recieverPhone).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userId = "";
                String balance = "";
                for (DataSnapshot child : snapshot.getChildren())
                {
                    userId = child.getKey().toString();
                    balance = child.child("own").child(coinName).getValue().toString();
                }
                int newBalance = Integer.parseInt(balance) + transferAmount;
                ref.child(userId).child("own").child(coinName).setValue(newBalance);
                ref.child(userId).child("history").child(historyId).setValue(historyId);
                ref.child(userId).child("notification").child(historyId).setValue(historyId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Toast.makeText(TransferActivity.this, "Giao dịch thành công!",Toast.LENGTH_SHORT).show();
        finish();
        overridePendingTransition( 0, 0);
        startActivity(getIntent());
        overridePendingTransition( 0, 0);
    }
}