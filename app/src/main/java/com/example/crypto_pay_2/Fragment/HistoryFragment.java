package com.example.crypto_pay_2.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crypto_pay_2.Activity.MainPage;
import com.example.crypto_pay_2.Adapter.HistoryAdapter;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HistoryFragment newInstance(String param1, String param2) {
        HistoryFragment fragment = new HistoryFragment();
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
    String my_email = currentUser.getEmail();
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("user");
    DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("history");


    private RecyclerView rvHistory;
    private HistoryAdapter historyAdapter;
    private List<History> mListHistory;
    private ProgressBar loadingPB;

    private MainPage mainPage;
    private TextView noNoti;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);


        initUI(view);

        getInfo();

        // Inflate the layout for this fragment
        return view;
    }

    private void initUI(View view){
        rvHistory = view.findViewById(R.id.list_history);
        mainPage = (MainPage) getActivity();
        noNoti = view.findViewById(R.id.no_noti);
        loadingPB = view.findViewById(R.id.idPBLoading);
        loadingPB.setVisibility(View.VISIBLE);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mainPage);
        rvHistory.setLayoutManager(linearLayoutManager);

        mListHistory = new ArrayList<>();
        historyAdapter = new HistoryAdapter(mListHistory);

        rvHistory.setAdapter(historyAdapter);
    }



    private void getInfo(){
        ref.orderByChild("mail").equalTo(my_email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String,String> trade = new HashMap<>();
                for(DataSnapshot child:snapshot.getChildren()){
                    if(child.child("history").getChildrenCount() != 0) trade = (HashMap) child.child("history").getValue();
                    else break;
                }
                if (!trade.isEmpty()){
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
                                    mListHistory.add(history);
                                    Collections.sort(mListHistory,History.sortDate);
                                }
                                historyAdapter.notifyDataSetChanged();
                                loadingPB.setVisibility(View.GONE);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
                else{
                    loadingPB.setVisibility(View.GONE);
                    noNoti.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        }
}