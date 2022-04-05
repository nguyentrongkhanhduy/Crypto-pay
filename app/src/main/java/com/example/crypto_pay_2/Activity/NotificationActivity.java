package com.example.crypto_pay_2.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.example.crypto_pay_2.Adapter.NotificationAdapter;
import com.example.crypto_pay_2.Model.History;
import com.example.crypto_pay_2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationActivity extends AppCompatActivity {

    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    String my_email = currentUser.getEmail();
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("user");
    DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("history");

    private RecyclerView rvNoti;
    private NotificationAdapter notificationAdapter;
    private List<History> mListNoti;
    private ProgressBar loadingPB;
    private TextView noNoti;
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_notification);

        initUI();

        createUI();

        getInfo();
    }

    private void initUI(){
        back = findViewById(R.id.back_icon);
        loadingPB = findViewById(R.id.idPBLoading);
        loadingPB.setVisibility(View.VISIBLE);
        noNoti = findViewById(R.id.no_noti);

        rvNoti = findViewById(R.id.list_noti);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvNoti.setLayoutManager(linearLayoutManager);

        mListNoti = new ArrayList<>();
        notificationAdapter = new NotificationAdapter(mListNoti, new NotificationAdapter.DeleteStuff() {
            @Override
            public void deleteNotiOnClick(History history) {
                new AlertDialog.Builder(NotificationActivity.this)
                        .setTitle("Lưu ý!!!").setMessage("Bạn có chắc chắn muốn xóa thông báo này không?")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteNotification(history);
                            }
                        }).setNegativeButton("Cancel", null).show();
            }
        });

        rvNoti.setAdapter(notificationAdapter);
    }

    private void createUI(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NotificationActivity.this,MainPage.class));
            }
        });
    }

    private void getInfo(){
        ref.orderByChild("mail").equalTo(my_email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String,String> trade = new HashMap<>();
                for(DataSnapshot child:snapshot.getChildren()){
                    if(child.child("notification").getChildrenCount() != 0) trade = (HashMap) child.child("notification").getValue();
                    else{
                        mListNoti.clear();
                        notificationAdapter.notifyDataSetChanged();
                        break;
                    }
                }
                if (!trade.isEmpty()){
                    if(mListNoti != null){
                        mListNoti.clear();
                    }

                    Object[] a = trade.values().toArray();
                    for (int i = 0; i < a.length; i++){
                        ref2.orderByChild("id").equalTo(String.valueOf(a[i])).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot child: snapshot.getChildren()){
                                    History history = new History(String.valueOf(child.child("name").getValue()),
                                            String.valueOf(child.child("amount").getValue()),
                                            String.valueOf(child.child("date").getValue()),
                                            String.valueOf(child.child("time").getValue()),
                                            String.valueOf(child.child("from").getValue()),
                                            String.valueOf(child.child("to").getValue()),
                                            String.valueOf(child.child("id").getValue()),
                                            String.valueOf(child.child("message").getValue()));
                                    mListNoti.add(history);
                                }
                                notificationAdapter.notifyDataSetChanged();
                                Collections.sort(mListNoti,History.sortDate);
                                loadingPB.setVisibility(View.GONE);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
                else {
                    loadingPB.setVisibility(View.GONE);
                    noNoti.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void deleteNotification(History history){
        ref.orderByChild("mail").equalTo(my_email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userId = "";
                for (DataSnapshot child : snapshot.getChildren())
                {
                    userId = child.getKey().toString();
                    break;
                }
                ref.child(userId).child("notification").child(history.getId()).removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}