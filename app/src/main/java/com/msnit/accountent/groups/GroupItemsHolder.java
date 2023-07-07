package com.msnit.accountent.groups;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.msnit.accountent.R;

public class GroupItemsHolder extends RecyclerView.ViewHolder {

    TextView titleTV;
    TextView lastChangeTV;
    TextView accountsNumTV;
    View view;

    public GroupItemsHolder(View itemView) {
        super(itemView);
        titleTV = itemView.findViewById(R.id.groupTitle);
        lastChangeTV = itemView.findViewById(R.id.lastChangeValue);
        accountsNumTV = itemView.findViewById(R.id.group_accounts_num);
        view = itemView;
    }
}
