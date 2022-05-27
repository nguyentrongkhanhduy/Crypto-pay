package com.example.crypto_pay_2.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crypto_pay_2.Model.History;
import com.example.crypto_pay_2.R;

import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> implements Filterable {

    private List<History> mListHistory;
    private List<History> mListHistoryOld;

    public HistoryAdapter(List<History> mListHistory) {
        this.mListHistory = mListHistory;
        this.mListHistoryOld = mListHistory;
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
            return;
        }
        holder.name.setText(history.getName());
        holder.amount.setText(history.getAmount());
        holder.date.setText(history.getDate());
        holder.time.setText(history.getTime());
        holder.from.setText("Từ: " + history.getFrom());
        holder.to.setText("Đến: " + history.getTo());
        holder.message.setText("Lời nhắn: " + history.getMessage());
    }

    @Override
    public int getItemCount() {
        if(mListHistory != null){
            return mListHistory.size();
        }
        return 0;
    }



    public class HistoryViewHolder extends RecyclerView.ViewHolder{

        private TextView name;
        private TextView amount;
        private TextView date;
        private TextView time;
        private TextView from;
        private TextView to;
        private TextView message;


        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.transaction_name);
            amount = itemView.findViewById(R.id.transaction_amount);
            date = itemView.findViewById(R.id.transaction_date);
            time = itemView.findViewById(R.id.transaction_time);
            from = itemView.findViewById(R.id.transaction_from);
            to = itemView.findViewById(R.id.transaction_to);
            message = itemView.findViewById(R.id.message);

        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String strSearch = charSequence.toString();
                if(strSearch.isEmpty()){
                    mListHistory = mListHistoryOld;
                }else{
                    List<History> newListHistory = new ArrayList<>();
                    for (History history : mListHistoryOld){
                        if(history.getName().toLowerCase().contains(strSearch.toLowerCase())){
                            newListHistory.add(history);
                        }
                        else if (history.getFrom().toLowerCase().contains(strSearch.toLowerCase())){
                            newListHistory.add(history);
                        }
                        else if (history.getTo().toLowerCase().contains(strSearch.toLowerCase())){
                            newListHistory.add(history);
                        }
                    }
                    mListHistory = newListHistory;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mListHistory;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mListHistory = (List<History>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
