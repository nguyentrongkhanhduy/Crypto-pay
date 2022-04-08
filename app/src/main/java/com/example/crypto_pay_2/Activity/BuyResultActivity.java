package com.example.crypto_pay_2.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.crypto_pay_2.Model.PhoneCard;
import com.example.crypto_pay_2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

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

    CardView cardOne;
    CardView cardTwo;
    CardView cardThree;
    CardView cardFour;
    CardView cardFive;


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

        typeCard5 = findViewById(R.id.typeCard5);
        fifthSingle = findViewById(R.id.fifth_single);
        card5Code = findViewById(R.id.card_5_code);
        fifthtSeri= findViewById(R.id.fifth_seri);

        toListCard = findViewById(R.id.to_list_card);
        toBuyMore = findViewById(R.id.to_buy_more);

        cardOne = findViewById(R.id.card1);
        cardTwo = findViewById(R.id.card2);
        cardThree = findViewById(R.id.card3);
        cardFour = findViewById(R.id.card4);
        cardFive = findViewById(R.id.card5);
    }

    private void createUI(){
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BuyResultActivity.this, MainPage.class);
                startActivity(intent);
                finishAffinity();
            }
        });

        toBuyMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BuyResultActivity.this, BuyCardActivity.class);
                startActivity(intent);
            }
        });

        toListCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BuyResultActivity.this, PhoneCardsListActivity.class));
            }
        });

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

        typeCard1.setText(typeOfCardService);
        typeCard2.setText(typeOfCardService);
        typeCard3.setText(typeOfCardService);
        typeCard4.setText(typeOfCardService);
        typeCard5.setText(typeOfCardService);

        firstSingle.setText(singleOfCard);
        secondSingle.setText(singleOfCard);
        thirdSingle.setText(singleOfCard);
        fourthSingle.setText(singleOfCard);
        fifthSingle.setText(singleOfCard);

        String code1 = generateRandom(12);
        String code2 = generateRandom(12);
        String code3 = generateRandom(12);
        String code4 = generateRandom(12);
        String code5 = generateRandom(12);

        String seri1 = generateRandom(15);
        String seri2 = generateRandom(15);
        String seri3 = generateRandom(15);
        String seri4 = generateRandom(15);
        String seri5 = generateRandom(15);

        card1Code.setText(code1);
        card2Code.setText(code2);
        card3Code.setText(code3);
        card4Code.setText(code4);
        card5Code.setText(code5);

        firstSeri.setText(seri1);
        secondSeri.setText(seri2);
        thirdSeri.setText(seri3);
        fourthSeri.setText(seri4);
        fifthtSeri.setText(seri5);

        if(amountOfCard.equals("1")){
            cardTwo.setVisibility(View.GONE);
            cardThree.setVisibility(View.GONE);
            cardFour.setVisibility(View.GONE);
            cardFive.setVisibility(View.GONE);
        }

        if(amountOfCard.equals("2")){
            cardThree.setVisibility(View.GONE);
            cardFour.setVisibility(View.GONE);
            cardFive.setVisibility(View.GONE);
        }

        if(amountOfCard.equals("3")){
            cardFour.setVisibility(View.GONE);
            cardFive.setVisibility(View.GONE);
        }

        if(amountOfCard.equals("4")){
            cardFive.setVisibility(View.GONE);
        }

        PhoneCard lCardOne = new PhoneCard(code1,typeOfCardService,code1,seri1,dateTimeTrans,singleOfCard,"0");
        PhoneCard lCardTwo = new PhoneCard(code2,typeOfCardService,code2,seri2,dateTimeTrans,singleOfCard,"0");
        PhoneCard lCardThree = new PhoneCard(code3,typeOfCardService,code3,seri3,dateTimeTrans,singleOfCard,"0");
        PhoneCard lCardFour = new PhoneCard(code4,typeOfCardService,code4,seri4,dateTimeTrans,singleOfCard,"0");
        PhoneCard lCardFive = new PhoneCard(code5,typeOfCardService,code5,seri5,dateTimeTrans,singleOfCard,"0");

        ref.orderByChild("mail").equalTo(my_email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userId = "";
                String cardsAmount = extras.getString("amount");
                for (DataSnapshot child : snapshot.getChildren()){
                    userId = child.getKey();
                    break;
                }
                if(cardsAmount.equals("1")){
                    ref.child(userId).child("phonecard").child(code1).setValue(lCardOne);
                }
                else if(cardsAmount.equals("2")){
                    ref.child(userId).child("phonecard").child(code1).setValue(lCardOne);
                    ref.child(userId).child("phonecard").child(code2).setValue(lCardTwo);
                }
                else if(cardsAmount.equals("3")){
                    ref.child(userId).child("phonecard").child(code1).setValue(lCardOne);
                    ref.child(userId).child("phonecard").child(code2).setValue(lCardTwo);
                    ref.child(userId).child("phonecard").child(code3).setValue(lCardThree);

                }
                else if(cardsAmount.equals("4")){
                    ref.child(userId).child("phonecard").child(code1).setValue(lCardOne);
                    ref.child(userId).child("phonecard").child(code2).setValue(lCardTwo);
                    ref.child(userId).child("phonecard").child(code3).setValue(lCardThree);
                    ref.child(userId).child("phonecard").child(code4).setValue(lCardFour);
                }
                else if(cardsAmount.equals("5")){
                    ref.child(userId).child("phonecard").child(code1).setValue(lCardOne);
                    ref.child(userId).child("phonecard").child(code2).setValue(lCardTwo);
                    ref.child(userId).child("phonecard").child(code3).setValue(lCardThree);
                    ref.child(userId).child("phonecard").child(code4).setValue(lCardFour);
                    ref.child(userId).child("phonecard").child(code5).setValue(lCardFive);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static String generateRandom(int length) {
        Random random = new Random();
        char[] digits = new char[length];
        digits[0] = (char) (random.nextInt(9) + '1');
        for (int i = 1; i < length; i++) {
            digits[i] = (char) (random.nextInt(10) + '0');
        }
        return String.valueOf(digits);
    }

    @Override
    public void onBackPressed() {

    }
}