package com.msnit.accountent;

import static com.msnit.accountent.AccountsActivity.ACCOUNTS_COLLECTION_PATH;
import static com.msnit.accountent.GroupsActivity.GROUPS_COLLECTION_PATH;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.msnit.accountent.accounts.AccountEntity;
import com.msnit.accountent.groups.ClickListener;
import com.msnit.accountent.groups.GroupEntity;
import com.msnit.accountent.transactions.TransactionEntity;
import com.msnit.accountent.transactions.TransactionsListAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TransactionActivity extends AppCompatActivity {


    public static final String TRANSACTIONS_CASH = "transactionsCash";
    private static final String TRANSACTIONS_COLLECTION_PATH = "transactions";
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
        accountAmount.setText(account.getAccountsCash() +" "+ account.getCurrency());


    }


    private void showCreateTransactionDialog() {
        TransactionActivity.CreateTransactionDialogFragment dialogFragment = new TransactionActivity.CreateTransactionDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "CreateTransactionDialog");
    }

    private void updateAccount(TransactionEntity transactionEntity) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> update = new HashMap<>();
        int cash = account.getAccountsCash();
        if (transactionEntity.isWithdrawal()) {
            cash -= transactionEntity.getAmount();

        } else cash += transactionEntity.getAmount();
        update.put("accountsCash", cash);
        update.put("lastChange", FieldValue.serverTimestamp());
        account.setAccountsCash(cash);
        accountAmount.setText(cash +" "+ account.getCurrency());

        db.collection(GROUPS_COLLECTION_PATH)
                .document(group.getId())
                .collection(ACCOUNTS_COLLECTION_PATH)
                .document(account.getId())
                .update(update)
                .addOnSuccessListener(aVoid -> {
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(TransactionActivity.this, "Failed to update group transactions", Toast.LENGTH_LONG).show();
                });
    }


    private void getAllTransactions() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(GROUPS_COLLECTION_PATH)
                .document(group.getId())
                .collection(ACCOUNTS_COLLECTION_PATH)
                .document(account.getId())
                .collection(TRANSACTIONS_COLLECTION_PATH)
                .orderBy("creationDate", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    transactionList.clear();
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        String transactionId = document.getString("id");
                        String name = document.getString("name");
                        Date creationDate = document.getDate("creationDate");
                        int transactionsCash = document.getLong(TRANSACTIONS_CASH).intValue();
                        String note = document.getString("note");
                        String type = document.getString("type");

                        TransactionEntity transactionEntity = new TransactionEntity(transactionId, name, creationDate, transactionsCash, note, type);

                        transactionList.add(transactionEntity);
                    }

                    adapter = new TransactionsListAdapter(transactionList, transactionClickListener);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(TransactionActivity.this));
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(TransactionActivity.this, "Failed to retrieve transactions", Toast.LENGTH_LONG).show();
                    adapter = new TransactionsListAdapter(transactionList, transactionClickListener);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(TransactionActivity.this));
                });
    }


    private void createTransaction(String name, String type, int transactionsCash, String note) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String transactionId = UUID.randomUUID().toString();

        Map<String, Object> transaction = new HashMap<>();
        int amount = Math.abs(transactionsCash);
        transaction.put("id", transactionId);
        transaction.put("name", name);
        transaction.put("creationDate", FieldValue.serverTimestamp());
        transaction.put("lastChange", FieldValue.serverTimestamp());
        transaction.put(TRANSACTIONS_CASH, amount);
        transaction.put("note", note);

        if (type.equals(getString(R.string.withdrawal))) {
            type = "withdrawal";
        } else {
            type = "deposit";
        }
        transaction.put("type", type);

        String finalType = type;
        db.collection(GROUPS_COLLECTION_PATH)
                .document(group.getId())
                .collection(ACCOUNTS_COLLECTION_PATH)
                .document(account.getId())
                .collection(TRANSACTIONS_COLLECTION_PATH)
                .document(transactionId)
                .set(transaction)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(TransactionActivity.this, "Transaction created successfully", Toast.LENGTH_LONG).show();
                    Date currentDate = new Date();
                    TransactionEntity transactionEntity = new TransactionEntity(transactionId, name, currentDate, amount, note, finalType);
                    transactionList.add(0, transactionEntity); // Add at the beginning of the list
                    adapter.notifyItemInserted(0); // Notify adapter of the new item at position 0
                    updateAccount(transactionEntity);
                })
                .addOnFailureListener(e -> Toast.makeText(TransactionActivity.this, "Failed to create transaction", Toast.LENGTH_LONG).show());
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
                builder.setView(view)
                        .setTitle("Create Transaction")
                        .setPositiveButton("OK", (dialog, which) -> {
                            String transactionName = transactionNameEditText.getText().toString();
                            String type = typeSpinner.getSelectedItem().toString();
                            int amount = Integer.parseInt(amountEditText.getText().toString());
                            String note = noteEditText.getText().toString();
                            createTransaction(transactionName, type, amount, note);
                            dialog.dismiss();
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

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