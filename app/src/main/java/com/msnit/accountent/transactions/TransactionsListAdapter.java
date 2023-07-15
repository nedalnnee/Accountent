package com.msnit.accountent.transactions;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.msnit.accountent.R;
import com.msnit.accountent.groups.ClickListener;

import java.text.SimpleDateFormat;
import java.util.List;

public class TransactionsListAdapter extends RecyclerView.Adapter<TransactionItemsHolder> {

    private final List<TransactionEntity> list;

    private final ClickListener listener;

    public TransactionsListAdapter(List<TransactionEntity> list, ClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TransactionItemsHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);


        View groupView = inflater.inflate(R.layout.transaction_item, parent, false);

        return new TransactionItemsHolder(groupView);
    }

    @Override
    public void onBindViewHolder(final TransactionItemsHolder viewHolder, final int position) {
        final int index = viewHolder.getAdapterPosition();
        TransactionEntity transactionEntity = list.get(position);
        viewHolder.titleTV.setText(transactionEntity.getName());

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE - dd/MM/yyyy - HH:mm");
        String formattedDate = dateFormat.format(transactionEntity.getCreationDate());

        viewHolder.transactionDate.setText(formattedDate);
        viewHolder.note.setText(transactionEntity.getNote());
        if (transactionEntity.isWithdrawal()) {
            viewHolder.transactionValue.setText("-"+transactionEntity.getAmount() );

            viewHolder.layout.setBackgroundColor(Color.parseColor("#FF6961"));
        }
        else {
            viewHolder.layout.setBackgroundColor(Color.parseColor("#96DEAE"));
            viewHolder.transactionValue.setText("+"+transactionEntity.getAmount() );

        }

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
