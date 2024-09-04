package com.msnit.accountent.accounts;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.msnit.accountent.R;
import com.msnit.accountent.groups.ClickListener;
import com.msnit.accountent.groups.GroupEntity;
import com.msnit.accountent.transactions.TransactionActivity;

import java.util.ArrayList;
import java.util.List;

public class AccountsActivity extends AppCompatActivity {


    private static GroupEntity group;
    private AccountListAdapter adapter;
    private RecyclerView recyclerView;
    private ClickListener accountClickListener;
    private final List<AccountEntity> accountList = new ArrayList<>();

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


    }

    private void getAllAccounts() {

    }


    private void createAccount(String name, String groupId, String currency, int accountsCash) {

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
                builder.setView(view).setTitle("Create Account").setPositiveButton("OK", (dialog, which) -> {
                    String accountName = accountNameEditText.getText().toString();
                    String currency = currencyEditText.getText().toString();
                    int initialCash = Integer.parseInt(initialCashEditText.getText().toString());
                    createAccount(accountName, currency, initialCash);
                    dialog.dismiss();
                }).setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

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