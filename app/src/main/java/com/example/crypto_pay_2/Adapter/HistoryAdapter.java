package com.example.crypto_pay_2.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crypto_pay_2.Model.History;
import com.example.crypto_pay_2.R;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>{

    private List<History> mListHistory;

    public HistoryAdapter(List<History> mListHistory) {
        this.mListHistory = mListHistory;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        History history = mListHistory.get(position);
        if(history == null) {
            System.out.println("rong");
            return;
        }
        System.out.println("khong rong");
        holder.name.setText(history.getName());
        holder.amount.setText(history.getAmount());
    }

    @Override
    public int getItemCount() {
        if(mListHistory != null){
            mListHistory.size();
        }
        return 0;
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder{

        private TextView name;
        private TextView amount;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.transaction_name);
            amount = itemView.findViewById(R.id.transaction_amount);
        }
    }
}
