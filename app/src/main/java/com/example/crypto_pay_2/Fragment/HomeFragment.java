package com.example.crypto_pay_2.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.crypto_pay_2.Activity.ConvertActivity;
import com.example.crypto_pay_2.Activity.DepositActivity;
import com.example.crypto_pay_2.Activity.ProfileActivity;
import com.example.crypto_pay_2.Activity.ScanActivity;
import com.example.crypto_pay_2.Activity.SearchCoinActivity;
import com.example.crypto_pay_2.Activity.QrActivity;
import com.example.crypto_pay_2.R;
import com.example.crypto_pay_2.Activity.TransferActivity;
import com.example.crypto_pay_2.Activity.WithdrawActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    String my_email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
    ImageView profile;
    AutoCompleteTextView autoCplt;
    TextInputEditText balance;
    ImageView deposit;
    ImageView withdraw;
    ImageView scan;
    ImageView code;
    ImageView transfer;
    ImageView graph;
    ImageView exchange;
    ImageView buy;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        creatUI(view);

        iniUI();

        getInfo();

        // Inflate the layout for this fragment
        return view;
    }

    private void creatUI(View view){
        profile = (ImageView) view.findViewById(R.id.account);
        autoCplt = view.findViewById(R.id.coin_dropdown);
        balance = (TextInputEditText) view.findViewById(R.id.coin_balance);
        deposit = (ImageView) view.findViewById(R.id.deposit_button);
        withdraw = (ImageView) view.findViewById(R.id.withdraw_button);
        scan = (ImageView) view.findViewById(R.id.scan_button);
        code = (ImageView) view.findViewById(R.id.code_button);
        transfer = (ImageView) view.findViewById(R.id.transfer_button);
        graph = (ImageView) view.findViewById(R.id.graph_button);
        exchange = (ImageView) view.findViewById(R.id.exchange_button);
        buy = (ImageView) view.findViewById(R.id.buy_button);
    }

    private void getInfo(){
        Uri photoUrl = currentUser.getPhotoUrl();
        Glide.with(getActivity()).load(photoUrl).error(R.drawable.avatardefault).into(profile);
    }

    private void iniUI(){
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileActivity profile = new ProfileActivity();
                Intent intent = new Intent(getActivity(),profile.getClass());
                startActivity(intent);
            }
        });


        String[] item = {"bitcoin","ethereum","lvcoin"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), R.layout.dropdown,item);
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

        deposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DepositActivity.class);
                startActivity(intent);
            }
        });

        withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), WithdrawActivity.class);
                startActivity(intent);
            }
        });

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ScanActivity.class);
                startActivity(intent);
            }
        });

        code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), QrActivity.class);
                startActivity(intent);
            }
        });

        transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TransferActivity.class);
                startActivity(intent);
            }
        });

        graph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SearchCoinActivity.class);
                startActivity(intent);
            }
        });

        exchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ConvertActivity.class);
                startActivity(intent);
            }
        });

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }




}