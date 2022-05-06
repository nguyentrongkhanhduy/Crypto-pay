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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.crypto_pay_2.AESCrypt;
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

import at.favre.lib.crypto.bcrypt.BCrypt;

public class ConfirmBuyCardActivity extends AppCompatActivity {

    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("user");
    DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("history");
    String my_email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

    ImageButton back;

    AutoCompleteTextView autoCplt;
    TextInputEditText balance;

    TextView typeCard;
    TextView single;
    TextView amount;
    TextView sum;

    Button confirm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_confirm_buy_card);

        initUI();

        getInfo();

        createUI();
    }

    private void initUI(){
        back = findViewById(R.id.back_icon);

        balance = findViewById(R.id.coin_balance);
        autoCplt = findViewById(R.id.coin_dropdown);

        confirm = findViewById(R.id.confirm);

        typeCard = findViewById(R.id.typeCard);
        single = findViewById(R.id.single);
        amount = findViewById(R.id.amount);
        sum = findViewById(R.id.sum);
    }

    private void getInfo(){
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            typeCard.setText(extras.getString("typeCard"));
            amount.setText(extras.getString("amount"));
            single.setText(extras.getString("single") + "lvcoin");
            sum.setText(extras.getString("sum") + "lvcoin");
            autoCplt.setText(extras.getString("typeCoin"));
            getBalance(extras.getString("typeCoin"));
        }
    }

    private void getBalance(String typeCoin) {
        ref.orderByChild("mail").equalTo(my_email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String,String> coinOwn = new HashMap<>();
                for (DataSnapshot child: snapshot.getChildren()){
                    coinOwn = (HashMap) child.child("own").getValue();
                }
                balance.setText(String.valueOf(coinOwn.get(typeCoin)));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void createUI(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfirmBuyCardActivity.super.onBackPressed();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTransactionCodeDialog(Gravity.CENTER);
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
                            Toast.makeText(ConfirmBuyCardActivity.this, "Mã xác thực không đúng!", Toast.LENGTH_SHORT).show();
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
        SimpleDateFormat formatToDisplayDate = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formatToDisplayTime = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat formatToAdd = new SimpleDateFormat("ddMMyyyyHHMMss");
        Calendar c = Calendar.getInstance();
        String date = formatToDisplayDate.format(c.getTime());
        String time = formatToDisplayTime.format(c.getTime());
        String historyId = formatToAdd.format(c.getTime());

        Bundle extras = getIntent().getExtras();
        String total = extras.getString("sum");
        String typeOfCard = typeCard.getText().toString();
        String singleOfCard = single.getText().toString();
        String amountOfCard = amount.getText().toString();
        String totalPaid = sum.getText().toString();
        String typeCoinPaid = autoCplt.getText().toString();

        int payAmount = Integer.parseInt(total);
        int currentAmount = Integer.parseInt(balance.getText().toString());
        int result = currentAmount - payAmount;
        String updateBalance = String.valueOf(result);

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
                ref.child(userId).child("own").child(typeCoinPaid).setValue(updateBalance);
                History history = new History("Mua mã thẻ điện thoại",
                        totalPaid,
                        date,
                        time,
                        senderName,
                        "Dịch vụ mua mã thẻ",
                        historyId,
                        amountOfCard + " " + typeOfCard + " " + singleOfCard);
                ref.child(userId).child("history").child(historyId).setValue(historyId);
                ref2.child(historyId).setValue(history);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        Intent intent = new Intent(ConfirmBuyCardActivity.this, BuyResultActivity.class);
        intent.putExtra("typeCard", typeCard.getText().toString());
        intent.putExtra("single", single.getText().toString());
        intent.putExtra("amount", amount.getText().toString());
        intent.putExtra("sum", sum.getText().toString());
        intent.putExtra("dateTime", time + " - " + date);
        intent.putExtra("typeCoin", autoCplt.getText().toString());
        intent.putExtra("transID", historyId);
        startActivity(intent);
    }

}