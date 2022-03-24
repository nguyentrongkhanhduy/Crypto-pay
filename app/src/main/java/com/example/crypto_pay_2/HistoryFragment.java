package com.example.crypto_pay_2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getActivity());
        rvHistory.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this.getActivity(), DividerItemDecoration.VERTICAL);
        rvHistory.addItemDecoration(dividerItemDecoration);

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
                    trade = (HashMap) child.child("history").getValue();
                }
                Object[] a = trade.values().toArray();
                for (int i = 0; i < a.length; i++){
                    ref2.orderByChild("id").equalTo(String.valueOf(a[i])).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            History history = new History(String.valueOf(snapshot.child("name").getValue()),
                                    String.valueOf(snapshot.child("amount").getValue()),
                                    String.valueOf(snapshot.child("date").getValue()),
                                    String.valueOf(snapshot.child("time").getValue()),
                                    String.valueOf(snapshot.child("from").getValue()),
                                    String.valueOf(snapshot.child("to").getValue()),
                                    String.valueOf(snapshot.child("id").getValue()),
                                    String.valueOf(snapshot.child("message").getValue()));
                            mListHistory.add(history);

                            historyAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        }

}