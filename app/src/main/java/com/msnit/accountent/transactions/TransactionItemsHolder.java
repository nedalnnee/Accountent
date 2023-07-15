package com.msnit.accountent.transactions;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.msnit.accountent.R;

public class TransactionItemsHolder extends RecyclerView.ViewHolder {

    TextView titleTV;
    TextView transactionDate;
    TextView transactionValue;
    TextView note;
    View view;

    public TransactionItemsHolder(View itemView) {
        super(itemView);
        titleTV = itemView.findViewById(R.id.transactionTitle);
        transactionDate = itemView.findViewById(R.id.transactionDate);
        transactionValue = itemView.findViewById(R.id.transactionValue);
        note = itemView.findViewById(R.id.note);
        view = itemView;
    }
}
