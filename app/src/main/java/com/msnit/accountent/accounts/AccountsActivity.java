package com.msnit.accountent.accounts;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.msnit.accountent.R;
import com.msnit.accountent.groups.ClickListener;
import com.msnit.accountent.groups.GroupEntity;
import com.msnit.accountent.transactions.TransactionActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AccountsActivity extends AppCompatActivity {


    private static GroupEntity group;
    private AccountListAdapter adapter;
    private RecyclerView recyclerView;
    private ClickListener accountClickListener;
    private List<AccountEntity> accountList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setBackground(null);

        Intent intent = getIntent();
        group = (GroupEntity) intent.getSerializableExtra("group");
        TextView title = findViewById(R.id.title);
        title.setText(group.getName() + " Accounts");


        recyclerView = findViewById(R.id.accountsList);
        accountClickListener = new ClickListener() {
            @Override
            public void click(int index) {
                AccountEntity accountEntity = accountList.get(index);
                Intent intent = new Intent(AccountsActivity.this, TransactionActivity.class);
                intent.putExtra("group", group);
                intent.putExtra("account", accountEntity);
                startActivity(intent);
            }

        };

        getAllAccounts();

        FloatingActionButton createAccountBtn = findViewById(R.id.createAccountBtn);
        createAccountBtn.setOnClickListener(view -> showCreateAccountDialog());
        findViewById(R.id.backButton).setOnClickListener(v -> onBackPressed());

    }

    private void showCreateAccountDialog() {
        AccountsActivity.CreateAccountDialogFragment dialogFragment = new AccountsActivity.CreateAccountDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "CreateAccountDialog");
    }

    private void updateAccountsNumber() {
        // Get the Firestore instance.
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Get the current authenticated user.
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Check if the user is signed in.
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Prepare the data to update.
            Map<String, Object> update = new HashMap<>();
            update.put("accountsNum", accountList.size());

            // Update the data in Firestore, considering the user is in the correct group per your rules.
            // Updated pathing: /users/{userId}/groups/{groupId}
            db.collection("users")
                    .document(userId)
                    .collection("groups")
                    .document(group.getId())
                    .update(update)
                    .addOnSuccessListener(aVoid -> {
                        // Optionally handle the success scenario (e.g., log it or provide user feedback).
                    })
                    .addOnFailureListener(e -> {
                        // Handle the failure scenario by showing a Toast message.
                        Toast.makeText(AccountsActivity.this, "Failed to update accounts number", Toast.LENGTH_LONG).show();
                    });
        } else {
            // If the user is not signed in, show a Toast message and potentially redirect them to a sign-in activity.
            Toast.makeText(AccountsActivity.this, "You need to sign in to update accounts number", Toast.LENGTH_LONG).show();
        }
    }

    private void getAllAccounts() {
        // Get the Firebase Firestore instance.
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Get the current authenticated user.
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Check if the user is signed in.
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Fetch data from Firestore, considering the user is in the correct group/account per your rules.
            // Adjusted pathing to align with Firestore rules: /users/{userId}/groups/{groupId}/accounts
            db.collection("users")
                    .document(userId)
                    .collection("groups")
                    .document(group.getId())
                    .collection("accounts")
                    .orderBy("lastChange", Query.Direction.DESCENDING)
                    .get()
                    .addOnSuccessListener(querySnapshot -> {
                        // Clear the existing list to avoid duplication of data when this method is run again.
                        accountList.clear();

                        // Loop through the fetched documents.
                        for (QueryDocumentSnapshot document : querySnapshot) {
                            // Extract data from the document.
                            String accountId = document.getString("id");
                            String name = document.getString("name");
                            Date creationDate = document.getDate("creationDate");
                            Date lastChange = document.getDate("lastChange");
                            String currency = document.getString("currency");

                            // Safeguard against null for accountsCash.
                            Long accountsCashLong = document.getLong("accountsCash");
                            int accountsCash = (accountsCashLong != null) ? accountsCashLong.intValue() : 0;

                            // Create an AccountEntity object and add it to the list.
                            AccountEntity accountEntity = new AccountEntity(accountId, name, creationDate, lastChange, currency, accountsCash);
                            accountList.add(accountEntity);
                        }

                        // Initialize the adapter with the fetched data and set it to the RecyclerView.
                        adapter = new AccountListAdapter(accountList, accountClickListener);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(AccountsActivity.this));

                        // Update the UI according to the fetched data. (Assuming you have implemented this method).
                        updateAccountsNumber();
                    })
                    .addOnFailureListener(e -> {
                        // Handle the failure scenario by showing a Toast message and updating the UI accordingly.
                        Toast.makeText(AccountsActivity.this, "Failed to retrieve accounts", Toast.LENGTH_LONG).show();

                        // Even in the case of failure, ensure the UI is consistent and usable.
                        adapter = new AccountListAdapter(accountList, accountClickListener);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(AccountsActivity.this));
                    });
        } else {
            // If the user is not signed in, show a Toast message and potentially redirect them to a sign-in activity.
            Toast.makeText(AccountsActivity.this, "You need to sign in to view accounts", Toast.LENGTH_LONG).show();
        }
    }


    private void createAccount(String name, String groupId, String currency, int accountsCash) {
        // Get Firestore and FirebaseAuth instances.
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();

        // Ensure the user is signed in.
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            String accountId = UUID.randomUUID().toString();

            // Prepare the account data.
            Map<String, Object> account = new HashMap<>();
            account.put("id", accountId);
            account.put("name", name);
            account.put("creator", userId);
            account.put("creationDate", FieldValue.serverTimestamp());
            account.put("lastChange", FieldValue.serverTimestamp());
            account.put("currency", currency);
            account.put("accountsCash", accountsCash);

            // Ensure the path aligns with your Firestore rules and data structure.
            // Update this path if your Firestore rules dictate a different structure.
            db.collection("groups")
                    .document(groupId)
                    .collection("accounts")
                    .document(accountId)
                    .set(account)
                    .addOnSuccessListener(aVoid -> {
                        // Update the UI on success.
                        Toast.makeText(AccountsActivity.this, "Account created successfully", Toast.LENGTH_LONG).show();
                        Date currentDate = new Date();
                        AccountEntity accountEntity = new AccountEntity(accountId, name, currentDate, currentDate, currency, accountsCash);
                        accountList.add(accountEntity);
                        adapter.notifyItemInserted(accountList.size() - 1);
                        updateAccountsNumber();
                    })
                    .addOnFailureListener(e -> {
                        // Handle failure to add the account to Firestore.
                        Toast.makeText(AccountsActivity.this, "Failed to create account", Toast.LENGTH_LONG).show();
                    });
        } else {
            // Notify the user that they need to be signed in to create an account.
            Toast.makeText(AccountsActivity.this, "You need to be signed in to create an account", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllAccounts();
    }

    public static class CreateAccountDialogFragment extends DialogFragment {
        private EditText accountNameEditText;
        private EditText currencyEditText;
        private EditText initialCashEditText;

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Activity activity = getActivity();
            if (activity != null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                View view = activity.getLayoutInflater().inflate(R.layout.dialog_create_account, null);
                builder.setView(view)
                        .setTitle("Create Account")
                        .setPositiveButton("OK", (dialog, which) -> {
                            String accountName = accountNameEditText.getText().toString();
                            String currency = currencyEditText.getText().toString();
                            int initialCash = Integer.parseInt(initialCashEditText.getText().toString());
                            createAccount(accountName, currency, initialCash);
                            dialog.dismiss();
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

                // Initialize the EditText fields
                accountNameEditText = view.findViewById(R.id.editTextAccountName);
                currencyEditText = view.findViewById(R.id.editTextCurrency);
                initialCashEditText = view.findViewById(R.id.editTextInitialCash);

                return builder.create();
            } else {
                throw new IllegalStateException("Activity cannot be null");
            }
        }

        private void createAccount(String name, String currency, int initialCash) {
            AccountsActivity activity = (AccountsActivity) getActivity();
            if (activity != null) {
                activity.createAccount(name, group.getId(), currency, initialCash);
            }
        }
    }
}