package com.example.crypto_pay_2.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crypto_pay_2.Model.History;
import com.example.crypto_pay_2.Model.PhoneCard;
import com.example.crypto_pay_2.R;

import java.util.List;

public class PhoneCardAdapter extends  RecyclerView.Adapter<PhoneCardAdapter.PhoneCardHolder>{

    private List<PhoneCard> mListCard;
    private PhoneCardAdapter.UpdateIsUsed updateIsUsed;

    public interface UpdateIsUsed {
        void updateNotiOnClick(PhoneCard phoneCard);
    }

    public PhoneCardAdapter(List<PhoneCard> mListCard, UpdateIsUsed updateIsUsed) {
        this.mListCard = mListCard;
        this.updateIsUsed = updateIsUsed;
    }

    @NonNull
    @Override
    public PhoneCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.phone_card, parent, false);
        return new PhoneCardHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhoneCardHolder holder, int position) {
        PhoneCard phoneCard = mListCard.get(position);
        if (phoneCard == null){
            return;
        }
        holder.typeCard.setText(phoneCard.getTypeCard());
        holder.single.setText(phoneCard.getSinglePrice());
        holder.dateTime.setText(phoneCard.getDateTime());
        holder.code.setText(phoneCard.getCodeNumber());
        holder.seri.setText(phoneCard.getSeriNumber());
        if(phoneCard.getIsUsed().equals("1")){
            holder.isUsed.setChecked(true);
            holder.isUsed.setEnabled(false);
        }
        holder.isUsed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateIsUsed.updateNotiOnClick(phoneCard);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mListCard != null)
            return mListCard.size();
        return 0;
    }

    public class PhoneCardHolder extends RecyclerView.ViewHolder{

        private TextView typeCard;
        private CheckBox isUsed;
        private TextView single;
        private TextView dateTime;
        private TextView code;
        private TextView seri;

        public PhoneCardHolder(@NonNull View itemView) {
            super(itemView);

            typeCard = itemView.findViewById(R.id.typeCard1);
            isUsed = itemView.findViewById(R.id.isUsed);
            single = itemView.findViewById(R.id.first_single);
            dateTime = itemView.findViewById(R.id.date_time);
            code = itemView.findViewById(R.id.card_1_code);
            seri = itemView.findViewById(R.id.first_seri);
        }
    }
}
