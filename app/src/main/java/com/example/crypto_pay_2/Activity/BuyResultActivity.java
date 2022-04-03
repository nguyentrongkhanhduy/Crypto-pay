package com.example.crypto_pay_2.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.crypto_pay_2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BuyResultActivity extends AppCompatActivity {

    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("user");
    String my_email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

    ImageButton home;

    TextView cardBought;
    TextView sumPayment;
    TextView idTrans;

    TextView dateTime;

    TextView typeCoinPaid;
    TextView balance;

    TextView typeCard;
    TextView single;
    TextView amount;

    TextView typeCard1;
    TextView firstSingle;
    TextView card1Code;
    TextView firstSeri;

    TextView typeCard2;
    TextView secondSingle;
    TextView card2Code;
    TextView secondSeri;

    TextView typeCard3;
    TextView thirdSingle;
    TextView card3Code;
    TextView thirdSeri;

    TextView typeCard4;
    TextView fourthSingle;
    TextView card4Code;
    TextView fourthSeri;

    TextView typeCard5;
    TextView fifthSingle;
    TextView card5Code;
    TextView fifthtSeri;

    Button toListCard;
    Button toBuyMore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_buy_result);

        initUI();

        createUI();

        getInfo();
    }

    private void initUI(){
        home = findViewById(R.id.home_icon);

        cardBought = findViewById(R.id.typeCardBought);
        sumPayment = findViewById(R.id.sum_payment);
        idTrans = findViewById(R.id.id_trans);

        dateTime= findViewById(R.id.date_time);

        typeCoinPaid = findViewById(R.id.typeCoin_paid);
        balance = findViewById(R.id.balance);

        typeCard = findViewById(R.id.typeCard);
        single = findViewById(R.id.single);

        amount = findViewById(R.id.amount);

        typeCard1 = findViewById(R.id.typeCard1);
        firstSingle = findViewById(R.id.first_single);
        card1Code = findViewById(R.id.card_1_code);
        firstSeri= findViewById(R.id.first_seri);

        typeCard2 = findViewById(R.id.typeCard2);
        secondSingle = findViewById(R.id.second_single);
        card2Code = findViewById(R.id.card_2_code);
        secondSeri= findViewById(R.id.second_seri);

        typeCard3 = findViewById(R.id.typeCard3);
        thirdSingle = findViewById(R.id.third_single);
        card3Code = findViewById(R.id.card_3_code);
        thirdSeri= findViewById(R.id.third_seri);

        typeCard4 = findViewById(R.id.typeCard4);
        fourthSingle = findViewById(R.id.fourth_single);
        card4Code = findViewById(R.id.card_4_code);
        fourthSeri= findViewById(R.id.fourth_seri);

        typeCard5 = findViewById(R.id.typeCard1);
        fifthSingle = findViewById(R.id.fifth_single);
        card5Code = findViewById(R.id.card_1_code);
        fifthtSeri= findViewById(R.id.fifth_seri);

        toListCard = findViewById(R.id.to_list_card);
        toBuyMore = findViewById(R.id.to_buy_more);
    }

    private void createUI(){

    }

    private void getInfo(){
        String typeOfCardService = "";
        String totalPrize = "";
        String singleOfCard = "";
        String amountOfCard = "";
        String dateTimeTrans = "";
        String typeOfCoinPaid = "";
        String idOfTrans = "";

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            typeOfCardService = extras.getString("typeCard");
            totalPrize = extras.getString("sum");
            singleOfCard = extras.getString("single");
            amountOfCard = extras.getString("amount");
            dateTimeTrans = extras.getString("dateTime");
            typeOfCoinPaid = extras.getString("typeCoin");
            idOfTrans = extras.getString("transID");
        }

        cardBought.setText(typeOfCardService);
        sumPayment.setText("-"+ totalPrize);
        idTrans.setText(idOfTrans);
        dateTime.setText(dateTimeTrans);

        typeCoinPaid.setText(typeOfCoinPaid);
        ref.orderByChild("mail").equalTo(my_email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot child:snapshot.getChildren()){
                    balance.setText(child.child("own").child(extras.getString("typeCoin")).getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        typeCard.setText(typeOfCardService);
        single.setText(singleOfCard);
        amount.setText(amountOfCard);
    }
}