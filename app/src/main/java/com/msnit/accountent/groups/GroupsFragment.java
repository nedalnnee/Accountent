package com.msnit.accountent.groups;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.msnit.accountent.R;
import com.msnit.accountent.accounts.AccountsActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            String groupId = UUID.randomUUID().toString();

            String creatorName = currentUser.getDisplayName();
            String creatorEmail = currentUser.getEmail();

            Map<String, Object> group = new HashMap<>();
            group.put("id", groupId);
            group.put("name", name);
            group.put("creationDate", FieldValue.serverTimestamp());
            group.put("creatorName", creatorName); // Add creator name
            group.put("creatorEmail", creatorEmail); // Add creator email
            group.put("accountsNum", 0);

            // Set the group document under user's groups collection
            // Updated path: /users/{userId}/groups/{groupId}
            db.collection("users")
                    .document(userId)
                    .collection("groups")
                    .document(groupId)
                    .set(group)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(getContext(), "Group created successfully", Toast.LENGTH_LONG).show();
                        Date currentDate = new Date();
                        GroupEntity groupEntity = new GroupEntity(groupId, name, creatorName, creatorEmail, currentDate, 0);
                        groupList.add(groupEntity);
                        adapter.notifyItemInserted(groupList.size() - 1);
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Failed to create group", Toast.LENGTH_LONG).show();
                    });
        } else {
            // Notify the user that they need to be signed in to create a group.
            Toast.makeText(getContext(), "You need to be signed in to create a group", Toast.LENGTH_LONG).show();
        }
    }

    private void getAllGroups() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        // Ensure the user is signed in.
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Fetch the groups.
            db.collection("users")
                    .document(userId)
                    .collection("groups")
                    .get()
                    .addOnSuccessListener(querySnapshot -> {
                        List<GroupEntity> userGroups = new ArrayList<>();
                        for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                            GroupEntity group = document.toObject(GroupEntity.class);
                            if (group != null) {
                                userGroups.add(group);
                            }
                        }
                        // Update the global groupList and the adapter.
                        groupList = userGroups;
                        setAdapterList(getContext(), groupList, groupClickListener);
                    })
                    .addOnFailureListener(e -> {
                        // Handle the error scenario appropriately.
                        setAdapterList(getContext(), Collections.emptyList(), groupClickListener);
                        Toast.makeText(getContext(), "Failed to retrieve groups", Toast.LENGTH_SHORT).show();
                    });
        } else {
            // Notify the user that they need to be signed in to view groups.
            Toast.makeText(getContext(), "You need to be signed in to view groups", Toast.LENGTH_SHORT).show();
        }
    }


}