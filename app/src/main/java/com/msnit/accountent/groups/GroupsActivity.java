package com.msnit.accountent.groups;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.msnit.accountent.user.ProfileFragment;
import com.msnit.accountent.R;

public class GroupsActivity extends AppCompatActivity {
    private static GroupsFragment groupsFragment;

    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setBackground(null);

        groupsFragment = new GroupsFragment();
        replaceFragment(groupsFragment);

        FloatingActionButton createGroupBtn = findViewById(R.id.createGroupBtn);
        createGroupBtn.setOnClickListener(view -> {
            replaceFragment(groupsFragment);
            Menu menu = bottomNavigationView.getMenu();
            for (int i = 0; i < menu.size(); i++) {
                MenuItem menuItem = menu.getItem(i);
                if (menuItem.getItemId() == R.id.groups) {
                    menuItem.setChecked(true);
                    break;
                }
            }

            showCreateGroupDialog();
        });


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
        if (fragment.getClass().equals(GroupsFragment.class)) {
            title = findViewById(R.id.title);
            title.setText(getResources().getString(R.string.my_groups));
        } else if (fragment.getClass().equals(ProfileFragment.class)) {
            title.setText(getResources().getString(R.string.my_profile));
        }
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
                        .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
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
