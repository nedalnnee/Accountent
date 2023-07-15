package com.msnit.accountent.groups;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.msnit.accountent.R;

import java.util.List;

public class GroupsListAdapter extends RecyclerView.Adapter<GroupItemsHolder> {

    private final List<GroupEntity> list;

    private final ClickListener listener;

    public GroupsListAdapter(List<GroupEntity> list, ClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public GroupItemsHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);


        View groupView = inflater.inflate(R.layout.group_item, parent, false);

        return new GroupItemsHolder(groupView);
    }

    @Override
    public void onBindViewHolder(final GroupItemsHolder viewHolder, final int position) {
        final int index = viewHolder.getAdapterPosition();
        viewHolder.titleTV.setText(list.get(position).getName());
        viewHolder.lastChangeTV.setText(list.get(position).getCreationDate().toString());
        viewHolder.accountsNumTV.setText(list.get(position).getAccountsNum() + " Accounts ");
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
