package com.msnit.accountent.accounts;

import static com.msnit.accountent.groups.GroupsActivity.ACCOUNTS_NUM;
import static com.msnit.accountent.groups.GroupsActivity.GROUPS_COLLECTION_PATH;

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
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.msnit.accountent.R;
import com.msnit.accountent.transactions.TransactionActivity;
import com.msnit.accountent.groups.ClickListener;
import com.msnit.accountent.groups.GroupEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AccountsActivity extends AppCompatActivity {


    public static final String ACCOUNTS_COLLECTION_PATH = "accounts";
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
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> update = new HashMap<>();
        update.put(ACCOUNTS_NUM, accountList.size());

        db.collection(GROUPS_COLLECTION_PATH)
                .document(group.getId())
                .update(update)
                .addOnSuccessListener(aVoid -> {
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AccountsActivity.this, "Failed to update accounts number", Toast.LENGTH_LONG).show();
                });
    }

    private void getAllAccounts() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(GROUPS_COLLECTION_PATH)
                .document(group.getId())
                .collection(ACCOUNTS_COLLECTION_PATH)
                .orderBy("lastChange", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    accountList.clear();
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        String accountId = document.getString("id");
                        String name = document.getString("name");
                        Date creationDate = document.getDate("creationDate");
                        Date lastChange = document.getDate("lastChange");
                        String currency = document.getString("currency");
                        int accountsCash = document.getLong("accountsCash").intValue();

                        AccountEntity accountEntity = new AccountEntity(accountId, name, creationDate, lastChange, currency, accountsCash);

                        accountList.add(accountEntity);
                    }

                    adapter = new AccountListAdapter(accountList, accountClickListener);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(AccountsActivity.this));
                    updateAccountsNumber();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AccountsActivity.this, "Failed to retrieve accounts", Toast.LENGTH_LONG).show();
                    adapter = new AccountListAdapter(accountList, accountClickListener);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(AccountsActivity.this));
                });
    }



    private void createAccount(String name, String groupId, String currency, int accountsCash) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userId = auth.getCurrentUser().getUid();
        String accountId = UUID.randomUUID().toString();

        Map<String, Object> account = new HashMap<>();
        account.put("id", accountId);
        account.put("name", name);
        account.put("creator", userId);
        account.put("creationDate", FieldValue.serverTimestamp());
        account.put("lastChange", FieldValue.serverTimestamp());
        account.put("currency", currency);
        account.put("accountsCash", accountsCash);

        db.collection(GROUPS_COLLECTION_PATH)
                .document(groupId)
                .collection(ACCOUNTS_COLLECTION_PATH)
                .document(accountId)
                .set(account)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(AccountsActivity.this, "Account created successfully", Toast.LENGTH_LONG).show();
                    Date currentDate = new Date();
                    AccountEntity accountEntity = new AccountEntity(accountId, name, currentDate, currentDate, currency, accountsCash);
                    accountList.add(accountEntity);
                    adapter.notifyItemInserted(accountList.size() - 1);
                    updateAccountsNumber();

                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AccountsActivity.this, "Failed to create account", Toast.LENGTH_LONG).show();
                });
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