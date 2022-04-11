package com.example.crypto_pay_2.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crypto_pay_2.Adapter.PhoneCardAdapter;
import com.example.crypto_pay_2.Model.PhoneCard;
import com.example.crypto_pay_2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PhoneCardsListActivity extends AppCompatActivity {

    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    String my_email = currentUser.getEmail();
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("user");

    private RecyclerView rvCard;
    private PhoneCardAdapter phoneCardAdapter;
    private List<PhoneCard> mListCard;
    private ProgressBar loadingPB;
    private TextView noNoti;
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_phone_cards_list);

        initUI();

        createUI();

        getInfo();
    }

    private void initUI() {
        back = findViewById(R.id.back_icon);
        loadingPB = findViewById(R.id.idPBLoading);
        loadingPB.setVisibility(View.VISIBLE);
        noNoti = findViewById(R.id.no_noti);

        rvCard = findViewById(R.id.list_card);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvCard.setLayoutManager(linearLayoutManager);

        mListCard = new ArrayList<>();
        phoneCardAdapter = new PhoneCardAdapter(mListCard, new PhoneCardAdapter.UpdateIsUsed() {
            @Override
            public void updateNotiOnClick(PhoneCard phoneCard) {
                setUsedCard(phoneCard);
            }
        });

        rvCard.setAdapter(phoneCardAdapter);

    }

    private void createUI() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhoneCardsListActivity.super.onBackPressed();
            }
        });
    }

    private void getInfo(){
        ref.orderByChild("mail").equalTo(my_email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot child: snapshot.getChildren()){
                    if(child.child("phonecard").getChildrenCount() == 0){
                        loadingPB.setVisibility(View.GONE);
                        noNoti.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ref.orderByChild("mail").equalTo(my_email).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String userID = snapshot.getKey();
                String path = ref.child(userID).child("phonecard").getKey();
                ref.child(userID).child("phonecard").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        PhoneCard phoneCard = new PhoneCard(String.valueOf(snapshot.child("id").getValue()),String.valueOf(snapshot.child("typeCard").getValue()),
                                String.valueOf(snapshot.child("codeNumber").getValue()),
                                String.valueOf(snapshot.child("seriNumber").getValue()),
                                String.valueOf(snapshot.child("dateTime").getValue()),
                                String.valueOf(snapshot.child("singlePrice").getValue()),
                                String.valueOf(snapshot.child("isUsed").getValue())
                        );
                        mListCard.add(phoneCard);
                        Collections.sort(mListCard,PhoneCard.sortDate);
                        phoneCardAdapter.notifyDataSetChanged();
                        loadingPB.setVisibility(View.GONE);
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

    private void setUsedCard(PhoneCard phoneCard){
        new AlertDialog.Builder(PhoneCardsListActivity.this)
                .setTitle("Lưu ý!!!").setMessage("Bạn có chắc đã sử dụng mã này không?")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ref.orderByChild("mail").equalTo(my_email).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String userId = "";
                                for (DataSnapshot child : snapshot.getChildren()){
                                    userId = child.getKey().toString();
                                    break;
                                }
                                ref.child(userId).child("phonecard").child(phoneCard.getId()).child("isUsed").setValue("1");
                                finish();
                                overridePendingTransition( 0, 0);
                                startActivity(getIntent());
                                overridePendingTransition( 0, 0);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }).setNegativeButton("Cancel", null).show();
    }
}