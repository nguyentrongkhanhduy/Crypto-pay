package com.example.crypto_pay_2.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crypto_pay_2.Model.History;
import com.example.crypto_pay_2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationHolder>{

    String my_email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("user");

    private List<History> mListNoti;

    public NotificationAdapter(List<History> mListNoti) {
        this.mListNoti = mListNoti;
    }

    @NonNull
    @Override
    public NotificationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.noti_item, parent, false);
        return new NotificationHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationHolder holder, int position) {
        History history = mListNoti.get(position);
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
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ref.orderByChild("mail").equalTo(my_email).addListenerForSingleValueEvent(new ValueEventListener() {
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
        });
    }

    @Override
    public int getItemCount() {
        if(mListNoti != null){
            return mListNoti.size();
        }
        return 0;
    }

    public class NotificationHolder extends RecyclerView.ViewHolder{

        private TextView name;
        private TextView amount;
        private TextView date;
        private TextView time;
        private TextView from;
        private TextView to;
        private TextView message;
        private ImageView delete;

        public NotificationHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.transaction_name);
            amount = itemView.findViewById(R.id.transaction_amount);
            date = itemView.findViewById(R.id.transaction_date);
            time = itemView.findViewById(R.id.transaction_time);
            from = itemView.findViewById(R.id.transaction_from);
            to = itemView.findViewById(R.id.transaction_to);
            message = itemView.findViewById(R.id.message);
            delete = itemView.findViewById(R.id.delete);
        }
    }

}
