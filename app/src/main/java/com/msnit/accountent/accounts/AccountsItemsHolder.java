package com.msnit.accountent.accounts;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.msnit.accountent.R;

public class AccountsItemsHolder extends RecyclerView.ViewHolder {

    TextView titleTV;
    TextView lastChangeTV;
    TextView accountSum;
    View view;

    public AccountsItemsHolder(View itemView) {
        super(itemView);
        titleTV = itemView.findViewById(R.id.accountTitle);
        lastChangeTV = itemView.findViewById(R.id.lastChangeValue);
        accountSum = itemView.findViewById(R.id.account_sum);
        view = itemView;
    }
}
