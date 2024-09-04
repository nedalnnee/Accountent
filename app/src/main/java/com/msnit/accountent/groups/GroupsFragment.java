package com.msnit.accountent.groups;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.msnit.accountent.R;
import com.msnit.accountent.accounts.AccountsActivity;

import java.util.ArrayList;
import java.util.List;

public class GroupsFragment extends Fragment {

    private GroupsListAdapter adapter;
    private RecyclerView recyclerView;
    private ClickListener groupClickListener;
    private List<GroupEntity> groupList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_groups, container, false);

        recyclerView = view.findViewById(R.id.groupsList);

        groupClickListener = new ClickListener() {
            @Override
            public void click(int index) {
                GroupEntity groupEntity = groupList.get(index);
                Intent intent = new Intent(getContext(), AccountsActivity.class);
                intent.putExtra("group", groupEntity);
                startActivity(intent);
            }
        };

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        getAllGroups();
    }

    public List<GroupEntity> getGroupList() {
        return groupList;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setAdapterList(Context context, List<GroupEntity> list, ClickListener listener) {
        adapter = new GroupsListAdapter(list, listener);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    void createGroup(String name) {
    }

    private void getAllGroups() {

    }


}