package com.msnit.accountent;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.msnit.accountent.groups.ClickListener;
import com.msnit.accountent.groups.GroupEntity;
import com.msnit.accountent.groups.GroupsListAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GroupsActivity extends AppCompatActivity {
    public static final String GROUPS_COLLECTION_PATH = "groups";
    public static final String ACCOUNTS_NUM = "accountsNum";

    private GroupsListAdapter adapter;
    private RecyclerView recyclerView;
    private ClickListener groupClickListener;
    private  List<GroupEntity> groupList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setBackground(null);

        recyclerView = findViewById(R.id.groupsList);
        groupClickListener = new ClickListener() {
            @Override
            public void click(int index) {
                GroupEntity groupEntity = groupList.get(index);
                Intent intent = new Intent(GroupsActivity.this, AccountsActivity.class);
                intent.putExtra("group", groupEntity);
                startActivity(intent);
            }
        };

        getAllGroups();

        FloatingActionButton createGroupBtn = findViewById(R.id.createGroupBtn);
        createGroupBtn.setOnClickListener(view -> showCreateGroupDialog());

    }


    private void showCreateGroupDialog() {
        CreateGroupDialogFragment dialogFragment = new CreateGroupDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "CreateGroupDialog");
    }


    private void getAllGroups() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userId = auth.getCurrentUser().getUid();

        db.collection(GROUPS_COLLECTION_PATH)
                .whereEqualTo("creator", userId)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<GroupEntity> userGroups = new ArrayList<>();
                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                        GroupEntity group = document.toObject(GroupEntity.class);
                        if (group != null) {
                            userGroups.add(group);
                        }
                    }
                    groupList = userGroups;
                    adapter = new GroupsListAdapter(groupList, groupClickListener);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(GroupsActivity.this));
                })
                .addOnFailureListener(e -> {
                    adapter = new GroupsListAdapter(Collections.EMPTY_LIST, groupClickListener);
                    Toast.makeText(GroupsActivity.this, "NO GROUPS", Toast.LENGTH_SHORT).show();
                });
    }
    @Override
    protected void onResume() {
        super.onResume();
        getAllGroups();
    }
    private void createGroup(String name) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userId = auth.getCurrentUser().getUid();
        String groupId = UUID.randomUUID().toString();

        Map<String, Object> group = new HashMap<>();
        group.put("id", groupId);
        group.put("name", name);
        group.put("creationDate", FieldValue.serverTimestamp());
        group.put("creator", userId);
        group.put(ACCOUNTS_NUM, 0);

        db.collection(GROUPS_COLLECTION_PATH)
                .document(groupId)
                .set(group)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(GroupsActivity.this, "Group created successfully", Toast.LENGTH_LONG).show();
                    Date currentDate = new Date();
                    GroupEntity groupEntity = new GroupEntity(groupId, name, userId, currentDate, 0);
                    groupList.add(groupEntity);
                    adapter.notifyItemInserted(groupList.size() - 1);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(GroupsActivity.this, "Failed to create group", Toast.LENGTH_LONG).show();
                });
    }

    public static class CreateGroupDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Activity activity = getActivity();
            if (activity != null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                View view = activity.getLayoutInflater().inflate(R.layout.dialog_create_group, null);
                builder.setView(view)
                        .setTitle("Create Group")
                        .setPositiveButton("OK", (dialog, which) -> {
                            EditText groupNameEditText = view.findViewById(R.id.editTextGroupName);
                            String groupName = groupNameEditText.getText().toString();
                            createGroup(groupName);
                            dialog.dismiss();
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> {
                            dialog.cancel();
                        });
                return builder.create();
            } else {
                throw new IllegalStateException("Activity cannot be null");
            }
        }

        private void createGroup(String name) {
            GroupsActivity activity = (GroupsActivity) getActivity();
            if (activity != null) {
                activity.createGroup(name);
            }
        }
    }
}
