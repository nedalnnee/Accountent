package com.msnit.accountent.transactions;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.msnit.accountent.R;
import com.msnit.accountent.accounts.AccountEntity;
import com.msnit.accountent.groups.ClickListener;
import com.msnit.accountent.groups.GroupEntity;

import java.util.ArrayList;
import java.util.List;

public class TransactionActivity extends AppCompatActivity {


    private static GroupEntity group;
    private static AccountEntity account;
    private final List<TransactionEntity> transactionList = new ArrayList<>();
    private TransactionsListAdapter adapter;
    private RecyclerView recyclerView;
    private ClickListener transactionClickListener;
    private TextView accountAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setBackground(null);

        Intent intent = getIntent();
        group = (GroupEntity) intent.getSerializableExtra("group");
        TextView title = findViewById(R.id.title);
        account = (AccountEntity) intent.getSerializableExtra("account");
        title.setText(account.getName() + " Transactions");


        recyclerView = findViewById(R.id.transactionList);
        transactionClickListener = new ClickListener() {
            @Override
            public void click(int index) {
                Toast.makeText(TransactionActivity.this, "Clicked item index is " + index, Toast.LENGTH_LONG).show();
            }
        };

        getAllTransactions();

        findViewById(R.id.createTransactionBtn).setOnClickListener(view -> showCreateTransactionDialog());
        findViewById(R.id.backButton).setOnClickListener(v -> onBackPressed());
        accountAmount = findViewById(R.id.account_amount);
        accountAmount.setText(account.getAccountsCash() + " " + account.getCurrency());


    }


    private void showCreateTransactionDialog() {
        TransactionActivity.CreateTransactionDialogFragment dialogFragment = new TransactionActivity.CreateTransactionDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "CreateTransactionDialog");
    }

    private void updateAccount(TransactionEntity transactionEntity) {

    }


    private void getAllTransactions() {

    }


    private void createTransaction(String name, String type, int transactionsCash, String note) {

    }

    public static class CreateTransactionDialogFragment extends DialogFragment {
        private EditText transactionNameEditText;
        private Spinner typeSpinner;
        private EditText amountEditText;
        private EditText noteEditText;

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Activity activity = getActivity();
            if (activity != null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                View view = activity.getLayoutInflater().inflate(R.layout.dialog_create_transaction, null);
                builder.setView(view).setTitle("Create Transaction").setPositiveButton("OK", (dialog, which) -> {
                    String transactionName = transactionNameEditText.getText().toString();
                    String type = typeSpinner.getSelectedItem().toString();
                    int amount = Integer.parseInt(amountEditText.getText().toString());
                    String note = noteEditText.getText().toString();
                    createTransaction(transactionName, type, amount, note);
                    dialog.dismiss();
                }).setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

                // Initialize the EditText and Spinner fields
                transactionNameEditText = view.findViewById(R.id.editTextTransactionName);
                typeSpinner = view.findViewById(R.id.spinnerType);
                amountEditText = view.findViewById(R.id.editTextAmount);
                noteEditText = view.findViewById(R.id.editTextNote);

                return builder.create();
            } else {
                throw new IllegalStateException("Activity cannot be null");
            }
        }

        private void createTransaction(String name, String type, int amount, String note) {
            TransactionActivity activity = (TransactionActivity) getActivity();
            if (activity != null) {
                activity.createTransaction(name, type, amount, note);
            }
        }
    }
}