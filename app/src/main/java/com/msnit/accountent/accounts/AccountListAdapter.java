package com.msnit.accountent.accounts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.msnit.accountent.R;
import com.msnit.accountent.groups.ClickListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AccountListAdapter extends RecyclerView.Adapter<AccountsItemsHolder> {

    private final List<AccountEntity> list;

    private final ClickListener listener;

    public AccountListAdapter(List<AccountEntity> list, ClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AccountsItemsHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);


        View groupView = inflater.inflate(R.layout.account_item, parent, false);

        return new AccountsItemsHolder(groupView);
    }

    @Override
    public void onBindViewHolder(final AccountsItemsHolder viewHolder, final int position) {
        final int index = viewHolder.getAdapterPosition();
        viewHolder.titleTV.setText(list.get(position).getName());

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE - dd/MM/yyyy - HH:mm");
        Date lastChange = list.get(position).getLastChange();
        if (lastChange!=null) {
            String formattedDate = dateFormat.format(lastChange);
            viewHolder.lastChangeTV.setText(formattedDate);
        }else
            viewHolder.lastChangeTV.setText("---");

        viewHolder.accountSum.setText(list.get(position).getAccountsCash() + " " + list.get(position).getCurrency());
        viewHolder.view.setOnClickListener(view -> listener.click(index));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


}
