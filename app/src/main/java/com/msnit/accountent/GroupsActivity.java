package com.msnit.accountent;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class GroupsActivity extends AppCompatActivity {
    public static final String GROUPS_COLLECTION_PATH = "groups";
    public static final String ACCOUNTS_NUM = "accountsNum";
    private static GroupsFragment groupsFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setBackground(null);

        groupsFragment = new GroupsFragment();
        replaceFragment(groupsFragment);


        FloatingActionButton createGroupBtn = findViewById(R.id.createGroupBtn);
        createGroupBtn.setOnClickListener(view -> showCreateGroupDialog());


        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.groups -> {
                    item.setChecked(true);
                    replaceFragment(groupsFragment);
                    return true;
                }
                case R.id.friends, R.id.settings -> {
                    item.setChecked(true);
                    return true;
                }
                case R.id.profile -> {
                    item.setChecked(true);
                    replaceFragment(new ProfileFragment());
                    return true;
                }
            }
            return false;
        });

    }


    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }

    private void showCreateGroupDialog() {
        CreateGroupDialogFragment dialogFragment = new CreateGroupDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "CreateGroupDialog");
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
            groupsFragment.createGroup(name);
        }
    }
}
