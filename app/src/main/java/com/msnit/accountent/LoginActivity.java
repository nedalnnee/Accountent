package com.msnit.accountent;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public class LoginActivity extends AppCompatActivity {
    EditText emailInput;
    EditText passwordInput;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseApp.initializeApp(this);
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .build();
        FirebaseFirestore.getInstance().setFirestoreSettings(settings);


        Button loginButton = findViewById(R.id.login);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);

        loginButton.setOnClickListener((view) -> signIn(emailInput, passwordInput));

        findViewById(R.id.go_to_login_from_sign_up_btn).setOnClickListener((view) -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        mAuth = FirebaseAuth.getInstance();


    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            login();
        }
    }


    private void signIn(EditText emailInput, EditText passwordInput) {
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();

        if (email.trim().isBlank() || password.trim().isBlank()) {
            Toast.makeText(getApplicationContext(), "Empty", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success");
                        login();
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.getException());

                        failedLogin();
                    }
                });
    }

    private void failedLogin() {
        Toast.makeText(LoginActivity.this, "Authentication failed.",
                Toast.LENGTH_SHORT).show();
    }

    private void login() {
        Intent intent = new Intent(LoginActivity.this, GroupsActivity.class);
        startActivity(intent);
        finish();

    }

}